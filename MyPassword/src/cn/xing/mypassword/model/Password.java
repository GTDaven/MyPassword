package cn.xing.mypassword.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 一条密码
 * 
 * @author zengdexing
 * 
 */
public class Password implements Serializable
{
	private static final long serialVersionUID = -961233794781935060L;

	/** 数据库主键 */
	private int id;

	/** 创建日期 */
	private long createDate;

	/** 标题 */
	private String title;
	/** 用户名 */
	private String userName;
	/** 密码 */
	private String password;
	/** 标注 */
	private String note;

	private boolean isTop = false;

	public int getId()
	{
		return id;
	}

	public long getCreateDate()
	{
		return createDate;
	}

	public String getTitle()
	{
		return title;
	}

	public String getUserName()
	{
		return userName;
	}

	public String getPassword()
	{
		return password;
	}

	public String getNote()
	{
		return note;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public void setCreateDate(long createDate)
	{
		this.createDate = createDate;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	public static Password createFormJson(String json) throws JSONException
	{
		Password password = new Password();
		JSONObject jsonObject = new JSONObject(json);
		password.setId(jsonObject.optInt("id", 0));
		password.setCreateDate(jsonObject.getLong("createDate"));
		password.setTitle(jsonObject.getString("title"));
		password.setUserName(jsonObject.getString("userName"));
		password.setPassword(jsonObject.getString("password"));
		password.setNote(jsonObject.getString("note"));
		password.setTop(jsonObject.optBoolean("isTop", false));
		return password;
	}

	public String toJSON()
	{
		JSONObject jsonObject = new JSONObject();
		try
		{
			jsonObject.put("id", id);
			jsonObject.put("createDate", createDate);
			jsonObject.put("title", title);
			jsonObject.put("userName", userName);
			jsonObject.put("password", password);
			jsonObject.put("note", note);
			jsonObject.put("isTop", isTop);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	public boolean isTop()
	{
		return isTop;
	}

	public void setTop(boolean isTop)
	{
		this.isTop = isTop;
	}

	@Override
	public String toString()
	{
		return "Password [id=" + id + ", createDate=" + createDate + ", title=" + title + ", userName=" + userName
				+ ", password=" + password + ", note=" + note + ", isTop=" + isTop + "]";
	}
}
