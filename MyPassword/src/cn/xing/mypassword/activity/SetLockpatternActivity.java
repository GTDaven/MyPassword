package cn.xing.mypassword.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.widget.TextView;
import cn.xing.mypassword.R;
import cn.xing.mypassword.app.BaseActivity;
import cn.xing.mypassword.view.LockPatternUtil;
import cn.xing.mypassword.view.LockPatternView;
import cn.xing.mypassword.view.LockPatternView.Cell;
import cn.xing.mypassword.view.LockPatternView.DisplayMode;
import cn.xing.mypassword.view.LockPatternView.OnPatternListener;

/**
 * 设置图案解锁界面
 * 
 * @author zengdexing
 * 
 */
public class SetLockpatternActivity extends BaseActivity implements OnPatternListener, Callback
{
	private LockPatternView lockPatternView;
	private TextView textView;

	/** 模式 认证 */
	private static final int MODE_AUTH = 0;
	/** 模式 处于第一步 */
	private static final int MODE_FIRST_STEP = 1;
	/** 模式 处于第二步 */
	private static final int MODE_SECOND_STEP = 2;

	private static final int MEG_AUTH_ERROR = 1;
	private static final int MEG_GOTO_SECOND_STEP = 2;
	private static final int MEG_SET_SUCCESS = 3;
	private static final int MEG_GOTO_FIRST_STEP = 4;

	/** 当前处于的模式 */
	private int mode = MODE_AUTH;
	private Handler handler = new Handler(this);

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_lockpattern);
		initActionBar();
		initView();
		initMode();
	}

	/**
	 * 初始化模式：如果用户没有设置密码，则处于第一步 {@link #MODE_FIRST_STEP}，否则用户需要验证
	 * {@link #MODE_AUTH}
	 */
	private void initMode()
	{
		List<LockPatternView.Cell> list = LockPatternUtil.getLocalCell(this);
		if (list.size() != 0)
		{
			mode = MODE_AUTH;
			textView.setText(R.string.set_lock_pattern_auth);
		}
		else
		{
			mode = MODE_FIRST_STEP;
			textView.setText(R.string.set_lock_pattern_first_step);
			showFirstUserDialog();
		}
	}

	/** 第一次使用，设置解锁图案 */
	private void showFirstUserDialog()
	{
		Builder builder = new Builder(this);
		builder.setMessage(R.string.set_lock_pattern_first_message);
		builder.setNeutralButton(R.string.set_lock_pattern_first_sure, null);
		builder.show();
	}

	private void initView()
	{
		lockPatternView = (LockPatternView) findViewById(R.id.set_lockpattern_view);
		textView = (TextView) findViewById(R.id.set_lockpattern_text);

		lockPatternView.setOnPatternListener(this);
	}

	private void initActionBar()
	{
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onPatternStart()
	{
		textView.setText(R.string.set_lock_pattern_step_tips);
	}

	@Override
	public void onPatternCleared()
	{

	}

	@Override
	public void onPatternCellAdded(List<Cell> pattern)
	{

	}

	private List<Cell> lastCells;

	@Override
	public void onPatternDetected(List<Cell> pattern)
	{
		switch (mode)
		{
			case MODE_AUTH:
				if (LockPatternUtil.authPatternCell(this, pattern))
				{
					// 验证通过，到第一步
					lockPatternView.setDisplayMode(DisplayMode.Correct);
					lockPatternView.setEnabled(false);
					textView.setText(R.string.set_lock_pattern_auth_ok);
					handler.sendEmptyMessageDelayed(4, 1000);
				}
				else
				{
					// 验证不通过，继续输入密码
					lockPatternView.setEnabled(false);
					lockPatternView.setDisplayMode(DisplayMode.Wrong);
					textView.setText(R.string.set_lock_pattern_auth_error);
					handler.sendEmptyMessageDelayed(MEG_AUTH_ERROR, 1000);
				}
				break;
			case MODE_FIRST_STEP:
				// 第一次输入，记录
				lockPatternView.setEnabled(false);
				lastCells = new ArrayList<LockPatternView.Cell>(pattern);
				textView.setText(R.string.set_lock_pattern_first_step_tips);
				handler.sendEmptyMessageDelayed(MEG_GOTO_SECOND_STEP, 1000);
				break;
			case MODE_SECOND_STEP:
				if (LockPatternUtil.checkPatternCell(lastCells, pattern))
				{
					// 设置成功
					lockPatternView.setEnabled(false);
					lockPatternView.setDisplayMode(DisplayMode.Correct);
					textView.setText(R.string.set_lock_pattern_second_step_tips);
					handler.sendEmptyMessageDelayed(MEG_SET_SUCCESS, 2000);
					LockPatternUtil.savePatternCell(this, pattern);
				}
				else
				{
					// 两次输入密码不一致，到第一步重新输入
					lockPatternView.setDisplayMode(DisplayMode.Wrong);
					lockPatternView.setEnabled(false);
					textView.setText(R.string.set_lock_pattern_second_step_error);
					handler.sendEmptyMessageDelayed(MEG_GOTO_FIRST_STEP, 1000);
				}
				break;
			default:
				break;
		}
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		lockPatternView.setEnabled(true);
		lockPatternView.clearPattern();
		switch (msg.what)
		{
			case MEG_AUTH_ERROR:
				textView.setText(R.string.set_lock_pattern_auth);
				break;

			case MEG_GOTO_SECOND_STEP:
				mode = MODE_SECOND_STEP;
				textView.setText(R.string.set_lock_pattern_second_step);
				break;
			case MEG_SET_SUCCESS:
				Intent intent = new Intent(this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
				break;

			case MEG_GOTO_FIRST_STEP:
				mode = MODE_FIRST_STEP;
				textView.setText(R.string.set_lock_pattern_first_step);
				break;
		}
		return true;
	}
}
