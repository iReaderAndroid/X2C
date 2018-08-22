package com.zhangyue.we.anoprocesser.xml;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/**
 * @author：chengwei 2018/8/9
 * @description
 */
public class MapWriter {

    private String mGroup;
    private HashMap<Integer, String> mMap;
    private Filer mFiler;

    public MapWriter(String group, HashMap<Integer, String> map, Filer filer) {
        this.mGroup = group;
        this.mMap = map;
        this.mFiler = filer;
    }

    public void write() {
        if (mMap == null || mMap.size() == 0) {
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\tView view;\n");
        stringBuffer.append("switch(layoutId){");
        for (Integer id : mMap.keySet()) {
            stringBuffer.append(String.format("\n\tcase %s:\n\t\tview = new %s().createView(context,%s);\n\t\tbreak;"
                    , id, mMap.get(id), id));
        }
        stringBuffer.append("\n\tdefault:\n\t\tview = null;\n\t\tbreak;");
        stringBuffer.append("\n}\n");
        stringBuffer.append("return view");

        MethodSpec methodSpec = MethodSpec.methodBuilder("createView")
                .addParameter(ClassName.get("android.content", "Context"), "context")
                .addParameter(int.class, "layoutId")
                .addStatement(stringBuffer.toString())
                .returns(ClassName.get("android.view", "View"))
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder("X2C_" + mGroup)
                .addSuperinterface(ClassName.get("com.zhangyue.we.x2c", "IViewCreator"))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodSpec)
                .addJavadoc(String.format("WARN!!! dont edit this file\n" +
                        "\nautho chengwei \nemail chengweidev@gmail.com\n"))
                .build();

        JavaFile javaFile = JavaFile.builder("com.zhangyue.we.x2c", typeSpec)
                .build();
        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void print(){

    }
}
