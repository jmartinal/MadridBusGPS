package com.jmartinal.madridbusgps.Utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by Jorge on 12/12/2015.
 */
public class App extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}
