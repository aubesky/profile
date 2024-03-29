package com.whai.resume.adapter;

import java.util.List;

import com.whai.resume.R;
import com.whai.resume.entity.NavDrawerItem;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerListAdapter extends BaseAdapter {

	private Context context;
	private List<NavDrawerItem> navDrawerItems;

	public NavDrawerListAdapter() {
		// TODO Auto-generated constructor stub
	}

	public NavDrawerListAdapter(Context context,
			List<NavDrawerItem> navDrawerItems) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
			ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
			TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
			//TextView txtCount = (TextView) convertView
			//		.findViewById(R.id.counter);

			imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
			txtTitle.setText(navDrawerItems.get(position).getTitle());
		}
		return convertView;
	}

}
