package com.fad.androidlibhelloworld.base;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fad.androidlib.activity.ActivityToFragmentListener;
import com.fad.androidlib.coreservice.CoreService;
import com.fad.androidlib.entity.LocalMessage;
import com.fad.androidlib.sharepreference.CommonPreferences;
import com.fad.androidlibhelloworld.R;
import com.fad.androidlibhelloworld.widget.CommonDialog;

/**
 * 实现的主要功能:
 * 碎片基类
 * @author zf 创建日期：2016/2/1
 * @modify 修改者:,修改日期:,修改内容:.
 */
public abstract class BaseFragment extends Fragment implements ActivityToFragmentListener {

    /**
     * core service的引用
     */
    protected CoreService coreService = null;

    /**
     * commonPreferences引用
     */
    protected CommonPreferences commonPreferences;

    /**
     * 设置core service的引用
     * @param coreService core service的引用
     */
    public void setCoreService(CoreService coreService) {
        this.coreService = coreService;
    }

    /**
     * 发送消息
     * @param localMessage
     * @return
     */
    protected boolean sendMessage(LocalMessage localMessage){
        if (coreService != null)
            return coreService.receiveMessage(localMessage);
        else
            return false;
    }

    /**
     * 消息处理完成
     * @param messageType 消息类型
     * @param ret 处理结果
     */
    @Override
    public void onActivityBaseMessageSolved(int messageType, Object ret){
        fragmentMessageSolved(messageType, ret);
    }

    /**
     * 消息处理完成，抽象方法，派生类实现
     * @param messageType 消息类型
     * @param ret 处理结果
     */
    protected abstract void fragmentMessageSolved(int messageType, Object ret);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 获取core service的引用
        //coreService = (CoreService) getArguments().getParcelable(CORE_SERVICE_KEY);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setCommonPreferences(CommonPreferences commonPreferences){
        this.commonPreferences = commonPreferences;
    }
    /**
     * bundle传递coreservice对象引用的key
     */
//    private static final String CORE_SERVICE_KEY = "core_service";

    /**
     * 获取相应类型的碎片实例
     * @param coreService coreservice引用
     * @param c 具体的派生fragment的类型
     * @return 返回fragment自定义基类
     */
//    public static BaseFragment getInstance(CoreService coreService, Class<?> c) {
//        BaseFragment baseFragment = null;
//        try {
//            baseFragment = (BaseFragment) c.newInstance();
//        } catch (java.lang.InstantiationException e) {
//            e.printStackTrace();
//            return null;
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//            return null;
//        }
//        // 设置参数
//        Bundle bdl = new Bundle();
//        bdl.putParcelable(CORE_SERVICE_KEY, coreService);
//        baseFragment.setArguments(bdl);
//        return baseFragment;
//    }

//    /**
//     * 开启activity，带右侧滑动进入
//     * @param it intent
//     * @param isFinish 是否开启activity后finish
//     */
//    protected void startActivityRightIn(Intent it, boolean isFinish){
//        startActivity(it);
//        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.hold);
//        if (isFinish)
//            getActivity().finish();
//    }
//
//    /**
//     * 开启activity for result，带右侧滑动进入
//     * @param it intent
//     * @param isFinish 是否开启activity后finish
//     */
//    protected void startActivityForResultRightIn(Intent it, int requestCode, boolean isFinish){
//        startActivityForResult(it, requestCode);
//        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.hold);
//        if (isFinish)
//            getActivity().finish();
//    }
    /**
     * 不带点击事件单个按钮弹框
     */
    protected void newDialog(String string) {
        new CommonDialog(
                this.getActivity(),
                string,
                CommonDialog.DIALOG_STYLE_ONE_BUTTON,
                getResources().getString(R.string.confirm),
                null,
                null,
                null,
                true,
                true).show();
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
                getActivity(),
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
                getActivity(),
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
}
