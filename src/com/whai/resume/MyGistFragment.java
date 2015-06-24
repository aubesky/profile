package com.whai.resume;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.ZoomDensity;
import android.widget.Toast;

public class MyGistFragment extends Fragment {
	private Activity activity;
	private View view;
	private ActionBar actionBar;
	private WebView webView;

	public MyGistFragment() {
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setActionBar();

		view = inflater.inflate(R.layout.my_gist, container, false);
		webView = (WebView) view.findViewById(R.id.my_gist);
		webView.getSettings().setJavaScriptEnabled(true);// 设置使用够执行JS脚本
		webView.getSettings().setBuiltInZoomControls(true);// 设置使支持缩放
		webView.loadUrl("https://github.com/whai362");
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				Toast.makeText(activity, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}
		});

		adaptScreen();

		return view;
	}

	private void setActionBar() {
		actionBar.setTitle(R.string.title_section6);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = activity;
		actionBar = activity.getActionBar();
	}

	// 使得网页适配屏幕
	@SuppressWarnings("deprecation")
	private void adaptScreen() {
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);

		int screenDensity = activity.getResources().getDisplayMetrics().densityDpi;
		switch (screenDensity) {
		case DisplayMetrics.DENSITY_LOW:
			webView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			webView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
			break;
		case DisplayMetrics.DENSITY_HIGH:
			webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
			break;
		case DisplayMetrics.DENSITY_TV:
			webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
			break;
		}
	}

	// 返回键按下时会被调用
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN
				&& webView.canGoBack()) {
			webView.goBack();
			return true;
		} else
			return false;
	}
}
