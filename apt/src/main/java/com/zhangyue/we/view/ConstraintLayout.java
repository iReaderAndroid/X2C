package com.zhangyue.we.view;

import java.util.TreeSet;

/**
 * @author：chengwei 2018/8/23
 * @description
 */
public class ConstraintLayout implements ITranslator {
    private TreeSet<String> mImports;
    private String mLayoutParamsObj;
    private boolean isHandle;

    public ConstraintLayout(TreeSet<String> imports, String layoutParamsObj) {
        this.mImports = imports;
        this.mLayoutParamsObj = layoutParamsObj;
    }

    @Override
    public boolean translate(StringBuffer stringBuffer, String key, String value) {
        switch (key) {
            case "app:layout_constraintDimensionRatio":
                return setLayoutConstraintDimensionRatio(stringBuffer, value);
            case "app:layout_constraintLeft_toLeftOf":
                return setLayoutConstraintLeftToLeftOf(stringBuffer, value);
            case "app:layout_constraintRight_toRightOf":
                return setLayoutConstraintRightToRightOf(stringBuffer, value);
            case "app:layout_constraintTop_toBottomOf":
                return setLayoutConstraintTopToBottomOf(stringBuffer, value);
            case "app:layout_constraintBottom_toBottomOf":
                return setLayoutConstraintBottomToBottomOf(stringBuffer, value);
            case "app:layout_constraintLeft_toRightOf":
                return setLayoutConstraintLeftToRightOf(stringBuffer, value);
            case "app:layout_constraintRight_toLeftOf":
                return setLayoutConstraintRightToLeftOf(stringBuffer, value);
            case "app:layout_constraintBottom_toTopOf":
                return setLayoutConstraintBottomToTopOf(stringBuffer, value);
            case "app:layout_constraintTop_toTopOf":
                return setLayoutConstraintTopToTopOf(stringBuffer, value);
            case "app:layout_constrainedHeight":
                return setLayoutConstrainedHeight(stringBuffer, value);
            case "app:layout_constrainedWidth":
                return setLayoutConstrainedWidth(stringBuffer, value);
            case "app:layout_constraintVertical_bias":
                return setLayoutConstraintVerticalBias(stringBuffer, value);
            case "app:layout_constraintHorizontal_bias":
                return setLayoutConstraintHorizontalBias(stringBuffer, value);
            case "app:layout_constraintHeight_default":
                return setLayoutConstraintHeightDefault(stringBuffer, value);
            case "app:layout_constraintWidth_default":
                return setLayoutConstraintWidthDefault(stringBuffer, value);
            case "app:layout_constraintVertical_weight":
                return setLayoutConstraintVerticalWeight(stringBuffer, value);
            case "app:layout_constraintHorizontal_weight":
                return setLayoutConstraintHorizontalWeight(stringBuffer, value);
            case "app:layout_constraintVertical_chainStyle":
                return setLayoutConstraintVerticalChainStyle(stringBuffer, value);
            case "app:layout_constraintHorizontal_chainStyle":
                return setLayoutConstraintHorizontalChainStyle(stringBuffer, value);
            case "app:layout_constraintCircle":
                return setLayoutConstraintCircle(stringBuffer, value);
            case "app:layout_constraintCircleAngle":
                return setLayoutConstraintCircleAngle(stringBuffer, value);
            case "app:layout_constraintCircleRadius":
                return setLayoutConstraintCircleRadius(stringBuffer, value);
            case "app:layout_goneMarginBottom":
                return setGoneMarginBottom(stringBuffer, value);
            case "app:layout_goneMarginLeft":
                return setGoneMarginLeft(stringBuffer, value);
            case "app:layout_goneMarginRight":
                return setGoneMarginRight(stringBuffer, value);
            case "app:layout_goneMarginTop":
                return setGoneMarginTop(stringBuffer, value);
            case "app:layout_goneMarginEnd":
                return setGoneMarginEnd(stringBuffer, value);
            case "app:layout_goneMarginStart":
                return setGoneMarginStart(stringBuffer, value);
            case "app:layout_editor_absoluteY":
                return setLayoutEditorAbsoluteY(stringBuffer, value);
            case "app:layout_editor_absoluteX":
                return setLayoutEditorAbsoluteX(stringBuffer, value);
            case "app:layout_constraintBaseline_toBaselineOf":
                return setLayoutConstraintBaselineToBaselineOf(stringBuffer, value);
            case "app:layout_constraintStart_toEndOf":
                return setLayoutConstraintStartToEndOf(stringBuffer, value);
            case "app:layout_constraintEnd_toEndOf":
                return setLayoutConstraintEndToEndOf(stringBuffer, value);
            case "app:layout_constraintEnd_toStartOf":
                return setLayoutConstraintEndToStartOf(stringBuffer, value);
            case "app:layout_constraintStart_toStartOf":
                return setLayoutConstraintStartToStartOf(stringBuffer, value);
            case "app:layout_constraintGuide_begin":
                return setLayoutConstraintGuideBegin(stringBuffer, value);
            case "app:layout_constraintGuide_end":
                return setLayoutConstraintGuideEnd(stringBuffer, value);
            case "app:layout_constraintGuide_percent":
                return setLayoutConstraintGuidePercent(stringBuffer, value);
            case "app:layout_constraintWidth_min":
                return setLayoutConstraintWidthMin(stringBuffer, value);
            case "app:layout_constraintWidth_max":
                return setLayoutConstraintWidthMax(stringBuffer, value);
            case "app:layout_constraintHeight_min":
                return setLayoutConstraintHeightMin(stringBuffer, value);
            case "app:layout_constraintHeight_max":
                return setLayoutConstraintHeightMax(stringBuffer, value);
            case "app:layout_constraintHeight_percent":
                return setLayoutConstraintHeightPercent(stringBuffer, value);
            case "app:layout_constraintWidth_percent":
                return setLayoutConstraintWidthPercent(stringBuffer, value);

            default:
                return false;
        }
    }

