package com.fad.androidlib.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.fad.androidlib.sharepreference.CommonPreferences;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * 实现的主要功能:
 * 通用工具类
 *
 * @author zf 创建日期：2016/6/6
 * @modify 修改者:,修改日期:,修改内容:.
 */
public class CommonUtils {

    /**
     * 判断service是否正在运行
     *
     * @param context     上下文
     * @param serviceName service名称
     * @return service是否在运行
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取系统版本号名 versionName
     *
     * @param context 上下文
     * @return versionName
     */
    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pinfo = null;
        try {
            pinfo = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (pinfo != null) {
            return pinfo.versionName;
        } else {
            return "";
        }
    }

    /**
     * 获取系统版本code versionCode
     *
     * @param context 上下文
     * @return versionCode
     */
    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pInfo = null;
        try {
            pInfo = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (pInfo != null) {
            return pInfo.versionCode;
        } else {
            return -1;
        }
    }

    /**
     * 检查当前网络是否可用
     *
     * @param con
     * @return
     */

    public static boolean isNetworkAvailable(Context con) {
        Context context = con.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取设备唯一ID
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        StringBuilder deviceId = new StringBuilder();
        // 渠道标志 androd:a
        deviceId.append("a");

        try {
            //IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (!TextUtils.isEmpty(imei)) {
                deviceId.append("imei");
                deviceId.append(imei);
                Log.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }

            //序列号（sn）
            String sn = tm.getSimSerialNumber();
            if (!TextUtils.isEmpty(sn)) {
                deviceId.append("sn");
                deviceId.append(sn);
                Log.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }

            //如果上面都没有， 则生成一个id：随机码
            String uuid = getUUID(context);
            if (!TextUtils.isEmpty(uuid)) {
                deviceId.append("id");
                deviceId.append(uuid);
                Log.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("id").append(getUUID(context));
        }

        Log.e("getDeviceId : ", deviceId.toString());

        return deviceId.toString();
    }

    /**
     * 得到全局唯一UUID
     */
    private static String getUUID(Context context) {
        CommonPreferences mShare = CommonPreferences.getInstance(context);
        String uuid = null;
        if (mShare != null) {
            uuid = mShare.get(CommonPreferences.UUID);
        }

        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            mShare.save(CommonPreferences.UUID, uuid);
        }

        Log.e("getUUID", "getUUID : " + uuid);
        return uuid;
    }

    /**
     * 判断字符串是否含有中文
     *
     * @param str
     * @return
     */
    public static boolean isStrHasChinese(String str) {
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前时间戳
     * @return 时间戳 long
     */
    public static long getLongSystemTime() {
        return System.currentTimeMillis();
    }

    public static final int NET_STATE_NOT_CONNECTED = 0;//无网络连接
    public static final int NET_STATE_CONNECTED_WIFI = 1;//无网络连接
    public static final int NET_STATE_CONNECTED_MOBILE = 2;//无网络连接

    /**
     * 获取当前网络状态
     * @param context
     * @return
     */
    public static int getCurrentNetState(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (gprs == null) { // gprs是空，说明该设备没有sim卡插槽，可能是个带wifi的pad
            if (wifi == null) { // 如果连wifi都是空的，那就没法上网了擦
                // 网络连接断开
                return NET_STATE_NOT_CONNECTED;
            } else if (!wifi.isConnected()) { // 如果wifi没有连接，木有网呀
                // 网络连接断开
                return NET_STATE_NOT_CONNECTED;
            } else { // wifi是连接上的
                // 网络连接成功
                return NET_STATE_CONNECTED_WIFI;
            }

        }

        int ret = NET_STATE_NOT_CONNECTED;

        if (!gprs.isConnected()) { // 移动网络没有连接
            // 那就判断一下wifi
            if (wifi == null){
                // wifi没有！！
                // 网络连接断开
                ret = NET_STATE_NOT_CONNECTED;
            } else{
                // 有wifi，判断wifi
                if(!wifi.isConnected()){
                    // wifi也没连，那就没网了
                    // 网络连接断开
                    ret = NET_STATE_NOT_CONNECTED;
                } else {
                    // 还是有wifi的
                    ret = NET_STATE_CONNECTED_WIFI;
                }
            }
        } else { // 移动网络有连接
            // 先要判断一下wifi,因为wifi是优先的
            if (wifi == null){
                // 没有wifi，那就只有移动网络了
                ret = NET_STATE_CONNECTED_MOBILE;
            } else {
                // 有wifi，那看它连上没
                if(!wifi.isConnected()){
                    // 没连上，那就是移动网络
                    ret = NET_STATE_CONNECTED_MOBILE;
                } else {
                    // 连上了，wifi优先
                    ret = NET_STATE_CONNECTED_WIFI;
                }
            }
        }

        return ret;
    }
}
