package cn.xing.mypassword.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import cn.xing.mypassword.R;
import cn.xing.mypassword.app.BaseActivity;
import cn.zdx.lib.annotation.FindViewById;
import cn.zdx.xing.feedback.Feedback;

/**
 * 意见反馈界面
 * 
 * @author zengdexing
 * 
 */
public class FeedbackActivity extends BaseActivity
{
	@FindViewById(R.id.feedback_edittext)
	private EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initActionBar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.feedback, menu);
		return true;
	}

	private void initActionBar()
	{
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_feedback_send:
				onSendClick();
				break;

			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void onSendClick()
	{
		String content = editText.getText().toString().trim();
		if (!content.equals(""))
		{
			final ProgressDialog progressDialog = new ProgressDialog(this);
			progressDialog.setMessage(getString(R.string.feedback_sending));
			progressDialog.setCancelable(true);
			progressDialog.setIndeterminate(false);
			
			progressDialog.show();
			AsyncTask<String, Void, Boolean> asyncTask = new AsyncTask<String, Void, Boolean>()
			{
				@Override
				protected Boolean doInBackground(String... params)
				{
					Feedback feedback = new Feedback();
					feedback.setAppName("MyPassword");
					feedback.setFeedback(params[0]);
					feedback.setDeviceInfo(getDeviceInfo());

					boolean result;
					try
					{
						feedback.commit();
						result = true;
						Thread.sleep(600);
					}
					catch (Exception e)
					{
						e.printStackTrace();
						result = false;
					}
					return result;
				}

				@Override
				protected void onPostExecute(Boolean result)
				{
					showToast(R.string.feedback_thanks);
					progressDialog.dismiss();
					finish();
				}
			};
			asyncTask.execute(content);
		}
	}

	public String getDeviceInfo()
	{
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		JSONObject jsonObject = new JSONObject();
		try
		{
			jsonObject.put("os", "Android");
			jsonObject.put("os_display", android.os.Build.DISPLAY);
			jsonObject.put("os_model", android.os.Build.MODEL);
			jsonObject.put("os_release", android.os.Build.VERSION.RELEASE);
			jsonObject.put("IMEI", tm.getDeviceId());
			jsonObject.put("wetwork_country_iso", tm.getNetworkCountryIso());
			jsonObject.put("version_code", getMyApplication().getVersionCode());
			jsonObject.put("version_name", getMyApplication().getVersionName());
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return jsonObject.toString();
	}
}
