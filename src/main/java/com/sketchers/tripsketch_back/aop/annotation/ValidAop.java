package com.sketchers.tripsketch_back.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 어노테이션을 실행하는 시점 - RUNTIME : 실행 중
@Target(ElementType.METHOD)         // 메소드 위에 다는 어노테이션
public @interface ValidAop {
}
