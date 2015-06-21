package com.whai.resume.entity;

import android.R.integer;

public class NavDrawerItem {

	private String title;
	private int icon, count;
	private boolean isCounterVisible;

	public NavDrawerItem() {
		// TODO Auto-generated constructor stub
	}

	public NavDrawerItem(String title, int icon) {
		this.title = title;
		this.icon = icon;
	}

	public NavDrawerItem(String title, int icon, boolean isCounterVisible,
			int count) {
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean isCounterVisible() {
		return isCounterVisible;
	}

	public void setCounterVisible(boolean isCounterVisible) {
		this.isCounterVisible = isCounterVisible;
	}
	
	

}
