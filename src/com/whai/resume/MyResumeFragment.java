package com.whai.resume;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyResumeFragment extends Fragment {

	private ActionBar actionBar;

	public MyResumeFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setActionBar();
		
		return inflater.inflate(R.layout.my_resume, container, false);
	}

	private void setActionBar() {
		actionBar.setTitle(R.string.title_section2);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		actionBar = activity.getActionBar();
	}

}
