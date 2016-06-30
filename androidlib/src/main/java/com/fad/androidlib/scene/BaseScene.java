package com.fad.androidlib.scene;

import android.support.annotation.NonNull;

import com.fad.androidlib.entity.LocalMessage;
import com.fad.androidlib.network.CommonNetParser;
import com.fad.androidlib.sharepreference.CommonPreferences;

/**
 * 实现的主要功能:
 * 业务场景基类
 *
 * @author zf 创建日期：2016/1/26
 * @modify 修改者:,修改日期:,修改内容:.
 */
public abstract class BaseScene {

    /**
     * 消息
     */
    protected LocalMessage lmMsg;

    /**
     * 业务场景完成回调
     */
    protected OnSceneCompleteListener lisSceneComplete = null;

    /**
     * SharedPreferences对象
     */
    protected CommonPreferences cpPreferences;

    /**
     * 网络返回解析对象
     */
    protected CommonNetParser commonNetParser = null;

    /**
     * 构造函数
     *
     * @param localMessage            本地消息
     * @param onSceneCompleteListener 业务场景完成回调
     */
    public BaseScene(@NonNull LocalMessage localMessage, OnSceneCompleteListener onSceneCompleteListener) {
        this.lmMsg = localMessage;
        this.lisSceneComplete = onSceneCompleteListener;

        cpPreferences = CommonPreferences.getInstance(lmMsg.getContext()); // 初始化share preferences

        commonNetParser = CommonNetParser.getInstance(lmMsg.getContext()); // 初始化网络返回解析对象
    }

    /**
     * 抽象方法，运行处理业务逻辑
     */
    abstract public void run();
}
