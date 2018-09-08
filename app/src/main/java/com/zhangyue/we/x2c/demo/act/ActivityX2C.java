package com.zhangyue.we.x2c.demo.act;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewStub;

import com.zhangyue.we.x2c.X2C;
import com.zhangyue.we.x2c.ano.Xml;
import com.zhangyue.we.x2c.demo.R;

/**
 * @author：chengwei 2018/8/24
 * @description
 */
@Xml(layouts = "activity_main")
public class ActivityX2C extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        X2C.setContentView(this, R.layout.activity_main);
        ViewStub stub = (ViewStub) findViewById(R.id.stub);
        stub.setLayoutResource(R.layout.stub_layout);
        stub.inflate();
    }
}