    @Override
    public void onAttributeEnd(StringBuffer stringBuffer) {
        if (isHandle) {
            stringBuffer.append(String.format("%s.validate();\n", mLayoutParamsObj));
        }
    }


    private boolean setLayoutConstraintWidthPercent(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.matchConstraintPercentWidth = %s ;\n", mLayoutParamsObj, getPositiveFloat(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintHeightPercent(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.matchConstraintPercentHeight = %s ;\n", mLayoutParamsObj, getPositiveFloat(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintHeightMax(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.matchConstraintMaxHeight = %s ;\n", mLayoutParamsObj, View.getWH(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintHeightMin(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.matchConstraintMinHeight = %s ;\n", mLayoutParamsObj, View.getWH(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintWidthMax(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.matchConstraintMaxWidth = %s ;\n", mLayoutParamsObj, View.getWH(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintWidthMin(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.matchConstraintMinWidth = %s ;\n", mLayoutParamsObj, View.getWH(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintGuidePercent(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.guidePercent = %s ;\n", mLayoutParamsObj, View.getFloat(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintGuideEnd(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.guideEnd = %s ;\n", mLayoutParamsObj, View.getDimen(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintGuideBegin(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.guideBegin = %s ;\n", mLayoutParamsObj, View.getDimen(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintStartToStartOf(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.startToStart = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintEndToStartOf(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.endToStart = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintEndToEndOf(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.endToEnd = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintStartToEndOf(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.startToEnd = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintBaselineToBaselineOf(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.baselineToBaseline = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutEditorAbsoluteX(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.editorAbsoluteX = %s ;\n", mLayoutParamsObj, View.getDimen(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutEditorAbsoluteY(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.editorAbsoluteY = %s ;\n", mLayoutParamsObj, View.getDimen(value)));
        isHandle = true;
        return true;
    }

    private boolean setGoneMarginStart(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.goneStartMargin = %s ;\n", mLayoutParamsObj, View.getDimen(value)));
        isHandle = true;
        return true;
    }

    private boolean setGoneMarginEnd(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.goneEndMargin = %s ;\n", mLayoutParamsObj, View.getDimen(value)));
        isHandle = true;
        return true;
    }

    private boolean setGoneMarginTop(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.goneTopMargin = %s ;\n", mLayoutParamsObj, View.getDimen(value)));
        isHandle = true;
        return true;
    }

    private boolean setGoneMarginRight(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.goneRightMargin = %s ;\n", mLayoutParamsObj, View.getDimen(value)));
        isHandle = true;
        return true;
    }

    private boolean setGoneMarginLeft(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.goneLeftMargin = %s ;\n", mLayoutParamsObj, View.getDimen(value)));
        isHandle = true;
        return true;
    }

    private boolean setGoneMarginBottom(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.goneBottomMargin = %s ;\n", mLayoutParamsObj, View.getDimen(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintCircleRadius(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.circleRadius = %s ;\n", mLayoutParamsObj, View.getDimen(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintCircleAngle(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.circleAngle = %s ;\n", mLayoutParamsObj, getCircleAngle(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintCircle(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.circleConstraint = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintHorizontalChainStyle(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.horizontalChainStyle = %s ;\n", mLayoutParamsObj, getConstraintChainStyle(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintVerticalChainStyle(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.verticalChainStyle = %s ;\n", mLayoutParamsObj, getConstraintChainStyle(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintHorizontalWeight(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.horizontalWeight = %s ;\n", mLayoutParamsObj, View.getFloat(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintVerticalWeight(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.verticalWeight = %s ;\n", mLayoutParamsObj, View.getFloat(value)));
        isHandle = true;
        return true;
    }


    private boolean setLayoutConstraintWidthDefault(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.matchConstraintDefaultWidth = %s ;\n", mLayoutParamsObj, getConstraintMode(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintHeightDefault(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.matchConstraintDefaultHeight = %s ;\n", mLayoutParamsObj, getConstraintMode(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintHorizontalBias(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.horizontalBias = %s ;\n", mLayoutParamsObj, View.getFloat(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintVerticalBias(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.verticalBias = %s ;\n", mLayoutParamsObj, View.getFloat(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstrainedWidth(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.constrainedWidth = %s ;\n", mLayoutParamsObj, View.getBoolean(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstrainedHeight(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.constrainedHeight = %s ;\n", mLayoutParamsObj, View.getBoolean(value)));
        isHandle = true;
        return true;
    }


    private boolean setLayoutConstraintTopToTopOf(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.topToTop = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintBottomToTopOf(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.bottomToTop = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintRightToLeftOf(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.rightToLeft = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintLeftToRightOf(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.leftToRight = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintTopToBottomOf(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.topToBottom = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintBottomToBottomOf(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.bottomToBottom = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintLeftToLeftOf(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.leftToLeft = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintRightToRightOf(StringBuffer stringBuffer, String value) {
        stringBuffer.append(String.format("%s.rightToRight = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }


    private boolean setLayoutConstraintDimensionRatio(StringBuffer stringBuffer, String value) {
        String dimensionRatio = value;
        stringBuffer.append(String.format("%s.dimensionRatio=\"%s\";\n", mLayoutParamsObj, dimensionRatio));
        int dimensionRatioSide = -1;

        int len = dimensionRatio.length();
        int commaIndex = dimensionRatio.indexOf(44);
        if (commaIndex > 0 && commaIndex < len - 1) {
            String dimension = dimensionRatio.substring(0, commaIndex);
            if (dimension.equalsIgnoreCase("W")) {
                dimensionRatioSide = 0;
                setFieldValue(stringBuffer, mLayoutParamsObj, "dimensionRatioSide", dimensionRatioSide);
            } else if (dimension.equalsIgnoreCase("H")) {
                dimensionRatioSide = 1;
                setFieldValue(stringBuffer, mLayoutParamsObj, "dimensionRatioSide", dimensionRatioSide);
            }

            ++commaIndex;
        } else {
            commaIndex = 0;
        }

        int colonIndex = dimensionRatio.indexOf(58);
        String r;
        if (colonIndex >= 0 && colonIndex < len - 1) {
            r = dimensionRatio.substring(commaIndex, colonIndex);
            String denominator = dimensionRatio.substring(colonIndex + 1);
            if (r.length() > 0 && denominator.length() > 0) {
                try {
                    float nominatorValue = Float.parseFloat(r);
                    float denominatorValue = Float.parseFloat(denominator);
                    if (nominatorValue > 0.0F && denominatorValue > 0.0F) {
                        if (dimensionRatioSide == 1) {
                            setFieldValue(stringBuffer, mLayoutParamsObj, "dimensionRatioValue", Math.abs(denominatorValue / nominatorValue) + "f");
                        } else {
                            setFieldValue(stringBuffer, mLayoutParamsObj, "dimensionRatioValue", Math.abs(nominatorValue / denominatorValue) + "f");
                        }
                    }
                } catch (NumberFormatException var16) {
                }
            }
        } else {
            r = dimensionRatio.substring(commaIndex);
            if (r.length() > 0) {
                try {
                    setFieldValue(stringBuffer, mLayoutParamsObj, "dimensionRatioValue", Float.parseFloat(r) + "f");
                } catch (NumberFormatException var15) {
                }
            }
        }
        isHandle = true;
        return true;
    }

    private String getConstraintMode(String value) {
        switch (value) {
            case "spread":
                return "ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_SPREAD";
            case "wrap":
                return "ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_WRAP";
            default:
                return "ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_PERCENT";
        }
    }

    private String getConstraintChainStyle(String value) {
        switch (value) {
            case "spread":
                return "ConstraintLayout.LayoutParams.CHAIN_SPREAD";
            case "spread_inside":
                return "ConstraintLayout.LayoutParams.CHAIN_SPREAD_INSIDE";
            default:
                return "ConstraintLayout.LayoutParams.CHAIN_PACKED";
        }
    }

    private String getResourceId(String value) {
        if (value.contains("id/")) {
            return "R.id." + value.substring(value.lastIndexOf("/") + 1);
        }
        return "0";
    }

    private String getCircleAngle(String value) {
        float circleAngle = Float.parseFloat(value) % 360.0F;
        if (circleAngle < 0f) {
            circleAngle = (360.0F - circleAngle) % 360.0F;
        }
        return String.valueOf(circleAngle) + "f";
    }

    private void setFieldValue(StringBuffer stringBuffer, Object object, String fieldName, Object value) {
//        mImports.add("java.lang.reflect.Field");
////        stringBuffer.append("\ntry {");
////        stringBuffer.append(String.format("\n\tField field = %s.getClass().getDeclaredField(\"%s\");", object, fieldName));
////        stringBuffer.append("\n\tfield.setAccessible(true);");
////        stringBuffer.append(String.format("\n\tfield.set(%s,%s);", object, value));
////        stringBuffer.append("\n} catch (Exception e) {");
////        stringBuffer.append("\n\te.printStackTrace();");
////        stringBuffer.append("\n}\n");
    }


    private String getPositiveFloat(String value) {
        Float f = Float.parseFloat(value);
        return String.valueOf(Math.max(0, f)) + "f";
    }

}
