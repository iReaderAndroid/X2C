package com.zhangyue.we.anoprocesser;

import com.zhangyue.we.anoprocesser.xml.LayoutManager;
import com.zhangyue.we.anoprocesser.xml.Style;

import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * @author：chengwei 2018/8/8
 * @description
 */
public class View {
    private View mParent;
    private ArrayList<View> mChilds;
    private String mViewStr;
    private String mTagName;
    private String mName;
    private String mObjName;
    private String mLayoutParams;
    private String mLayoutParamsObj;
    private Attributes mAttributes;
    private String mPackageName;
    private TreeSet<String> mImports;
    private HashMap<String, String> mStyleAttributes;
    private int mIndex;
    private String mPadding = "0";
    private String mPaddingLeft = "0";
    private String mPaddingTop = "0";
    private String mPaddingRight = "0";
    private String mPaddingBottom = "0";

    public View(String packageName, String name, Attributes attributes) {
        mImports = new TreeSet<>();
        mPackageName = packageName;
        mName = getName(name);
        mTagName = name;
        mAttributes = attributes;

        mImports.add("android.content.res.Resources");
        mImports.add("android.view.View");
        mImports.add(String.format("%s.R", mPackageName));
    }

    public void setParent(View parent) {
        this.mParent = parent;
        if (parent != null) {
            parent.addChilden(this);
        }
        mViewStr = generateView(mAttributes);
    }

    public void addChilden(View child) {
        if (mChilds == null) {
            mChilds = new ArrayList<>();
        }
        mChilds.add(child);
    }

    public void translate(StringBuffer stringBuffer) {
        stringBuffer.append(mViewStr);
        if (mChilds != null) {
            for (View view : mChilds) {
                view.translate(stringBuffer);
                mImports.addAll(view.mImports);
            }
        }
    }

    public TreeSet<String> getImports() {
        return mImports;
    }

    private String getName(String name) {
        if (!name.contains(".")) {
            switch (name) {
                case "include":
                case "View":
                    name = "android.view.View";
                    break;
                case "WebView":
                    name = "android.webkit.WebView";
                    break;
                default:
                    name = "android.widget." + name;
                    break;
            }
        }

        mImports.add(name);
        return name.substring(name.lastIndexOf(".") + 1);
    }

    public String getObjName() {
        if (mObjName == null) {
            View root = getRootView();
            mObjName = mName.substring(0, 1).toLowerCase() + mName.substring(1) + root.mIndex;
            mLayoutParamsObj = "layoutParam" + root.mIndex;
            root.mIndex++;
        }
        return mObjName;
    }

    public String getLayoutParams() {
        if (mLayoutParams == null) {
            mLayoutParams = mName + ".LayoutParams";
        }
        return mLayoutParams;
    }


    private String generateView(Attributes attributes) {
        StringBuffer stringBuffer = new StringBuffer();
        if (mParent == null) {
            stringBuffer.append("\tResources res = ctx.getResources();\n\n");
        }
        String obj = getObjName();
        if (mTagName.equals("include")) {
            String javaName = LayoutManager.instance().translate(getIncludeLayout());
            stringBuffer.append(String.format("%s %s =(View) new %s().createView(ctx,0);\n"
                    , mName, obj, javaName));
        } else {
            stringBuffer.append(String.format("%s %s = new %s(ctx);\n", mName, obj, mName));
        }

        mStyleAttributes = getStyleAttribute();
        String width = getWidth(getWidthStr());
        String height = getHeight(getHeightStr());
        if (mParent != null) {
            String paramsName = mParent.getLayoutParams();
            stringBuffer.append(String.format("%s %s = new %s(%s,%s);\n", paramsName, mLayoutParamsObj
                    , paramsName, width, height));
            stringBuffer.append(String.format("%s.setLayoutParams(%s);\n", obj, mLayoutParamsObj));
            stringBuffer.append(String.format("%s.addView(%s);\n", mParent.getObjName(), obj));
        }

        String key;
        String value;

        if (mStyleAttributes != null) {
            for (String styleKey : mStyleAttributes.keySet()) {
                translate(stringBuffer, styleKey, mStyleAttributes.get(styleKey));
            }
        }

        int N = attributes.getLength();
        for (int i = 0; i < N; i++) {
            key = attributes.getQName(i);
            value = attributes.getValue(i);
            translate(stringBuffer, key, value);
        }


        if (!mPadding.equals("0")) {
            stringBuffer.append(getObjName()).append(String.format(".setPadding(%s,%s,%s,%s);\n", mPadding, mPadding, mPadding, mPadding));
        } else if (!mPaddingLeft.equals("0") || !mPaddingTop.equals("0") || !mPaddingRight.equals("0") || !mPaddingBottom.equals("0")) {
            stringBuffer.append(getObjName()).append(String.format(".setPadding(%s,%s,%s,%s);\n", mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom));
        }
        stringBuffer.append("\n");
        return stringBuffer.toString();
    }

