package com.zhangyue.we.anoprocesser.xml;

import com.zhangyue.we.anoprocesser.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author：chengwei 2018/8/25
 * @description
 */
public class Attr2FuncReader {
    private File mFile;
    private SAXParser mParser;
    private HashMap<String, Func> mAttr2Funcs = new HashMap<>();

    public Attr2FuncReader(File file) {
        this.mFile = file;
    }

    public HashMap<String, Attr> parse() {
        try {
            this.mParser = SAXParserFactory.newInstance().newSAXParser();
            if (mFile.exists()) {
                mParser.parse(mFile, new Attr2FuncHandler());
                return new AttrReader(mFile.getParentFile(), mAttr2Funcs).parse();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class Attr2FuncHandler extends DefaultHandler {

        private String mName;
        private String mToFunc;

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            mName = attributes.getValue("name");
            mToFunc = attributes.getValue("toFunc");
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            mAttr2Funcs.put(mName, new Func(mToFunc.trim()));
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
            mParser.reset();
        }
    }
}
