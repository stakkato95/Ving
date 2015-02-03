package com.github.stakkato95.ving.api;

import android.content.Context;

import com.github.stakkato95.ving.processor.Processor;
import com.github.stakkato95.ving.source.VkDataSource;

/**
 * Created by Artyom on 02.02.2015.
 */
public class ShipperBuilder {

    private String mMessage;
    private String mRequestField;
    private Processor mProcessor;
    private Shipper.Callback mCallback;
    private VkDataSource mDataSource;

    public ShipperBuilder setMessageText(String message) {
        mMessage = message;
        return this;
    }

    public ShipperBuilder setRequestField(String requestField) {
        mRequestField = requestField;
        return this;
    }

    public ShipperBuilder setCallback(Shipper.Callback callback) {
        mCallback = callback;
        return this;
    }

    public ShipperBuilder setProcessor(Processor processor) {
        mProcessor = processor;
        return this;
    }

    public ShipperBuilder setContext(Context context) {
        mDataSource = VkDataSource.get(context);
        return this;
    }

    public Shipper build() {
        String query = Api.sendMessage() + mRequestField + "&" + Api.FIELD_MESSAGE + mMessage;

        return new Shipper(query, mDataSource, mProcessor, mCallback);
    }

}
