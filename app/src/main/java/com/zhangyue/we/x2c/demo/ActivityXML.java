package com.zhangyue.we.x2c.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewStub;

/**
 * @author：chengwei 2018/8/24
 * @description
 */
public class ActivityXML extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewStub stub = (ViewStub)findViewById(R.id.stub);
        stub.setLayoutResource(R.layout.stub_layout);
        stub.inflate();
    }
}
