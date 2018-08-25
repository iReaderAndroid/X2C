package com.zhangyue.we.view;

import com.zhangyue.we.anoprocesser.xml.Attr;
import com.zhangyue.we.anoprocesser.xml.LayoutManager;

import java.util.HashMap;
import java.util.TreeSet;

/**
 * @author：chengwei 2018/8/25
 * @description
 */
public class CustomAttr implements ITranslator {

    private TreeSet<String> mImports;
    private String mView;
    private String mLayoutParams;
    private HashMap<String, Attr> mAttrs;

    public CustomAttr(TreeSet<String> imports, String layoutParams, String view) {
        this.mImports = imports;
        this.mView = view;
        this.mLayoutParams = layoutParams;
        this.mAttrs = LayoutManager.instance().getAttrs();
    }


    @Override
    public boolean translate(StringBuffer stringBuffer, String key, String value) {
        if (mAttrs == null || mAttrs.size() == 0) {
            return false;
        }
        Attr attr = mAttrs.get(key);
        if (attr != null) {
            if (attr.toFunc != null && attr.toFunc.name != null) {
                stringBuffer.append(String.format(attr.toFunc.name + "\n", attr.toFunc.isView ? mView : mLayoutParams
                        , getValue(attr, value)));
            }

            return true;
        }
        return false;
    }

    private String getValue(Attr attr, String value) {
        String[] ss = value.split("\\|");
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < ss.length; i++) {
            ret.append(getSingleValue(attr, ss[i]));
            if (i < ss.length - 1) {
                ret.append("|");
            }
        }
        return ret.toString();
    }

    private String getSingleValue(Attr attr, String value) {
        String ret = attr.enums.get(value);
        if (ret != null) {
            return ret;
        }

        if (value.startsWith("@drawable/")) {
            return "R.drawable." + value.substring(value.indexOf("/") + 1);
        } else if (value.startsWith("@mipmap/")) {
            return "R.mipmap." + value.substring(value.indexOf("/") + 1);
        } else if (value.startsWith("@color/") || value.startsWith("#")) {
            return View.getColor(value);
        } else if (value.startsWith("@id/") || value.startsWith("@+id/")) {
            return "R.id." + value.substring(value.indexOf("/") + 1);
        } else if (value.startsWith("@dimen/")
                || value.endsWith("dp")
                || value.endsWith("dip")
                || value.endsWith("dip")
                || value.endsWith("px")
                || value.endsWith("sp")) {
            return View.getDimen(value);
        } else if (value.startsWith("@string/")) {
            return View.getString(value);
        } else if (value.startsWith("@anim/")) {
            return "R.anim." + value.substring(value.indexOf("/") + 1);
        } else {
            switch (attr.toFunc.paramsType.toLowerCase()) {
                case "float":
                    return value + "f";
                case "string":
                    return "\"" + value + "\"";
                default:
                    return value;
            }
        }
    }

    @Override
    public void onAttributeEnd(StringBuffer stringBuffer) {

    }
}
