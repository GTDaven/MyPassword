package cn.xing.mypassword.service.task;

import java.util.List;

import cn.xing.mypassword.database.PasswordDatabase;
import cn.xing.mypassword.model.AsyncResult;
import cn.xing.mypassword.model.AsyncSingleTask;
import cn.xing.mypassword.model.Password;
import cn.xing.mypassword.service.OnGetAllPasswordCallback;

public class GetAllPasswordTask extends AsyncSingleTask<List<Password>>
{
	private PasswordDatabase passwordDatabase;
	private OnGetAllPasswordCallback onGetAllPasswordCallback;

	public GetAllPasswordTask(PasswordDatabase passwordDatabase,
			OnGetAllPasswordCallback onGetAllPasswordCallback)
	{
		this.passwordDatabase = passwordDatabase;
		this.onGetAllPasswordCallback = onGetAllPasswordCallback;
	}

	@Override
	protected AsyncResult<List<Password>> doInBackground(AsyncResult<List<Password>> asyncResult)
	{
		List<Password> passwords = passwordDatabase.getAllPassword();
		asyncResult.setData(passwords);
		return asyncResult;
	}

	@Override
	protected void runOnMainThread(AsyncResult<List<Password>> asyncResult)
	{
		onGetAllPasswordCallback.onGetAllPassword(asyncResult.getData());
	}

}
