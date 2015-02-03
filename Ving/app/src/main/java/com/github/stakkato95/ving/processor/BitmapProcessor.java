package com.github.stakkato95.ving.processor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.github.stakkato95.ving.source.HttpDataSource;
import com.github.stakkato95.ving.utils.IOUtils;

import java.io.InputStream;

/**
 * Created by Artyom on 21.11.2014.
 */
public class BitmapProcessor implements Processor<InputStream, Bitmap> {

    @Override
    public Bitmap process(InputStream inputStream) throws Exception {
        try {
            return BitmapFactory.decodeStream(inputStream);
        } finally {
            IOUtils.closeStream(inputStream);
        }
    }

}