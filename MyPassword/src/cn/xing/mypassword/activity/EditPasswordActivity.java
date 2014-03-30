package cn.xing.mypassword.activity;

import android.app.ActionBar;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import cn.xing.mypassword.R;
import cn.xing.mypassword.app.BaseActivity;
import cn.xing.mypassword.model.Password;
import cn.xing.mypassword.service.Mainbinder;
import cn.xing.mypassword.service.OnGetPasswordCallback;

/**
 * 密码新增和编辑界面
 * 
 * @author zengdexing
 * 
 */
public class EditPasswordActivity extends BaseActivity implements OnGetPasswordCallback
{
	/** 传入参数 ID */
	public static final String ID = "password_id";
	/** 添加模式 */
	private static final int MODE_ADD = 0;
	/** 修改模式 */
	private static final int MODE_MODIFY = 1;

	/** 当前模式，默认增加 */
	private int MODE = MODE_ADD;
	/** */
	private int id;

	/** 数据源 */
	private Mainbinder mainbinder;

	private EditText titleView;
	private EditText nameView;
	private EditText passwordView;
	private EditText noteView;
	private CheckBox isTopView;

	private ServiceConnection serviceConnection = new ServiceConnection()
	{
		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			mainbinder = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			mainbinder = (Mainbinder) service;
			if (MODE == MODE_MODIFY)
			{
				mainbinder.getPassword(id, EditPasswordActivity.this);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_password);
		id = getIntent().getIntExtra(ID, -1);
		if (id == -1)
		{
			MODE = MODE_ADD;
		}
		else
		{
			MODE = MODE_MODIFY;
		}

		initActionBar();
		findView();
		Intent intent = new Intent("cn.xing.mypassword");
		this.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

	private void findView()
	{
		titleView = (EditText) findViewById(R.id.editview_title);
		nameView = (EditText) findViewById(R.id.editview_name);
		passwordView = (EditText) findViewById(R.id.editview_password);
		noteView = (EditText) findViewById(R.id.editview_note);
		isTopView = (CheckBox) findViewById(R.id.is_top);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		unbindService(serviceConnection);
	}

	private void initActionBar()
	{
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		if (MODE == MODE_ADD)
		{
			actionBar.setTitle(R.string.title_activity_add_password);
		}
		else
		{
			actionBar.setTitle(R.string.title_activity_modify_password);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.edit_password, menu);
		if (MODE == MODE_ADD)
		{
			menu.findItem(R.id.action_save).setIcon(R.drawable.ic_action_ok);
		}
		else
		{
			menu.findItem(R.id.action_save).setIcon(R.drawable.ic_action_save);
			menu.findItem(R.id.action_delete).setVisible(true);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_save)
		{
			if (mainbinder != null)
			{
				onSaveBtnClick();
			}
			return true;
		}
		else if (id == R.id.action_delete)
		{
			if (mainbinder != null)
			{
				deletePassword();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void deletePassword()
	{
		Builder builder = new Builder(this);
		builder.setMessage(R.string.alert_delete_message);
		builder.setNeutralButton(R.string.yes, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				mainbinder.deletePassword(id);
				finish();
			}
		});
		builder.setNegativeButton(R.string.no, null);
		builder.show();
	}

	private void onSaveBtnClick()
	{
		if (titleView.getText().toString().trim().equals(""))
		{
			Toast.makeText(this, R.string.add_password_save_no_data, Toast.LENGTH_SHORT).show();
		}
		else
		{
			Password password = new Password();
			password.setTitle(titleView.getText().toString().trim());
			password.setUserName(nameView.getText().toString().trim());
			password.setPassword(passwordView.getText().toString().trim());
			password.setNote(noteView.getText().toString().trim());
			password.setTop(isTopView.isChecked());
			if (MODE == MODE_ADD)
			{
				password.setCreateDate(System.currentTimeMillis());
				mainbinder.insertPassword(password);
			}
			else
			{
				password.setId(id);
				mainbinder.updatePassword(password);
			}
			finish();
		}
	}

	@Override
	public void onGetPassword(Password password)
	{
		if (password == null)
		{
			Toast.makeText(this, R.string.toast_password_has_deleted, Toast.LENGTH_SHORT).show();
			finish();
		}

		titleView.setText(password.getTitle());
		nameView.setText(password.getUserName());
		passwordView.setText(password.getPassword());
		noteView.setText(password.getNote());
		isTopView.setChecked(password.isTop());
		titleView.setSelection(titleView.getText().length());
	}
}
