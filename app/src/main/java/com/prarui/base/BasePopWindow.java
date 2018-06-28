package com.prarui.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.prarui.listeners.OnPopBaseListener;
import com.prarui.utils.system.ScreenUtils;
import com.prarui.utils.common.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prarui on 2017/11/30.
 */

public abstract class BasePopWindow {
    protected Context context;
    protected Bundle bundle;
    public PopupWindow getPopupWindow() {
        return popupWindow;
    }
    protected List<String> strings = new ArrayList<>();
    protected PopupWindow popupWindow = null;
    protected OnPopBaseListener listener;
    protected String message;

    public abstract int popLayoutId();

    public abstract void findViews(View view);

    public BasePopWindow setPopupWindow(Context context, View view) {
        setPopupWindow(context, null, view, null, null);
        return this;
    }

    public BasePopWindow setPopupWindow(Context context, Bundle bundle, View view, String message) {
        setPopupWindow(context, bundle, view, message, null);
        return this;
    }

    public BasePopWindow setPopupWindow(Context context, Bundle bundle, View view, OnPopBaseListener listener) {
        setPopupWindow(context, bundle, view, null, listener);
        return this;
    }

    public BasePopWindow setPopupWindow(Context context, View view, OnPopBaseListener listener) {
        setPopupWindow(context, null, view, null, listener);
        return this;
    }

    public BasePopWindow setPopupWindow(Context context, View view, String message, OnPopBaseListener listener) {
        setPopupWindow(context, null, view, message, listener);
        return this;
    }

    public BasePopWindow setPopupWindow(final Context context, Bundle bundle, View view, String message, OnPopBaseListener listener) {
        this.context = context;
        this.listener = listener;
        this.message = message;
        this.bundle = bundle;
        popupWindow = new PopupWindow();
        View inflateView = LayoutInflater.from(context).inflate(popLayoutId(), null);
        ColorDrawable dw = new ColorDrawable(-00000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setContentView(inflateView);
        setBackgroundAlpha(0.5f, context);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setHeight(ScreenUtils.getScreenHeight(context) / 5 * 2);
        popupWindow.setWidth(ScreenUtils.getScreenWidth(context));
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //监听关闭后把颜色变回去
                setBackgroundAlpha(1.0f, context);

            }
        });
        findViews(inflateView);
        return this;
    }

    public BasePopWindow setData(List<String> strings) {
        this.strings = strings;
        return this;
    }

    public BasePopWindow setTitle(String title, String feedBack) {
        return this;
    }

    public void dismiss() {
        try {
            popupWindow.dismiss();
            setBackgroundAlpha(1, context);
        } catch (Exception e) {
            ToastUtils.showToast("pop为空");
        }

    }

    protected void setBackgroundAlpha(float bgAlpha, Context mContext) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

}
