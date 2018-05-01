package com.nikitagordia.chatme.utils;

/**
 * Created by nikitagordia on 3/30/18.
 */

public class StringUtils {

    public static String cut(String str, int len) {
        if (str.length() > len - 3) {
            return str.substring(0, len - 3) + "...";
        } else return str;
    }
}
