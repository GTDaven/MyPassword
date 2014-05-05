package cn.xing.mypassword.activity;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.xing.mypassword.R;
import cn.xing.mypassword.adapter.MainAdapter;
import cn.xing.mypassword.app.BaseFragment;
import cn.xing.mypassword.app.OnSettingChangeListener;
import cn.xing.mypassword.model.Password;
import cn.xing.mypassword.model.SettingKey;
import cn.xing.mypassword.service.Mainbinder;
import cn.xing.mypassword.service.OnGetAllPasswordCallback;
import cn.xing.mypassword.service.OnPasswordListener;

import com.twotoasters.jazzylistview.JazzyEffect;
import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.JazzyListView;

/**
 * 主界面，密码列表展示界面
 * 
 * @author zengdexing
 * 
 */
public class MainFragment extends BaseFragment implements OnGetAllPasswordCallback, OnPasswordListener,
		OnSettingChangeListener, android.view.View.OnClickListener
{
	/** 数据 */
	private MainAdapter mainAdapter;

	/** 数据源 */
	private Mainbinder mainbinder;

	private JazzyListView listView;
	/** 没有数据的提示框 */
	private View noDataView;

	private ServiceConnection serviceConnection = new ServiceConnection()
	{
		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			unregistOnPasswordListener();
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			mainbinder = (Mainbinder) service;
			mainbinder.getAllPassword(MainFragment.this);
			mainbinder.registOnPasswordListener(MainFragment.this);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mainAdapter = new MainAdapter(getActivity());

		getBaseActivity().getMyApplication().registOnSettingChangeListener(SettingKey.JAZZY_EFFECT, this);

		Intent intent = new Intent("cn.xing.mypassword");
		getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

	/**
	 * 获得本地保存的特效：用户设置
	 * 
	 * @return
	 */
	private JazzyEffect getJazzyEffect()
	{
		String strKey = getBaseActivity().getSetting(SettingKey.JAZZY_EFFECT, JazzyHelper.TILT + "");
		JazzyEffect jazzyEffect = JazzyHelper.valueOf(Integer.valueOf(strKey));
		return jazzyEffect;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		unregistOnPasswordListener();
		getActivity().unbindService(serviceConnection);
		getBaseActivity().getMyApplication().unregistOnSettingChangeListener(SettingKey.JAZZY_EFFECT, this);
	}

	private void unregistOnPasswordListener()
	{
		if (mainbinder != null)
		{
			mainbinder.unregistOnPasswordListener(this);
			mainbinder = null;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		listView = (JazzyListView) rootView.findViewById(R.id.main_listview);
		listView.setAdapter(mainAdapter);
		listView.setTransitionEffect(getJazzyEffect());

		noDataView = rootView.findViewById(R.id.main_no_passsword);
		noDataView.setOnClickListener(this);
		if (mainbinder == null)
		{
			noDataView.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
		}
		else
		{
			initView();
		}

		return rootView;
	}

	private void initView()
	{
		if (noDataView != null)
		{
			if (mainAdapter.getCount() == 0)
			{
				noDataView.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
			}
			else
			{
				noDataView.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		listView = null;
		noDataView = null;
	}

	@Override
	public void onGetAllPassword(List<Password> passwords)
	{
		mainAdapter.setData(passwords, mainbinder);
		initView();
	}

	@Override
	public void onSettingChange(SettingKey key)
	{
		if (listView != null && key == SettingKey.JAZZY_EFFECT)
		{
			listView.setTransitionEffect(getJazzyEffect());
		}
	}

	@Override
	public void onNewPassword(Password password)
	{
		mainAdapter.onNewPassword(password);
		initView();
	}

	@Override
	public void onDeletePassword(int id)
	{
		mainAdapter.onDeletePassword(id);
		initView();
	}

	@Override
	public void onUpdatePassword(Password newPassword)
	{
		mainAdapter.onUpdatePassword(newPassword);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.main_no_passsword:
				Intent intent = new Intent(getActivity(), EditPasswordActivity.class);
				getActivity().startActivity(intent);
				break;
			default:
				break;
		}
	}
}
