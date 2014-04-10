package cn.xing.mypassword.app;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

public class BaseFragment extends Fragment
{
	protected final boolean DEBUG = Config.DEBUG;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
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
	
	protected BaseActivity getBaseActivity()
	{
		return (BaseActivity) getActivity();
	}
}
