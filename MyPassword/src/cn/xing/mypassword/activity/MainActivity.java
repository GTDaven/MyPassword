package cn.xing.mypassword.activity;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import cn.xing.mypassword.R;
import cn.xing.mypassword.app.BaseActivity;

public class MainActivity extends BaseActivity
{
	private DrawLayoutFragment drawLayoutFragment;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initActionBar();

		FragmentManager fragmentManager = getFragmentManager();

		initDrawerLayout(fragmentManager);
	}

	private void initDrawerLayout(FragmentManager fragmentManager)
	{
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		drawLayoutFragment = (DrawLayoutFragment) fragmentManager.findFragmentById(R.id.navigation_drawer);
		drawLayoutFragment.setDrawerLayout(drawerLayout);
		mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer,
				R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawerLayout.setDrawerListener(mDrawerToggle);
	}

	/**
	 * ≥ı ºªØActionBar
	 */
	private void initActionBar()
	{
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (mDrawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}
		int id = item.getItemId();
		if (id == R.id.action_add_password)
		{
			if (drawerLayout.isDrawerOpen(Gravity.START))
			{
				drawerLayout.closeDrawer(Gravity.START);
			}
			startActivity(new Intent(this, EditPasswordActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
