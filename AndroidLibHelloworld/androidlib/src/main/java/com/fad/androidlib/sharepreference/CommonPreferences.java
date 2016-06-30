package com.fad.androidlib.sharepreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;


/**
 * 实现的主要功能:
 * share preference存储
 *
 * @author zf 创建日期：2016/1/28
 * @modify 修改者:,修改日期:,修改内容:.
 */
public class CommonPreferences {
    private static final String PREFRENCE_NAME = "CommonPreferences";

    private static CommonPreferences commonPreferences = null;

    /**
     * SharedPreferences对象
     */
    private SharedPreferences spPrefs;

    /**
     * 上下文
     */
    private Context cContext;

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    private CommonPreferences(@NonNull Context context) {
        cContext = context.getApplicationContext();
        initPrefs();
    }

    /**
     * 静态工厂方法
     */
    public static CommonPreferences getInstance(Context context) {
        if (commonPreferences == null) {
            commonPreferences = new CommonPreferences(context);
        }
        return commonPreferences;
    }

    /**
     * 初始化SharedPreferences
     */
    private void initPrefs() {
        spPrefs = cContext.getSharedPreferences(PREFRENCE_NAME, 0);
    }

    public static String CORE_SERVICE_FULL_NAME = "core_service_full_name";//核心服务类的包路径+名称
    public static String UUID = "uuid";//获取IMEI失败、序列号（sn）失败后生成uuid进行存储

    /**
     * 检查是否存在某一个数据
     */
    public boolean check(String key) {
        if (spPrefs.contains(key) == false) {
            return false;
        }
        return true;
    }

    /**
     * 保存数据到sharedpreferences
     */
    public void save(String key, String value) {
        if (spPrefs == null) {
            return;
        }
        SharedPreferences.Editor prefEditor = spPrefs.edit();
        prefEditor.putString(key, value);
        prefEditor.commit();
    }

    /**
     * 获取sharedpreferences中的数据
     */
    public String get(String key) {

        if (spPrefs == null) {
            return null;
        }

        return spPrefs.getString(key, null);
    }

    /**
     * 删除某一条数据
     */
    public void remove(String key) {
        if (spPrefs == null) {
            return;
        }
        SharedPreferences.Editor prefEditor = spPrefs.edit();
        prefEditor.remove(key);
        prefEditor.commit();
    }


    /**
     * 删除全部数据
     */
    public void removeAll() {
        if (spPrefs == null) {
            return;
        }
        SharedPreferences.Editor prefEditor = spPrefs.edit();
        prefEditor.clear();
        prefEditor.commit();
    }


}
