package com.fad.androidlib.scene;

import com.fad.androidlib.entity.LocalMessage;

/**
 * 实现的主要功能:
 * 业务场景管理类
 * @author zf 创建日期：2016/1/26
 * @modify 修改者:,修改日期:,修改内容:.
 */
public class SceneManager {

    /**
     * 业务场景
     */
    private BaseScene bsScenen = null;

    /**
     * 业务逻辑完成回调函数
     */
    private OnSceneCompleteListener lisSceneComplete = null;

    /**
     * 业务场景工厂
     */
    private SceneFactory sfSceneFactory;

    /**
     * 构造函数
     * @param onSceneCompleteListener 业务逻辑完成回调函数
     */
    public SceneManager(OnSceneCompleteListener onSceneCompleteListener){
        this.lisSceneComplete = onSceneCompleteListener;
        sfSceneFactory = new SceneFactory();
    }

    /**
     * 实例化具体的业务场景，并开始处理业务
     * @param localMessage 本地消息
     */
    public void initSceneAndRun(LocalMessage localMessage){
        bsScenen = sfSceneFactory.createScene(localMessage, lisSceneComplete);

        if (bsScenen != null)
            bsScenen.run();
    }
}
