package com.whai.resume;

import com.whai.resume.chart.AverageScoreChart;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MyGradeFragment extends Fragment {
	private Activity activity;
	private ActionBar actionBar;
	private AverageScoreChart averageScoreChart;

	public MyGradeFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.my_grade, container, false);

		setActionBar();

		Button button = (Button) view.findViewById(R.id.button1);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				averageScoreChart = new AverageScoreChart();
				startActivity(averageScoreChart.execute(activity));
			}
		});

		return view;
	}

	private void setActionBar() {
		actionBar.setTitle(R.string.title_section3);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = activity;
		actionBar = activity.getActionBar();
	}

}
