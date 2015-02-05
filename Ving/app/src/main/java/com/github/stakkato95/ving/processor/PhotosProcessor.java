package com.github.stakkato95.ving.processor;

import com.github.stakkato95.ving.bo.JSONArrayWrapper;
import com.github.stakkato95.ving.bo.Photo;

import java.io.InputStream;

/**
 * Created by Artyom on 05.02.2015.
 */
public class PhotosProcessor implements Processor<InputStream, Photo[]> {

    @Override
    public Photo[] process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONArrayWrapper jsonArray = new JSONArrayWrapper(string);
        Photo[] photos = new Photo[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            photos[i] = new Photo(jsonArray.getJSONObject(i));
        }
        return photos;
    }

}
