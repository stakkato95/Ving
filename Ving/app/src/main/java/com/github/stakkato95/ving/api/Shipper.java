package com.github.stakkato95.ving.api;

import com.github.stakkato95.ving.os.ZAsynchTask;
import com.github.stakkato95.ving.processor.Processor;
import com.github.stakkato95.ving.source.VkDataSource;

import java.io.InputStream;

/**
 * Created by Artyom on 02.02.2015.
 */
public class Shipper<Output> {

    public interface Callback<Output> {
        void onShippingPerformed(Output output);
    }

    private String mQuery;
    private Processor<InputStream, Output> mProcessor;
    private Callback<Output> mCallback;
    private VkDataSource mDataSource;

    public Shipper(String query, VkDataSource dataSource, Processor<InputStream, Output> processor, Callback<Output> callback) {
        mQuery = query;
        mDataSource = dataSource;
        mProcessor = processor;
        mCallback = callback;
    }

    public void send() {
        //todo the same for DataLoder
        new ZAsynchTask<String, Output>() {

            @Override
            public void onPreExecute() {

            }

            @Override
            public Output doInBackground(String input) throws Exception {
                InputStream stream = mDataSource.getResult(input);
                return mProcessor.process(stream);
            }

            @Override
            public void onPostExecute(Output output) {
                mCallback.onShippingPerformed(output);
            }

            @Override
            public void onException(Exception e) {

            }

        }.execute(mQuery);
    }

}
