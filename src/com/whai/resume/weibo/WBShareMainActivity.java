package com.whai.resume.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.api.share.IWeiboDownloadListener;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.whai.resume.R;

/**
 * �����Ƿ����ܵ���ڡ�
 * 
 * @author SINA
 * @since 2013-09-29
 */
public class WBShareMainActivity extends Activity {

	/** ΢������Ľӿ�ʵ�� */
	private IWeiboShareAPI mWeiboShareAPI;

	/** ΢������ť */
	private Button mShareButton;

	/**
	 * @see {@link Activity#onCreate}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_main);
		initialize();
	}

	/**
	 * ��ʼ�� UI ��΢���ӿ�ʵ�� ��
	 */
	private void initialize() {

		// ����΢�� SDK �ӿ�ʵ��
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.APP_KEY);

		// ��ȡ΢���ͻ��������Ϣ�����Ƿ�װ��֧�� SDK �İ汾
		boolean isInstalledWeibo = mWeiboShareAPI.isWeiboAppInstalled();
		int supportApiLevel = mWeiboShareAPI.getWeiboAppSupportAPI();

		// ���δ��װ΢���ͻ��ˣ���������΢����Ӧ�Ļص�
		if (!isInstalledWeibo) {
			mWeiboShareAPI
					.registerWeiboDownloadListener(new IWeiboDownloadListener() {
						@Override
						public void onCancel() {
							Toast.makeText(
									WBShareMainActivity.this,
									R.string.weibosdk_demo_cancel_download_weibo,
									Toast.LENGTH_SHORT).show();
						}
					});
		}

		/**
		 * ��ʼ�� UI
		 */
		// ������ʾ�ı�
		((TextView) findViewById(R.id.register_app_to_weibo_hint))
				.setMovementMethod(LinkMovementMethod.getInstance());
		((TextView) findViewById(R.id.weibosdk_demo_support_api_level_hint))
				.setMovementMethod(LinkMovementMethod.getInstance());

		// ����΢���ͻ��������Ϣ
		String installInfo = getString(isInstalledWeibo ? R.string.weibosdk_demo_has_installed_weibo
				: R.string.weibosdk_demo_has_installed_weibo);
		((TextView) findViewById(R.id.weibosdk_demo_is_installed_weibo))
				.setText(installInfo);
		((TextView) findViewById(R.id.weibosdk_demo_support_api_level))
				.setText("\t" + supportApiLevel);

		// ����ע�ᰴť��Ӧ�ص�
		((Button) findViewById(R.id.register_app_to_weibo))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// ע�ᵽ����΢��
						mWeiboShareAPI.registerApp();
						Toast.makeText(
								WBShareMainActivity.this,
								R.string.weibosdk_demo_toast_register_app_to_weibo,
								Toast.LENGTH_LONG).show();

						mShareButton.setEnabled(true);
					}
				});

		// ���÷���ť��Ӧ�ص�
		mShareButton = (Button) findViewById(R.id.share_to_weibo);
		mShareButton.setEnabled(false);
		mShareButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(WBShareMainActivity.this,
						WBShareActivity.class));
			}
		});
	}
}
