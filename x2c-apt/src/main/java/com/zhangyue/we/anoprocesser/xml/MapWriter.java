package com.zhangyue.we.anoprocesser.xml;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.WeJavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.zhangyue.we.anoprocesser.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/**
 * @author chengwei 2018/8/9
 */
public class MapWriter {

    private int mGroupId;
    private ArrayList<File> mLayouts;
    private ArrayList<String> mJavaNames;
    private Filer mFiler;

    MapWriter(int groupId, ArrayList<File> layouts, ArrayList<String> javaNames, Filer filer) {
        this.mGroupId = groupId;
        this.mLayouts = layouts;
        this.mJavaNames = javaNames;
        this.mFiler = filer;
    }

    public void write() {
        if (mJavaNames == null || mJavaNames.size() == 0 || mLayouts == null || mLayouts.size() == 0) {
            return;
        }
        TreeSet<String> imports = new TreeSet<>();
        StringBuilder stringBuilder = new StringBuilder();
        if (mLayouts.size() == 1 && mJavaNames.size() == 1) {
            stringBuilder.append(String.format("return new %s().createView(context)", mJavaNames.get(0)));
        } else {
            stringBuilder.append("View view = null ;");
            stringBuilder.append("\nint sdk = Build.VERSION.SDK_INT;");
            imports.add("android.os.Build");
            for (int i = 0; i < mJavaNames.size(); i++) {
                if (i == mJavaNames.size() - 1) {
                    stringBuilder.append(" else {");

                } else {
                    String layoutCategory = Util.getLayoutCategory(mLayouts.get(i));
                    if (layoutCategory.equals("land")) {
                        stringBuilder.append("\nint orientation = context.getResources().getConfiguration().orientation;");
                        stringBuilder.append("\nboolean isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE;");
                        stringBuilder.append("\nif (isLandscape) {");
                        imports.add("android.content.res.Configuration");
                    } else if (layoutCategory.startsWith("v")) {
                        String sdk = layoutCategory.substring(layoutCategory.lastIndexOf("v") + 1);
                        stringBuilder.append(String.format(" else if (sdk >= %s) {", sdk));
                    }
                }
                stringBuilder.append(String.format("\n\tview = new %s().createView(context);\n}", mJavaNames.get(i)));
            }
            stringBuilder.append("\nreturn view");
        }

        MethodSpec methodSpec = MethodSpec.methodBuilder("createView")
                .addParameter(ClassName.get("android.content", "Context"), "context")
                .addStatement(stringBuilder.toString())
                .returns(ClassName.get("android.view", "View"))
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .build();

        String name = mLayouts.get(0).getName().substring(0, mLayouts.get(0).getName().indexOf("."));
        TypeSpec typeSpec = TypeSpec.classBuilder(String.format("X2C%s_%s", mGroupId, name))
                .addSuperinterface(ClassName.get("com.zhangyue.we.x2c", "IViewCreator"))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodSpec)
                .addJavadoc(String.format("WARN!!! don't edit this file\n" +
                        "\nauthor chengwei \nemail chengwei@zhangyue.com\n"))
                .build();

        WeJavaFile javaFile = WeJavaFile.builder("com.zhangyue.we.x2c", typeSpec)
                .addImports(imports)
                .build();
        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
