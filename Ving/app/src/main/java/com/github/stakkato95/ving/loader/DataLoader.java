package com.github.stakkato95.ving.loader;

import android.os.Handler;
import android.os.Looper;

import com.github.stakkato95.ving.os.ZExecutor;
import com.github.stakkato95.ving.processor.DatabaseProcessor;
import com.github.stakkato95.ving.processor.Processor;
import com.github.stakkato95.ving.source.DataSource;

import java.io.InputStream;

/**
 * Created by Artyom on 17.01.2015.
 */
public class DataLoader {

    private interface BaseCallback {
        void onLoadingStarted();
        void onLoadingError(Exception e);
    }

    public static interface Callback<Output> extends BaseCallback {
        void onLoadingFinished(Output output);
    }

    public static interface DatabaseCallback extends BaseCallback {
        void onLoadingFinished();
    }

    private static final ZExecutor sExecutor;
    private final Handler mHandler;

    public DataLoader() {
        mHandler = new Handler();
    }

    static {
        sExecutor = new ZExecutor();
    }

    public void getDataToDatabase(final DatabaseCallback callback,
                                                          final String input,
                                                          final DataSource<String, InputStream> source,
                                                          final DatabaseProcessor processor) {
        sExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onLoadingStarted();
                        }
                    });
                    InputStream sourceOutput = source.getResult(input);
                    processor.process(sourceOutput);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onLoadingFinished();
                        }
                    });
                } catch (final Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onLoadingError(e);
                        }
                    });
                }
            }
        });
    }

    public <Input,SourceOutput,Output> void getDataAsynch(final Callback callback,
                                                       final Input input,
                                                       final DataSource<Input,SourceOutput> source,
                                                       final Processor<SourceOutput,Output> processor) {
        sExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onLoadingStarted();
                        }
                    });
                    SourceOutput sourceOutput = source.getResult(input);
                    final Output output = processor.process(sourceOutput);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onLoadingFinished(output);
                        }
                    });
                } catch (final Exception e){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onLoadingError(e);
                        }
                    });
                }
            }
        });
    }

    public static  <Input,SourceOutput,Output> Output getDataDirectly(Input input,
                                             DataSource<Input,SourceOutput> source,
                                             Processor<SourceOutput,Output> processor) throws Exception {
        SourceOutput sourceOutput = source.getResult(input);
        return processor.process(sourceOutput);
    }

}
