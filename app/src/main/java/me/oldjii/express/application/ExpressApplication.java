package me.oldjii.express.application;

import android.app.Application;

/**
 * Created by oldjii on 2019/4
 */

//单例
public class ExpressApplication extends Application {
    private static ExpressApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static ExpressApplication getInstance() {
        return sInstance;
    }
}
