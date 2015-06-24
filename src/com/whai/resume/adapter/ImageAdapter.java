package com.whai.resume.adapter;

import java.util.List;

import com.whai.resume.R;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context context;
	private List<String> imageList;

	public ImageAdapter(Context context, List<String> imageList) {
		this.context = context;
		this.imageList = imageList;
	}

	public int getCount() {
		return imageList.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(context);
		Bitmap bitmap = BitmapFactory.decodeFile(imageList.get(position)
				.toString());
		imageView.setImageBitmap(bitmap);

		// 重新设定Layout的宽高
		imageView.setLayoutParams(new Gallery.LayoutParams(300, 150));

		// 传回imageView对象
		return imageView;
	}

	public Bitmap getBitmap(int position) {
		ImageView imageView = new ImageView(context);
		Bitmap bitmap = BitmapFactory.decodeFile(imageList.get(position)
				.toString());
		return bitmap;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public List<String> getImageList() {
		return imageList;
	}

	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}

	public String getNowImagePath(int position){
		return imageList.get(position);
	}
	
}
