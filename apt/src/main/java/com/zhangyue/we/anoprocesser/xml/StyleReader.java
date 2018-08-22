package com.zhangyue.we.anoprocesser.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author：chengwei 2018/8/15
 * @description
 */
public class StyleReader {
    private HashMap<String, Style> mStyles;
    private File mRootFile;
    private SAXParser mParser;

    public StyleReader(File file) {
        this.mRootFile = file;
        this.mStyles = new HashMap<>();
        try {
            this.mParser = SAXParserFactory.newInstance().newSAXParser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Style> parse() {
        File[] styleFiles = new File(mRootFile, "values").listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().contains("style");
            }
        });

        for (File file : styleFiles) {
            try {
                mParser.parse(file, new StyleHandler());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mStyles;
    }

    private class StyleHandler extends DefaultHandler {

        private Style mStyle;
        private String mName;
        private String mQname;

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            mName = attributes.getValue("name");
            if (qName.equals("style")) {
                mStyle = new Style();
                mStyle.name = mName;
                mStyle.parent = attributes.getValue("parent");
                if (mStyle.parent == null && mName.contains(".")) {
                    mStyle.parent = mName.substring(0, mName.lastIndexOf("."));
                }
            } else if (qName.equals("item")) {
                mStyle.attribute.put(mName, localName);
            }

            this.mQname = qName;

        }


        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            if (mQname != null) {
                String value = "";
                try {
                    value = new String(ch, start, length);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (mStyle != null) {
                    mStyle.attribute.put(mName, value);
                }
            }
            mQname = null;
        }


        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if (qName.equals("style")) {
                mStyles.put(mStyle.name, mStyle);
                mStyle = null;
            }
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }
    }

}
