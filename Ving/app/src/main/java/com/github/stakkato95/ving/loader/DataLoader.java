package com.github.stakkato95.ving.loader;

import android.content.Context;
import android.os.Handler;

import com.github.stakkato95.ving.os.VingExecutor;
import com.github.stakkato95.ving.processor.DatabaseProcessor;
import com.github.stakkato95.ving.processor.Processor;
import com.github.stakkato95.ving.source.DataSource;

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

    private static final VingExecutor sExecutor;
    private final Handler mHandler;

    public DataLoader(Context context) {
        mHandler = new Handler();
    }

    static {
        sExecutor = new VingExecutor();
    }

    public <Input,SourceOutput> void getDataToDatabase(final DatabaseCallback callback,
                                                          final Input input,
                                                          final DataSource<Input, SourceOutput> source,
                                                          final DatabaseProcessor<SourceOutput> processor) {
        sExecutor.execute(new Runnable() {
            @Override
            public void run() {
                SourceOutput sourceOutput = null;

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
