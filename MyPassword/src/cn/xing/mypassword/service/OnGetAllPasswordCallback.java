package cn.xing.mypassword.service;

import java.util.List;

import cn.xing.mypassword.model.Password;

public interface OnGetAllPasswordCallback
{
	public void onGetAllPassword(List<Password> passwords);
}
