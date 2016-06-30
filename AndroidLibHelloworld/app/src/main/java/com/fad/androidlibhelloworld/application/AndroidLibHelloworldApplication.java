package com.fad.androidlibhelloworld.application;

import android.app.Application;

import com.fad.androidlib.utils.CoreServiceUtils;

/**
 * 实现的主要功能:
 * application
 *
 * @author zf 创建日期：2016/6/29
 * @modify 修改者:,修改日期:,修改内容:.
 */
public class AndroidLibHelloworldApplication extends Application{

    private static AndroidLibHelloworldApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        // 设置app coreservice
        if (CoreServiceUtils.getCoreServiceFullName(getApplicationContext()) == null)
            CoreServiceUtils.setCoreServiceFullName(getApplicationContext(), "com.fad.androidlibhelloworld.service.AppCoreService");
    }

    public static AndroidLibHelloworldApplication getApplication(){
        return instance;
    }
}
