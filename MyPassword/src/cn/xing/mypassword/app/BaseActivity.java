package cn.xing.mypassword.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import cn.xing.mypassword.model.SettingKey;

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
}
