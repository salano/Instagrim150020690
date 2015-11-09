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
import java.util.Date;
import java.util.Hashtable;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.stores.Comment;
import uk.ac.dundee.computing.aec.instagrim.lib.*;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

/**
 *
 * @author Salano
 */
public class CommentModel {
    private Cluster cluster;
    public void commentModel(){
        
    }
    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
    
    public void addComment(java.util.UUID picid, String username, String comment){
        Session session = cluster.connect("instagrim_150020690");
        Convertors convertor = new Convertors();
        java.util.UUID commentid = convertor.getTimeUUID();
        Date date = new Date();
        PreparedStatement ps = session.prepare("insert into comments (commentid,picid,user,date_added,comment_text) Values(?,?,?,?,?)");
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        commentid,picid,username,date,comment));
        //We are assuming this always works.  Also a transaction would be good here !
        session.close();
        
    }
    
    public void removeComment(java.util.UUID commentid){
        Session session = cluster.connect("instagrim_150020690");
        PreparedStatement ps = session.prepare("delete from comments where commentid = ?");
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        commentid));
        //We are assuming this always works.  Also a transaction would be good here !
        session.close();
    }
       
    public java.util.LinkedList<Comment> getComment(java.util.UUID picid){
        Session session = cluster.connect("instagrim_150020690"); 
        java.util.LinkedList<Comment> Comments = new java.util.LinkedList<>();
        ResultSet records =null;
        PreparedStatement ps = session.prepare("select * from comments where picid = ?");
        BoundStatement boundStatement = new BoundStatement(ps);
        records = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        picid));
        if (!records.isExhausted()) {
           for (Row row : records) {
               Comment comment = new Comment();
               comment.setCommenId(row.getUUID("commentid"));
               comment.setPicId(row.getUUID("picid"));
               comment.setDate(row.getDate("date_added"));
               comment.setUserName(row.getString("user"));
               comment.setCommenText(row.getString("comment_text"));
               Comments.add(comment);
           }
        }
               //addresses = row.getMap("address", null, null);
        //We are assuming this always works.  Also a transaction would be good here !
        session.close();
        return Comments;
    }
    
           
}
