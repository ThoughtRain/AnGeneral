package com.prarui.wigth;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.prarui.base.BaseDialog;
import com.prarui.utils.R;
import com.prarui.utils.system.ScreenUtils;


/**
 * Created by Prarui on 2018/1/15.
 */

public class HmAlertDialog extends BaseDialog {
    private static HmAlertDialog hmAlertDialog;

    public static HmAlertDialog getInstance() {
        if (hmAlertDialog == null) {
            hmAlertDialog = new HmAlertDialog();
        }
        return hmAlertDialog;
    }


    @Override
    protected int setLayoutId() {
        return R.layout.layout_alert_dialog;
    }
    public void showWindow(Context context,String title, String message, OnAlertDialogListener listener){
        Bundle bundle=new Bundle();
        bundle.putString("title",title);
        bundle.putString("message",message);
        show(context,bundle,listener,true);
    }
    @Override
    protected WindowManager.LayoutParams setLayoutLayoutParams(WindowManager.LayoutParams attributes) {
        attributes.width = ScreenUtils.getScreenWidth(context) / 10 * 9;
        attributes.height = (int) (ScreenUtils.getScreenHeight(context) / 10 * 2.5);
        attributes.gravity = Gravity.CENTER;
        return attributes;
    }

    @Override
    protected void findLayoutView(View view) {

        if(null!=listener){
            view.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((OnAlertDialogListener)listener).onButtonClick(v);
                }
            });
        }
        String title = bundle.getString("title");
        String message=bundle.getString("message");
        TextView tvMessage= view.findViewById(R.id.tv_message);
        tvMessage.setText(message);
        TextView tvTitle=view.findViewById(R.id.tv_title);
        tvTitle.setText(title);
    }

    @Override
    protected void setLogic() {

    }

    public interface OnAlertDialogListener extends OnBaseDialogListener{
        void onButtonClick(View view);
    }
}
