package cn.xing.mypassword.model;

import android.app.Activity;
import android.view.View;

public abstract class ViewFinder
{
	public abstract <V extends View> V findViewById(int id);

	public static ViewFinder create(final Activity activity)
	{
		ViewFinder viewFinder = new ViewFinder()
		{
			@SuppressWarnings("unchecked")
			@Override
			public <V extends View> V findViewById(int id)
			{
				return (V) activity.findViewById(id);
			}
		};

		return viewFinder;
	}

	public static ViewFinder create(final View view)
	{
		ViewFinder viewFinder = new ViewFinder()
		{
			@SuppressWarnings("unchecked")
			@Override
			public <V extends View> V findViewById(int id)
			{
				return (V) view.findViewById(id);
			}
		};

		return viewFinder;
	}
}
