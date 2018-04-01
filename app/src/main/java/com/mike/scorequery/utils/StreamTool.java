package com.mike.scorequery.utils;

import android.graphics.Bitmap;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StreamTool {

    private static LogTools logTools = new LogTools("StreamTool");

    public static String decodeInputStream(InputStream in) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(in, "gbk"));
        StringBuffer buffer = new StringBuffer();
        String line = null;
        while ((line = br.readLine()) != null) {
            buffer.append(line + "\n");
        }
        br.close();
        String result = StringUtils.substring(buffer.toString(), 0, buffer.toString().length() - 1);
        logTools.d(buffer.toString());
        return result;
    }

    /**
     * 保存图片
     *
     * @param name
     * @param bitmap
     * @return
     */
    public static String saveBitmap(String name, Bitmap bitmap) {
        //获得系统当前时间，并以该时间作为文件名
        String paintPath = "";
        String str = name + ".png";
        File dir = new File("/sdcard/notes/");
        File file = new File("/sdcard/notes/", str);
        if (!dir.exists()) {
            dir.mkdir();
        } else {
            if (file.exists()) {
                file.delete();
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            //保存绘图文件路径
            paintPath = "/sdcard/notes/" + str;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return paintPath;
    }

    /*
     * 保存所绘图形
     * 返回绘图文件的存储路径
     * */
    public static String saveBitmap(Bitmap bitmap) {
        //获得系统当前时间，并以该时间作为文件名
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        String paintPath = "";
        str = str + "paint.png";
        File dir = new File("/sdcard/notes/");
        File file = new File("/sdcard/notes/", str);
        if (!dir.exists()) {
            dir.mkdir();
        } else {
            if (file.exists()) {
                file.delete();
            }
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            //保存绘图文件路径
            paintPath = "/sdcard/notes/" + str;


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return paintPath;
    }

    public static InputStream getStringStream(String sInputString) {
        if (sInputString != null && !sInputString.trim().equals("")) {
            try {
                ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
                return tInputStringStream;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public static List readFile(InputStream is) throws IOException {
        List list = new ArrayList<>();//总的
        List<String> list_province = new ArrayList<>();//总的省
        List<String> list_total = new ArrayList<>();//总的城市
        List<Integer> list_count = new ArrayList();//各省城市的个数集合
        Map<String, List<String>> map = new HashMap();//省市的map
        List<List<String>> list_citys=new ArrayList<>();//各省城市的集合;

        List<String> provinceNames = new ArrayList<>();//总的省名称
        List<List<String>> cityNames = new ArrayList<>();//总各省城市名称集合
        List<String> provinceCodes = new ArrayList<>();//总的省代码
        List<List<String>> cityCodes = new ArrayList<>();//总各省城市代码集合

        BufferedReader br = new BufferedReader(new InputStreamReader(is, "gbk"));
        int a = 0;
        int b = 0;
        String line = null;
        while ((line = br.readLine()) != null) {
            String strc = null;
            line = line.trim();
            String str = StringUtils.substring(line, line.length() - 1, line.length());
            if (",".equals(str)) {
                list_count.add(b);
                a++;
                b = 0;
                int index = StringUtils.indexOf(line, ",");
                String pstr = StringUtils.substring(line, 0, index);
                list_province.add(pstr);
                provinceNames.add(StringUtils.split(pstr,"|")[0].trim());
                provinceCodes.add(StringUtils.split(pstr,"|")[1].trim());
            } else {
                b++;
                strc = line.trim();
                list_total.add(strc);
            }
        }

        list_count.add(b);
        list_count.remove(0);
        logTools.d(a);
        logTools.d(list_count.size());
        logTools.d(list_total);
        logTools.d(list_total.size());
        logTools.d(list_count);
        logTools.d(list_province);

        int sum = 0;
        int t = 0;
        for (int i = 0; i < list_count.size(); i++) {
            List<String> list_city = new ArrayList<>();
            List<String> list_cityNames = new ArrayList<>();
            List<String> list_cityCodes = new ArrayList<>();
            for (int j = sum; j < list_total.size(); j++) {
                if (t == list_count.get(i)) {
                    t = 0;
                    sum = sum + list_count.get(i);
                    break;
                }
                list_city.add(list_total.get(j));
                list_cityNames.add(StringUtils.split(list_total.get(j),"|")[0].trim());
                list_cityCodes.add(StringUtils.split(list_total.get(j),"|")[1].trim());
                t++;
            }
            map.put(list_province.get(i), list_city);
            list_citys.add(list_city);
            cityNames.add(list_cityNames);
            cityCodes.add(list_cityCodes);
        }
        logTools.d(map);
        br.close();
        list.add(provinceNames);
        list.add(provinceCodes);
        list.add(cityNames);
        list.add(cityCodes);
        list.add(list_province);
        list.add(list_citys);
        list.add(map);
        return list;
    }
}