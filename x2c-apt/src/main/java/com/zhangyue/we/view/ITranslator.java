package com.zhangyue.we.view;

/**
 * @author chengwei 2018/8/23
 */
public interface ITranslator {
    boolean translate(StringBuilder stringBuilder, String key, String value);
    void onAttributeEnd(StringBuilder stringBuilder);
}
