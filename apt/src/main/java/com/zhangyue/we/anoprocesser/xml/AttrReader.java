package com.zhangyue.we.anoprocesser.xml;

import com.zhangyue.we.anoprocesser.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author：chengwei 2018/8/25
 * @description
 */
public class AttrReader {
    private HashMap<String, Attr> mAttrs = new HashMap<>();
    private File mFile;
    private SAXParser mParser;
    private HashMap<String, Func> mAttr2Funcs;
    /**
     * attName like "progress",but when we used in layout is "app:progress"
     * so we need a map
     */
    private HashMap<String, String> mAttrNameMap = new HashMap<>();

    public AttrReader(File file, HashMap<String, Func> attr2Func) {
        this.mFile = getValueFile(file);
        this.mAttr2Funcs = attr2Func;
        for (String key : mAttr2Funcs.keySet()) {
            mAttrNameMap.put(key.substring(key.lastIndexOf(":") + 1), key);
        }
        try {
            this.mParser = SAXParserFactory.newInstance().newSAXParser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File getValueFile(File root) {
        File valueFile = null;
        String sep = File.separator;
        File valueDir = new File(root.getAbsolutePath() + sep + "build" + sep
                + "intermediates" + sep + "incremental");
        if (valueDir != null) {
            File[] filse = valueDir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    String fileName = file.getName();
                    return fileName.startsWith("merge") && fileName.endsWith("Resources");
                }
            });
            long maxTime = 0;

            File tmp;
            for (File f : filse) {

                tmp = new File(f.getAbsolutePath() + sep + "merged.dir" + sep + "values" + sep + "values.xml");
                if (tmp.lastModified() > maxTime) {
                    maxTime = tmp.lastModified();
                    valueFile = tmp;
                }
            }
        }
        if (valueFile == null) {
            valueFile = new File(root.getAbsolutePath() + sep + "src" + sep + "main" + sep + "res" + sep + "values"
                    + sep + "attrs.xml");
        }

        return valueFile;
    }

    public HashMap<String, Attr> parse() {
        try {
            mParser.parse(mFile, new AttrHandler());
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mAttrs;
    }

    private class AttrHandler extends DefaultHandler {

        private Attr mAttr;

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            String name = attributes.getValue("name");
            if (qName.equals("attr")) {
                if (mAttrNameMap.containsKey(name)) {
                    mAttr = new Attr();
                    mAttr.name = mAttrNameMap.get(name);
                    mAttr.format = attributes.getValue("format");
                    mAttr.toFunc = mAttr2Funcs.get(mAttrNameMap.get(name));
                } else {
                    mAttr = null;
                }
            } else if (qName.equals("enum") || qName.equals("flag")) {
                if (mAttr != null) {
                    mAttr.enums.put(name, attributes.getValue("value"));
                }
            } else {
                mAttr = null;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if (qName.equals("attr")) {
                if (mAttr != null) {
                    Attr oth = mAttrs.get(mAttr.name);
                    if (mAttr.compareTo(oth) > 0) {
                        mAttrs.put(mAttr.name, mAttr);
                    }
                }
                mAttr = null;
            }
        }

    }
}
