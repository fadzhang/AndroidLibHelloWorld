package com.fad.androidlibhelloworld.base;

import android.support.annotation.NonNull;

import com.fad.androidlib.entity.LocalMessage;
import com.fad.androidlib.scene.BaseScene;
import com.fad.androidlib.scene.OnSceneCompleteListener;

/**
 * 实现的主要功能:
 * 业务处理基类
 *
 * @author zf 创建日期：2016/6/9
 * @modify 修改者:,修改日期:,修改内容:.
 */
public abstract class AppBaseScene extends BaseScene {

    //protected CommonNetInterface commonNetInterface;

    /**
     * 构造函数
     *
     * @param localMessage            本地消息
     * @param onSceneCompleteListener 业务场景完成回调
     */
    public AppBaseScene(@NonNull LocalMessage localMessage, OnSceneCompleteListener onSceneCompleteListener) {
        super(localMessage, onSceneCompleteListener);

        // 初始化网络请求对象
        //commonNetInterface = new CommonNetImp(localMessage.getContext());
    }
}
