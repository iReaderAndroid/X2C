package com.zhangyue.we.anoprocesser;

import com.zhangyue.we.anoprocesser.xml.LayoutMgr;
import com.zhangyue.we.x2c.ano.Xml;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * @author：chengwei 2018/8/7
 * @description
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes("com.zhangyue.we.x2c.ano.Xml")
public class XmlProcesser extends AbstractProcessor {

    private String mGroupName;
    private LayoutMgr mLayoutMgr;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        Log.init(processingEnvironment.getMessager());
        mLayoutMgr = LayoutMgr.instance();
        mLayoutMgr.setFiler(processingEnvironment.getFiler());
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Xml.class);
        TreeSet<Integer> layouts = new TreeSet<>();
        for (Element element : elements) {
            Xml xml = element.getAnnotation(Xml.class);
            int[] ids = xml.layouts();
            for (int id : ids) {
                layouts.add(id);
            }
        }

        for (Integer id : layouts) {
            if (mGroupName == null) {
                mGroupName = "A" + Integer.toHexString(id >> 24);
            }
            mLayoutMgr.setGroupName(mGroupName);
            mLayoutMgr.translate(mLayoutMgr.getLayoutName(id));
        }

        mLayoutMgr.generateMap();
        return false;
    }


}
