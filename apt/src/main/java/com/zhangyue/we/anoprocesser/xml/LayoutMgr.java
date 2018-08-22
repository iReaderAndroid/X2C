package com.zhangyue.we.anoprocesser.xml;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.zhangyue.we.anoprocesser.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;

/**
 * @author：chengwei 2018/8/10
 * @description
 */
public class LayoutMgr {

    private static LayoutMgr sInstance;
    private File mRootFile;
    private File mLayoutFile;
    private String mPackageName;
    private String mGroupName;
    private Filer mFiler;
    private HashMap<Integer, String> mMap;
    private HashMap<String, Integer> mRjava;
    private HashMap<Integer, String> mRjavaId;
    private HashMap<String, Style> mStyles;
    private HashMap<String, String> mTranslateMap;

    private LayoutMgr() {
        mMap = new HashMap<>();
        mRjavaId = new HashMap<>();
        mTranslateMap = new HashMap<>();
    }

    public static LayoutMgr instance() {
        if (sInstance == null) {
            synchronized (LayoutMgr.class) {
                if (sInstance == null) {
                    sInstance = new LayoutMgr();
                }
            }
        }
        return sInstance;
    }

    public void setFiler(Filer filer) {
        this.mFiler = filer;
        getLayoutPath();
        mPackageName = getPackageName();
        mRjava = getR();
    }

    public void setGroupName(String groupName) {
        this.mGroupName = groupName;
    }

    public String getLayoutName(int id) {
        return mRjavaId.get(id);
    }

    public Integer getLayoutId(String layoutName) {
        return mRjava.get(layoutName);
    }

    public String translate(String layoutName) {
        String fileName;
        Integer layoutId = getLayoutId(layoutName);
        if (mMap.containsKey(layoutId)) {
            fileName = mMap.get(layoutId);
        } else {
            LayoutReader reader = new LayoutReader(mLayoutFile, layoutName, mFiler, mPackageName, mGroupName);
            fileName = reader.parse();
            mMap.put(layoutId, fileName);
        }
        mTranslateMap.put(fileName + ".java", layoutName + ".xml");
        return fileName;
    }

    public Style getStyle(String name) {
        if (name == null) {
            return null;
        }
        if (mStyles == null) {
            mStyles = new StyleReader(mLayoutFile.getParentFile()).parse();
        }
        return mStyles.get(name);
    }

    public void generateMap() {
        printTranslate();

        MapWriter mapWriter = new MapWriter(mGroupName, mMap, mFiler);
        mapWriter.write();
        mMap.clear();
        mTranslateMap.clear();
    }

    private void printTranslate() {
        if(mTranslateMap.size()==0){
            return;
        }
        int maxTab = 0;
        int tabCount = 0;
        for (String layoutName : mTranslateMap.values()) {
            tabCount = layoutName.length() / 4 + 1;
            if (tabCount > maxTab) {
                maxTab = tabCount;
            }
        }

        StringBuilder stringBuilder;
        String layoutName;
        for (String javaName : mTranslateMap.keySet()) {
            layoutName = mTranslateMap.get(javaName);
            tabCount = layoutName.length() / 4;
            stringBuilder = new StringBuilder(layoutName);
            if (tabCount < maxTab) {
                for (int j = 0; j < maxTab - tabCount; j++) {
                    stringBuilder.append("\t");
                }
            }
            Log.w(String.format("%s->\t%s", stringBuilder.toString(), javaName));
        }
    }


    private void getLayoutPath() {
        try {
            JavaFileObject fileObject = mFiler.createSourceFile("bb");
            String path = fileObject.toUri().toString();
            String preFix = "file:/";
            if (path.startsWith(preFix)) {
                path = path.substring(preFix.length()-1);
            }
            File file = new File(path);
            while (!file.getName().equals("build")) {
                file = file.getParentFile();
            }
            mRootFile = file.getParentFile();
            String sep = File.separator;
            mLayoutFile = new File(mRootFile.getAbsolutePath() + sep + "src" + sep + "main" + sep + "res" + sep + "layout");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getPackageName() {
        File buildGradle = new File(mRootFile, "build.gradle");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(buildGradle));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("applicationId")) {
                    return line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private HashMap<String, Integer> getR() {
        HashMap<String, Integer> map = new HashMap<>();
        File rFile = getRFile();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(rFile));
            String line;
            boolean layoutStarted = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains("public static final class layout")) {
                    layoutStarted = true;
                } else if (layoutStarted) {
                    if (line.contains("}")) {
                        break;
                    } else {
                        line = line.substring(line.lastIndexOf(" ") + 1, line.indexOf(";"));
                        String ss[] = line.split("=");
                        int id = Integer.decode(ss[1]);
                        map.put(ss[0], id);
                        mRjavaId.put(id, ss[0]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private File getRFile() {
        String sep = File.separator;
        StringBuilder stringBuilder = new StringBuilder(mRootFile.getAbsolutePath());
        File rDir = new File(stringBuilder.append(sep).append("build").append(sep)
                .append("generated").append(sep)
                .append("source").append(sep).append("r").toString());

        File files[] = rDir.listFiles();
        File rFile = null;
        long time = 0;
        for (File file : files) {
            File f = new File(file.getAbsolutePath() + sep + mPackageName.replace(".", sep) + sep + "R.java");
            if (f.lastModified() > time) {
                rFile = f;
                time = f.lastModified();
            }
        }
        return rFile;
    }


}
