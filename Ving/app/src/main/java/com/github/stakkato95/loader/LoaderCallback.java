package com.github.stakkato95.loader;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by Artyom on 14.12.2014.
 */
public interface LoaderCallback {

    void onLoadingFinished(Bitmap bmp, String url, ImageView imageView);

}
