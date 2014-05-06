package com.mrbanana.app.account.triphistory;

import homemade.apps.framework.homerlibs.utils.imageloader.ImageLoader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mrbanana.R;
import com.mrbanana.base.ModelJob;

public class ListAdapterWorkHistory extends ArrayAdapter<ModelJob> {

	// //////////////////////////////////////////////////////
	// //// START OF VARIABLE DECLARATIONS ////////////////
	// ///////////////////////////////////////////////

	private ArrayList<ModelJob> mArrLisOfModelArticles = new ArrayList<ModelJob>();

	private WeakReference<Context> mWrOfContext = null;

	private static ArrayList<View> mArrlisOfViewsMemoryCache = new ArrayList<View>();
	private ImageLoader mImageLoader;
	private OnClickListener listener;

	public ArrayList<ModelJob> getLisOfModelArticles() {
		return mArrLisOfModelArticles;
	}

	public void setLisOfModelArticles(ArrayList<ModelJob> mArrLisOfModelArticles) {
		this.mArrLisOfModelArticles = mArrLisOfModelArticles;
	}

	public static ArrayList<View> getLisOfViewsMemoryCache() {
		return mArrlisOfViewsMemoryCache;
	}

	public static void setLisOfViewsMemoryCache(
			ArrayList<View> mArrlisOfViewsMemoryCache_) {
		mArrlisOfViewsMemoryCache = mArrlisOfViewsMemoryCache_;
	}

	// //////////////////////////////////////////////////////
	// //// END OF VARIABLE DECLARATIONS ////////////////
	// ///////////////////////////////////////////////

	public ListAdapterWorkHistory(Context context,
			ArrayList<ModelJob> arrListOfModelArticles, OnClickListener listener) {
		super(context, 0);
		this.mArrLisOfModelArticles = arrListOfModelArticles;
		mWrOfContext = new WeakReference<Context>(context);

		mImageLoader = new ImageLoader(mWrOfContext.get());
		this.listener = listener;
	}

	@Override
	public int getCount() {
		return mArrLisOfModelArticles.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mWrOfContext.get()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.ath_list_layout_onerow,
					null);
		}

		TextView Header = (TextView) convertView
				.findViewById(R.id.ath_l_l_orTvHeader);

		TextView description = (TextView) convertView
				.findViewById(R.id.ath_l_l_orTvHeader);

		RelativeLayout mRlparent = (RelativeLayout) convertView
				.findViewById(R.id.ath_l_l_orRlScreenParentLayout);

		ModelJob job = mArrLisOfModelArticles.get(position);

		Header.setText(job.getmStrFirstName() + " "
				+ job.getmStrTradesmanLevel());

		description.setText(ActivityWorkHistoryListing.getJobStatusforNo(job
				.getmStrJobStatus()));

		mRlparent.setTag(job);
		mRlparent.setOnClickListener(listener);

		return convertView;
	}

	@Override
	public void add(ModelJob object) {
		super.add(object);
		getLisOfModelArticles().add(object);
	}

}
