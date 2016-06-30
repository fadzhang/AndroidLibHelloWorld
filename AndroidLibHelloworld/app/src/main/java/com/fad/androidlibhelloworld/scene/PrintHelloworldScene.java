package com.fad.androidlibhelloworld.scene;

import android.support.annotation.NonNull;

import com.fad.androidlib.entity.LocalMessage;
import com.fad.androidlib.scene.OnSceneCompleteListener;
import com.fad.androidlibhelloworld.base.AppBaseScene;
import com.fad.androidlibhelloworld.mockdata.MockPrintText;

/**
 * 实现的主要功能:
 * 打印显示hello world 业务处理
 *
 * @author zf 创建日期：2016/6/29
 * @modify 修改者:,修改日期:,修改内容:.
 */
public class PrintHelloworldScene extends AppBaseScene{
    /**
     * 构造函数
     *
     * @param localMessage            本地消息
     * @param onSceneCompleteListener 业务场景完成回调
     */
    public PrintHelloworldScene(@NonNull LocalMessage localMessage, OnSceneCompleteListener onSceneCompleteListener) {
        super(localMessage, onSceneCompleteListener);
    }

    @Override
    public void run() {
        String ret = new MockPrintText().getJsonData();

        lisSceneComplete.onSceneComplete(lmMsg.getiMsgType(), ret, true);
    }
}
