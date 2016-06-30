package com.fad.androidlib.network;

import android.content.Context;

import com.fad.androidlib.entity.CommonResult;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.fad.androidlib.R;

/**
 * 实现的主要功能:
 * 网络数据解析
 *
 * @author zf 创建日期：2016/3/2
 * @modify 修改者:,修改日期:,修改内容:.
 */
public class CommonNetParser {

    /**
     * 上下文
     */
    private Context context;

    /**
     * 单例的netparser
     */
    private static CommonNetParser commonNetParser = null;

    /**
     * 获取netparser的实例
     *
     * @param context
     * @return
     */
    public static CommonNetParser getInstance(Context context) {
        if (commonNetParser == null) {
            commonNetParser = new CommonNetParser(context);
        }

        return commonNetParser;
    }

    /**
     * 构造函数
     *
     * @param context
     */
    private CommonNetParser(Context context) {
        this.context = context;
    }

    /**
     * 解析的泛型方法
     * @param str json数据
     * @param c class type
     * @param <T> MyResult的派生类
     * @return MyResult的派生类对象
     */
    public <T extends CommonResult> T parseResult(String str, Class<T> c) {
        T ret = null;

        try {
            ret = new Gson().fromJson(str, c);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            try {
                ret = c.newInstance();
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
            if (ret != null) {
                ret.setSuccess(false);
                ret.setMsg(context.getResources().getString(R.string.net_parse_error));
            }
        }
        return ret;
    }
}
