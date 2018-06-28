package com.prarui.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.prarui.utils.transmit.EventMS;

/**
 * Created by Administrator on 2018/6/28.
 */

public class TwoActivity extends AppCompatActivity {
   private Button send;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send=findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("codeThere","400");
                EventMS.getInstance().sendData(MainActivity.class,bundle);
                TwoActivity.this.finish();
            }
        });
    }
}
