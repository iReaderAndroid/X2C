package com.zhangyue.we.view;

import java.util.TreeSet;

/**
 * @author chengwei 2018/8/23
 */
public class RelativeLayout implements ITranslator {
    private TreeSet<String> mImports;
    private String mLayoutParamsObj;

    public RelativeLayout(TreeSet<String> imports, String layoutParamsObj) {
        this.mImports = imports;
        this.mLayoutParamsObj = layoutParamsObj;
    }

    @Override
    public boolean translate(StringBuilder StringBuilder, String key, String value) {
        switch (key) {
            case "android:layout_centerInParent":
                return centerInParent(StringBuilder, value);
            case "android:layout_centerVertical":
                return centerVertical(StringBuilder, value);
            case "android:layout_centerHorizontal":
                return centerHorizontal(StringBuilder, value);
            case "android:layout_alignParentLeft":
                return alignParentLeft(StringBuilder, value);
            case "android:layout_alignParentTop":
                return alignParentTop(StringBuilder, value);
            case "android:layout_alignParentRight":
                return alignParentRight(StringBuilder, value);
            case "android:layout_alignParentBottom":
                return alignParentBottom(StringBuilder, value);
            case "android:layout_above":
                return above(StringBuilder, value);
            case "android:layout_below":
                return below(StringBuilder, value);
            case "android:layout_toLeftOf":
                return toLeftOf(StringBuilder, value);
            case "android:layout_toRightOf":
                return toRightOf(StringBuilder, value);
            case "android:layout_alignLeft":
                return alignLeft(StringBuilder, value);
            case "android:layout_alignTop":
                return alignTop(StringBuilder, value);
            case "android:layout_alignRight":
                return alignRight(StringBuilder, value);
            case "android:layout_alignBottom":
                return alignBottom(StringBuilder, value);
            default:
                return false;
        }
    }

    @Override
    public void onAttributeEnd(StringBuilder StringBuilder) {

    }

    private boolean alignLeft(StringBuilder StringBuilder, String value) {
        String rule = "RelativeLayout.ALIGN_LEFT";
        String ruleValue = getRuleValue(value);
        addRule(StringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean alignTop(StringBuilder StringBuilder, String value) {
        String rule = "RelativeLayout.ALIGN_TOP";
        String ruleValue = getRuleValue(value);
        addRule(StringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean alignRight(StringBuilder StringBuilder, String value) {
        String rule = "RelativeLayout.ALIGN_RIGHT";
        String ruleValue = getRuleValue(value);
        addRule(StringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean alignBottom(StringBuilder StringBuilder, String value) {
        String rule = "RelativeLayout.ALIGN_BOTTOM";
        String ruleValue = getRuleValue(value);
        addRule(StringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean toRightOf(StringBuilder StringBuilder, String value) {
        String rule = "RelativeLayout.RIGHT_OF";
        String ruleValue = getRuleValue(value);
        addRule(StringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean toLeftOf(StringBuilder StringBuilder, String value) {
        String rule = "RelativeLayout.LEFT_OF";
        String ruleValue = getRuleValue(value);
        addRule(StringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean above(StringBuilder StringBuilder, String value) {
        String rule = "RelativeLayout.ABOVE";
        String ruleValue = getRuleValue(value);
        addRule(StringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean below(StringBuilder StringBuilder, String value) {
        String rule = "RelativeLayout.BELOW";
        String ruleValue = getRuleValue(value);
        addRule(StringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }


    private boolean alignParentLeft(StringBuilder StringBuilder, String value) {
        String rule = "RelativeLayout.ALIGN_PARENT_LEFT";
        String ruleValue = getRuleValue(value);
        addRule(StringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean alignParentTop(StringBuilder StringBuilder, String value) {
        String rule = "RelativeLayout.ALIGN_PARENT_TOP";
        String ruleValue = getRuleValue(value);
        addRule(StringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean alignParentRight(StringBuilder StringBuilder, String value) {
        String rule = "RelativeLayout.ALIGN_PARENT_RIGHT";
        String ruleValue = getRuleValue(value);
        addRule(StringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean alignParentBottom(StringBuilder StringBuilder, String value) {
        String rule = "RelativeLayout.ALIGN_PARENT_BOTTOM";
        String ruleValue = getRuleValue(value);
        addRule(StringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean centerVertical(StringBuilder StringBuilder, String value) {
        String rule = "RelativeLayout.CENTER_VERTICAL";
        String ruleValue = getRuleValue(value);
        addRule(StringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean centerHorizontal(StringBuilder StringBuilder, String value) {
        String rule = "RelativeLayout.CENTER_HORIZONTAL";
        String ruleValue = getRuleValue(value);
        addRule(StringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean centerInParent(StringBuilder StringBuilder, String value) {
        String rule = "RelativeLayout.CENTER_IN_PARENT";
        String ruleValue = getRuleValue(value);
        addRule(StringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private void addRule(StringBuilder StringBuilder, String rule, String ruleValue) {
        if (mLayoutParamsObj != null) {
            StringBuilder.append(String.format("%s.addRule(%s,%s);\n", mLayoutParamsObj, rule, ruleValue));
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
