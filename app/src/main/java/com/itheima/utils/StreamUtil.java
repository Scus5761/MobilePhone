package com.itheima.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * Created by ThinkPad on 2017-05-17.
 */

public class StreamUtil {

    public static String parserStreamUtil(InputStream inputStream) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringWriter stringWriter = new StringWriter();
        String str=null;

        while ((str=reader.readLine())!=null) {
            stringWriter.write(str);
        }
        reader.close();
        stringWriter.close();

        return stringWriter.toString();
    }

}
