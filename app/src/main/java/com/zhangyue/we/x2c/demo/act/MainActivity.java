package com.zhangyue.we.x2c.demo.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.zhangyue.we.x2c.X2C;
import com.zhangyue.we.x2c.ano.Xml;
import com.zhangyue.we.x2c.demo.R;
import com.zhangyue.x2c.demo.ActivitySubModule;

import butterknife.ButterKnife;
import butterknife.OnClick;


@Xml(layouts = "activity")
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        X2C.setContentView(this, R.layout.activity);
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

    @OnClick(R.id.sub)
    void toSub() {
        startActivity(new Intent(this, ActivitySubModule.class));
    }


}
