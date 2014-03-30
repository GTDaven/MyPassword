package cn.xing.mypassword.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.xing.mypassword.app.BaseActivity;
import cn.xing.mypassword.model.SettingKey;
import cn.xing.mypassword.view.LockPatternView.Cell;

public class LockPatternUtil
{
	public static void savePatternCell(BaseActivity baseActivity, List<Cell> cells)
	{
		try
		{
			JSONArray jsonArray = new JSONArray();
			for (Cell cell : cells)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("row", cell.getRow());
				jsonObject.put("column", cell.getColumn());
				jsonArray.put(jsonObject);
			}
			baseActivity.putSetting(SettingKey.LOCK_PATTERN, jsonArray.toString());
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	public static List<Cell> getLocalCell(BaseActivity baseActivity)
	{
		List<Cell> cells = new ArrayList<Cell>();

		String JSONArrayString = baseActivity.getSetting(SettingKey.LOCK_PATTERN, "[]");

		try
		{
			JSONArray jsonArray = new JSONArray(JSONArrayString);
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				int row = jsonObject.getInt("row");
				int column = jsonObject.getInt("column");
				Cell cell = Cell.of(row, column);
				cells.add(cell);
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}

		return cells;
	}

	public static boolean authPatternCell(BaseActivity baseActivity, List<Cell> cells)
	{
		return checkPatternCell(getLocalCell(baseActivity), cells);
	}

	public static boolean checkPatternCell(List<Cell> cells1, List<Cell> cells2)
	{
		boolean result = true;
		if (cells1.size() == cells2.size())
		{
			int size = cells1.size();
			for (int i = 0; i < size; i++)
			{
				if (!cells1.get(i).equals(cells2.get(i)))
				{
					result = false;
					break;
				}
			}
		}
		else
		{
			result = false;
		}
		return result;
	}
}
