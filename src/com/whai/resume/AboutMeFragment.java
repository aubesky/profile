package com.whai.resume;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.whai.resume.helper.SensorManagerHelper;
import com.whai.resume.helper.SensorManagerHelper.OnShakeListener;

import android.R.bool;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.media.MediaPlayer;
import android.media.MediaRecorder.VideoEncoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Video;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.widget.VideoView;

public class AboutMeFragment extends Fragment {

	private Activity activity;
	private ActionBar actionBar;
	private View view;
	private ViewPager viewPager;
	private View voiceView;
	private View videoView;
	private PagerAdapter pagerAdapter;
	private boolean isChanging;
	private SeekBar voiceSeekBar;
	private MediaPlayer musicPlayer;
	private Button startMusic;
	private Button pauseMusic;
	private Button stopMusic;

	private MediaController mediaController;
	private VideoView videoPlayer;

	private int viewPagerPosition = 0;
	private int scrollDir = 0;

	SensorManagerHelper sensorHelper;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setActionBar();

		view = inflater.inflate(R.layout.about_me, container, false);
		viewPager = (ViewPager) view.findViewById(R.id.about_me);

		LayoutInflater layoutInflater = LayoutInflater.from(activity);
		voiceView = layoutInflater.inflate(R.layout.voice, null);
		videoView = layoutInflater.inflate(R.layout.video, null);

		final List<View> views = new ArrayList<View>();
		views.add(voiceView);
		views.add(videoView);

		final List<String> titles = new ArrayList<String>();
		titles.add("“Ù∆µΩÈ…‹");
		titles.add(" ”∆µΩÈ…‹");

		pagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				// TODO Auto-generated method stub
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public CharSequence getPageTitle(int position) {
				// TODO Auto-generated method stub
				return titles.get(position);
			}

			@Override
			public Object instantiateItem(View container, int position) {
				// TODO Auto-generated method stub
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};

		viewPager.setAdapter(pagerAdapter);

		addMusicPlayer();
		addVedioPlayer();
		addShakeSensorHelper();

		return view;
	}

	private void stopMusicPlayer() {
		if (musicPlayer != null && musicPlayer.isPlaying()) {
			musicPlayer.stop();
			musicPlayer.reset();
			musicPlayer = MediaPlayer.create(activity, R.raw.test_music);
			isChanging = false;
		}
	}

	private void addMusicPlayer() {

		startMusic = (Button) voiceView.findViewById(R.id.start_music);
		pauseMusic = (Button) voiceView.findViewById(R.id.pause_music);
		stopMusic = (Button) voiceView.findViewById(R.id.stop_music);
		voiceSeekBar = (SeekBar) voiceView.findViewById(R.id.voice_seekBar);

		musicPlayer = MediaPlayer.create(activity, R.raw.test_music);
		voiceSeekBar.setMax(musicPlayer.getDuration());

		isChanging = false;
		Timer mTimer = new Timer();
		TimerTask mTimerTask = new TimerTask() {
			@Override
			public void run() {
				if (isChanging == true)
					return;
				voiceSeekBar.setProgress(musicPlayer.getCurrentPosition());
			}
		};
		mTimer.schedule(mTimerTask, 0, 10);

		startMusic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				musicPlayer.start();
			}
		});

		pauseMusic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				musicPlayer.pause();
			}
		});

		stopMusic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopMusicPlayer();
			}
		});

		voiceSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				int dest = seekBar.getProgress();
				musicPlayer.seekTo(dest);
				isChanging = false;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				isChanging = true;
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void addVedioPlayer() {
		mediaController = new MediaController(activity);
		videoPlayer = (VideoView) videoView.findViewById(R.id.video_view);
		videoPlayer.setVideoURI(Uri.parse("android.resource://"
				+ activity.getPackageName() + "/" + R.raw.test_video));
		videoPlayer.setMediaController(mediaController);
		mediaController.setMediaPlayer(videoPlayer);
	}

	private void addShakeSensorHelper() {
		sensorHelper = new SensorManagerHelper(activity);
		sensorHelper.setOnShakeListener(new OnShakeListener() {

			@Override
			public void onShake() {
				// TODO Auto-generated method stub
				// System.out.println("shake");
				viewPager.arrowScroll(2);
			}
		});
		sensorHelper.start();
	}

	private void setActionBar() {
		actionBar.setTitle(R.string.title_section8);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = activity;
		actionBar = activity.getActionBar();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopMusicPlayer();
	}
}
