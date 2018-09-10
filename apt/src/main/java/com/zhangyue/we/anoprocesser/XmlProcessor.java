package com.zhangyue.we.anoprocesser;

import com.zhangyue.we.anoprocesser.xml.LayoutManager;
import com.zhangyue.we.x2c.ano.Xml;

import java.util.Set;
import java.util.TreeSet;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

/**
 * @author：chengwei 2018/8/7
 * @description
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes("com.zhangyue.we.x2c.ano.Xml")
public class XmlProcessor extends AbstractProcessor {

    private int mGroupId = 0;
    private LayoutManager mLayoutMgr;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        Log.init(processingEnvironment.getMessager());
        mLayoutMgr = LayoutManager.instance();
        mLayoutMgr.setFiler(processingEnvironment.getFiler());
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Xml.class);
        TreeSet<String> layouts = new TreeSet<>();
        for (Element element : elements) {
            Xml xml = element.getAnnotation(Xml.class);
            String[] names = xml.layouts();
            for (String name : names) {
                layouts.add(name);
            }
        }

        for (String name : layouts) {
            if (mGroupId == 0 && mLayoutMgr.getLayoutId(name) != null) {
                mGroupId = (mLayoutMgr.getLayoutId(name) >> 24);
            }
            mLayoutMgr.setGroupId(mGroupId);
            mLayoutMgr.translate(name);
        }

        mLayoutMgr.printTranslate();
        return false;
    }


}
