package cn.xing.mypassword.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.xing.mypassword.R;
import cn.xing.mypassword.model.Password;
import cn.zdx.lib.annotation.FindViewById;
import cn.zdx.lib.annotation.XingAnnotationHelper;

/**
 * 主界面密码适配器
 * 
 * @author zengdexing
 * 
 */
public class MainAdapter extends BaseAdapter
{
	private List<PasswordItem> passwords = new ArrayList<PasswordItem>();
	private Context context;
	private SimpleDateFormat simpleDateFormatYear = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	private int padding;

	private SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("MM-dd", Locale.getDefault());

	/** 一天含有的秒数 */
	private static final long DAY = 1000 * 60 * 60 * 24;

	private Comparator<PasswordItem> comparator = new Comparator<PasswordItem>()
	{
		@Override
		public int compare(PasswordItem lhs, PasswordItem rhs)
		{
			// 置顶排序
			if (lhs.password.isTop() || rhs.password.isTop())
			{
				if (lhs.password.isTop() && rhs.password.isTop())
				{
					return (int) (rhs.password.getCreateDate() - lhs.password.getCreateDate());
				}
				else if (lhs.password.isTop())
				{
					return -1;
				}
				else
				{
					return 1;
				}
			}
			return (int) (rhs.password.getCreateDate() - lhs.password.getCreateDate());
		}
	};

	public int dip2px(float dipValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public MainAdapter(Context context)
	{
		this.context = context;
		padding = dip2px(6);
	}

	public void setData(List<Password> passwords)
	{
		this.passwords.clear();
		for (Password password : passwords)
		{
			this.passwords.add(new PasswordItem(password));
		}
		Collections.sort(this.passwords, comparator);
		notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		return passwords.size();
	}

	@Override
	public PasswordItem getItem(int position)
	{
		return passwords.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public void notifyDataSetChanged()
	{
		for (PasswordItem passwordItem : passwords)
		{
			passwordItem.initDataString();
		}
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder;
		if (convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.main_password_item, null);
			convertView.setTag(viewHolder);
			XingAnnotationHelper.findView(viewHolder, convertView);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (position == 0)
		{
			convertView.setPadding(padding, padding, padding, padding);
		}
		else
		{
			convertView.setPadding(padding, 0, padding, padding);
		}

		PasswordItem passwordItem = getItem(position);

		viewHolder.bindView(passwordItem);

		return convertView;
	}

	private class ViewHolder
	{
		@FindViewById(R.id.main_item_title)
		public TextView titleView;

		@FindViewById(R.id.main_item_date)
		public TextView dateView;

		@FindViewById(R.id.main_item_name)
		public TextView nameView;

		@FindViewById(R.id.main_item_password)
		public TextView passwordView;

		@FindViewById(R.id.main_item_note)
		public TextView noteView;

		@FindViewById(R.id.main_item_note_container)
		public View noteConainer;

		@FindViewById(R.id.main_item_top)
		public View topIconView;

		void bindView(PasswordItem passwordItem)
		{
			titleView.setText(passwordItem.password.getTitle());
			dateView.setText(passwordItem.dataString);
			nameView.setText(passwordItem.password.getUserName());
			passwordView.setText(passwordItem.password.getPassword());

			String note = passwordItem.password.getNote();
			if (TextUtils.isEmpty(note))
			{
				noteConainer.setVisibility(View.GONE);
			}
			else
			{
				noteConainer.setVisibility(View.VISIBLE);
				noteView.setText(note);
			}

			if (passwordItem.password.isTop())
			{
				topIconView.setVisibility(View.VISIBLE);
				dateView.setTextColor(context.getResources().getColor(R.color.title_color));
			}
			else
			{
				topIconView.setVisibility(View.GONE);
				dateView.setTextColor(context.getResources().getColor(R.color.text_color));
			}
		}
	}

	public class PasswordItem
	{
		public String dataString;
		public Password password;

		private PasswordItem(Password password)
		{
			this.password = password;
			initDataString();
		}

		public void initDataString()
		{
			dataString = fomartDate(password.getCreateDate());
		}
	}

	private String fomartDate(long createDate)
	{
		String result = "";
		long currentTime = System.currentTimeMillis();
		long distance = currentTime - createDate;
		if (createDate > currentTime)
		{
			result = simpleDateFormatYear.format(createDate);
		}
		else if (distance < 1000 * 60)
		{
			result = context.getString(R.string.just);
		}
		else if (distance < 1000 * 60 * 60)
		{
			String dateString = context.getString(R.string.minute_ago);
			result = String.format(Locale.getDefault(), dateString, distance / (1000 * 60));
		}
		else if (distance < DAY)
		{
			String dateString = context.getString(R.string.hour_ago);
			result = String.format(Locale.getDefault(), dateString, distance / (1000 * 60 * 60));
		}
		else if (distance < (DAY * 7))
		{
			String dateString = context.getString(R.string.day_ago);
			result = String.format(Locale.getDefault(), dateString, distance / (DAY));
		}
		else if (distance < DAY * 30)
		{
			String dateString = context.getString(R.string.week_ago);
			result = String.format(Locale.getDefault(), dateString, distance / (DAY * 7));
		}
		else if (distance < DAY * 365)
		{
			result = simpleDateFormatMonth.format(createDate);
		}
		else
		{
			result = simpleDateFormatYear.format(createDate);
		}

		return result;
	}

	public void onNewPassword(Password password)
	{
		passwords.add(0, new PasswordItem(password));
		Collections.sort(this.passwords, comparator);
		notifyDataSetChanged();
	}

	public void onDeletePassword(int id)
	{
		for (int i = 0; i < passwords.size(); i++)
		{
			PasswordItem passwordItem = passwords.get(i);
			if (passwordItem.password.getId() == id)
			{
				passwords.remove(i);
				break;
			}
		}
		notifyDataSetChanged();
	}

	public void onUpdatePassword(Password newPassword)
	{
		boolean needSort = false;
		for (int i = 0; i < passwords.size(); i++)
		{
			Password oldPassword = passwords.get(i).password;
			if (oldPassword.getId() == newPassword.getId())
			{
				if (newPassword.getCreateDate() != 0)
					oldPassword.setCreateDate(newPassword.getCreateDate());
				if (newPassword.getTitle() != null)
					oldPassword.setTitle(newPassword.getTitle());
				if (newPassword.getUserName() != null)
					oldPassword.setUserName(newPassword.getUserName());
				if (newPassword.getPassword() != null)
					oldPassword.setPassword(newPassword.getPassword());
				if (newPassword.getNote() != null)
					oldPassword.setNote(newPassword.getNote());
				if (oldPassword.isTop() != newPassword.isTop())
				{
					oldPassword.setTop(newPassword.isTop());
					needSort = true;
				}
				break;
			}
		}
		if (needSort)
			Collections.sort(this.passwords, comparator);
		notifyDataSetChanged();
	}
}