    private String getWidthStr() {
        if (mAttributes == null) {
            return "";
        }
        String widthStr = mAttributes.getValue("android:layout_width");
        if (widthStr != null) {
            return widthStr;
        }
        if (mStyleAttributes != null) {
            return mStyleAttributes.get("android:layout_width");
        }
        return null;
    }

    private String getHeightStr() {
        if (mAttributes == null) {
            return "";
        }
        String widthStr = mAttributes.getValue("android:layout_height");
        if (widthStr != null) {
            return widthStr;
        }
        if (mStyleAttributes != null) {
            return mStyleAttributes.get("android:layout_height");
        }
        return null;
    }

    private void translate(StringBuffer stringBuffer, String key, String value) {
        switch (key) {
            case "android:textSize":
                setTextSize(stringBuffer, value);
                break;
            case "android:textColor":
                setTextColor(stringBuffer, value);
                break;
            case "android:text":
                setText(stringBuffer, value);
                break;
            case "android:background":
                setBackground(stringBuffer, value);
                break;
            case "android:textStyle":
                setTypeface(stringBuffer, value);
                break;
            case "android:layout_margin":
                setMargin(stringBuffer, value);
                break;
            case "android:layout_marginLeft":
                setMarginLeft(stringBuffer, value);
                break;
            case "android:tag":
                setTag(stringBuffer, value);
                break;
            case "android:layout_marginTop":
                setMarginTop(stringBuffer, value);
                break;
            case "android:layout_marginRight":
                setMarginRight(stringBuffer, value);
                break;
            case "android:layout_marginBottom":
                setMarginBottom(stringBuffer, value);
                break;
            case "android:paddingLeft":
                mPaddingLeft = getWH(value);
                break;
            case "android:paddingTop":
                mPaddingTop = getWH(value);
                break;
            case "android:paddingRight":
                mPaddingRight = getWH(value);
                break;
            case "android:paddingBottom":
                mPaddingBottom = getWH(value);
                break;
            case "android:padding":
                mPadding = getWH(value);
                break;
            case "android:gravity":
                setGravity(stringBuffer, value);
                break;
            case "android:orientation":
                setOrientation(stringBuffer, value);
                break;
            case "android:id":
                setId(stringBuffer, value);
                break;
            case "android:layout_centerInParent":
                centerInParent(stringBuffer, value);
                break;
            case "android:layout_centerVertical":
                centerVertical(stringBuffer, value);
                break;
            case "android:layout_centerHorizontal":
                centerHorizontal(stringBuffer, value);
                break;
            case "android:layout_alignParentLeft":
                alignParentLeft(stringBuffer, value);
                break;
            case "android:layout_alignParentTop":
                alignParentTop(stringBuffer, value);
                break;
            case "android:layout_alignParentRight":
                alignParentRight(stringBuffer, value);
                break;
            case "android:layout_alignParentBottom":
                alignParentBottom(stringBuffer, value);
                break;
            case "android:layout_above":
                above(stringBuffer, value);
                break;
            case "android:layout_below":
                below(stringBuffer, value);
                break;
            case "android:layout_toLeftOf":
                toLeftOf(stringBuffer, value);
                break;
            case "android:layout_toRightOf":
                toRightOf(stringBuffer, value);
                break;
            case "android:layout_alignLeft":
                alignLeft(stringBuffer, value);
                break;
            case "android:layout_alignTop":
                alignTop(stringBuffer, value);
                break;
            case "android:layout_alignRight":
                alignRight(stringBuffer, value);
                break;
            case "android:layout_alignBottom":
                alignBottom(stringBuffer, value);
                break;
            case "android:scaleType":
                setScaleType(stringBuffer, value);
                break;
            case "android:src":
                setImageResource(stringBuffer, value);
                break;
            case "android:visibility":
                setVisibility(stringBuffer, value);
                break;
            case "android:clipToPadding":
                setClipToPadding(stringBuffer, value);
                break;
            case "android:ellipsize":
                setEllipsize(stringBuffer, value);
                break;
            case "android:lineSpacingExtra":
                setLineSpacing(stringBuffer, value);
                break;
            case "android:maxLines":
                setMaxLines(stringBuffer, value);
                break;
        }
    }

