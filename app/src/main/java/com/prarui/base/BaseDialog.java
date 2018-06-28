package com.prarui.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.prarui.utils.R;


/**
 * Created by Prarui on 2018/1/15.
 */

public abstract class BaseDialog {
    protected static AlertDialog dialog;
    protected Context context;
    protected OnBaseDialogListener listener;


    protected Bundle bundle;

    /**
     * 布局的id；
     */
    protected abstract int setLayoutId();

    protected abstract WindowManager.LayoutParams setLayoutLayoutParams(WindowManager.LayoutParams attributes);

    /**
     * 布局
     *
     * @param view
     */
    protected abstract void findLayoutView(View view);

    protected abstract void setLogic();

    /**
     * @param context
     * @param bundle
     * @param listener
     * @param isCanOut
     */
    public void show(Context context, Bundle bundle, OnBaseDialogListener listener, Boolean isCanOut) {
        dismiss();
        try {
            this.listener = listener;
            this.context = context;
            this.bundle=bundle;
            dialog = new AlertDialog.Builder(context).create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Window window = dialog.getWindow();
            window.setWindowAnimations(R.style.DialogAnimation);
            window.getDecorView().setPadding(0, 0, 0, 0);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
            dialog.show();
            WindowManager.LayoutParams params = setLayoutLayoutParams(window.getAttributes());
            View inflate = LayoutInflater.from(context).inflate(setLayoutId(), null, false);
            findLayoutView(inflate);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            window.addContentView(inflate, layoutParams);
            dialog.setCanceledOnTouchOutside(isCanOut);
            window.setAttributes(params);
            setLogic();
        } catch (Exception e) {

        }


    }

    public void dismiss() {
        try {
            dialog.dismiss();
        } catch (Exception x) {

        }
    }


    public interface OnBaseDialogListener {

    }

}
