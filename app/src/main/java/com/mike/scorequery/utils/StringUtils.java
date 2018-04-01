package com.mike.scorequery.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 2016/2/17.
 */
public class StringUtils {

    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static String[] split(String str, String separatorChars)
    {
        return splitWorker(str, separatorChars, -1, false);
    }

    private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens)
    {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }
        List list = new ArrayList();
        int sizePlus1 = 1;
        int i = 0; int start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if (separatorChars == null)
        {
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if ((match) || (preserveAllTokens)) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    i++; start = i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        }
        if (separatorChars.length() == 1)
        {
            char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if ((match) || (preserveAllTokens)) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    i++; start = i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        }
        else {
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if ((match) || (preserveAllTokens)) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    i++; start = i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        }
        if ((match) || ((preserveAllTokens) && (lastMatch))) {
            list.add(str.substring(start, i));
        }
        return (String[])list.toArray(new String[list.size()]);
    }

    public static int indexOf(CharSequence seq, CharSequence searchSeq)
    {
        if ((seq == null) || (searchSeq == null)) {
            return -1;
        }
        return CharSequenceUtils.indexOf(seq, searchSeq, 0);
    }
    public static class CharSequenceUtils
    {
        public static CharSequence subSequence(CharSequence cs, int start)
        {
            return cs == null ? null : cs.subSequence(start, cs.length());
        }

        static int indexOf(CharSequence cs, int searchChar, int start)
        {
            if ((cs instanceof String)) {
                return ((String)cs).indexOf(searchChar, start);
            }
            int sz = cs.length();
            if (start < 0) {
                start = 0;
            }
            for (int i = start; i < sz; i++) {
                if (cs.charAt(i) == searchChar) {
                    return i;
                }
            }
            return -1;
        }

        static int indexOf(CharSequence cs, CharSequence searchChar, int start)
        {
            return cs.toString().indexOf(searchChar.toString(), start);
        }

        static int lastIndexOf(CharSequence cs, int searchChar, int start)
        {
            if ((cs instanceof String)) {
                return ((String)cs).lastIndexOf(searchChar, start);
            }
            int sz = cs.length();
            if (start < 0) {
                return -1;
            }
            if (start >= sz) {
                start = sz - 1;
            }
            for (int i = start; i >= 0; i--) {
                if (cs.charAt(i) == searchChar) {
                    return i;
                }
            }
            return -1;
        }

        static int lastIndexOf(CharSequence cs, CharSequence searchChar, int start)
        {
            return cs.toString().lastIndexOf(searchChar.toString(), start);
        }

        static char[] toCharArray(CharSequence cs)
        {
            if ((cs instanceof String)) {
                return ((String)cs).toCharArray();
            }
            int sz = cs.length();
            char[] array = new char[cs.length()];
            for (int i = 0; i < sz; i++) {
                array[i] = cs.charAt(i);
            }
            return array;
        }

        static boolean regionMatches(CharSequence cs, boolean ignoreCase, int thisStart, CharSequence substring, int start, int length)
        {
            if (((cs instanceof String)) && ((substring instanceof String))) {
                return ((String)cs).regionMatches(ignoreCase, thisStart, (String)substring, start, length);
            }

            return cs.toString().regionMatches(ignoreCase, thisStart, substring.toString(), start, length);
        }
    }

    public static String substring(String str, int start)
    {
        if (str == null) {
            return null;
        }

        if (start < 0) {
            start = str.length() + start;
        }

        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return "";
        }

        return str.substring(start);
    }


    public static String substring(String str, int start, int end)
    {
        if (str == null) {
            return null;
        }

        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }

        if (end > str.length()) {
            end = str.length();
        }

        if (start > end) {
            return "";
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        return str.substring(start, end);
    }

    public static String NumberCharToHexASCII(String str){

        char[] chars=str.toCharArray();
        StringBuffer buffer= new StringBuffer();
        for (int i = 0; i <chars.length; i++) {
            int ascii = chars[i];
            int hex_ascii=ascii-18;
            String hex= String.valueOf(hex_ascii);
            buffer.append(hex);
        }
        return buffer.toString();

    }

    public static String reverseString(String string){
       return TextUtils.isEmpty(string)?"":new StringBuffer(string).reverse().toString();
    }
}

