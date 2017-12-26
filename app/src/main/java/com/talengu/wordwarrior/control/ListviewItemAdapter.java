package com.talengu.wordwarrior.control;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.talengu.wordwarrior.R;
import com.talengu.wordwarrior.model.ListviewItem;

import java.util.List;

public class ListviewItemAdapter extends BaseAdapter {

	private Context context;
	private List<ListviewItem> mListviewItemzList;

	public ListviewItemAdapter(Context context, List<ListviewItem> mListviewItemzList) {
		this.context = context;
		this.mListviewItemzList = mListviewItemzList;
	}

	@Override
	public int getCount() {
		return mListviewItemzList.size();
	}

	@Override
	public ListviewItem getItem(int position) {
		return mListviewItemzList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public int getViewTypeCount() {
		// menu type count
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		// current menu type
		ListviewItem item = getItem(position);
		return item.color==0?0:1;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder=null;
		//优化 ref http://blog.csdn.net/s003603u/article/details/47261393
		if (convertView == null) {
			convertView = View.inflate(context.getApplicationContext(), R.layout.item_list_app, null);
			holder=new ViewHolder(convertView);
		}else
		{holder = (ViewHolder) convertView.getTag();}
		//ViewHolder holder = (ViewHolder) convertView.getTag();
		ListviewItem item = getItem(position);
		// holder.iv_icon.setImageDrawable(item.loadIcon(context.getPackageManager()));
		holder.tv_name.setText(item.fileName);

		convertView.setBackgroundColor(item.color);

		return convertView;
	}

	class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;

		public ViewHolder(View view) {
			iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
			tv_name = (TextView) view.findViewById(R.id.tv_name);

			view.setTag(this);
		}
	}
}
