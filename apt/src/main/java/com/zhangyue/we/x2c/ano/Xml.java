package com.zhangyue.we.x2c.ano;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author：chengwei 2018/8/7
 * @description
 */

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.SOURCE)
public @interface Xml {
    int[] layouts();
}
