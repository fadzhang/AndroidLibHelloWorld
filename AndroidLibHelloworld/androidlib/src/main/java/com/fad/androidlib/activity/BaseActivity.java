package com.fad.androidlib.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

import com.fad.androidlib.coreservice.CoreService;
import com.fad.androidlib.coreservice.OnMessageSolvedListener;
import com.fad.androidlib.entity.LocalMessage;
import com.fad.androidlib.sharepreference.CommonPreferences;
import com.fad.androidlib.utils.CommonUtils;
import com.fad.androidlib.utils.CoreServiceUtils;
import com.fad.androidlib.R;

/**
 * 实现的主要功能:
 * activity 基类
 *
 * @author zf 创建日期：2016/6/6
 * @modify 修改者:,修改日期:,修改内容:.
 */
public abstract class BaseActivity extends Activity implements OnMessageSolvedListener {
    private static final String TAG = "BaseActivity";

    /**
     * core service的引用
     */
    protected CoreService coreService = null;

    /**
     * 向fragment发送消息的回调，如果acitivity中有fragment的话，注意：activity中当前只有一个fragment能注册此回调
     */
    protected ActivityToFragmentListener activityToFragmentListener = null;

    /**
     * commonPreferences引用
     */
    protected CommonPreferences commonPreferences = null;

    /**
     * service连接，监听绑定结果
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
            // TODO: 2016/1/27 解绑的回调
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            // CoreService绑定成功
            // 返回一个MsgService对象
            coreService = ((CoreService.CoreBinder) service).getCoreService();
            // 设置消息处理完成的结果回调
            setCoreServiceListener();

            onCoreServiceBindSuccess();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        init();

        // 绑定core service
        bindCoreService();

        initParam();
        initView();
        initData();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();

        // activity重新到前台时需要重新设置service的listener
        setCoreServiceListener();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart");
        super.onRestart();
    }


    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");

        super.onStop();

        Log.d(TAG, "onStop onBindActivityStop");
        if (coreService != null) {
            coreService.onBindActivityStop();
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();

        Log.d(TAG, "onDestroy unbindService");
        if (coreService != null) {
            // 解绑coreservice
            unbindService(serviceConnection);
        }
    }

    /**
     * 设置回调listener
     */
    private void setCoreServiceListener() {
        Log.d(TAG, "setCoreServiceListener");
        if (coreService != null) {
            Log.d(TAG, "coreService != null");
            coreService.setLisMessageSolved(this);
        }
    }

    @Override
    public void onMessageSolved(int messageType, Object ret) {
        onBaseMessageSolved(messageType, ret);
        if (activityToFragmentListener != null) {
            activityToFragmentListener.onActivityBaseMessageSolved(messageType, ret);
        }
    }

    /**
     * 初始化
     */
    private void init() {
        Log.d(TAG, "init");

        // 检查是否存储了service的名称
        String serviceName = CoreServiceUtils.getCoreServiceFullName(this);
        if (serviceName != null) {
            // 检测core service是否在运行，否则启动coreservice
            if (!CommonUtils.isServiceRunning(this, serviceName)) {
                Log.d(TAG, "start core service");
                Intent intent = new Intent();
                intent.setClassName(this, serviceName);
                startService(intent);
            }
        }

        // 默认把输入法隐藏
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //初始化CommonPreferences
        commonPreferences = CommonPreferences.getInstance(this);

    }

    /**
     * 绑定coreservice
     */
    private void bindCoreService() {
        Log.d(TAG, "bindCoreService");
        // 检查是否存储了service的名称
        String serviceName = CoreServiceUtils.getCoreServiceFullName(this);
        if (serviceName != null) {
            // 检测core service是否在运行，否则等待一秒再重新发起绑定
            if (CommonUtils.isServiceRunning(this, serviceName)) {
                Log.d(TAG, "start bindCoreService");
                Intent intent = new Intent();
                intent.setClassName(this, serviceName);
                // 发起绑定
                if (!bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)) {
                    // 启动重新绑定等待线程
                    new BindRetryTask().execute();
                }
            } else {
                // 启动重新绑定等待线程
                new BindRetryTask().execute();
            }
        } else {
            // 启动重新绑定等待线程
            new BindRetryTask().execute();
        }
    }

    /**
     * 等待重新绑定线程
     */
    private class BindRetryTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                // 等待1秒
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // 再次发起绑定
            bindCoreService();
        }
    }

    /**
     * core service绑定成功 抽象方法，派生类实现, 注意：有fragment的话要在此实现中初始化
     */
    protected abstract void onCoreServiceBindSuccess();

    /**
     * 消息处理完成，抽象方法，派生类实现
     *
     * @param messageType 消息类型
     * @param ret         处理结果
     */
    protected abstract void onBaseMessageSolved(int messageType, Object ret);

    /**
     * 初始化intent传递数据
     */
    protected abstract void initParam();

    /**
     * 初始化ui
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 发送消息
     * @param localMessage
     * @return
     */
    public boolean sendMessage(LocalMessage localMessage){
        if (coreService != null)
            return coreService.receiveMessage(localMessage);
        else
            return false;
    }

    /**
     * 开启activity，带右侧滑动进入
     *
     * @param it       intent
     * @param isFinish 是否开启activity后finish
     */
    protected void startActivityRightIn(Intent it, boolean isFinish) {
        startActivity(it);
        overridePendingTransition(R.anim.slide_right_in, R.anim.hold);
        if (isFinish)
            finish();
    }

    /**
     * 开启activity for result，带右侧滑动进入
     *
     * @param it       intent
     * @param isFinish 是否开启activity后finish
     */
    protected void startActivityForResultRightIn(Intent it, int requestCode, boolean isFinish) {
        startActivityForResult(it, requestCode);
        overridePendingTransition(R.anim.slide_right_in, R.anim.hold);
        if (isFinish)
            finish();
    }
}
