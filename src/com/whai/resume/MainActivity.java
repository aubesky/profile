package com.whai.resume;

import com.whai.resume.weibo.WBDemoMainActivity;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	private static final int TAB_HOME = 0;
	private static final int TAB_MY_RESUME = 1;
	private static final int TAB_MY_GRADE = 2;
	private static final int TAB_MY_PRIZE = 3;
	private static final int TAB_MY_BLOG = 4;
	private static final int TAB_MY_GIST = 5;
	private static final int TAB_MY_WEIBO = 6;
	private static final int TAB_ABOUT_ME = 7;

	private Fragment nowFragment; // fg记录当前的Fragment
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		switch (position) {
		case TAB_HOME:
			nowFragment = new HomeFragment();
			break;
		case TAB_MY_RESUME:
			nowFragment = new MyResumeFragment();
			break;
		case TAB_MY_GRADE:
			nowFragment = new MyGradeFragment();
			break;
		case TAB_MY_PRIZE:
			nowFragment = new MyPrizeFragment();
			break;
		case TAB_MY_BLOG:
			nowFragment = new MyBlogFragment();
			break;
		case TAB_MY_GIST:
			nowFragment = new MyGistFragment();
			break;
		case TAB_MY_WEIBO:
			nowFragment = new MyWeiboFragment();
			break;
		case TAB_ABOUT_ME:
			nowFragment = new AboutMeFragment();
		}
		fragmentManager.beginTransaction().replace(R.id.container, nowFragment)
				.commit();
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		// actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			if (nowFragment instanceof MyPrizeFragment)
				getMenuInflater().inflate(R.menu.my_prize_menu, menu);
			else if (nowFragment instanceof MyWeiboFragment)
				getMenuInflater().inflate(R.menu.my_weibo_menu, menu);
			else
				getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.add_pic) {
			((MyPrizeFragment) nowFragment).openImageSelectDialog();
		}
		if (id == R.id.add_weibo) {
			startActivity(new Intent(MainActivity.this,
					WBDemoMainActivity.class));
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (nowFragment instanceof MyBlogFragment
				&& ((MyBlogFragment) nowFragment).onKeyDown(keyCode, event)) {
			return true;
		}
		if (nowFragment instanceof MyGistFragment
				&& ((MyGistFragment) nowFragment).onKeyDown(keyCode, event)) {
			return true;
		}

		if (nowFragment instanceof MyWeiboFragment
				&& ((MyWeiboFragment) nowFragment).onKeyDown(keyCode, event)) {
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
