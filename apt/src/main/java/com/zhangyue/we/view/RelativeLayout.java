package com.zhangyue.we.view;

import java.util.TreeSet;

/**
 * @author：chengwei 2018/8/23
 * @description
 */
public class RelativeLayout implements ITranslator {
    private TreeSet<String> mImports;
    private String mLayoutParamsObj;

    public RelativeLayout(TreeSet<String> imports, String layoutParamsObj) {
        this.mImports = imports;
        this.mLayoutParamsObj = layoutParamsObj;
    }

    @Override
    public boolean translate(StringBuffer stringBuffer, String key, String value) {
        switch (key) {
            case "android:layout_centerInParent":
                return centerInParent(stringBuffer, value);
            case "android:layout_centerVertical":
                return centerVertical(stringBuffer, value);
            case "android:layout_centerHorizontal":
                return centerHorizontal(stringBuffer, value);
            case "android:layout_alignParentLeft":
                return alignParentLeft(stringBuffer, value);
            case "android:layout_alignParentTop":
                return alignParentTop(stringBuffer, value);
            case "android:layout_alignParentRight":
                return alignParentRight(stringBuffer, value);
            case "android:layout_alignParentBottom":
                return alignParentBottom(stringBuffer, value);
            case "android:layout_above":
                return above(stringBuffer, value);
            case "android:layout_below":
                return below(stringBuffer, value);
            case "android:layout_toLeftOf":
                return toLeftOf(stringBuffer, value);
            case "android:layout_toRightOf":
                return toRightOf(stringBuffer, value);
            case "android:layout_alignLeft":
                return alignLeft(stringBuffer, value);
            case "android:layout_alignTop":
                return alignTop(stringBuffer, value);
            case "android:layout_alignRight":
                return alignRight(stringBuffer, value);
            case "android:layout_alignBottom":
                return alignBottom(stringBuffer, value);
            default:
                return false;
        }
    }

    @Override
    public void onAttributeEnd(StringBuffer stringBuffer) {

    }

    private boolean alignLeft(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.ALIGN_LEFT";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean alignTop(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.ALIGN_TOP";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean alignRight(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.ALIGN_RIGHT";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean alignBottom(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.ALIGN_BOTTOM";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean toRightOf(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.RIGHT_OF";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean toLeftOf(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.LEFT_OF";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean above(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.ABOVE";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean below(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.BELOW";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }


    private boolean alignParentLeft(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.ALIGN_PARENT_LEFT";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean alignParentTop(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.ALIGN_PARENT_TOP";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean alignParentRight(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.ALIGN_PARENT_RIGHT";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean alignParentBottom(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.ALIGN_PARENT_BOTTOM";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean centerVertical(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.CENTER_VERTICAL";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean centerHorizontal(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.CENTER_HORIZONTAL";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean centerInParent(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.CENTER_IN_PARENT";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private void addRule(StringBuffer stringBuffer, String rule, String ruleValue) {
        if (mLayoutParamsObj != null) {
            stringBuffer.append(String.format("%s.addRule(%s,%s);\n", mLayoutParamsObj, rule, ruleValue));
        }
    }

    private String getRuleValue(String value) {
        if (value.equals("true")) {
            mImports.add("android.widget.RelativeLayout");
            return "RelativeLayout.TRUE";
        } else if (value.equals("false")) {
            return "0";
        } else {
            return "R.id." + value.substring(value.indexOf("/") + 1);
        }
    }
}
