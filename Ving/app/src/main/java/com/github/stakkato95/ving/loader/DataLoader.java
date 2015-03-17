package com.github.stakkato95.ving.loader;

import com.github.stakkato95.ving.os.ZAsynchTask;
import com.github.stakkato95.ving.processor.DBProcessor;
import com.github.stakkato95.ving.processor.Processor;
import com.github.stakkato95.ving.source.DataSource;

import java.io.InputStream;

/**
 * Created by Artyom on 17.01.2015.
 */
public class DataLoader {

    private static interface BaseCallback {
        void onLoadingStarted();
        void onLoadingError(Exception e);
    }

    public static interface Callback<Output> extends BaseCallback {
        void onLoadingFinished(Output output);
    }

    public static interface DatabaseCallback extends BaseCallback {
        void onLoadingFinished();
    }

    public void getDataToDatabase(final DatabaseCallback callback,
                                                          final String input,
                                                          final DataSource<String, InputStream> source,
                                                          final DBProcessor processor) {
        new ZAsynchTask<String, Void>() {

            @Override
            public void onPreExecute() {
                callback.onLoadingStarted();
            }

            @Override
            public Void doInBackground(String s) throws Exception {
                InputStream sourceOutput = source.getResult(input);
                processor.process(sourceOutput);
                return null;
            }

            @Override
            public void onPostExecute(Void aVoid) {
                callback.onLoadingFinished();
            }

            @Override
            public void onException(Exception e) {
                callback.onLoadingError(e);
            }
        }.execute(input);
    }

    public <Input,SourceOutput,Output> void getDataAsync(final Callback callback,
                                                         final Input input,
                                                         final DataSource<Input, SourceOutput> source,
                                                         final Processor<SourceOutput, Output> processor) {
        new ZAsynchTask<Input,Output>() {
            @Override
            public void onPreExecute() {
                callback.onLoadingStarted();
            }

            @Override
            public Output doInBackground(Input input) throws Exception {
                SourceOutput sourceOutput = source.getResult(input);
                return processor.process(sourceOutput);
            }

            @Override
            public void onPostExecute(Output output) {
                callback.onLoadingFinished(output);
            }

            @Override
            public void onException(Exception e) {
                callback.onLoadingError(e);
            }
        }.execute(input);
    }

    public static  <Input,SourceOutput,Output> Output getDataDirectly(Input input,
                                             DataSource<Input,SourceOutput> source,
                                             Processor<SourceOutput,Output> processor) throws Exception {
        SourceOutput sourceOutput = source.getResult(input);
        return processor.process(sourceOutput);
    }

}
