package com.github.stakkato95.ving.manager;


import android.support.annotation.NonNull;

import com.github.stakkato95.ving.os.AsyncTask;
import com.github.stakkato95.ving.processing.Processor;
import com.github.stakkato95.ving.source.DataSource;

/**
 * Created by Artyom on 19.11.2014.
 */
public class DataManager {

    public static interface Callback<Result> {
        void onDataLoadStart();

        void onDone(Result data);

        void onError(Exception e);
    }

    public static <Result, DataSourceResult, Param> void loadData(@NonNull final Callback<Result> callback,
                                                                  @NonNull final Param param,
                                                                  @NonNull final DataSource<DataSourceResult, Param> dataSource,
                                                                  @NonNull final Processor<Result, DataSourceResult> processor) {
        executeInAsyncTask(callback, param, dataSource, processor);
    }

    private static <Result, DataSourceResult, Param> void executeInAsyncTask(final Callback<Result> callback,
                                                                             Param param,
                                                                             final DataSource<DataSourceResult, Param> dataSource,
                                                                             final Processor<Result, DataSourceResult> processor) {
        new AsyncTask<Param, Void, Result>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                callback.onDataLoadStart();
            }

            @Override
            protected void onPostExecute(Result result) {
                super.onPostExecute(result);
                callback.onDone(result);
            }

            @Override
            protected Result doInBackground(Param param) throws Exception {
                DataSourceResult dataSourceResult = dataSource.getResult(param);
                return processor.process(dataSourceResult);
            }

            @Override
            protected void onPostException(Exception e) {
                callback.onError(e);
            }

        }.execute(param);
    }

}