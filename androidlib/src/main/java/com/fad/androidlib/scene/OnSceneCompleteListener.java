package com.fad.androidlib.scene;

/**
 * 业务逻辑完成回调
 * @author 张帆 创建日期：2016/1/26
 * 修改者，修改日期，修改内容。
 */
public interface OnSceneCompleteListener {
    public void onSceneComplete(int messageType, Object ret, boolean isNeedCallback);

    public void callbackUiDirect(int messageType, Object ret);
}
