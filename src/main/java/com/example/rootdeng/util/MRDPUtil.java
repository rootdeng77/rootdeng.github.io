package com.example.rootdeng.util;

public class MRDPUtil {
    public static String getStrings(String s){
        return s.substring(0,getLocation(s));
    }

    static int getLocation(String s){
        int i;
        for(i=0;i<s.length();i++){
            if(s.charAt(i)=='/'){
                break;
            }
        }
        return i;
    }
}
