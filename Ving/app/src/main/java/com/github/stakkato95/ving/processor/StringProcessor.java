package com.github.stakkato95.ving.processor;

import com.github.stakkato95.imageloader.assist.ImageLoaderAssistant;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Artyom on 21.11.2014.
 */
public class StringProcessor implements Processor<InputStream, String> {

    @Override
    public String process(InputStream inputStream) throws Exception {
        InputStreamReader inputStreamReader = null;
        BufferedReader in = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream);
            in = new BufferedReader(inputStreamReader);
            String str;
            StringBuilder builder = new StringBuilder();
            while ((str = in.readLine()) != null) {
                builder.append(str);
            }
            return builder.toString();
        } finally {
            ImageLoaderAssistant.closeStream(in, inputStream, inputStreamReader);
        }
    }

}