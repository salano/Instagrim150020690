package uk.ac.dundee.computing.aec.instagrim.lib;

import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.*;

public final class Keyspaces {

    public Keyspaces() {

    }

    public static void SetUpKeySpaces(Cluster c) {
        try {
            //Add some keyspaces here
            //String Dropkeyspace = "drop keyspace if exists instagrim_150020690;"; 
            
            String createkeyspace = "create keyspace if not exists instagrim_150020690  WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}";
            String CreatePicTable = "CREATE TABLE if not exists instagrim_150020690.Pics ("
                    + " user varchar,"
                    + " picid uuid, "
                    + " interaction_time timestamp,"
                    + " title varchar,"
                    + " image blob,"
                    + " thumb blob,"
                    + " processed blob,"
                    + " imagelength int,"
                    + " thumblength int,"
                    + "  processedlength int,"
                    + " type  varchar,"
                    + " name  varchar,"
                    + " PRIMARY KEY (picid)"
                    + ")";
            String Createuserpiclist = "CREATE TABLE if not exists instagrim_150020690.userpiclist (\n"
                    + "picid uuid,\n"
                    + "user varchar,\n"
                    + "pic_added timestamp,\n"
                    + "PRIMARY KEY (user,pic_added)\n"
                    + ") WITH CLUSTERING ORDER BY (pic_added desc);";
            String CreateuserpiclistIndex ="CREATE INDEX userpicindex ON instagrim_150020690.userpiclist (picid);";
            String CreateAddressType = "CREATE TYPE if not exists instagrim_150020690.address (\n"
                    + "      street text,\n"
                    + "      city text,\n"
                    + "      zip int\n"
                    + "  );";
            String CreateUserProfile = "CREATE TABLE if not exists instagrim_150020690.userprofiles (\n"
                    + "      login text PRIMARY KEY,\n"
                     + "     password text,\n"
                    + "      age int,\n"
                    + "      martial_status text,\n"
                    + "      telephone text,\n"
                    + "      first_name text,\n"
                    + "      last_name text,\n"
                    + "      email set<text>,\n"
                    + "      addresses  map<text, frozen <instagrim_150020690.address>>\n"
                    + "  );";
            String Createcomments = "CREATE TABLE if not exists instagrim_150020690.comments (\n"
                    + "commentid uuid PRIMARY KEY,\n"
                    + "comment_text varchar,\n"
                    + "date_added timestamp,\n"
                    + "picid uuid,\n"
                    + "user varchar,\n"
                    + "  );";
            String CreatecommentsIndex ="CREATE INDEX picture ON instagrim_150020690.comments (picid);";
            
            String Createuserpiclikes = "CREATE TABLE if not exists instagrim_150020690.userpiclikes (\n"
                    + "users set<text>,\n"
                    + "picid uuid PRIMARY KEY,\n"                  
                    + "  );";
            String Createprofilepic = "CREATE TABLE if not exists instagrim_150020690.profilepic (\n"
                    + "picid uuid ,\n"
                    + "user varchar PRIMARY KEY,\n"
                    + "  );";
            
            Session session = c.connect();
            /*try {
                SimpleStatement cqlQuery = new SimpleStatement(Dropkeyspace);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create tweet table " + et);
            }*/
            try {
                PreparedStatement statement = session
                        .prepare(createkeyspace);
                BoundStatement boundStatement = new BoundStatement(
                        statement);
                ResultSet rs = session
                        .execute(boundStatement);
                System.out.println("created instagrim ");
            } catch (Exception et) {
                System.out.println("Can't create instagrim " + et);
            }

            //now add some column families 
            System.out.println("" + CreatePicTable);

            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreatePicTable);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create tweet table " + et);
            }
            System.out.println("" + Createuserpiclist);

            try {
                SimpleStatement cqlQuery = new SimpleStatement(Createuserpiclist);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create user pic list table " + et);
            }
            System.out.println("" + CreateAddressType);
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreateAddressType);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create Address type " + et);
            }
            System.out.println("" + CreateUserProfile);
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreateUserProfile);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create Address Profile " + et);
            }
            /******************************************/
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreateuserpiclistIndex);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't index on pic list " + et);
            }
            try {
                SimpleStatement cqlQuery = new SimpleStatement(Createcomments);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create comments table " + et);
            }
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreatecommentsIndex);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't index on comments " + et);
            }
            
            try {
                SimpleStatement cqlQuery = new SimpleStatement(Createuserpiclikes);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create user pic likes " + et);
            }
            try {
                SimpleStatement cqlQuery = new SimpleStatement(Createprofilepic);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create user profile pic " + et);
            }
            
            
            session.close();

        } catch (Exception et) {
            System.out.println("Other keyspace or coulm definition error" + et);
        }

    }
}
