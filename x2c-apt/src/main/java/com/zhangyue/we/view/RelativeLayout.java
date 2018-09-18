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
    public boolean translate(StringBuilder stringBuilder, String key, String value) {
        switch (key) {
            case "android:layout_centerInParent":
                return centerInParent(stringBuilder, value);
            case "android:layout_centerVertical":
                return centerVertical(stringBuilder, value);
            case "android:layout_centerHorizontal":
                return centerHorizontal(stringBuilder, value);
            case "android:layout_alignParentLeft":
                return alignParentLeft(stringBuilder, value);
            case "android:layout_alignParentTop":
                return alignParentTop(stringBuilder, value);
            case "android:layout_alignParentRight":
                return alignParentRight(stringBuilder, value);
            case "android:layout_alignParentBottom":
                return alignParentBottom(stringBuilder, value);
            case "android:layout_above":
                return above(stringBuilder, value);
            case "android:layout_below":
                return below(stringBuilder, value);
            case "android:layout_toLeftOf":
                return toLeftOf(stringBuilder, value);
            case "android:layout_toRightOf":
                return toRightOf(stringBuilder, value);
            case "android:layout_alignLeft":
                return alignLeft(stringBuilder, value);
            case "android:layout_alignTop":
                return alignTop(stringBuilder, value);
            case "android:layout_alignRight":
                return alignRight(stringBuilder, value);
            case "android:layout_alignBottom":
                return alignBottom(stringBuilder, value);
            default:
                return false;
        }
    }

    @Override
    public void onAttributeEnd(StringBuilder stringBuilder) {

    }

    private boolean alignLeft(StringBuilder stringBuilder, String value) {
        String rule = "RelativeLayout.ALIGN_LEFT";
        String ruleValue = getRuleValue(value);
        addRule(stringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean alignTop(StringBuilder stringBuilder, String value) {
        String rule = "RelativeLayout.ALIGN_TOP";
        String ruleValue = getRuleValue(value);
        addRule(stringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean alignRight(StringBuilder stringBuilder, String value) {
        String rule = "RelativeLayout.ALIGN_RIGHT";
        String ruleValue = getRuleValue(value);
        addRule(stringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean alignBottom(StringBuilder stringBuilder, String value) {
        String rule = "RelativeLayout.ALIGN_BOTTOM";
        String ruleValue = getRuleValue(value);
        addRule(stringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean toRightOf(StringBuilder stringBuilder, String value) {
        String rule = "RelativeLayout.RIGHT_OF";
        String ruleValue = getRuleValue(value);
        addRule(stringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean toLeftOf(StringBuilder stringBuilder, String value) {
        String rule = "RelativeLayout.LEFT_OF";
        String ruleValue = getRuleValue(value);
        addRule(stringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean above(StringBuilder stringBuilder, String value) {
        String rule = "RelativeLayout.ABOVE";
        String ruleValue = getRuleValue(value);
        addRule(stringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean below(StringBuilder stringBuilder, String value) {
        String rule = "RelativeLayout.BELOW";
        String ruleValue = getRuleValue(value);
        addRule(stringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }


    private boolean alignParentLeft(StringBuilder stringBuilder, String value) {
        String rule = "RelativeLayout.ALIGN_PARENT_LEFT";
        String ruleValue = getRuleValue(value);
        addRule(stringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean alignParentTop(StringBuilder stringBuilder, String value) {
        String rule = "RelativeLayout.ALIGN_PARENT_TOP";
        String ruleValue = getRuleValue(value);
        addRule(stringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean alignParentRight(StringBuilder stringBuilder, String value) {
        String rule = "RelativeLayout.ALIGN_PARENT_RIGHT";
        String ruleValue = getRuleValue(value);
        addRule(stringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean alignParentBottom(StringBuilder stringBuilder, String value) {
        String rule = "RelativeLayout.ALIGN_PARENT_BOTTOM";
        String ruleValue = getRuleValue(value);
        addRule(stringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean centerVertical(StringBuilder stringBuilder, String value) {
        String rule = "RelativeLayout.CENTER_VERTICAL";
        String ruleValue = getRuleValue(value);
        addRule(stringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean centerHorizontal(StringBuilder stringBuilder, String value) {
        String rule = "RelativeLayout.CENTER_HORIZONTAL";
        String ruleValue = getRuleValue(value);
        addRule(stringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private boolean centerInParent(StringBuilder stringBuilder, String value) {
        String rule = "RelativeLayout.CENTER_IN_PARENT";
        String ruleValue = getRuleValue(value);
        addRule(stringBuilder, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
        return true;
    }

    private void addRule(StringBuilder stringBuilder, String rule, String ruleValue) {
        if (mLayoutParamsObj != null) {
            stringBuilder.append(String.format("%s.addRule(%s,%s);\n", mLayoutParamsObj, rule, ruleValue));
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
