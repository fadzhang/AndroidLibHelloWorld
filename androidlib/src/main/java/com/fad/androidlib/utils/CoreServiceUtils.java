package com.fad.androidlib.utils;

import android.content.Context;

import com.fad.androidlib.sharepreference.CommonPreferences;

/**
 * 实现的主要功能:
 * 核心服务工具
 * @author zf 创建日期：2016/6/6
 * @modify 修改者:,修改日期:,修改内容:.
 */
public class CoreServiceUtils {

    /**
     * 核心服务类的包路径+名称
     */
    private static String CORE_SERVICE_FULL_NAME = null;

    /**
     * 设置核心服务类的包路径，并存储至share preference
     * @param context 上下文
     * @param name 核心服务类的包路径+名称
     */
    public static void setCoreServiceFullName(Context context, String name){
        CORE_SERVICE_FULL_NAME = name;

        // 保存到share preference
        CommonPreferences.getInstance(context).save(CommonPreferences.CORE_SERVICE_FULL_NAME, name);
    }

    /**
     * 获取核心服务类的包路径+名称
     * @param context
     * @return
     */
    public static String getCoreServiceFullName(Context context){
        if (CORE_SERVICE_FULL_NAME == null){ // 如果全局变量被干掉了，从share preference中取出
            if (CommonPreferences.getInstance(context).check(CommonPreferences.CORE_SERVICE_FULL_NAME)){ // 如果share preference都没有，那就没办法了
                CORE_SERVICE_FULL_NAME = CommonPreferences.getInstance(context).get(CommonPreferences.CORE_SERVICE_FULL_NAME);
            }
        }

        return CORE_SERVICE_FULL_NAME;
    }
}
