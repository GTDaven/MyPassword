package cn.xing.mypassword.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import cn.xing.mypassword.model.SettingKey;

import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d(getClass().getSimpleName(), "onCreate()");
	}

	public BaseActivity getActivity()
	{
		return this;
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		//友盟统计
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		//友盟统计
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Log.d(getClass().getSimpleName(), "onDestroy()");
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
}
