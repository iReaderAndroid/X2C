package com.zhangyue.we.anoprocesser.xml;

import com.zhangyue.we.anoprocesser.Log;

/**
 * @author：chengwei 2018/8/25
 * @description
 */
public class Func {

    public String name;
    public boolean isView;
    public String paramsType;

    public Func(String toFunc) {
        String[] ss = toFunc.split("\\.");
        if (ss.length == 2) {
            isView = ss[0].equalsIgnoreCase("view");
            int index = ss[1].indexOf("=");
            if (index > 0) {
                paramsType = ss[1].substring(index + 1);
                name = "%s." + ss[1].substring(0, index + 1) + "%s";
            } else {
                index = ss[1].indexOf("(");
                int indexQot = ss[1].indexOf(")");
                paramsType = ss[1].substring(ss[1].indexOf("(") + 1, ss[1].indexOf(")"));
                name = "%s." + ss[1].substring(0, index + 1) + "%s" + ss[1].substring(indexQot);
            }
            if (!name.endsWith(";")) {
                name += ";";
            }
        }
    }


    @Override
    public String toString() {
        return "Func{" +
                "name='" + name + '\'' +
                ", isView=" + isView +
                ", paramsType='" + paramsType + '\'' +
                '}';
    }
}
