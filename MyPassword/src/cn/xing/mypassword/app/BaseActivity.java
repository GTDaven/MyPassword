package cn.xing.mypassword.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;
import cn.xing.mypassword.model.SettingKey;
import cn.zdx.lib.annotation.FindViewById;
import cn.zdx.lib.annotation.OnClick;
import cn.zdx.lib.annotation.ViewFinder;
import cn.zdx.lib.annotation.XingAnnotationHelper;

import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends Activity
{
	protected final boolean DEBUG = Config.DEBUG;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (DEBUG)
			logI("onCreate()");
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (!DEBUG)
			MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		if (!DEBUG)
			MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (DEBUG)
			logI("onDestroy()");
	}

	public BaseActivity getActivity()
	{
		return this;
	}

	protected void logI(String msg)
	{
		Log.i(getClass().getSimpleName(), msg);
	}

	protected void logD(String msg)
	{
		Log.d(getClass().getSimpleName(), msg);
	}

	protected void logE(String msg)
	{
		Log.e(getClass().getSimpleName(), msg);
	}

	protected void logW(String msg)
	{
		Log.w(getClass().getSimpleName(), msg);
	}

	public void showToast(int id)
	{
		Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
	}

	public MyApplication getMyApplication()
	{
		return (MyApplication) getApplication();
	}

	public String getSetting(SettingKey key, String defValue)
	{
		return getMyApplication().getString(key, defValue);
	}

	public void putSetting(SettingKey key, String value)
	{
		getMyApplication().putString(key, value);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setContentView(int layoutResID)
	{
		super.setContentView(layoutResID);
		initAnnotation();
	}

	@Override
	public void setContentView(View view)
	{
		super.setContentView(view);
		initAnnotation();
	}

	@Override
	public void setContentView(View view, LayoutParams params)
	{
		super.setContentView(view, params);
		initAnnotation();
	}

	private void initAnnotation()
	{
		ViewFinder viewFinder = ViewFinder.create(this);
		Class<?> clazz = getClass();
		do
		{
			findView(clazz, viewFinder);
			bindOnClick(clazz, viewFinder);
		}
		while ((clazz = clazz.getSuperclass()) != BaseActivity.class);
	}

	/** 初始化 {@link FindViewById} */
	private void findView(Class<?> clazz, ViewFinder viewFinder)
	{
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null && fields.length > 0)
		{
			for (int i = 0; i < fields.length; i++)
			{
				Field field = fields[i];
				XingAnnotationHelper.findView(this, field, viewFinder);
			}
		}
	}

	/** 初始化 {@link OnClick} */
	private void bindOnClick(Class<?> clazz, ViewFinder viewFinder)
	{
		Method[] methods = clazz.getDeclaredMethods();
		if (methods != null && methods.length > 0)
		{
			for (Method method : methods)
			{
				XingAnnotationHelper.bindOnClick(this, method, viewFinder);
			}
		}
	}
}
