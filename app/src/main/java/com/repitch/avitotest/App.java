package com.repitch.avitotest;

import android.app.Application;

public class App extends Application {
    public static final String TAG = "Chokavo logs";

    private static App application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static App getInstance() {
        return application;
    }

}
