package com.zhangyue.we.x2c.demo;

import android.app.Activity;
import android.os.Bundle;

import com.zhangyue.we.x2c.X2C;
import com.zhangyue.we.x2c.ano.Xml;

/**
 * @author：chengwei 2018/8/24
 * @description
 */
@Xml(layouts = {R.layout.activity_main})
public class ActivityX2C extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        X2C.setContentView(this, R.layout.activity_main);

    }
}
