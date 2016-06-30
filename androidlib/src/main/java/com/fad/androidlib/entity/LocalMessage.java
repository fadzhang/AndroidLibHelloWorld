package com.fad.androidlib.entity;

import android.content.Context;
import android.support.annotation.NonNull;

import com.fad.androidlib.scene.BaseScene;

import java.util.Map;

/**
 * 本地消息
 *
 * @author zf 创建日期：2016/1/26
 *         修改者，修改日期，修改内容。
 */
public class LocalMessage {
    /**
     * 上下文
     */
    private Context context;

    /**
     * 业务处理类class
     */
    private Class<?> sceneClass;

    /**
     * 消息类型
     */
    private int iMsgType;

    /**
     * 消息参数
     */
    private Map mapParams;

    /**
     * 构造函数
     *
     * @param context     上下文，不能为空
     * @param messageType 消息类型
     * @param sceneClass  业务处理类的class
     * @param params      消息参数
     */
    public <T extends BaseScene> LocalMessage(@NonNull Context context, int messageType, Class<T> sceneClass, Map params) {
        this.context = context;
        this.iMsgType = messageType;
        this.sceneClass = sceneClass;
        this.mapParams = params;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Class<?> getSceneClass() {
        return sceneClass;
    }

    public void setSceneClass(Class<?> sceneClass) {
        this.sceneClass = sceneClass;
    }

    public Map getMapParams() {
        return mapParams;
    }

    public void setMapParams(Map mapParams) {
        this.mapParams = mapParams;
    }

    public int getiMsgType() {
        return iMsgType;
    }

    public void setiMsgType(int iMsgType) {
        this.iMsgType = iMsgType;
    }
}
