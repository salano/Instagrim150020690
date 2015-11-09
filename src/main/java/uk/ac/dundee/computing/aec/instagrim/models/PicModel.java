package uk.ac.dundee.computing.aec.instagrim.models;

/*
 * Expects a cassandra columnfamily defined as
 * use keyspace2;
 CREATE TABLE Tweets (
 user varchar,
 interaction_time timeuuid,
 tweet varchar,
 PRIMARY KEY (user,interaction_time)
 ) WITH CLUSTERING ORDER BY (interaction_time DESC);
 * To manually generate a UUID use:
 * http://www.famkruithof.net/uuid/uuidgen
 */
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.Bytes;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import javax.imageio.ImageIO;
import static org.imgscalr.Scalr.*;
import org.imgscalr.Scalr.Method;

import uk.ac.dundee.computing.aec.instagrim.lib.*;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;
//import uk.ac.dundee.computing.aec.stores.TweetStore;

public class PicModel {

    Cluster cluster;

    public void PicModel() {

    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public void insertPic(byte[] b, String type, String name, String user, boolean profile) {
        try {
            Convertors convertor = new Convertors();

            String types[]=Convertors.SplitFiletype(type);
            ByteBuffer buffer = ByteBuffer.wrap(b);
            int length = b.length;
            java.util.UUID picid = convertor.getTimeUUID();
            
            //The following is a quick and dirty way of doing this, will fill the disk quickly !
            Boolean success = (new File("/var/tmp/instagrim/")).mkdirs();
            FileOutputStream output = new FileOutputStream(new File("/var/tmp/instagrim/" + picid));

            output.write(b);
            byte []  thumbb = picresize(picid.toString(),types[1]);
            int thumblength= thumbb.length;
            ByteBuffer thumbbuf=ByteBuffer.wrap(thumbb);
            byte[] processedb = picdecolour(picid.toString(),types[1]);
            ByteBuffer processedbuf=ByteBuffer.wrap(processedb);
            int processedlength=processedb.length;
            Session session = cluster.connect("instagrim_150020690");

            PreparedStatement psInsertPic = session.prepare("insert into pics ( picid, image,thumb,processed, user, interaction_time,imagelength,thumblength,processedlength,type,name) values(?,?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement psInsertPicToUser = session.prepare("insert into userpiclist ( picid, user, pic_added) values(?,?,?)");
            BoundStatement bsInsertPic = new BoundStatement(psInsertPic);
            BoundStatement bsInsertPicToUser = new BoundStatement(psInsertPicToUser);
            if(profile){
                PreparedStatement psInsertProPic = session.prepare("insert into profilepic (picid,user) values(?,?)");
                BoundStatement bsInsertProfilePic = new BoundStatement(psInsertProPic);
                session.execute(bsInsertProfilePic.bind(picid, user));
            }
            
            PreparedStatement psInsertLikePic = session.prepare("insert into userpiclikes (picid) values(?)");
            BoundStatement bsInsertLikePic = new BoundStatement(psInsertLikePic);
            session.execute(bsInsertLikePic.bind(picid));

            Date DateAdded = new Date();
            session.execute(bsInsertPic.bind(picid, buffer, thumbbuf,processedbuf, user, DateAdded, length,thumblength,processedlength, type, name));
            session.execute(bsInsertPicToUser.bind(picid, user, DateAdded));
            session.close();

        } catch (IOException ex) {
            System.out.println("Error --> " + ex);
        }
    }

    public byte[] picresize(String picid,String type) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
            BufferedImage thumbnail = createThumbnail(BI);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, type, baos);
            baos.flush();
            
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
    }
    
    public byte[] picdecolour(String picid,String type) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
            BufferedImage processed = createProcessed(BI);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(processed, type, baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
    }

    public static BufferedImage createThumbnail(BufferedImage img) {
        img = resize(img, Method.SPEED, 250, OP_ANTIALIAS, OP_GRAYSCALE);
        // Let's add a little border before we return result.
        return pad(img, 2);
    }
    
   public static BufferedImage createProcessed(BufferedImage img) {
        int Width=img.getWidth()-1;
        img = resize(img, Method.SPEED, Width, OP_ANTIALIAS, OP_GRAYSCALE);
        return pad(img, 4);
    }
   
    public java.util.LinkedList<Pic> getPicsForUser(String User) {
        java.util.LinkedList<Pic> Pics = new java.util.LinkedList<>();
        Session session = cluster.connect("instagrim_150020690");
        PreparedStatement ps = session.prepare("select picid from userpiclist where user =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        User));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return null;
        } else {
            for (Row row : rs) {
                                Pic pic = new Pic();
                CommentModel commentmodel = new CommentModel();
                commentmodel.setCluster(cluster);
                java.util.UUID UUID = row.getUUID("picid");
                System.out.println("UUID" + UUID.toString());
                pic.setUUID(UUID);
                pic.addComments(commentmodel.getComment(UUID));
                pic.addLikes(getPicLikes(UUID));
                Pics.add(pic);



            }
        }
        session.close();
        return Pics;
    }