    private void setMargin(StringBuffer stringBuffer, String value) {
        if (mLayoutParamsObj != null) {
            stringBuffer.append(getObjName()).append(String.format(".setMargins(%s,%s,%s,%s);\n",
                    getWH(value),
                    getWH(value), 
                    getWH(value), 
                    getWH(value)));
        }
    }

    private void setTypeface(StringBuffer stringBuffer, String value) {
        mImports.add("android.graphics.Typeface");
        stringBuffer.append(String.format("%s.setTypeface(%s);\n", getObjName(), getTextStyle(value)));
    }

    private void setScaleType(StringBuffer stringBuffer, String value) {
        mImports.add("android.widget.ImageView.ScaleType");
        stringBuffer.append(String.format("%s.setScaleType(%s);\n", getObjName(), getScaleType(value)));
    }

    private void setMaxLines(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setMaxLines(%s);\n", getObjName(), value));
    }

    private void setLineSpacing(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setLineSpacing(%s,1);\n", getObjName(), getDimen(value)));
    }

    private void setEllipsize(StringBuffer stringBuffer, String value) {
        mImports.add("android.text.TextUtils");
        stringBuffer.append(String.format("%s.setEllipsize(%s);\n", getObjName(), getEllipsize(value)));
    }

    private void setClipToPadding(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setClipToPadding(%s);\n", getObjName(), value));
    }

    private void setVisibility(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setVisibility(%s);\n", getObjName(), getVisibility(value)));
    }

    private void setTextColor(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setTextColor(%s);\n", getObjName(), getColor(value)));
    }

    private void setText(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setText(%s);\n", getObjName(), getString(value)));
    }

    private void setTag(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setTag(%s);\n", getObjName(), getString(value)));
    }

    private void setMarginLeft(StringBuffer stringBuffer, String value) {
        if (mLayoutParamsObj != null) {
            stringBuffer.append(String.format("%s.leftMargin= %s ;\n", mLayoutParamsObj, getWH(value)));
        }
    }

    private void setMarginTop(StringBuffer stringBuffer, String value) {
        if (mLayoutParamsObj != null) {
            stringBuffer.append(String.format("%s.topMargin= %s ;\n", mLayoutParamsObj, getWH(value)));
        }
    }

    private void setMarginRight(StringBuffer stringBuffer, String value) {
        if (mLayoutParamsObj != null) {
            stringBuffer.append(String.format("%s.rightMargin= %s ;\n", mLayoutParamsObj, getWH(value)));
        }
    }

    private void setMarginBottom(StringBuffer stringBuffer, String value) {
        if (mLayoutParamsObj != null) {
            stringBuffer.append(String.format("%s.bottomMargin= %s ;\n", mLayoutParamsObj, getWH(value)));
        }
    }


    private void alignLeft(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.ALIGN_LEFT";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
    }

    private void alignTop(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.ALIGN_TOP";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
    }

    private void alignRight(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.ALIGN_RIGHT";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
    }

    private void alignBottom(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.ALIGN_BOTTOM";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
    }

    private void toRightOf(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.RIGHT_OF";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
    }

    private void toLeftOf(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.LEFT_OF";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
    }

    private void above(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.ABOVE";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
    }

    private void below(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.BELOW";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
    }


    private void alignParentLeft(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.ALIGN_PARENT_LEFT";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
    }

    private void alignParentTop(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.ALIGN_PARENT_TOP";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
    }

    private void alignParentRight(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.ALIGN_PARENT_RIGHT";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
    }

    private void alignParentBottom(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.ALIGN_PARENT_BOTTOM";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
    }

    private void centerVertical(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.CENTER_VERTICAL";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
    }

    private void centerHorizontal(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.CENTER_HORIZONTAL";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
    }

    private void centerInParent(StringBuffer stringBuffer, String value) {
        String rule = "RelativeLayout.CENTER_IN_PARENT";
        String ruleValue = getRuleValue(value);
        addRule(stringBuffer, rule, ruleValue);
        mImports.add("android.widget.RelativeLayout");
    }

    private void addRule(StringBuffer stringBuffer, String rule, String ruleValue) {
        if (mLayoutParamsObj != null) {
            stringBuffer.append(String.format("%s.addRule(%s,%s);\n", mLayoutParamsObj, rule, ruleValue));
        }
    }

