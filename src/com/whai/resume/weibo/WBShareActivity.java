package com.whai.resume.weibo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.VoiceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboDownloadListener;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboShareException;
import com.sina.weibo.sdk.utils.Utility;
import com.whai.resume.R;

/**
 * ������ʾ�˵�����Ӧ�����ͨ��΢���ͻ��˷������֡�ͼƬ����Ƶ�����ֵȡ� ִ�����̣� �ӱ�Ӧ��->΢��->��Ӧ��
 * 
 * @author SINA
 * @since 2013-10-22
 */
public class WBShareActivity extends Activity implements OnClickListener,
		IWeiboHandler.Response {
	@SuppressWarnings("unused")
	private static final String TAG = "WBShareActivity";

	/** ������� */
	private TextView mTitleView;
	/** ���ڿ����Ƿ�����ı��� CheckBox */
	private CheckBox mTextCheckbox;

	/** ����ť */
	private Button mSharedBtn;

	/** ΢��΢������ӿ�ʵ�� */
	private IWeiboShareAPI mWeiboShareAPI = null;

	/**
	 * @see {@link Activity#onCreate}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		initViews();

		// ����΢������ӿ�ʵ��
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.APP_KEY);

		// ע�������Ӧ�õ�΢���ͻ����У�ע��ɹ����Ӧ�ý���ʾ��΢����Ӧ���б��С�
		// ���ø��������ɷ���Ȩ����Ҫ�������룬������鿴 Demo ��ʾ
		// NOTE���������ǰע�ᣬ�������ʼ����ʱ�����Ӧ�ó����ʼ��ʱ������ע��
		mWeiboShareAPI.registerApp();

		// ���δ��װ΢���ͻ��ˣ���������΢����Ӧ�Ļص�
		if (!mWeiboShareAPI.isWeiboAppInstalled()) {
			mWeiboShareAPI
					.registerWeiboDownloadListener(new IWeiboDownloadListener() {
						@Override
						public void onCancel() {
							Toast.makeText(
									WBShareActivity.this,
									R.string.weibosdk_demo_cancel_download_weibo,
									Toast.LENGTH_SHORT).show();
						}
					});
		}

		// �� Activity �����³�ʼ��ʱ���� Activity ���ں�̨ʱ�����ܻ������ڴ治�㱻ɱ���ˣ���
		// ��Ҫ���� {@link IWeiboShareAPI#handleWeiboResponse} ������΢���ͻ��˷��ص����ݡ�
		// ִ�гɹ������� true�������� {@link IWeiboHandler.Response#onResponse}��
		// ʧ�ܷ��� false�������������ص�
		if (savedInstanceState != null) {
			mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
		}
	}

	/**
	 * @see {@link Activity#onNewIntent}
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		// �ӵ�ǰӦ�û���΢�������з���󣬷��ص���ǰӦ��ʱ����Ҫ�ڴ˴����øú���
		// ������΢���ͻ��˷��ص����ݣ�ִ�гɹ������� true��������
		// {@link IWeiboHandler.Response#onResponse}��ʧ�ܷ��� false�������������ص�
		mWeiboShareAPI.handleWeiboResponse(intent, this);
	}

	/**
	 * ����΢�ͻ��˲���������ݡ� ��΢���ͻ��˻���ǰӦ�ò����з���ʱ���÷��������á�
	 * 
	 * @param baseRequest
	 *            ΢���������ݶ���
	 * @see {@link IWeiboShareAPI#handleWeiboRequest}
	 */
	@Override
	public void onResponse(BaseResponse baseResp) {
		switch (baseResp.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			Toast.makeText(this, R.string.weibosdk_demo_toast_share_success,
					Toast.LENGTH_LONG).show();
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			Toast.makeText(this, R.string.weibosdk_demo_toast_share_canceled,
					Toast.LENGTH_LONG).show();
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			Toast.makeText(
					this,
					getString(R.string.weibosdk_demo_toast_share_failed)
							+ "Error Message: " + baseResp.errMsg,
					Toast.LENGTH_LONG).show();
			break;
		}
	}

	/**
	 * �û��������ť������΢���ͻ��˽��з���
	 */
	@Override
	public void onClick(View v) {
		if (R.id.share_to_btn == v.getId()) {
			try {
				// ���΢���ͻ��˻����Ƿ����������δ��װ΢���������Ի���ѯ���û�����΢���ͻ���
				if (mWeiboShareAPI.checkEnvironment(true)) {
					sendMessage(mTextCheckbox.isChecked(), false, false, false,
							false, false);
				}
			} catch (WeiboShareException e) {
				e.printStackTrace();
				Toast.makeText(WBShareActivity.this, e.getMessage(),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * ��ʼ�����档
	 */
	private void initViews() {
		mTitleView = (TextView) findViewById(R.id.share_title);
		mTitleView.setText(R.string.weibosdk_demo_share_to_weibo_title);
		mTextCheckbox = (CheckBox) findViewById(R.id.share_text_checkbox);

		mSharedBtn = (Button) findViewById(R.id.share_to_btn);
		mSharedBtn.setOnClickListener(this);
	}

	/**
	 * ���� RadioButton �ĵ���¼���
	 */
	private WBShareItemView.OnCheckedChangeListener mCheckedChangeListener = new WBShareItemView.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(WBShareItemView view, boolean isChecked) {
			view.setIsChecked(isChecked);
		}
	};

	/**
	 * ������Ӧ�÷���������Ϣ��΢��������΢��������档
	 * 
	 * @see {@link #sendMultiMessage} ���� {@link #sendSingleMessage}
	 */
	private void sendMessage(boolean hasText, boolean hasImage,
			boolean hasWebpage, boolean hasMusic, boolean hasVideo,
			boolean hasVoice) {

		if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
			int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
			if (supportApi >= 10351 /* ApiUtils.BUILD_INT_VER_2_2 */) {
				sendMultiMessage(hasText, hasImage, hasWebpage, hasMusic,
						hasVideo, hasVoice);
			} else {
				sendSingleMessage(hasText, hasImage, hasWebpage, hasMusic,
						hasVideo/* , hasVoice */);
			}
		} else {
			Toast.makeText(this, R.string.weibosdk_demo_not_support_api_hint,
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * ������Ӧ�÷���������Ϣ��΢��������΢��������档 ע�⣺��
	 * {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 ʱ��֧��ͬʱ���������Ϣ��
	 * ͬʱ���Է����ı���ͼƬ�Լ�����ý����Դ����ҳ�����֡���Ƶ�������е�һ�֣���
	 * 
	 * @param hasText
	 *            ����������Ƿ����ı�
	 * @param hasImage
	 *            ����������Ƿ���ͼƬ
	 * @param hasWebpage
	 *            ����������Ƿ�����ҳ
	 * @param hasMusic
	 *            ����������Ƿ�������
	 * @param hasVideo
	 *            ����������Ƿ�����Ƶ
	 * @param hasVoice
	 *            ����������Ƿ�������
	 */
	private void sendMultiMessage(boolean hasText, boolean hasImage,
			boolean hasWebpage, boolean hasMusic, boolean hasVideo,
			boolean hasVoice) {

		// 1. ��ʼ��΢���ķ�����Ϣ
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		if (hasText) {
			weiboMessage.textObject = getTextObj();
		}

		// 2. ��ʼ���ӵ�������΢������Ϣ����
		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		// ��transactionΨһ��ʶһ������
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.multiMessage = weiboMessage;

		// 3. ����������Ϣ��΢��������΢���������
		mWeiboShareAPI.sendRequest(request);
	}

	/**
	 * ������Ӧ�÷���������Ϣ��΢��������΢��������档 ��{@link IWeiboShareAPI#getWeiboAppSupportAPI()}
	 * < 10351 ʱ��ֻ֧�ַ�������Ϣ���� �ı���ͼƬ����ҳ�����֡���Ƶ�е�һ�֣���֧��Voice��Ϣ��
	 * 
	 * @param hasText
	 *            ����������Ƿ����ı�
	 * @param hasImage
	 *            ����������Ƿ���ͼƬ
	 * @param hasWebpage
	 *            ����������Ƿ�����ҳ
	 * @param hasMusic
	 *            ����������Ƿ�������
	 * @param hasVideo
	 *            ����������Ƿ�����Ƶ
	 */
	private void sendSingleMessage(boolean hasText, boolean hasImage,
			boolean hasWebpage, boolean hasMusic, boolean hasVideo/*
																 * , boolean
																 * hasVoice
																 */) {

		// 1. ��ʼ��΢���ķ�����Ϣ
		// �û����Է����ı���ͼƬ�е�һ��
		WeiboMessage weiboMessage = new WeiboMessage();
		if (hasText) {
			weiboMessage.mediaObject = getTextObj();
		}
		/*
		 * if (hasVoice) { weiboMessage.mediaObject = getVoiceObj(); }
		 */

		// 2. ��ʼ���ӵ�������΢������Ϣ����
		SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
		// ��transactionΨһ��ʶһ������
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.message = weiboMessage;

		// 3. ����������Ϣ��΢��������΢���������
		mWeiboShareAPI.sendRequest(request);
	}

	/**
	 * ��ȡ������ı�ģ�塣
	 * 
	 * @return ������ı�ģ��
	 */
	private String getSharedText() {
		int formatId = R.string.weibosdk_demo_share_text_template;
		String format = getString(formatId);
		String text = format;
		String demoUrl = getString(R.string.weibosdk_demo_app_url);
		if (mTextCheckbox.isChecked()) {
			format = getString(R.string.weibosdk_demo_share_text_template);
		}
		return text;
	}

	/**
	 * �����ı���Ϣ����
	 * 
	 * @return �ı���Ϣ����
	 */
	private TextObject getTextObj() {
		TextObject textObject = new TextObject();
		textObject.text = getSharedText();
		return textObject;
	}
}
