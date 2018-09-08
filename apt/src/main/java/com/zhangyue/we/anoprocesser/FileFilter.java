package com.zhangyue.we.anoprocesser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author：chengwei 2018/9/7
 * @description
 */
public class FileFilter {
    private File mRoot;
    private ArrayList<String> excludes;
    private ArrayList<String> includes;
    private String mFileStart;

    public FileFilter(File root) {
        this.mRoot = root;
    }

    public FileFilter include(String include) {
        if (this.includes == null) {
            this.includes = new ArrayList();
        }
        this.includes.add(include);
        return this;
    }

    public FileFilter fileStart(String fileStart) {
        this.mFileStart = fileStart;
        return this;
    }

    public FileFilter exclude(String name) {
        if (excludes == null) {
            excludes = new ArrayList<>();
        }
        excludes.add(name);
        return this;
    }

    public HashMap<String, ArrayList<File>> filter() {
        HashMap<String, ArrayList<File>> map = new HashMap<>();
        filter(mRoot, map);
        return map;
    }


    private void filter(File dir, HashMap<String, ArrayList<File>> map) {
        String dirName = dir.getName();
        ArrayList<File> list;
        if (dir.isDirectory() && !isExclude(dirName)) {
            File[] files = dir.listFiles();
            boolean fitParent = isInclude(dirName);
            boolean fitFile;
            String fName;
            for (File f : files) {
                if (fitParent) {
                    fName = f.getName();
                    fitFile = mFileStart == null || fName.startsWith(mFileStart);
                    if (!fitFile) {
                        continue;
                    }
                    fName = fName.substring(0, fName.lastIndexOf("."));
                    list = map.getOrDefault(fName, null);
                    if (list == null) {
                        list = new ArrayList<>();
                        map.put(fName, list);
                    }
                    list.add(f);
                } else {
                    filter(f, map);
                }
            }
        }
    }

    private boolean isExclude(String fName) {
        if (excludes == null) {
            return false;
        }
        for (String name : excludes) {
            if (fName.contains(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInclude(String fName) {
        if (includes == null) {
            return false;
        }
        for (String name : includes) {
            if (fName.contains(name)) {
                return true;
            }
        }
        return false;
    }
}
