package cn.xing.mypassword.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import cn.xing.mypassword.R;
import cn.xing.mypassword.app.BaseActivity;
import cn.zdx.lib.annotation.FindViewById;

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
		
	}
}
