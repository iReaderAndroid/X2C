package com.zhangyue.x2c.demo;

import android.app.Activity;
import android.os.Bundle;


import com.zhangyue.we.x2c.X2C;
import com.zhangyue.we.x2c.ano.Xml;

import butterknife.OnClick;
import plugin.zhangyue.com.sub_module.R;

/**
 * @author：chengwei 2018/9/5
 * @description
 */
@Xml(layouts = "sub_activity")
public class ActivitySubModule extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        X2C.setContentView(this, R.layout.sub_activity);
    }

}
