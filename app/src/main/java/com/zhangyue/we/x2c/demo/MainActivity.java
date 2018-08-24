package com.zhangyue.we.x2c.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
    }

    public void startX2C(View v) {
        startActivity(new Intent(this, ActivityX2C.class));
    }

    public void startXML(View v) {
        startActivity(new Intent(this, ActivityXML.class));
    }


}
