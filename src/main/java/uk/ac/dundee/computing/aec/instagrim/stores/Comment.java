/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.stores;
import java.util.Date;

/**
 *
 * @author Salano
 */
public class Comment {
    private java.util.UUID commentid;
    private java.util.UUID picid;
    private Date date_added;
    private String username;
    private String comment_text;
    public void Comment(){
        
    }
    
    public void setCommenId(java.util.UUID commentid){
        this.commentid = commentid;
    }
    public void setPicId(java.util.UUID picid){
        this.picid = picid;
    }
    public void setDate(Date date_added){
        this.date_added = date_added;
    }
    public void setUserName(String username){
        this.username = username;
    }
    public void setCommenText(String comment_text){
        this.comment_text = comment_text;
    }
    
    public java.util.UUID getCommenId(){
        return this.commentid;
    }
    public java.util.UUID getPicId(){
        return this.picid;
    }
    public Date getDate(){
        return this.date_added;
    }
    public String getUserName(){
        return this.username;
    }
    public String getCommenText(){
        return this.comment_text;
    }
}
