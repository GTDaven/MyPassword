package cn.xing.mypassword.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MainService extends Service
{
	private Mainbinder mainbinder;

	@Override
	public IBinder onBind(Intent intent)
	{
		return mainbinder;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		mainbinder = new Mainbinder(this);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		mainbinder.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		if (intent != null)
		{
			handlerIntent(intent);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void handlerIntent(Intent intent)
	{
		//
	}
}
