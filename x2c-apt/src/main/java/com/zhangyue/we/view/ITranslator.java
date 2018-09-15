package com.zhangyue.we.view;

/**
 * @author chengwei 2018/8/23
 */
public interface ITranslator {
    boolean translate(StringBuilder StringBuilder, String key, String value);
    void onAttributeEnd(StringBuilder StringBuilder);
}
