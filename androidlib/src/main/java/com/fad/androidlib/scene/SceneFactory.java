package com.fad.androidlib.scene;


import com.fad.androidlib.entity.LocalMessage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 实现的主要功能:
 * 场景类工厂，添加业务场景需在工厂添加对应的实例化case
 *
 * @author zf 创建日期：2016/1/26
 * @modify 修改者:,修改日期:,修改内容:.
 */
public class SceneFactory {

    /**
     * 创建业务场景
     *
     * @param localMessage            本地消息
     * @param onSceneCompleteListener 业务处理完成回调
     * @return 场景基类
     */
    public BaseScene createScene(LocalMessage localMessage, OnSceneCompleteListener onSceneCompleteListener) {
        BaseScene ret = null;

        /*以下调用带参的构造函数*/
        Constructor c = null;
        try {
            c = localMessage.getSceneClass().getDeclaredConstructor(LocalMessage.class, OnSceneCompleteListener.class);
            c.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            if (c != null) {
                ret = (BaseScene) c.newInstance(localMessage,onSceneCompleteListener);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return ret;
    }


}
