/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.stores;

/**
 *
 * @author Salano
 */
public class Person {
    private String firstname;
    private String lastname;
    private String username; 
    
    public void Person(){
        
    }
    public void setFirstname(String firstname){
        this.firstname = firstname;
    }
    public void setLastname(String lastname){
        this.lastname = lastname;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getFirstname(){
        return this.firstname;
    }
    public String getLastname(){
        return this.lastname;
    }
    public String getUsername(){
        return this.username;
    }
}
