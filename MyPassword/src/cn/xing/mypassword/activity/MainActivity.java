package cn.xing.mypassword.activity;

import java.util.HashMap;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import cn.xing.mypassword.R;
import cn.xing.mypassword.app.BaseActivity;
import cn.xing.mypassword.model.SettingKey;

import com.umeng.analytics.MobclickAgent;

/**
 * 主界面
 * 
 * @author zengdexing
 * 
 */
public class MainActivity extends BaseActivity
{
	private long lastBackKeyTime;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		switch (id)
		{
			case R.id.action_add_password:
				startActivity(new Intent(this, EditPasswordActivity.class));
				break;

			case R.id.action_import_and_export:
				break;
			case R.id.action_set_lock_pattern:
				startActivity(new Intent(this, SetLockpatternActivity.class));
				break;
			case R.id.action_set_effect:
				onEffectClick();
				break;
			case R.id.action_about:
				onAboutClick();
				break;

			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_BACK:
				long delay = Math.abs(System.currentTimeMillis() - lastBackKeyTime);
				if (delay > 4000)
				{
					// 双击退出程序
					showToast(R.string.toast_key_back);
					lastBackKeyTime = System.currentTimeMillis();
					return true;
				}
				break;

			default:
				break;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void onEffectClick()
	{
		Builder builder = new Builder(this);
		builder.setTitle(R.string.action_jazzy_effect);

		final String[] effectArray = getResources().getStringArray(R.array.jazzy_effects);
		builder.setItems(effectArray, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				getActivity().putSetting(SettingKey.JAZZY_EFFECT, which + "");
				onEventEffect(effectArray[which]);
			}
		});
		builder.show();
	}

	/**
	 * 友盟的事件统计“effect”
	 * 
	 * @param effect
	 */
	private void onEventEffect(String effect)
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("effect", effect);
		MobclickAgent.onEvent(getActivity(), "effect", map);
	}

	/**
	 * 关于对话框
	 */
	private void onAboutClick()
	{
		Builder builder = new Builder(getActivity());
		builder.setTitle(R.string.action_about_us);
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
	 * 获得版本号
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
