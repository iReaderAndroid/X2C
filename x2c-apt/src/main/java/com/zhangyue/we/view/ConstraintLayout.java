package com.zhangyue.we.view;

import java.util.TreeSet;

/**
 * @author chengwei 2018/8/23
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
    public boolean translate(StringBuilder stringBuilder, String key, String value) {
        switch (key) {
            case "app:layout_constraintDimensionRatio":
                return setLayoutConstraintDimensionRatio(stringBuilder, value);
            case "app:layout_constraintLeft_toLeftOf":
                return setLayoutConstraintLeftToLeftOf(stringBuilder, value);
            case "app:layout_constraintRight_toRightOf":
                return setLayoutConstraintRightToRightOf(stringBuilder, value);
            case "app:layout_constraintTop_toBottomOf":
                return setLayoutConstraintTopToBottomOf(stringBuilder, value);
            case "app:layout_constraintBottom_toBottomOf":
                return setLayoutConstraintBottomToBottomOf(stringBuilder, value);
            case "app:layout_constraintLeft_toRightOf":
                return setLayoutConstraintLeftToRightOf(stringBuilder, value);
            case "app:layout_constraintRight_toLeftOf":
                return setLayoutConstraintRightToLeftOf(stringBuilder, value);
            case "app:layout_constraintBottom_toTopOf":
                return setLayoutConstraintBottomToTopOf(stringBuilder, value);
            case "app:layout_constraintTop_toTopOf":
                return setLayoutConstraintTopToTopOf(stringBuilder, value);
            case "app:layout_constrainedHeight":
                return setLayoutConstrainedHeight(stringBuilder, value);
            case "app:layout_constrainedWidth":
                return setLayoutConstrainedWidth(stringBuilder, value);
            case "app:layout_constraintVertical_bias":
                return setLayoutConstraintVerticalBias(stringBuilder, value);
            case "app:layout_constraintHorizontal_bias":
                return setLayoutConstraintHorizontalBias(stringBuilder, value);
            case "app:layout_constraintHeight_default":
                return setLayoutConstraintHeightDefault(stringBuilder, value);
            case "app:layout_constraintWidth_default":
                return setLayoutConstraintWidthDefault(stringBuilder, value);
            case "app:layout_constraintVertical_weight":
                return setLayoutConstraintVerticalWeight(stringBuilder, value);
            case "app:layout_constraintHorizontal_weight":
                return setLayoutConstraintHorizontalWeight(stringBuilder, value);
            case "app:layout_constraintVertical_chainStyle":
                return setLayoutConstraintVerticalChainStyle(stringBuilder, value);
            case "app:layout_constraintHorizontal_chainStyle":
                return setLayoutConstraintHorizontalChainStyle(stringBuilder, value);
            case "app:layout_constraintCircle":
                return setLayoutConstraintCircle(stringBuilder, value);
            case "app:layout_constraintCircleAngle":
                return setLayoutConstraintCircleAngle(stringBuilder, value);
            case "app:layout_constraintCircleRadius":
                return setLayoutConstraintCircleRadius(stringBuilder, value);
            case "app:layout_goneMarginBottom":
                return setGoneMarginBottom(stringBuilder, value);
            case "app:layout_goneMarginLeft":
                return setGoneMarginLeft(stringBuilder, value);
            case "app:layout_goneMarginRight":
                return setGoneMarginRight(stringBuilder, value);
            case "app:layout_goneMarginTop":
                return setGoneMarginTop(stringBuilder, value);
            case "app:layout_goneMarginEnd":
                return setGoneMarginEnd(stringBuilder, value);
            case "app:layout_goneMarginStart":
                return setGoneMarginStart(stringBuilder, value);
            case "app:layout_editor_absoluteY":
                return setLayoutEditorAbsoluteY(stringBuilder, value);
            case "app:layout_editor_absoluteX":
                return setLayoutEditorAbsoluteX(stringBuilder, value);
            case "app:layout_constraintBaseline_toBaselineOf":
                return setLayoutConstraintBaselineToBaselineOf(stringBuilder, value);
            case "app:layout_constraintStart_toEndOf":
                return setLayoutConstraintStartToEndOf(stringBuilder, value);
            case "app:layout_constraintEnd_toEndOf":
                return setLayoutConstraintEndToEndOf(stringBuilder, value);
            case "app:layout_constraintEnd_toStartOf":
                return setLayoutConstraintEndToStartOf(stringBuilder, value);
            case "app:layout_constraintStart_toStartOf":
                return setLayoutConstraintStartToStartOf(stringBuilder, value);
            case "app:layout_constraintGuide_begin":
                return setLayoutConstraintGuideBegin(stringBuilder, value);
            case "app:layout_constraintGuide_end":
                return setLayoutConstraintGuideEnd(stringBuilder, value);
            case "app:layout_constraintGuide_percent":
                return setLayoutConstraintGuidePercent(stringBuilder, value);
            case "app:layout_constraintWidth_min":
                return setLayoutConstraintWidthMin(stringBuilder, value);
            case "app:layout_constraintWidth_max":
                return setLayoutConstraintWidthMax(stringBuilder, value);
            case "app:layout_constraintHeight_min":
                return setLayoutConstraintHeightMin(stringBuilder, value);
            case "app:layout_constraintHeight_max":
                return setLayoutConstraintHeightMax(stringBuilder, value);
            case "app:layout_constraintHeight_percent":
                return setLayoutConstraintHeightPercent(stringBuilder, value);
            case "app:layout_constraintWidth_percent":
                return setLayoutConstraintWidthPercent(stringBuilder, value);

            default:
                return false;
        }
    }

    @Override
    public void onAttributeEnd(StringBuilder StringBuilder) {
        if (isHandle) {
            StringBuilder.append(String.format("%s.validate();\n", mLayoutParamsObj));
        }
    }


    private boolean setLayoutConstraintWidthPercent(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.matchConstraintPercentWidth = %s ;\n", mLayoutParamsObj, getPositiveFloat(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintHeightPercent(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.matchConstraintPercentHeight = %s ;\n", mLayoutParamsObj, getPositiveFloat(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintHeightMax(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.matchConstraintMaxHeight = %s ;\n", mLayoutParamsObj, View.getWH(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintHeightMin(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.matchConstraintMinHeight = %s ;\n", mLayoutParamsObj, View.getWH(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintWidthMax(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.matchConstraintMaxWidth = %s ;\n", mLayoutParamsObj, View.getWH(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintWidthMin(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.matchConstraintMinWidth = %s ;\n", mLayoutParamsObj, View.getWH(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintGuidePercent(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.guidePercent = %s ;\n", mLayoutParamsObj, View.getFloat(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintGuideEnd(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.guideEnd = %s ;\n", mLayoutParamsObj, View.getDimen(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintGuideBegin(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.guideBegin = %s ;\n", mLayoutParamsObj, View.getDimen(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintStartToStartOf(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.startToStart = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintEndToStartOf(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.endToStart = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintEndToEndOf(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.endToEnd = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintStartToEndOf(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.startToEnd = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintBaselineToBaselineOf(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.baselineToBaseline = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutEditorAbsoluteX(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.editorAbsoluteX = %s ;\n", mLayoutParamsObj, View.getDimen(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutEditorAbsoluteY(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.editorAbsoluteY = %s ;\n", mLayoutParamsObj, View.getDimen(value)));
        isHandle = true;
        return true;
    }

    private boolean setGoneMarginStart(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.goneStartMargin = %s ;\n", mLayoutParamsObj, View.getDimen(value)));
        isHandle = true;
        return true;
    }

    private boolean setGoneMarginEnd(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.goneEndMargin = %s ;\n", mLayoutParamsObj, View.getDimen(value)));
        isHandle = true;
        return true;
    }

    private boolean setGoneMarginTop(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.goneTopMargin = %s ;\n", mLayoutParamsObj, View.getDimen(value)));
        isHandle = true;
        return true;
    }

    private boolean setGoneMarginRight(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.goneRightMargin = %s ;\n", mLayoutParamsObj, View.getDimen(value)));
        isHandle = true;
        return true;
    }

    private boolean setGoneMarginLeft(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.goneLeftMargin = %s ;\n", mLayoutParamsObj, View.getDimen(value)));
        isHandle = true;
        return true;
    }

    private boolean setGoneMarginBottom(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.goneBottomMargin = %s ;\n", mLayoutParamsObj, View.getDimen(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintCircleRadius(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.circleRadius = %s ;\n", mLayoutParamsObj, View.getDimen(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintCircleAngle(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.circleAngle = %s ;\n", mLayoutParamsObj, getCircleAngle(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintCircle(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.circleConstraint = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintHorizontalChainStyle(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.horizontalChainStyle = %s ;\n", mLayoutParamsObj, getConstraintChainStyle(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintVerticalChainStyle(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.verticalChainStyle = %s ;\n", mLayoutParamsObj, getConstraintChainStyle(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintHorizontalWeight(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.horizontalWeight = %s ;\n", mLayoutParamsObj, View.getFloat(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintVerticalWeight(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.verticalWeight = %s ;\n", mLayoutParamsObj, View.getFloat(value)));
        isHandle = true;
        return true;
    }


    private boolean setLayoutConstraintWidthDefault(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.matchConstraintDefaultWidth = %s ;\n", mLayoutParamsObj, getConstraintMode(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintHeightDefault(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.matchConstraintDefaultHeight = %s ;\n", mLayoutParamsObj, getConstraintMode(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintHorizontalBias(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.horizontalBias = %s ;\n", mLayoutParamsObj, View.getFloat(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintVerticalBias(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.verticalBias = %s ;\n", mLayoutParamsObj, View.getFloat(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstrainedWidth(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.constrainedWidth = %s ;\n", mLayoutParamsObj, View.getBoolean(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstrainedHeight(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.constrainedHeight = %s ;\n", mLayoutParamsObj, View.getBoolean(value)));
        isHandle = true;
        return true;
    }


    private boolean setLayoutConstraintTopToTopOf(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.topToTop = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintBottomToTopOf(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.bottomToTop = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintRightToLeftOf(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.rightToLeft = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintLeftToRightOf(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.leftToRight = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintTopToBottomOf(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.topToBottom = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintBottomToBottomOf(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.bottomToBottom = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintLeftToLeftOf(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.leftToLeft = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }

    private boolean setLayoutConstraintRightToRightOf(StringBuilder StringBuilder, String value) {
        StringBuilder.append(String.format("%s.rightToRight = %s ;\n", mLayoutParamsObj, getResourceId(value)));
        isHandle = true;
        return true;
    }


    private boolean setLayoutConstraintDimensionRatio(StringBuilder StringBuilder, String value) {
        String dimensionRatio = value;
        StringBuilder.append(String.format("%s.dimensionRatio=\"%s\";\n", mLayoutParamsObj, dimensionRatio));
        int dimensionRatioSide = -1;

        int len = dimensionRatio.length();
        int commaIndex = dimensionRatio.indexOf(44);
        if (commaIndex > 0 && commaIndex < len - 1) {
            String dimension = dimensionRatio.substring(0, commaIndex);
            if (dimension.equalsIgnoreCase("W")) {
                dimensionRatioSide = 0;
                setFieldValue(StringBuilder, mLayoutParamsObj, "dimensionRatioSide", dimensionRatioSide);
            } else if (dimension.equalsIgnoreCase("H")) {
                dimensionRatioSide = 1;
                setFieldValue(StringBuilder, mLayoutParamsObj, "dimensionRatioSide", dimensionRatioSide);
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
                            setFieldValue(StringBuilder, mLayoutParamsObj, "dimensionRatioValue", Math.abs(denominatorValue / nominatorValue) + "f");
                        } else {
                            setFieldValue(StringBuilder, mLayoutParamsObj, "dimensionRatioValue", Math.abs(nominatorValue / denominatorValue) + "f");
                        }
                    }
                } catch (NumberFormatException var16) {
                }
            }
        } else {
            r = dimensionRatio.substring(commaIndex);
            if (r.length() > 0) {
                try {
                    setFieldValue(StringBuilder, mLayoutParamsObj, "dimensionRatioValue", Float.parseFloat(r) + "f");
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

    private void setFieldValue(StringBuilder StringBuilder, Object object, String fieldName, Object value) {
//        mImports.add("java.lang.reflect.Field");
////        StringBuilder.append("\ntry {");
////        StringBuilder.append(String.format("\n\tField field = %s.getClass().getDeclaredField(\"%s\");", object, fieldName));
////        StringBuilder.append("\n\tfield.setAccessible(true);");
////        StringBuilder.append(String.format("\n\tfield.set(%s,%s);", object, value));
////        StringBuilder.append("\n} catch (Exception e) {");
////        StringBuilder.append("\n\te.printStackTrace();");
////        StringBuilder.append("\n}\n");
    }


    private String getPositiveFloat(String value) {
        Float f = Float.parseFloat(value);
        return String.valueOf(Math.max(0, f)) + "f";
    }

}
