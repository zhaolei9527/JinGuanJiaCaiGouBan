package com.jinguanjiacaigouban;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.jinguanjiacaigouban.utils.PausableThreadPoolExecutor;
import com.mob.MobSDK;
import com.tencent.bugly.Bugly;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by 赵磊 on 2017/7/12.
 */

public class App extends MultiDexApplication {

    public static Context context;
    public static PausableThreadPoolExecutor pausableThreadPoolExecutor;

    protected String getAppkey() {
        return null;
    }

    protected String getAppSecret() {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        MobSDK.init(this);
        MultiDex.install(this);
        Bugly.init(getApplicationContext(), "e4d6b083e4", false);
        pausableThreadPoolExecutor = new PausableThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());
    }

}
