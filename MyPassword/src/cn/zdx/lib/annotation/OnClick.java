/*
 * Copyright (c) 2014 zengdexing
 */
package cn.zdx.lib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * OnClick注解，使用该注解方法必须含有且只含有一个参数：View
 * 
 * <pre>
 * <code>
 * 使用方法：
 * at OnClick({ R.id.button1, R.id.button2 }) 
 * public void onClick(View view){ 
 * 	System.out.println("点击了！！！！");
 * }</code>
 * </pre>
 * 
 * @author zengdexing
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface OnClick
{
	int[] value();
}
