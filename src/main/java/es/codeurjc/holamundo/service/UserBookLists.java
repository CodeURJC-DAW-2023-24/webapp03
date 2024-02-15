package es.codeurjc.holamundo.service;

import java.util.HashMap;
import java.util.Map;

public class UserBookLists {
    //Data structure will store book list form user
    private Map<String, Integer[]> readed = new HashMap<>(); //Map<UserName, String[]> Books readed
    private Map<String, Integer[]> reading = new HashMap<>(); //Map<UserName, String[]> Books reading
    private Map<String, Integer[]> wanted = new HashMap<>(); //Map<UserName, String[]> Books wanted to read

    //Constructor
    public UserBookLists(){
        readed.put("BookReader_14", new Integer[]{1, 2, 4, 5, 6, 7});
        reading.put("BookReader_14", new Integer[]{3});
        wanted.put("BookReader_14", new Integer[]{4, 5, 6, 7});

        readed.put("FanBook_785", new Integer[]{1});
        reading.put("FanBook_785", new Integer[]{});
        wanted.put("FanBook_785", new Integer[]{4, 2});

        readed.put("YourReader", new Integer[]{1, 2, 4, 7, 10, 6});
        reading.put("YourReader", new Integer[]{3, 5});
        wanted.put("YourReader", new Integer[]{18, 20, 12});
    }

    public Integer[] getReadedList(String userName){
        return readed.get(userName);
    }

    public Integer[] getReadingList(String userName){
        return reading.get(userName);
    }

    public Integer[] getWantedList(String userName){
        return wanted.get(userName);
    }
}
