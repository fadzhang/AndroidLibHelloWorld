package com.fad.androidlibhelloworld.activity;

import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.fad.androidlib.entity.LocalMessage;
import com.fad.androidlibhelloworld.R;
import com.fad.androidlibhelloworld.base.AppBaseActivity;
import com.fad.androidlibhelloworld.constants.MessageType;
import com.fad.androidlibhelloworld.scene.PrintHelloworldScene;

/**
 * 实现的主要功能:
 * hello world
 *
 * @author zf 创建日期：2016/6/29
 * @modify 修改者:,修改日期:,修改内容:.
 */
public class HelloworldActivity extends AppBaseActivity{

    private static final String TAG = "HelloworldActivity";

    private TextView tv_hello_world; // 文字展示

    @Override
    protected void onCoreServiceBindSuccess() {
        // print text
        printText();
    }

    @Override
    protected void onBaseMessageSolved(int messageType, Object ret) {
        super.onBaseMessageSolved(messageType, ret);

        if (MessageType.PRINT_HELLO_WORLD == messageType){
            String str = (String) ret;

            Log.e(TAG, str);
            tv_hello_world.setText(str);
        }
    }

    @Override
    protected void initParam() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_hello_world);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); // 通知栏半透明 level19以上有效


        tv_hello_world = (TextView) findViewById(R.id.tv_hello_world); // 文字展示
    }

    @Override
    protected void initData() {

    }

    /**
     * 发送消息给业务处理层
     */
    private void printText(){
        sendMessage(new LocalMessage(this, MessageType.PRINT_HELLO_WORLD, PrintHelloworldScene.class, null));
    }
}
