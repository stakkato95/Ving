package com.github.stakkato95.ving.loader;

import android.os.Handler;

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
                InputStream sourceOutput = null;

                try {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onLoadingStarted();
                        }
                    });
                    sourceOutput = source.getResult(input);


                } catch (final Exception e){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onLoadingError(e);
                        }
                    });
                } finally {
                    try {
                        processor.process(sourceOutput);
                    } catch (Exception e) {
                        //exception is just impossible
                    } finally {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onLoadingFinished();
                            }
                        });
                    }
                }
            }
        });
    }

    public <Input,SourceOutput,Output> void getData(final Callback callback,
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

}
