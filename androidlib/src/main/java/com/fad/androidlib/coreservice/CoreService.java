package com.fad.androidlib.coreservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.fad.androidlib.entity.LocalMessage;
import com.fad.androidlib.scene.OnSceneCompleteListener;
import com.fad.androidlib.scene.SceneManager;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 核心服务类：处理UI模块的消息，发送消息给UI模块，在后台运行保持长连接
 *
 * @author 张帆 创建日期：2016/1/25
 *         修改者，修改日期，修改内容。
 */
public class CoreService extends Service implements OnSceneCompleteListener {
    private static final String TAG = "CoreService";

    /**
     * 消息队列
     */
    private Queue<LocalMessage> queueMessage;

    /**
     * 是否正在处理消息
     */
    private boolean bIsSolvingMessage = false;

    /**
     * 消息处理完毕，向UI层传递消息的回调
     */
    private OnMessageSolvedListener lisMessageSolved = null;

    /**
     * 业务场景管理
     */
    private SceneManager smSceneManager;

    /**
     * 设置向UI层传递消息的回调
     *
     * @param lisMessageSolved
     */
    public void setLisMessageSolved(OnMessageSolvedListener lisMessageSolved) {
        this.lisMessageSolved = lisMessageSolved;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();

        // 初始化工作
        queueMessage = new LinkedList<>(); // 初始化消息队列
        smSceneManager = new SceneManager(this); // 初始化场景管理类，并设置回调
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind");
        lisMessageSolved = null;
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 服务被绑定时返回binder实例
     *
     * @param intent
     * @return binder实例
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return new CoreBinder();
    }

    /**
     * 核心服务的binder，用于和activity绑定并通信
     */
    public class CoreBinder extends Binder {

        public CoreService getCoreService() {
            return CoreService.this;
        }
    }

    /**
     * 接收ui层的消息
     *
     * @param localMessage 消息
     * @return 是否成功添加进消息队列
     */
    public boolean receiveMessage(LocalMessage localMessage) {
        // 往queue中插入一条待处理消息
        boolean bRet = queueMessage.offer(localMessage);

        if (bRet == true) {
            // scene层处理massage
            if (bIsSolvingMessage == false) { // secene层不在处理消息
                bIsSolvingMessage = true; // 设置处理消息标志位
                solveMessage(); // 处理消息
            }
        }

        return bRet;
    }

    /**
     * scene层处理消息完毕的回调
     *
     * @param messageType    消息类型
     * @param ret            处理结果
     * @param isNeedCallback 是否要回调ui
     */
    @Override
    public void onSceneComplete(int messageType, Object ret, boolean isNeedCallback) {
        // 一条消息处理完成了，从队列中移除，并回调结果
        if (queueMessage.peek().getiMsgType() == messageType) { // 验证队列头部的消息和处理完成的消息类型是否一致
            queueMessage.poll();

            // 回调UI层完成一条消息处理
            if (isNeedCallback) {
                if (lisMessageSolved != null)
                    lisMessageSolved.onMessageSolved(messageType, ret);
            }
        }

        // 处理下一条消息
        solveMessage();
    }

    /**
     * 直接回调ui
     *
     * @param messageType 消息类型
     * @param ret         处理结果
     */
    @Override
    public void callbackUiDirect(int messageType, Object ret) {
        if (lisMessageSolved != null)
            lisMessageSolved.onMessageSolved(messageType, ret);
    }

    /**
     * 处理消息
     */
    private void solveMessage() {
        if (queueMessage.peek() != null) { // 队列不为空
            // scene层处理队列第一条消息
            smSceneManager.initSceneAndRun(queueMessage.peek());
        } else {
            bIsSolvingMessage = false; // 没有要处理的消息了，设置处理消息标志位
        }
    }

    /**
     * 绑定的acitivity stop，取消所有未返回的网络请求
     */
    public void onBindActivityStop() {
        Log.d(TAG, "onBindActivityStop");
        // 解除绑定时取消所有网络请求
        // TODO: 2016/3/5 这里有问题：
        // 如果需要在网络回调中将消息从队列中移除的话，如果取消了网络回调，消息将无法移除，队列中的其他消息将得不到处理
        // 暂时注释
        //smSceneManager.cancelSceneNetwork();
    }
}
