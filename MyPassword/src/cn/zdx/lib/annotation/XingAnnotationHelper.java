/*
 * Copyright (c) 2014 zengdexing
 */
package cn.zdx.lib.annotation;

import java.lang.reflect.Field;

import android.app.Activity;
import android.view.View;

/**
 * 注解帮助类，支持任何对象的成员变量使用{@link FindViewById}查找控件、方法使用{@link OnClick}绑定点击事件。
 * <p>
 * 注意：继承自{@link XingBaseActivity}的UI不需要再使用该类，BaseActivity已经提供了对
 * {@link FindViewById}和 {@link OnClick} 的支持，在BaseActivity中直接使用
 * </p>
 * 
 * @author zengdexing
 */
public class XingAnnotationHelper
{
	/**
	 * 初始化Activity中使用了{@link FindViewById}注解的成员变量
	 * 
	 * @param target
	 */
	public static void findView(Activity target)
	{
		findView(target, ViewFinder.create(target));
	}

	/**
	 * 初始化Object中使用了{@link FindViewById}注解的成员变量
	 * 
	 * @param target
	 */
	public static void findView(Object target, View view)
	{
		findView(target, ViewFinder.create(view));
	}

	/**
	 * 初始化使用了{@link FindViewById}注解的类成员变量
	 * 
	 * @param target
	 *            需要初始化的对象，该对象的成员变量使用了{@link FindViewById}注解
	 * @param viewFinder
	 *            View查找器
	 */
	public static void findView(Object target, ViewFinder viewFinder)
	{
		Class<?> clazz = target.getClass();

		Field[] fields = clazz.getDeclaredFields();
		if (fields != null && fields.length > 0)
		{
			for (int i = 0; i < fields.length; i++)
			{
				Field field = fields[i];
				findView(target, field, viewFinder);
			}
		}
	}

	/**
	 * 绑定使用{@link FindViewById}注解的方法
	 * 
	 * @param target
	 *            需要绑定的对象，该对象有成员变量使用了{@link FindViewById}
	 * @param field
	 *            需要绑定的变量
	 * @param viewFinder
	 *            VIew查找器
	 */
	public static void findView(Object target, Field field, ViewFinder viewFinder)
	{
		if (field.isAnnotationPresent(FindViewById.class))
		{
			if (!field.isAccessible())
			{
				field.setAccessible(true);
			}

			int id = field.getAnnotation(FindViewById.class).value();
			View view = viewFinder.findViewById(id);

			checkView(field.getName(), view, field.getType());

			try
			{
				field.set(target, view);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 检查类型。</br>
	 * <p>
	 * 如果产生{@link NullPointerException}异常，通常是注解的ID错误，即：根据该ID找不到相应的控件。
	 * </p>
	 * <p>
	 * 如果产生{@link ClassCastException}异常，通常是注解ID的控件在XML中的类型和需要绑定的对象类型不一致。
	 * </p>
	 */
	private static void checkView(String msg, View targetView, Class<?> bindType)
	{
		if (targetView == null)
		{
			throw new NullPointerException("\"" + msg + "\"要绑定的控件不存在!!");
		}
		else if (!bindType.isInstance(targetView))
		{
			String error = "类型匹配错误，\"" + msg + "\"要绑定的类型为：" + bindType + ",而目标类型为：" + targetView.getClass();
			throw new ClassCastException(error);
		}
	}
}
