package es.codeurjc.holamundo.service;

import java.util.HashMap;
import java.util.Map;

public class UserList {
    //Data structure will store info user(UserName, role, alias, description, profileImage, email, password)
    private Map<String, String[]> users = new HashMap<>(); //Map<UserName, UserInfo:String>

    //Constructor
    public UserList(){
        users.put("BookReader_14", new String[]{"BookReader_14", "User", "BookReader", "I'm a reader fan that loves fantasy books", "", "bookreader14@gmail.com", "12345678"});
        users.put("FanBook_785", new String[]{"FanBook_785", "User", "FanB", "I love books", "", "fanBook@gmail.com", "12345678"});
        users.put("YourReader", new String[]{"YourReader", "User", "YourReader", "I'm a reader", "", "reader@gmail.com", "12345678"});
    }

    public Map<String, String[]> getUser(){
        return users;
    }
    
    public String[] getUserInfo(String userName){
        return users.get(userName);
    }
}
