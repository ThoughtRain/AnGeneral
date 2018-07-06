package com.prarui.mvp.view;

import android.view.View;

/**
 * Created by prarui on 2018/7/6.
 */

public interface BaseView {

    /**
     * 显示正在加载
     *
     * @param msg
     */
    void showLoading(String msg);

    /**
     * 显示正在加载
     *
     * @param msg
     */
    void showProgress(String msg);

    /**
     * 隐藏正在加载
     *
     */
    void closeProgress();

    /**
     * 隐藏正在加载
     */
    void hideLoading();

    /**
     * 显示错误信息(显示异常信息)
     * @param msg
     */
    void showError(String msg,View.OnClickListener onClickListener);

    /**
     * 显示空布局信息
     * @param msg
     */
    void showEmpty(int drawableRes,String msg,View.OnClickListener onClickListener);

    /**
     * 显示网络错误
     */
    void showNetError(View.OnClickListener onClickListener);


}
