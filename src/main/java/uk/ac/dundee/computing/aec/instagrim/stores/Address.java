/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.stores;
import com.datastax.driver.mapping.annotations.UDT;
import com.datastax.driver.mapping.annotations.Field;

/**
 *
 * @author Salano
 */
@UDT(name = "address", keyspace = "instagrim_150020690")
public class Address {
    public void Address(){
        
    }
    
    @Field(name = "street")
    private String street;
    @Field(name = "city")
    private String city;
    @Field(name = "zip")
    private int zip ;
    
    public void setStreet(String street){
        this.street = street;
    }
    public void setCity(String city){
        this.city = city;
    }
    public void setZip(int zip){
        this.zip = zip;
    }
    
    public String getStreet(){
        return this.street;
    }
    public String getCity(){
        return this.city;
    }
    public int getZip(){
        return this.zip;
    }
}
