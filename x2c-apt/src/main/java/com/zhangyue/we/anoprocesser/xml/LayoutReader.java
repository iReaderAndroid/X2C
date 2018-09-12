package com.zhangyue.we.anoprocesser.xml;

import com.zhangyue.we.anoprocesser.Util;
import com.zhangyue.we.view.View;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.util.Stack;

import javax.annotation.processing.Filer;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author chengwei 2018/8/7
 */
public class LayoutReader {
    private Filer mFiler;
    private SAXParser mParser;
    private String mName;
    private String mFullName;
    private String mLayoutName;
    private String mPackageName;
    private File mFile;

    public LayoutReader(File file, String name, Filer filer, String packageName, int groupId) {
        mFile = file;
        mFiler = filer;
        mPackageName = packageName;
        mLayoutName = name;
        mName = getJavaName(groupId, name);
    }

    public String parse() {
        try {
            mParser = SAXParserFactory.newInstance().newSAXParser();
            mParser.parse(mFile, new XmlHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mFullName;
    }

    private class XmlHandler extends DefaultHandler {
        private Stack<View> mStack;
        private View mRootView;
        private boolean isDataBinding;

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            mStack = new Stack<>();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            View view = createView(qName, attributes);
            if (view != null) {
                if (mStack.size() > 0) {
                    view.setParent(mStack.get(mStack.size() - 1));
                } else {
                    view.setParent(null);
                }
                mStack.push(view);
            }
            super.startElement(uri, localName, qName, attributes);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);

            if (mStack.size() > 0) {
                View view = mStack.pop();
                if (mStack.size() == 0) {
                    mRootView = view;
                    StringBuffer stringBuffer = new StringBuffer();
                    mRootView.translate(stringBuffer);
                    stringBuffer.append("return ").append(mRootView.getObjName());
                    LayoutWriter writer = new LayoutWriter(stringBuffer.toString(), mFiler, mName, mPackageName
                            , Util.getLayoutCategory(mFile), mLayoutName, mRootView.getImports());
                    mFullName = writer.write();
                }
            }
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }

        private View createView(String name, Attributes attributes) {
            if (name.equals("layout") || name.equals("data") || name.equals("variable")) {
                isDataBinding = true;
                return null;
            }
            View view = new View(mPackageName, name, attributes);
            if (mStack.size() == 0) {
                view.setDirName(Util.getDirName(mFile));
                view.setIsDataBinding(isDataBinding);
            }
            view.setLayoutName(mLayoutName);
            return view;
        }
    }


    private String getJavaName(int groupId, String name) {
        String retName = groupId + "_" + name;
        String[] ss = retName.split("_");
        StringBuilder stringBuilder = new StringBuilder("X2C");
        for (int i = 0; i < ss.length; i++) {
            stringBuilder.append(ss[i].substring(0, 1).toUpperCase())
                    .append(ss[i].substring(1));
            if (i < ss.length - 1) {
                stringBuilder.append("_");
            }
        }
        return stringBuilder.toString();
    }


}
