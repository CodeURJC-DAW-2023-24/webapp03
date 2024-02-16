package es.codeurjc.holamundo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserBookLists {
    //Data structure will store book list form user
    private Map<String, Integer[]> read = new HashMap<>(); //Map<UserName, String[]> Books read
    private Map<String, Integer[]> reading = new HashMap<>(); //Map<UserName, String[]> Books reading
    private Map<String, Integer[]> wanted = new HashMap<>(); //Map<UserName, String[]> Books wanted to read

    //Constructor
    public UserBookLists() {
        read.put("BookReader_14", new Integer[]{1, 2, 4, 5, 6, 7});
        reading.put("BookReader_14", new Integer[]{3});
        wanted.put("BookReader_14", new Integer[]{4, 5, 6, 7});

        read.put("FanBook_785", new Integer[]{1});
        reading.put("FanBook_785", new Integer[]{});
        wanted.put("FanBook_785", new Integer[]{4, 2});

        read.put("YourReader", new Integer[]{1, 2, 4, 7});
        reading.put("YourReader", new Integer[]{3, 5});
        wanted.put("YourReader", new Integer[]{});
    }

    public Integer[] getReadList(String userName) {
        return read.get(userName);
    }

    public Integer[] getReadingList(String userName) {
        return reading.get(userName);
    }

    public Integer[] getWantedList(String userName) {
        return wanted.get(userName);
    }

    public ArrayList<Integer> getRangeList(int page, int size, String listType, String userName) {
        Integer[] list = new Integer[0];
        ArrayList<Integer> listToReturn = new ArrayList<>();
        switch (listType) {
            case "read":
                list = getReadList(userName);
                break;
            case "reading":
                list = getReadingList(userName);
                break;
            case "wanted":
                list = getWantedList(userName);
                break;
        }
        for (int i = page; i < size && i < list.length; i++) {
            listToReturn.add(list[i]);
        }
        return listToReturn;
    }
}
