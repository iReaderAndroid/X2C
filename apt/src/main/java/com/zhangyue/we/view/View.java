package com.zhangyue.we.view;

import com.zhangyue.we.anoprocesser.Log;
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
public class View implements ITranslator {
    private View mParent;
    private ArrayList<View> mChilds;
    private String mViewStr;
    private String mTagName;
    private String mName;
    private String mObjName;
    private String mLayoutParams;
    protected String mLayoutParamsObj;
    private Attributes mAttributes;
    private String mPackageName;
    protected TreeSet<String> mImports;
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
        mImports.add("android.util.TypedValue");
        mImports.add("android.graphics.Color");
        mImports.add("android.view.ViewGroup");
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
                case "ViewStub":
                    name = "android.view.ViewStub";
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
        }

        ArrayList<ITranslator> translators = createTranslator();
        if (mStyleAttributes != null) {
            for (String styleKey : mStyleAttributes.keySet()) {
                for (ITranslator translator : translators) {
                    if (translator.translate(stringBuffer, styleKey, mStyleAttributes.get(styleKey))) {
                        break;
                    }
                }
            }
        }

        String key;
        String value;
        int N = attributes.getLength();
        for (int i = 0; i < N; i++) {
            key = attributes.getQName(i);
            value = attributes.getValue(i);
            for (ITranslator translator : translators) {
                if (translator.translate(stringBuffer, key, value)) {
                    break;
                }
            }
        }

        for (ITranslator translator : translators) {
            translator.onAttributeEnd(stringBuffer);
        }

        if (mParent != null) {
            stringBuffer.append(String.format("%s.setLayoutParams(%s);\n", obj, mLayoutParamsObj));
            stringBuffer.append(String.format("%s.addView(%s);\n", mParent.getObjName(), obj));
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

    @Override
    public boolean translate(StringBuffer stringBuffer, String key, String value) {
        switch (key) {
            case "android:textSize":
                return setTextSize(stringBuffer, value);
            case "android:textColor":
                return setTextColor(stringBuffer, value);
            case "android:text":
                return setText(stringBuffer, value);
            case "android:background":
                return setBackground(stringBuffer, value);
            case "android:textStyle":
                return setTypeface(stringBuffer, value);
            case "android:layout_margin":
                return setMargin(stringBuffer, value);
            case "android:layout_marginLeft":
                return setMarginLeft(stringBuffer, value);
            case "android:tag":
                return setTag(stringBuffer, value);
            case "android:layout_marginTop":
                return setMarginTop(stringBuffer, value);
            case "android:layout_marginRight":
                return setMarginRight(stringBuffer, value);
            case "android:layout_marginBottom":
                return setMarginBottom(stringBuffer, value);
            case "android:paddingLeft":
                mPaddingLeft = getWH(value);
                return true;
            case "android:paddingTop":
                mPaddingTop = getWH(value);
                return true;
            case "android:paddingRight":
                mPaddingRight = getWH(value);
                return true;
            case "android:paddingBottom":
                mPaddingBottom = getWH(value);
                return true;
            case "android:padding":
                mPadding = getWH(value);
                return true;
            case "android:gravity":
                return setGravity(stringBuffer, value);
            case "android:orientation":
                return setOrientation(stringBuffer, value);
            case "android:id":
                return setId(stringBuffer, value);
            case "android:scaleType":
                return setScaleType(stringBuffer, value);
            case "android:src":
                return setImageResource(stringBuffer, value);
            case "android:visibility":
                return setVisibility(stringBuffer, value);
            case "android:clipToPadding":
                return setClipToPadding(stringBuffer, value);
            case "android:ellipsize":
                return setEllipsize(stringBuffer, value);
            case "android:lineSpacingExtra":
                return setLineSpacing(stringBuffer, value);
            case "android:maxLines":
                return setMaxLines(stringBuffer, value);
            case "android:maxHeight":
                return setMaxHeight(stringBuffer, value);
            case "android:maxWidth":
                return setMaxWidth(stringBuffer, value);
            case "android:minWidth":
                return setMinWidth(stringBuffer, value);
            case "android:minHeight":
                return setMinHeight(stringBuffer, value);
            default:
                return false;
        }
    }

    private boolean setMargin(StringBuffer stringBuffer, String value) {
        if (mLayoutParamsObj != null) {
            stringBuffer.append(String.format("%s.setMargins(%s,%s,%s,%s);\n",
                    mLayoutParamsObj,
                    getWH(value),
                    getWH(value),
                    getWH(value),
                    getWH(value)));
        }
        return true;
    }

    private boolean setTypeface(StringBuffer stringBuffer, String value) {
        mImports.add("android.graphics.Typeface");
        stringBuffer.append(String.format("%s.setTypeface(%s);\n", getObjName(), getTextStyle(value)));
        return true;
    }

    @Override
    public void onAttributeEnd(StringBuffer stringBuffer) {

    }

    private boolean setScaleType(StringBuffer stringBuffer, String value) {
        mImports.add("android.widget.ImageView.ScaleType");
        stringBuffer.append(String.format("%s.setScaleType(%s);\n", getObjName(), getScaleType(value)));
        return true;
    }

    private boolean setMaxLines(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setMaxLines(%s);\n", getObjName(), value));
        return true;
    }

    private boolean setMaxHeight(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setMaxHeight(%s);\n", getObjName(), getDimen(value)));
        return true;
    }

    private boolean setMaxWidth(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setMaxWidth(%s);\n", getObjName(), getDimen(value)));
        return true;
    }

    private boolean setMinWidth(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setMinWidth(%s);\n", getObjName(), getDimen(value)));
        return true;
    }

    private boolean setMinHeight(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setMinHeight(%s);\n", getObjName(), getDimen(value)));
        return true;
    }

    private boolean setLineSpacing(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setLineSpacing(%s,1);\n", getObjName(), getDimen(value)));
        return true;
    }

    private boolean setEllipsize(StringBuffer stringBuffer, String value) {
        mImports.add("android.text.TextUtils");
        stringBuffer.append(String.format("%s.setEllipsize(%s);\n", getObjName(), getEllipsize(value)));
        return true;
    }

    private boolean setClipToPadding(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setClipToPadding(%s);\n", getObjName(), value));
        return true;
    }

    private boolean setVisibility(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setVisibility(%s);\n", getObjName(), getVisibility(value)));
        return true;
    }

    private boolean setTextColor(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setTextColor(%s);\n", getObjName(), getColor(value)));
        return true;
    }

    private boolean setText(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setText(%s);\n", getObjName(), getString(value)));
        return true;
    }

    private boolean setTag(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setTag(%s);\n", getObjName(), getString(value)));
        return true;
    }

    private boolean setMarginLeft(StringBuffer stringBuffer, String value) {
        if (mLayoutParamsObj != null) {
            stringBuffer.append(String.format("%s.leftMargin= %s ;\n", mLayoutParamsObj, getWH(value)));
        }
        return true;
    }

    private boolean setMarginTop(StringBuffer stringBuffer, String value) {
        if (mLayoutParamsObj != null) {
            stringBuffer.append(String.format("%s.topMargin= %s ;\n", mLayoutParamsObj, getWH(value)));
        }
        return true;
    }

    private boolean setMarginRight(StringBuffer stringBuffer, String value) {
        if (mLayoutParamsObj != null) {
            stringBuffer.append(String.format("%s.rightMargin= %s ;\n", mLayoutParamsObj, getWH(value)));
        }
        return true;
    }

    private boolean setMarginBottom(StringBuffer stringBuffer, String value) {
        if (mLayoutParamsObj != null) {
            stringBuffer.append(String.format("%s.bottomMargin= %s ;\n", mLayoutParamsObj, getWH(value)));
        }
        return true;
    }


    private boolean setTextSize(StringBuffer stringBuffer, String value) {
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
        return true;
    }

    private boolean setImageResource(StringBuffer stringBuffer, String value) {
        if (value.startsWith("#") || value.startsWith("@color")) {
            stringBuffer.append(String.format("%s.setBackgroundColor(%s);\n", getObjName(), getColor(value)));
        } else {
            stringBuffer.append(String.format("%s.setImageResource(%s);\n", getObjName(), getDrawable(value)));
        }
        return true;
    }

    private boolean setBackground(StringBuffer stringBuffer, String value) {
        if (value.startsWith("#") || value.startsWith("@color")) {
            stringBuffer.append(String.format("%s.setBackgroundColor(%s);\n", getObjName(), getColor(value)));
        } else if (value.equals("null")) {
            stringBuffer.append(String.format("%s.setBackgroundDrawable(%s);\n", getObjName(), "null"));
        } else {
            stringBuffer.append(String.format("%s.setBackgroundResource(%s);\n", getObjName(), getDrawable(value)));
        }
        return true;
    }

    private boolean setOrientation(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setOrientation(%s);\n", getObjName(), getOrientation(value)));
        return true;
    }

    private boolean setGravity(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setGravity(%s);\n", getObjName(), getGravity(value)));
        return true;
    }

    private boolean setId(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.setId(R.id.%s);\n", getObjName(),
                value.substring(value.indexOf("/") + 1)));
        return true;
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

    public static String getWH(String value) {
        if (value == null) {
            return "0";
        }
        switch (value) {
            case "fill_parent":
                return "ViewGroup.LayoutParams.FILL_PARENT";
            case "match_parent":
                return "ViewGroup.LayoutParams.MATCH_PARENT";
            case "wrap":
            case "wrap_content":
                return "ViewGroup.LayoutParams.WRAP_CONTENT";
            default:
                return getDimen(value);
        }
    }

    public static String getDimen(String value) {
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

        return String.format("(int)(TypedValue.applyDimension(%s,%s,res.getDisplayMetrics()))"
                , unit, dim);
    }

    public static String getColor(String value) {
        if (value.startsWith("#")) {
            return "Color.parseColor(\"" + value + "\")";
        } else if (value.startsWith("@")) {
            return "res.getColor(R.color." + value.substring(value.indexOf("/") + 1) + ")";
        } else {
            return "0";
        }
    }

    public static String getId(String value) {
        if (value.contains("id/")) {
            return "R.id." + value.substring(value.lastIndexOf("/") + 1);
        }
        return "0";
    }

    public static String getFloat(String value) {
        return value + "f";
    }

    public static String getBoolean(String value) {
        if (value.startsWith("@")) {
            return String.format("res.getBoolean(R.bool.%s)") + value.substring(value.indexOf("/") + 1);
        }
        return value;
    }

    public static String getString(String value) {
        if (value.startsWith("@")) {
            return "R.string." + value.substring(value.indexOf("/") + 1);
        }
        return String.format("\"%s\"", value);
    }

    public static String getDrawable(String value) {
        if (value.startsWith("@drawable")) {
            return "R.drawable." + value.substring(value.indexOf("/") + 1);
        } else if (value.startsWith("@mipmap")) {
            return "R.mipmap." + value.substring(value.indexOf("/") + 1);
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

    private ArrayList<ITranslator> createTranslator() {
        ArrayList<ITranslator> list = new ArrayList();
        list.add(this);
        list.add(new ConstraintLayout(mImports, mLayoutParamsObj));
        list.add(new RelativeLayout(mImports, mLayoutParamsObj));
        list.add(new CustomAttr(mImports, mLayoutParamsObj, mObjName));
        return list;
    }


}
