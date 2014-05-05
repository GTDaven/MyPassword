package cn.xing.mypassword.model;

import android.os.Bundle;

public class AsyncResult<Data>
{
	private int result;
	private Data data;
	private Bundle bundle;
	
	public int getResult()
	{
		return result;
	}
	public Data getData()
	{
		return data;
	}
	public Bundle getBundle()
	{
		return bundle;
	}
	public void setResult(int result)
	{
		this.result = result;
	}
	public void setData(Data data)
	{
		this.data = data;
	}
	public void setBundle(Bundle bundle)
	{
		this.bundle = bundle;
	}
}
