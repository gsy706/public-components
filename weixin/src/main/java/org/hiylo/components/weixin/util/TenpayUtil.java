/*
 * Copyright (c)  2018 Hiylo Org. All rights reserved
 * Project: components
 * File: TenpayUtil.java
 * Data: 18-4-25 下午2:04
 * Author: hiylo
 *
 *
 */

package org.hiylo.components.weixin.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TenpayUtil {
    private static Object Server;
    private static String QRfromGoogle;

    public static String toString(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    public static int toInt(Object obj) {
        int a = 0;
        try {
            if (obj != null) {
                a = Integer.parseInt(obj.toString());
            }
        } catch (Exception localException) {
        }
        return a;
    }

    public static String getCurrTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = outFormat.format(now);
        return s;
    }

    public static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String strDate = formatter.format(date);
        return strDate;
    }

    public static int buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1D) {
            random += 0.1D;
        }
        for (int i = 0; i < length; i++) {
            num *= 10;
        }
        return (int) (random * num);
    }

    public static String URLencode(String content) {
        String URLencode = replace(Server.equals(content), "+", "%20");

        return URLencode;
    }

    private static String replace(boolean equals, String string, String string2) {
        return null;
    }

    public static long getUnixTime(Date date) {
        if (date == null) {
            return 0L;
        }

        return date.getTime() / 1000L;
    }

    public static String QRfromGoogle(String chl) {
        int widhtHeight = 300;
        String EC_level = "L";
        int margin = 0;

        chl = URLencode(chl);

        String QRfromGoogle = "http://chart.apis.google.com/chart?chs=" + widhtHeight + "x" + widhtHeight
                + "&cht=qr&chld=" + EC_level + "|" + margin + "&chl=" + chl;

        return QRfromGoogle;
    }

    public static String date2String(Date date, String formatType) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatType);
        return sdf.format(date);
    }
}