    public Pic getPic(int image_type, java.util.UUID picid) {
        Session session = cluster.connect("instagrim_150020690");
        ByteBuffer bImage = null;
        String type = null;
        int length = 0;
        try {
            Convertors convertor = new Convertors();
            ResultSet rs = null;
            PreparedStatement ps = null;
         
            if (image_type == Convertors.DISPLAY_IMAGE) {
                
                ps = session.prepare("select image,imagelength,type from pics where picid =?");
            } else if (image_type == Convertors.DISPLAY_THUMB) {
                ps = session.prepare("select thumb,imagelength,thumblength,type from pics where picid =?");
            } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                ps = session.prepare("select processed,processedlength,type from pics where picid =?");
            }
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            picid));

            if (rs.isExhausted()) {
                System.out.println("No Images returned");
                return null;
            } else {
                for (Row row : rs) {
                    if (image_type == Convertors.DISPLAY_IMAGE) {
                        bImage = row.getBytes("image");
                        length = row.getInt("imagelength");
                    } else if (image_type == Convertors.DISPLAY_THUMB) {
                        bImage = row.getBytes("thumb");
                        length = row.getInt("thumblength");
                
                    } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                        bImage = row.getBytes("processed");
                        length = row.getInt("processedlength");
                    }
                    
                    type = row.getString("type");

                }
            }
        } catch (Exception et) {
            System.out.println("Can't get Pic" + et);
            return null;
        }
        session.close();
        Pic p = new Pic();
        p.setPic(bImage, length, type);

        return p;

    }
   public java.util.UUID getProfileUUID(String username)
    {
        java.util.UUID UUID = null;
        Session session = cluster.connect("instagrim_150020690");
        PreparedStatement ps = session.prepare("select picid from profilepic where user =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return null;
        } else {
            for (Row row : rs) {
                UUID = row.getUUID("picid");
            }
        }
        session.close();
        return UUID;
    }
    public Pic getProfilePic(String username){
        Pic p =null;
        if(getProfileUUID(username) !=null){
        p = getPic(Convertors.DISPLAY_THUMB, getProfileUUID(username));
        p.setUUID(getProfileUUID(username));
      
        }
        return p;
    }
    public void setProfilePic(String username, java.util.UUID picid){
        Session session = cluster.connect("instagrim_150020690");
        PreparedStatement psInsertProPic = session.prepare("insert into profilepic (picid,user) values(?,?)");
        BoundStatement bsInsertProfilePic = new BoundStatement(psInsertProPic);
        session.execute(bsInsertProfilePic.bind(picid, username));
        session.close();
    }
    
    public void addPicLike(String name,java.util.UUID picid){
        Session session = cluster.connect("instagrim_150020690");
        Set<String> likes = new HashSet<String>();
        likes.add(name);
        PreparedStatement psAddLike = session.prepare("update userpiclikes set users = users + ? where picid = ?");
        BoundStatement bsAddLike = new BoundStatement(psAddLike);
        session.execute(bsAddLike.bind(likes,picid));
        session.close();
    }
    public void removePicLike(String name, java.util.UUID picid){
        Session session = cluster.connect("instagrim_150020690");
        Set<String> likes = new HashSet<String>();
        likes.add(name);
        PreparedStatement psAddLike = session.prepare("update userpiclikes set users = users - ? where picid = ?");
        BoundStatement bsAddLike = new BoundStatement(psAddLike);
        session.execute(bsAddLike.bind(likes,picid));
        session.close();
    }
    public java.util.LinkedList<String> getPicLikes(java.util.UUID picid){
        java.util.LinkedList<String> Likes = new java.util.LinkedList<>();
        Set<String> data = new HashSet<String>();
        Session session = cluster.connect("instagrim_150020690");
        PreparedStatement ps = session.prepare("select users from userpiclikes where picid =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        picid));
        if (!rs.isExhausted()) {
           for (Row row : rs) {
               data= row.getSet("users",String.class );
               Iterator it=data.iterator();
                   while(it.hasNext())
                   {
                       Likes.add((String)it.next());
                   }
           }
        }
                   
      
        return Likes;
    }
    
    public BufferedImage alteredImage(String picid){
        try {
        BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
        BI = resize(BI, Method.SPEED, 250, OP_ANTIALIAS, OP_BRIGHTER);
        // Let's add a little border before we return result.
            /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(BI, "png", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            ByteBuffer processedbuf=ByteBuffer.wrap(imageInByte);
            return processedbuf;*/
        return pad(BI, 2);
        } catch (IOException et) {

        }
        return null;
    }
            
}
