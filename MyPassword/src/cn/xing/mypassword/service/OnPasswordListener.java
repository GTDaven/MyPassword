package cn.xing.mypassword.service;

import cn.xing.mypassword.model.Password;

public interface OnPasswordListener
{
	public void onNewPassword(Password password);
	public void onDeletePassword(int id);
	public void onUpdatePassword(Password password);
}
