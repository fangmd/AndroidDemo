package com.fangmingdong.androiddemo;

import android.app.Application;

import com.fangmingdong.androiddemo.backgroundforeground.BackgroundForegroundDelegate;
import com.fangmingdong.androiddemo.backgroundforeground.BackgroundForegroundHandler;

/**
 * Created by nerc on 2018/2/7.
 */

public class App extends Application implements BackgroundForegroundDelegate {

    private static App sApp;
    public static App getInstance() {
        return sApp;
    }

    private BackgroundForegroundHandler mBackgroundForegroundHandler;
    private boolean mIsBackground = true;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        mBackgroundForegroundHandler = new BackgroundForegroundHandler(this);
        registerLifecycleHandler();
    }

    private void registerLifecycleHandler() {
        registerActivityLifecycleCallbacks();
        registerComponentCallbacks();
    }

    private void unregisterLifecycleHandler() {
        unregisterActivityLifecycleCallbacks();
        unregisterComponentCallbacks();
    }

    public void registerActivityLifecycleCallbacks() {
        synchronized (sApp) {
            registerActivityLifecycleCallbacks(mBackgroundForegroundHandler);
        }
    }

    public void unregisterActivityLifecycleCallbacks() {
        synchronized (sApp) {
            unregisterActivityLifecycleCallbacks(mBackgroundForegroundHandler);
        }
    }

    public void registerComponentCallbacks() {
        synchronized (mBackgroundForegroundHandler) {
            this.registerComponentCallbacks(mBackgroundForegroundHandler);
        }
    }

    public void unregisterComponentCallbacks() {
        synchronized (mBackgroundForegroundHandler) {
            this.unregisterComponentCallbacks(mBackgroundForegroundHandler);
        }
    }

    @Override
    public void onBackground() {
        if (mIsBackground) {
            return;
        }
        mIsBackground = true;
        // do something
    }

    @Override
    public void onForeground() {
        if (mIsBackground) {
            mIsBackground = false;

            // do something
        }
    }
}
