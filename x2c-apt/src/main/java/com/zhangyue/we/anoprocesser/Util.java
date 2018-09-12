package com.zhangyue.we.anoprocesser;

import java.io.Closeable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author chengwei 2018/9/7
 */
public class Util {

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getDirName(File file) {
        return file.getParentFile().getName();
    }

    public static String getLayoutCategory(File file) {
        String name = file.getParentFile().getName();
        switch (name) {
            case "layout":
                return "";
            default:
                return name.substring(name.lastIndexOf("-") + 1);
        }
    }

    public static void sortLayout(ArrayList<File> list) {
        if (list == null || list.size() <= 1) {
            return;
        }

        Collections.sort(list, new Comparator<File>() {
            @Override
            public int compare(File file, File t1) {
                String sort1 = getLayoutCategory(file);
                String sort2 = getLayoutCategory(t1);
                if (sort1 == null || sort1.equals("")) {
                    return 1;
                } else if (sort2 == null || sort2.equals("")) {
                    return -1;
                } else if (sort1.equals("land")) {
                    return -1;
                } else if (sort2.equals("land")) {
                    return 1;
                } else {
                    int v1 = Integer.parseInt(sort1.substring(sort1.indexOf("v") + 1));
                    int v2 = Integer.parseInt(sort2.substring(sort2.indexOf("v") + 1));
                    return v2 - v1;
                }
            }
        });
    }


}