    private void setTextSize(StringBuffer stringBuffer, String value) {
        String unit;
        String dim;
        if (value.startsWith("@")) {
            unit = "TypedValue.COMPLEX_UNIT_PX";
            dim = String.format("(int)res.getDimension(R.dimen.%s)", value.substring(value.indexOf("/") + 1));
        } else {
            if (value.endsWith("dp") || value.endsWith("dip")) {
                unit = "TypedValue.COMPLEX_UNIT_DIP";
                dim = value.substring(0, value.indexOf("d"));
            } else if (value.endsWith("sp")) {
                unit = "TypedValue.COMPLEX_UNIT_SP";
                dim = value.substring(0, value.indexOf("s"));
            } else {
                unit = "TypedValue.COMPLEX_UNIT_PX";
                dim = value.substring(0, value.indexOf("p"));
            }
        }
        stringBuffer.append(String.format("%s.setTextSize(%s,%s);\n", getObjName(), unit, dim));
        mImports.add("android.util.TypedValue");
    }

    private void setImageResource(StringBuffer stringBuffer, String value) {
        if (value.startsWith("#") || value.startsWith("@color")) {
            stringBuffer.append(String.format("%s.setBackgroundColor(%s);\n", getObjName(), getColor(value)));
        } else {
            stringBuffer.append(String.format("%s.setImageResource(%s);\n", getObjName(), getDrawable(value)));
        }
    }

    private void setBackground(StringBuffer stringBuffer, String value) {
        if (value.startsWith("#") || value.startsWith("@color")) {
            stringBuffer.append(String.format("%s.setBackgroundColor(%s);\n", getObjName(), getColor(value)));
        } else if (value.equals("null")) {
            stringBuffer.append(String.format("%s.setBackgroundDrawable(%s);\n", getObjName(), "null"));
        } else {
            stringBuffer.append(String.format("%s.setBackgroundResource(%s);\n", getObjName(), getDrawable(value)));
        }
    }

    private void setOrientation(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setOrientation(%s);\n", getObjName(), getOrientation(value)));
    }

    private void setGravity(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setGravity(%s);\n", getObjName(), getGravity(value)));
    }

