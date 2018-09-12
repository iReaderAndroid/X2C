package com.zhangyue.we.x2c.demo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhangyue.we.x2c.X2C;
import com.zhangyue.we.x2c.ano.Xml;

/**
 * @author:chengwei 2018/8/28
 * @description
 */
@Xml(layouts = "fragmetn_layout")
public class MyFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return X2C.inflate(inflater.getContext(), R.layout.fragmetn_layout, null);
    }

}
