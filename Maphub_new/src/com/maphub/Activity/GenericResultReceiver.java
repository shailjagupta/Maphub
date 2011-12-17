package com.maphub.Activity;


import android.os.Bundle;
import android.os.ResultReceiver;
import android.os.Handler;

/**
 * 
 * @author Shailja
 * To receive the results from imageService
 *
 */

public class GenericResultReceiver extends ResultReceiver {
    private Receiver mReceiver;

    public GenericResultReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}