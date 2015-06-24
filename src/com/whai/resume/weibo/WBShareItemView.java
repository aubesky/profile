package com.whai.resume.weibo;

import com.whai.resume.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * ����򵥵ķ�װ�˷����У�������ʾ���֡�ͼƬ����Ƶ�����ֵ����ݵ���Ͽؼ���
 * 
 * @author SINA
 * @since 2013-10-22
 */
public class WBShareItemView extends LinearLayout {

	/** UI Ԫ�� */
	private TextView mTitleView;
	private ImageView mThumbView;
	private TextView mShareTitleView;
	private TextView mShareDescView;
	private TextView mShareUrlView;
	private RadioButton mCheckedBtn;

	/** RadioButton �ڵ��ʱ�Ļص����� */
	private OnCheckedChangeListener mOnCheckedChangeListener;

	/**
	 * �� RadioButton ���ʱ���ýӿڶ�Ӧ�ĵĻص����������á�
	 */
	public interface OnCheckedChangeListener {
		public void onCheckedChanged(WBShareItemView view, boolean isChecked);
	}

	/**
	 * ����һ����Ͽؼ���
	 * 
	 * @see View#View(Context)
	 */
	public WBShareItemView(Context context) {
		this(context, null);
	}

	/**
	 * �� XML �����ļ��д���һ����Ͽؼ���
	 * 
	 * @see View#View(Context, AttributeSet)
	 */
	public WBShareItemView(Context context, AttributeSet attrs) {
		// Need API Level > 8
		// this(context, attrs, 0);
		super(context, attrs);
		initialize(context);
	}

	/**
	 * �� XML �����ļ��д���һ����Ͽؼ���
	 * 
	 * @see View#View(Context, AttributeSet)
	 */
	// Need API Level > 8
	/*
	 * public WBShareItemView(Context context, AttributeSet attrs, int defStyle)
	 * { super(context, attrs, defStyle); initialize(context); }
	 */
	/**
	 * ����Դ ID ����ʼ������Ԫ�ء�
	 */
	public void initWithRes(int titleResId, int thumbResId,
			int shareTitleResId, int shareDescResId, int shareUrlResId) {
		mTitleView.setText(titleResId);
		mThumbView.setImageResource(thumbResId);
		mShareTitleView.setText(shareTitleResId);
		mShareDescView.setText(shareDescResId);
		mShareUrlView.setText(shareUrlResId);
	}

	/**
	 * ��ʼ������Ԫ�ء�
	 */
	public void initWithData(String title, Drawable thumb, String shareTitle,
			String shareDesc, String shareUrl) {
		mTitleView.setText(title);
		mThumbView.setImageDrawable(thumb);
		mShareTitleView.setText(shareTitle);
		mShareDescView.setText(shareDesc);
		mShareUrlView.setText(shareUrl);
	}

	/**
	 * ���� RadioButton �ڵ��ʱ�Ļص�������
	 * 
	 * @param listener
	 *            �ص�������
	 */
	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		mOnCheckedChangeListener = listener;
	}

	/**
	 * ��ȡ��ǰ�ؼ��� RadioButton �Ƿ�ѡ�е�״̬��
	 * 
	 * @return ѡ�У����� true�����򣬷��� false
	 */
	public boolean isChecked() {
		return mCheckedBtn.isChecked();
	}

	/**
	 * ���õ�ǰ�ؼ��� RadioButton �ı�ѡ��״̬��
	 * 
	 * @param isChecked
	 *            ѡ��״̬
	 */
	public void setIsChecked(boolean isChecked) {
		mCheckedBtn.setChecked(isChecked);
	}

	/**
	 * ��ȡ��ǰ����� Title��
	 */
	public String getTitle() {
		return mTitleView.getText().toString();
	}

	/**
	 * ��ȡ��ǰ���������ͼ��Ӧ�� Drawable��
	 */
	public Drawable getThumbDrawable() {
		return mThumbView.getDrawable();
	}

	/**
	 * ��ȡ��ǰ���������ͼ��Ӧ�� Bitmap��
	 */
	public Bitmap getThumbBitmap() {
		return ((BitmapDrawable) mThumbView.getDrawable()).getBitmap();
	}

	/**
	 * ��ȡ��ǰ����ķ��� Title��
	 */
	public String getShareTitle() {
		return mShareTitleView.getText().toString();
	}

	/**
	 * ��ȡ��ǰ����ķ���������
	 */
	public String getShareDesc() {
		return mShareDescView.getText().toString();
	}

	/**
	 * ��ȡ��ǰ����ķ��� URL��
	 */
	public String getShareUrl() {
		return mShareUrlView.getText().toString();
	}

	/**
	 * ��ʼ�����档
	 */
	private void initialize(Context context) {
		LayoutInflater.from(context)
				.inflate(R.layout.share_item_template, this);

		mTitleView = (TextView) findViewById(R.id.item_title_view);
		mThumbView = (ImageView) findViewById(R.id.item_thumb_image_btn);
		mShareTitleView = (TextView) findViewById(R.id.item_share_title_view);
		mShareDescView = (TextView) findViewById(R.id.item_share_desc_view);
		mShareUrlView = (TextView) findViewById(R.id.item_share_url_view);
		mCheckedBtn = (RadioButton) findViewById(R.id.item_checked_btn);

		mCheckedBtn
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (mOnCheckedChangeListener != null) {
							mOnCheckedChangeListener.onCheckedChanged(
									WBShareItemView.this, isChecked);
						}
					}
				});
	}
}
