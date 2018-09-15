package com.zhangyue.demo.databinding;

import android.app.Activity;
import android.databinding.DataBindingX2C;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zhangyue.we.x2c.demo.databinding.ActivityDatabindingBinding;

import com.zhangyue.we.x2c.ano.Xml;
import com.zhangyue.we.x2c.demo.R;

/**
 * @author：chengwei 2018/9/10
 * @description
 */
@Xml(layouts = "activity_databinding")
public class ActivityDataBinding extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDatabindingBinding binding = DataBindingX2C.setContentView(this, R.layout.activity_databinding);
        binding.setBean(new Bean());
    }
}
