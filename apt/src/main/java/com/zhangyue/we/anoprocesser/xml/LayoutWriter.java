package com.zhangyue.we.anoprocesser.xml;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.TreeSet;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/**
 * @author：chengwei 2018/8/8
 * @description
 */
public class LayoutWriter {
    private Filer mFiler;
    private String mName;
    private String mMethodSpec;
    private String mPkgName;
    private String mLayoutName;
    private TreeSet<String> mImports;

    public LayoutWriter(String methodSpec, Filer filer, String javaName, String pkgName, String layoutName
            , TreeSet<String> imports) {
        this.mMethodSpec = methodSpec;
        this.mFiler = filer;
        this.mName = javaName;
        this.mPkgName = pkgName;
        this.mLayoutName = layoutName;
        this.mImports = imports;
    }

    public void write() {

        MethodSpec methodSpec = MethodSpec.methodBuilder("createView")
                .addParameter(ClassName.get("android.content", "Context"), "ctx")
                .addParameter(int.class, "layoutId")
                .addStatement(mMethodSpec)
                .returns(ClassName.get("android.view", "View"))
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder(mName)
                .addMethod(methodSpec)
                .addSuperinterface(ClassName.get("com.zhangyue.we.x2c", "IViewCreator"))
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc(String.format("WARN!!! dont edit this file\ntranslate from {@link  %s.R.layout.%s}" +
                        "\nautho chengwei \nemail chengwei@zhangyue.com\n", mPkgName, mLayoutName))
                .build();

        JavaFile javaFile = JavaFile.builder("com.zhangyue.we.x2c", typeSpec)
                .addImports(mImports)
                .build();
        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
