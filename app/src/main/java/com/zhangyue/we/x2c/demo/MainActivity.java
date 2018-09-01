package com.zhangyue.we.x2c.demo;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingX2C;
import android.os.Bundle;
import com.zhangyue.we.x2c.demo.databinding.ActivityBinding;

import com.zhangyue.we.x2c.ano.Xml;

import butterknife.ButterKnife;
import butterknife.OnClick;


@Xml(layouts = R.layout.activity)
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBinding binding = DataBindingX2C.setContentView(this, R.layout.activity);
        binding.setBean(new Bean());
        ButterKnife.bind(this);
    }


    @OnClick(R.id.x2c)
    void toX2C() {
        startActivity(new Intent(this, ActivityX2C.class));
    }

    @OnClick(R.id.xml)
    void toXML() {
        startActivity(new Intent(this, ActivityXML.class));
    }
}
