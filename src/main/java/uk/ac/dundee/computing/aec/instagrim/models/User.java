/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.models;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.UserType;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import uk.ac.dundee.computing.aec.instagrim.lib.AeSimpleSHA1;
import uk.ac.dundee.computing.aec.instagrim.stores.Person;
import uk.ac.dundee.computing.aec.instagrim.stores.Address;
import java.util.Set;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import com.datastax.driver.core.UDTValue;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.UDTMapper;

/**
 *
 * @author Administrator
 */
public class User {
    Cluster cluster;
    public User(){
        
    }
    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
    
    public boolean RegisterUser(String username, String Password, String firstname,String lastname, String email, String age,String street, String city, String zip){
        AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
        Map map = new HashMap();
        try {
            EncodedPassword= sha1handler.SHA1(Password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrim_150020690");
        UserType addressType = cluster.getMetadata().getKeyspace("instagrim_150020690").getUserType("address");
        UDTValue address = addressType.newValue()
                               .setString("street", street)
                               .setString("city", city)
                               .setInt("zip", Integer.parseInt(zip));
        map.put("home", address);
        PreparedStatement ps = session.prepare("insert into userprofiles (login,password,first_name,last_name,email,age, addresses) Values(?,?,?,?,?,?,?)");
        Set<String> emails = new HashSet<String>();
        emails.add(email);
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username,EncodedPassword,firstname,lastname,emails,Integer.parseInt(age), map ));
        //We are assuming this always works.  Also a transaction would be good here !
        session.close();
        return true;
    }
    
    public boolean IsValidUser(String username, String Password){
        AeSimpleSHA1 sha1handler=  new AeSimpleSHA1();
        String EncodedPassword=null;
        try {
            EncodedPassword= sha1handler.SHA1(Password);
        }catch (UnsupportedEncodingException | NoSuchAlgorithmException et){
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrim_150020690");
        PreparedStatement ps = session.prepare("select password from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return false;
        } else {
            for (Row row : rs) {
               
                String StoredPass = row.getString("password");
                if (StoredPass.compareTo(EncodedPassword) == 0)
                    return true;
            }
        }
   
    session.close();
    return false;  
    }
    public Hashtable getUser(String username){
       ResultSet record =null;
       Hashtable rs = new Hashtable();
       Set<String> emails = new HashSet<String>();
       //Map addresses = new HashMap();
       String value ="";
       Session session = cluster.connect("instagrim_150020690");
       UDTMapper<Address> mapper = new MappingManager(session).udtMapper(Address.class);
       PreparedStatement ps = session.prepare("select * from userprofiles where login =?");
       BoundStatement boundStatement = new BoundStatement(ps);
       record = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
       if (!record.isExhausted()) {
           for (Row row : record) {
               
               rs.put("firstname", row.getString("first_name"));
               rs.put("lastname", row.getString("last_name"));
               rs.put("age", row.getInt("age")); 
               rs.put("martial_status", (row.getString("martial_status")  ==null ? "" : row.getString("martial_status")) );
               rs.put("telephone", (row.getString("telephone")  ==null ? "" : row.getString("telephone")));
               //addresses = row.getMap("address", null, null);
               emails= row.getSet("email",String.class );
               Iterator it=emails.iterator();
                   while(it.hasNext())
                   {
                       value += (String)it.next();
                   }
                   rs.put("email", value);
               Map<String, UDTValue> addresses = row.getMap("addresses", String.class, UDTValue.class);
               for (String key : addresses.keySet()) {
                    Address address = mapper.fromUDT(addresses.get(key));
                    rs.put("address", address);
                }
            }
           
       }
       session.close();
       return rs;
    }   
    public void updateUser(String username,String firstname,String lastname,int age,String email, String telephone, String martial_status, String street, String city, String zip) {
        
        Session session = cluster.connect("instagrim_150020690");
        Set<String> emails = new HashSet<String>();
        emails.add(email);
        Map map = new HashMap();
        UserType addressType = cluster.getMetadata().getKeyspace("instagrim_150020690").getUserType("address");
        UDTValue address = addressType.newValue()
                               .setString("street", street)
                               .setString("city", city)
                               .setInt("zip", Integer.parseInt(zip));
        map.put("home", address);
        PreparedStatement ps = session.prepare("update userprofiles  set first_name = ?,last_name = ?,email = ?,age = ?, telephone= ?, martial_status = ?, addresses = ? where login = ?");
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        firstname,lastname,emails,age,telephone,martial_status ,map,username ));
   }
      public java.util.LinkedList<Person> getUsers(){
       Session session = cluster.connect("instagrim_150020690");
       java.util.LinkedList<Person> Persons = new java.util.LinkedList<>();
        ResultSet result = session.execute("select login, first_name, last_name from userprofiles");
        if (!result.isExhausted()) {
           for (Row row : result) {
              Person person = new Person();
              person.setFirstname(row.getString("first_name"));
              person.setLastname(row.getString("last_name"));
              person.setUsername(row.getString("login"));
              Persons.add(person);
           }
        }
        session.close();
        return Persons;
    }
    public boolean userExist(String username){
        boolean exist = false;
        Session session = cluster.connect("instagrim_150020690");
        PreparedStatement ps = session.prepare("select password from userprofiles where login =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        if (!rs.isExhausted()) {
            exist = true;
        }
        
        return exist;
    }
           
    
}
