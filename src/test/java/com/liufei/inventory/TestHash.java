package com.liufei.inventory;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

public class TestHash {
    public static void main(String[] args) {
        /*for (Integer i = 0; i < 18 ; i++) {
            System.out.println(i&7);
        }*/
        Hashtable<Object, Object> hashtable = new Hashtable<>();
        HashMap<Object, Object> hashMap = new HashMap<>();
        ConcurrentHashMap<Object, Object> concurrentHashMap = new ConcurrentHashMap<>();
    }

    public static int getHash(Integer key){
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
