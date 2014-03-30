package cn.xing.mypassword.activity;

import java.util.HashMap;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.xing.mypassword.R;
import cn.xing.mypassword.app.BaseFragment;
import cn.xing.mypassword.model.SettingKey;

import com.umeng.analytics.MobclickAgent;

/**
 * 侧滑菜单
 * 
 * @author zengdexing
 * 
 */
public class DrawLayoutFragment extends BaseFragment implements OnClickListener
{
	DrawerLayout drawerLayout;

	public void setDrawerLayout(DrawerLayout drawerLayout)
	{
		this.drawerLayout = drawerLayout;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_drawerlayout, null);
		rootView.findViewById(R.id.drawer_item_about).setOnClickListener(this);
		rootView.findViewById(R.id.drawer_item_setpassword).setOnClickListener(this);
		rootView.findViewById(R.id.drawer_item_jazzy_effect).setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.drawer_item_setpassword:
				// 设置密码
				startActivity(new Intent(getActivity(), SetLockpatternActivity.class));
				break;

			case R.id.drawer_item_jazzy_effect:
				onEffectClick();
				break;
			case R.id.drawer_item_about:
				onAboutClick();
				break;

			default:
				break;
		}
		drawerLayout.closeDrawer(Gravity.START);
	}

	private void onEffectClick()
	{
		Builder builder = new Builder(getActivity());
		builder.setTitle(R.string.drawer_jazzy_effect);

		final String[] effectArray = getResources().getStringArray(R.array.jazzy_effects);
		builder.setItems(effectArray, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				getBaseActivity().putSetting(SettingKey.JAZZY_EFFECT, which + "");
				onEventEffect(effectArray[which]);
			}
		});
		builder.show();
	}

	private void onEventEffect(String effect)
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("effect", effect);
		MobclickAgent.onEvent(getActivity(), "effect", map);
	}

	/**
	 * 关于的点击事件
	 */
	private void onAboutClick()
	{
		Builder builder = new Builder(getActivity());
		builder.setTitle(R.string.drawer_about_us);
		builder.setNeutralButton(R.string.action_save, null);
		String message = getString(R.string.drawer_about_detail, getVersionName());
		TextView textView = new TextView(getActivity());
		textView.setGravity(Gravity.CENTER);
		textView.setText(message);
		textView.setTextSize(15);
		builder.setView(textView);
		builder.show();
	}

	/**
	 * 获得当前版本
	 * 
	 * @return
	 */
	private String getVersionName()
	{
		PackageManager packageManager = getActivity().getPackageManager();
		PackageInfo packInfo;
		String version = "";
		try
		{
			packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
			version = packInfo.versionName;
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return version;
	}
}
