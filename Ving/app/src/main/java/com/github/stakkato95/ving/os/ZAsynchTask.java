package com.github.stakkato95.ving.os;

import android.os.Handler;

/**
 * Created by Artyom on 02.02.2015.
 */
public abstract class ZAsynchTask<Input,Output> {

    private static final ZExecutor sExecutor;

    static {
        sExecutor = new ZExecutor();
    }

    public void onPreExecute() {}

    public abstract Output doInBackground(Input input) throws Exception;

    public abstract void onPostExecute(Output output);

    public void onException(Exception e) {}

    public void execute(final Input input) {
        final Handler handler = new Handler();
        onPreExecute();
        sExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final Output output = doInBackground(input);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onPostExecute(output);
                        }
                    });
                } catch (final Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onException(e);
                        }
                    });
                }
            }
        });
    }

}
