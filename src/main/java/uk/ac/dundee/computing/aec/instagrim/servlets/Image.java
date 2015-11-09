package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.util.Hashtable;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.CommentModel;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 * Servlet implementation class Image
 */
@WebServlet(urlPatterns = {
    "/Image",
    "/Image/*",
    "/Thumb/*",
    "/Pimage/*",
    "/Images",
    "/Images/*"
})
@MultipartConfig

public class Image extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Cluster cluster;
    private HashMap CommandsMap = new HashMap();
    
    

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Image() {
        super();
        // TODO Auto-generated constructor stub
        CommandsMap.put("Image", 1);
        CommandsMap.put("Images", 2);
        CommandsMap.put("Thumb", 3);
        CommandsMap.put("Pimage", 4);
    }

    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();

    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        synchronized(this){
            String args[] = Convertors.SplitRequestPath(request);

            int command;
            try {
                command = (Integer) CommandsMap.get(args[1]);
            } catch (Exception et) {
                error("Bad Operator", response);

                return;
            }
            /*PrintWriter out = response.getWriter();
            out.write(command);*/
            switch (command) {
                case 1:
                    if(args[2].compareTo("Comments") ==0 )
                    {
                       HttpSession session=request.getSession();
                       CommentModel cm = new CommentModel();
                       cm.setCluster(cluster);
                       Pic pic = new Pic();
                       pic.setUUID(UUID.fromString(args[3]));
                       pic.addComments(cm.getComment(UUID.fromString(args[3])));
                       RequestDispatcher rd = request.getRequestDispatcher("/comments.jsp");
                       session.setAttribute("pic", pic);
                       session.setAttribute("picid", null);
                       rd.forward(request, response); 
                    }
                    DisplayImage(Convertors.DISPLAY_PROCESSED,args[2], response);
                    break;
                case 2:
                    if(args[2].compareTo("Likes") ==0 ){
                        PicModel pm = new PicModel();
                        pm.setCluster(cluster);
                        java.util.LinkedList<String> Likes= pm.getPicLikes(UUID.fromString(args[4]));  
                        if(Likes.contains((String)args[3]))
                        {
                            pm.removePicLike(args[3], UUID.fromString(args[4]));
                        }else
                        {
                            pm.addPicLike(args[3], UUID.fromString(args[4]));
                        }
                        args[2] = args[3];
                    }
                    DisplayImageList(args[2], request, response);
                    break;
                case 3:
                    DisplayImage(Convertors.DISPLAY_THUMB,args[2],  response);
                    break;
                case 4:
                    PicModel pm = new PicModel();
                    pm.setCluster(cluster);
                    pm.setProfilePic(args[2], UUID.fromString(args[3]));
                    DisplayImageList(args[2], request, response);
                    break;
                default:
                    error("Bad Operator", response);
            }
            notify();
        }
    }

    private void DisplayImageList(String User, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PicModel tm = new PicModel();
        tm.setCluster(cluster);
         /*PrintWriter out = response.getWriter();
        out.println("Here");
       */
        HttpSession session=request.getSession();
        LoggedIn lg= (LoggedIn)session.getAttribute("LoggedIn");         
        java.util.LinkedList<Pic> lsPics = tm.getPicsForUser(User);
        RequestDispatcher rd = request.getRequestDispatcher("/UsersPics.jsp");
        request.setAttribute("Pics", lsPics);
        request.setAttribute("loggedIn", lg);
        rd.forward(request, response);

    }

    private void DisplayImage(int type,String Image, HttpServletResponse response) throws ServletException, IOException {
        PicModel tm = new PicModel();
        tm.setCluster(cluster);
  
        
        Pic p = tm.getPic(type,java.util.UUID.fromString(Image));
        
        OutputStream out = response.getOutputStream();

        response.setContentType(p.getType());
        response.setContentLength(p.getLength());
        //out.write(Image);
        InputStream is = new ByteArrayInputStream(p.getBytes());
        BufferedInputStream input = new BufferedInputStream(is);
        byte[] buffer = new byte[8192];
        for (int length = 0; (length = input.read(buffer)) > 0;) {
            out.write(buffer, 0, length);
        }
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        synchronized(this){
           boolean profilepic =false;
            PicModel tm = new PicModel();
            tm.setCluster(cluster);
            if(request.getParameter("profile_pic") !=null )
                profilepic=true;

           for (Part part : request.getParts()) {
                System.out.println("Part Name " + part.getName());

                String type = part.getContentType();
                String filename = part.getSubmittedFileName();

                InputStream is = request.getPart(part.getName()).getInputStream();
                int i = is.available();
                HttpSession session=request.getSession();
                LoggedIn lg= (LoggedIn)session.getAttribute("LoggedIn");
                String username="majed";
                if (lg.getlogedin()){
                    username=lg.getUsername();
                }
                  if (i > 0) {
                    byte[] b = new byte[i + 1];
                    is.read(b);
                    System.out.println("Length : " + b.length);
                    tm.insertPic(b, type, filename, username,profilepic);

                    is.close();
                }
                User currentUser = new User();
                currentUser.setCluster(cluster);
                Hashtable userProfile = new Hashtable();
                userProfile = currentUser.getUser(username); 
                Pic p =null;
                if(profilepic){ 
                    //process profile pic    
                    p = tm.getProfilePic(username);
                    RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp");
                    session.setAttribute("userProfile", userProfile);  
                    session.setAttribute("profilePICTURE", p);
                    session.setAttribute("profileName", username);
                    rd.forward(request, response);

                }
                else{
                    session.setAttribute("userProfile", userProfile);
                    session.setAttribute("profilePICTURE", p);
                    RequestDispatcher rd = request.getRequestDispatcher("/upload.jsp");
                    rd.forward(request, response);
                }
            } 
            
            notify();
        }

    }

    private void error(String mess, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = null;
        out = new PrintWriter(response.getOutputStream());
        out.println("<h1>You have a na error in your input</h1>");
        out.println("<h2>" + mess + "</h2>");
        out.close();
        return;
    }
}
