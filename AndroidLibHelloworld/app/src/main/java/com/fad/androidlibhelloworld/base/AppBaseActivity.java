package com.fad.androidlibhelloworld.base;

import com.fad.androidlib.activity.BaseActivity;
import com.fad.androidlibhelloworld.R;
import com.fad.androidlibhelloworld.widget.CommonDialog;

/**
 * 实现的主要功能:
 * app基础activity
 *
 * @author zf 创建日期：2016/6/9
 * @modify 修改者:,修改日期:,修改内容:.
 */
public abstract class AppBaseActivity extends BaseActivity {

    @Override
    protected void onBaseMessageSolved(int messageType, Object ret) {

    }

    //等待框
    protected CommonDialog progressDialog;

    /**
     * 显示等待对话框
     *
     * @param resID       显示提示文字的资源ID
     * @param touchCancel 点击对话框外部区域可消除对话框
     * @param backCancel  点击返回键可消除对话框
     */
    protected void showProgress(int resID, boolean touchCancel, boolean backCancel) {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
        progressDialog = new CommonDialog(
                this,
                getResources().getString(resID),
                CommonDialog.DIALOG_STYLE_PROGRESS, null, null, null, null, touchCancel, backCancel);
        progressDialog.show();
    }

    /**
     * 显示等待对话框
     *
     * @param str         显示提示文字的字符串
     * @param touchCancel 点击对话框外部区域可消除对话框
     * @param backCancel  点击返回键可消除对话框
     */
    protected void showProgress(String str, boolean touchCancel,
                                boolean backCancel) {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
        progressDialog = new CommonDialog(
                this,
                str,
                CommonDialog.DIALOG_STYLE_PROGRESS, null, null, null, null, touchCancel, backCancel);
        progressDialog.show();
    }

    /**
     * 消除等待对话框
     */
    protected void hideProgress() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }

    /**
     * 不带点击事件单个按钮弹框
     */
    protected void newDialog(String string) {
        new CommonDialog(
                this,
                string,
                CommonDialog.DIALOG_STYLE_ONE_BUTTON,
                getResources().getString(R.string.confirm),
                null,
                null,
                null,
                true,
                true).show();
    }

    /**
     * 带点击关闭activity的单个按钮弹框
     */
    protected void newBackDialog(String string) {
        new CommonDialog(
                this,
                string,
                CommonDialog.DIALOG_STYLE_ONE_BUTTON,
                getResources().getString(R.string.confirm),
                null,
                null,
                new CommonDialog.OnDialogBtnClickListener() {
                    @Override
                    public void onDialogBtnClick(int clickId) {
                        if (clickId == CommonDialog.BUTTON_CLICK_ID_ONE) {
                            finish();
                        }
                    }
                },
                true,
                true).show();
    }
}