    private void setId(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setId(R.id.%s);\n", getObjName(),
                value.substring(value.indexOf("/") + 1)));
    }

    private String getWidth(String value) {
        if (value == null) {
            mImports.add("android.view.ViewGroup");
            return "ViewGroup.LayoutParams.MATCH_PARENT";
        }
        return getWH(value);
    }

    private String getHeight(String value) {
        if (value == null) {
            mImports.add("android.view.ViewGroup");
            return "ViewGroup.LayoutParams.WRAP_CONTENT";
        }
        return getWH(value);
    }

    private String getWH(String value) {
        if (value == null) {
            return "0";
        }
        mImports.add("android.view.ViewGroup");
        switch (value) {
            case "fill_parent":
                return "ViewGroup.LayoutParams.FILL_PARENT";
            case "match_parent":
                return "ViewGroup.LayoutParams.MATCH_PARENT";
            case "wrap_content":
                return "ViewGroup.LayoutParams.WRAP_CONTENT";
            default:
                return getDimen(value);
        }
    }

    private String getDimen(String value) {
        if (value.startsWith("@")) {
            return String.format("(int)res.getDimension(R.dimen.%s)", value.substring(value.indexOf("/") + 1));
        }
        String unit;
        String dim;

        if (value.endsWith("dp") || value.endsWith("dip")) {
            unit = "TypedValue.COMPLEX_UNIT_DIP";
            dim = value.substring(0, value.indexOf("d"));
        } else if (value.endsWith("sp")) {
            unit = "TypedValue.COMPLEX_UNIT_SP";
            dim = value.substring(0, value.indexOf("s"));
        } else {
            unit = "TypedValue.COMPLEX_UNIT_PX";
            dim = value.substring(0, value.indexOf("p"));
        }
        mImports.add("android.util.TypedValue");
        return String.format("(int)(TypedValue.applyDimension(%s,%s,res.getDisplayMetrics()))"
                , unit, dim);
    }

    private String getColor(String value) {
        if (value.startsWith("#")) {
            mImports.add("android.graphics.Color");
            return "Color.parseColor(\"" + value + "\")";
        } else if (value.startsWith("@")) {
            return "res.getColor(R.color." + value.substring(value.indexOf("/") + 1) + ")";
        } else {
            return "0";
        }
    }

    private String getString(String value) {
        if (value.startsWith("@")) {
            return "R.string." + value.substring(value.indexOf("/") + 1);
        }
        return String.format("\"%s\"", value);
    }

    private String getDrawable(String value) {
        if (value.startsWith("@")) {
            return "R.drawable." + value.substring(value.indexOf("/") + 1);
        }
        return value;
    }


    private String getGravity(String value) {
        String[] ss = value.split("\\|");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ss.length; i++) {
            sb.append(getGravitySingle(ss[i]));
            if (i < ss.length - 1) {
                sb.append("|");
            }
        }
        return sb.toString();
    }

    private String getGravitySingle(String value) {
        mImports.add("android.view.Gravity");
        switch (value) {
            case "left":
                return "Gravity.LEFT";
            case "top":
                return "Gravity.TOP";
            case "right":
                return "Gravity.RIGHT";
            case "bottom":
                return "Gravity.BOTTOM";
            case "center":
                return "Gravity.CENTER";
            case "center_vertical":
                return "Gravity.CENTER_VERTICAL";
            case "center_horizontal":
                return "Gravity.CENTER_HORIZONTAL";
            case "fill":
                return "Gravity.FILL";
            case "fill_horizontal":
                return "Gravity.FILL_HORIZONTAL";
            case "fill_vertical":
                return "Gravity.FILL_VERTICAL";
            case "clip_vertical":
                return "Gravity.CLIP_VERTICAL";
            case "clip_horizontal":
                return "Gravity.CLIP_HORIZONTAL";
            default:
                return "Gravity.LEFT";
        }
    }

    private String getOrientation(String value) {
        mImports.add("android.widget.LinearLayout");
        switch (value) {
            case "vertical":
                return "LinearLayout.VERTICAL";
            default:
                return "LinearLayout.HORIZONTAL";
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

    private String getIncludeLayout() {
        String layout = mAttributes.getValue("layout");
        return layout.substring(layout.lastIndexOf("/") + 1);
    }

    private View getRootView() {
        View root = this;
        while (root.mParent != null) {
            root = root.mParent;
        }
        return root;
    }

    private HashMap<String, String> getStyleAttribute() {
        if (mAttributes == null || mStyleAttributes != null) {
            return mStyleAttributes;
        }

        String styleName = mAttributes.getValue("style");

        if (styleName != null && styleName.startsWith("@")) {
            LayoutManager layoutMgr = LayoutManager.instance();
            Style style = layoutMgr.getStyle(styleName.substring(styleName.lastIndexOf("/") + 1));
            if (style != null) {
                mStyleAttributes = style.attribute;
                while (style.parent != null) {
                    style = layoutMgr.getStyle(style.parent);
                    if (style != null) {
                        for (String key : style.attribute.keySet()) {
                            if (!mStyleAttributes.containsKey(key)) {
                                mStyleAttributes.put(key, style.attribute.get(key));
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        return mStyleAttributes;

    }

    private String getVisibility(String value) {
        switch (value) {
            case "gone":
                return "View.GONE";
            case "invisible":
                return "View.INVISIBLE";
            default:
                return "View.VISIBLE";
        }
    }


    private String getEllipsize(String value) {
        switch (value) {
            case "start":
                return "TextUtils.TruncateAt.START";
            case "middle":
                return "TextUtils.TruncateAt.MIDDLE";
            case "marquee":
                return "TextUtils.TruncateAt.MARQUEE";
            default:
                return "TextUtils.TruncateAt.END";
        }
    }

    private String getTextStyle(String value) {
        switch (value) {
            case "bold":
                return "Typeface.DEFAULT_BOLD";
            case "italic":
                return "Typeface.ITALIC";
            default:
                return "Typeface.DEFAULT";
        }
    }

    private String getScaleType(String value) {
        switch (value) {
            case "matrix":
                return "ScaleType.MATRIX";
            case "fitStart":
                return "ScaleType.FIT_START";
            case "fitCenter":
                return "ScaleType.FIT_CENTER";
            case "fitEnd":
                return "ScaleType.FIT_END";
            case "center":
                return "ScaleType.CENTER";
            case "centerCrop":
                return "ScaleType.CENTER_CROP";
            case "centerInside":
                return "ScaleType.CENTER_INSIDE";
            default:
                return "ScaleType.FIT_XY";
        }
    }
}
