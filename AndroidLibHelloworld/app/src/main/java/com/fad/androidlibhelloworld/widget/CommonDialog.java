package com.fad.androidlibhelloworld.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fad.androidlibhelloworld.R;

/**
 * 实现的主要功能:
 * 等待对话框、一个按钮的提示对话框、两个按钮的提示选择对话框
 * @author zf 创建日期：2016/2/17
 * @modify 修改者:,修改日期:,修改内容:.
 */
public class CommonDialog extends Dialog implements View.OnClickListener{

    public static final int DIALOG_STYLE_PROGRESS = 0; // 等待对话框
    public static final int DIALOG_STYLE_ONE_BUTTON = 1; // 一个按钮的提示对话框
    public static final int DIALOG_STYLE_TWO_BUTTON = 2; // 两个按钮的提示选择对话框

    public static final int BUTTON_CLICK_ID_ONE = 3; // 一个按钮的提示对话框 按钮点击ID
    public static final int BUTTON_CLICK_ID_TWO_LEFT = 4; // 两个按钮的提示选择对话框 左侧按钮点击ID
    public static final int BUTTON_CLICK_ID_TWO_RIGHT = 5; // 两个按钮的提示选择对话框 右侧按钮点击ID

    /**
     * 上下文
     */
    private Context context;

    /**
     * 按钮点击回调
     */
    private OnDialogBtnClickListener onDialogBtnClickListener;

    /**
     * 等待旋转
     */
    private ProgressBar progressLoading;

    /**
     * 提示文字
     */
    private TextView tvConfirmText;

    /**
     * 按钮 布局
     */
    private LinearLayout llButtons;

    /**
     * 一个按钮的对话框的按钮
     */
    private Button btnOne;

    /**
     * 两个按钮的对话框的按钮 布局
     */
    private LinearLayout llTwoBtn;

    /**
     * 两个按钮的对话框的左侧按钮
     */
    private Button btnLeft;

    /**
     * 两个按钮的对话框的右侧按钮
     */
    private Button btnRight;

    /**
     * 是否可以触摸消除
     */
    private boolean isTouchCancel;

    /**
     * 是否可以按返回键消除
     */
    private boolean isBackCancel;

    /**
     * 是否已经cancel了
     */
    private boolean isCanceled = false;

    /**
     * 构造函数
     * @param context 上下文
     * @param confirmText 提示文字
     * @param dialogStyle 对话框类型：0：等待对话框， 1：一个按钮的提示对话框， 2：两个按钮的提示选择对话框
     * @param textOneBtn 一个按钮的对话框 按钮文字
     * @param textLeftBtn 两个按钮的提示选择对话框 左侧按钮文字
     * @param textRightBtn 两个按钮的提示选择对话框 右侧按钮文字
     * @param listener 按钮点击回调
     * @param touchCancel 是否可以触摸消除对话框
     * @param backCancel 是否可以按back键消除对话框
     */
    public CommonDialog(Context context,
                        String confirmText,
                        int dialogStyle,
                        String textOneBtn,
                        String textLeftBtn,
                        String textRightBtn,
                        OnDialogBtnClickListener listener,
                        boolean touchCancel,
                        boolean backCancel) {
        super(context, R.style.CommonDialogTheme);

        this.context = context;
        this.onDialogBtnClickListener = listener;
        isTouchCancel = touchCancel;
        isBackCancel = backCancel;

        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        setContentView(R.layout.dialog_common);

        progressLoading = (ProgressBar) findViewById(R.id.progress_loading); // 等待旋转
        tvConfirmText = (TextView) findViewById(R.id.tv_confirm_text); // 提示文字
        llButtons = (LinearLayout) findViewById(R.id.ll_buttons); // 按钮 布局
        btnOne = (Button) findViewById(R.id.btn_one); // 一个按钮的对话框的按钮
        btnOne.setOnClickListener(this);
        llTwoBtn = (LinearLayout) findViewById(R.id.ll_two_btn); // 两个按钮的对话框的按钮 布局
        btnLeft = (Button) findViewById(R.id.btn_left); // 两个按钮的对话框的左侧按钮
        btnLeft.setOnClickListener(this);
        btnRight = (Button) findViewById(R.id.btn_right); // 两个按钮的对话框的右侧按钮
        btnRight.setOnClickListener(this);


        tvConfirmText.setText(confirmText); // 设置提示文字

        // 判断dialog的类型
        switch (dialogStyle){
            case DIALOG_STYLE_PROGRESS: // 等待对话框
                progressLoading.setVisibility(View.VISIBLE);
                llButtons.setVisibility(View.GONE);
                break;
            case DIALOG_STYLE_ONE_BUTTON: // 一个按钮的提示对话框
                progressLoading.setVisibility(View.GONE);
                llButtons.setVisibility(View.VISIBLE);
                btnOne.setVisibility(View.VISIBLE);
                llTwoBtn.setVisibility(View.GONE);

                btnOne.setText(textOneBtn);
                break;
            case DIALOG_STYLE_TWO_BUTTON: // 两个按钮的提示选择对话框
                progressLoading.setVisibility(View.GONE);
                llButtons.setVisibility(View.VISIBLE);
                btnOne.setVisibility(View.GONE);
                llTwoBtn.setVisibility(View.VISIBLE);

                btnLeft.setText(textLeftBtn);
                btnRight.setText(textRightBtn);
                break;
        }
    }

    /**
     * 设置提示文字
     * @param confirmText 提示文字
     */
    public void setConfirmText(String confirmText){
        tvConfirmText.setText(confirmText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_one: // 一个按钮的对话框的按钮
                if (onDialogBtnClickListener != null)
                    onDialogBtnClickListener.onDialogBtnClick(BUTTON_CLICK_ID_ONE);
                break;
            case R.id.btn_left: // 两个按钮的对话框的左侧按钮
                if (onDialogBtnClickListener != null)
                    onDialogBtnClickListener.onDialogBtnClick(BUTTON_CLICK_ID_TWO_LEFT);
                break;
            case R.id.btn_right: // 两个按钮的对话框的右侧按钮
                if (onDialogBtnClickListener != null)
                    onDialogBtnClickListener.onDialogBtnClick(BUTTON_CLICK_ID_TWO_RIGHT);
                break;
        }

        cancel();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (isTouchCancel && event.getAction() == MotionEvent.ACTION_DOWN
                && isOutOfBounds(event)) {
            cancel();
            isCanceled = true;
            return true;
        }

        return false;
    }

    private boolean isOutOfBounds(MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context)
                .getScaledWindowTouchSlop();
        final View decorView = getWindow().getDecorView();
        return (x < -slop) || (y < -slop)
                || (x > (decorView.getWidth() + slop))
                || (y > (decorView.getHeight() + slop));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (isBackCancel == true) {
                cancel();
                isCanceled = true;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 对话框按钮点击回调
     */
    public interface OnDialogBtnClickListener{
        public void onDialogBtnClick(int clickId);
    }
}
