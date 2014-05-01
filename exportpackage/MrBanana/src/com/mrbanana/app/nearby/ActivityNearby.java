package com.mrbanana.app.nearby;

import homemade.apps.framework.homerlibs.utils.HomerLogger;
import homemade.apps.framework.homerlibs.utils.NetworkApis;
import homemade.apps.framework.homerlibs.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrbanana.R;
import com.mrbanana.app.account.ActivityAccountActionList;
import com.mrbanana.base.ActivityBase;
import com.mrbanana.base.AppBase;

public class ActivityNearby extends ActivityBase implements OnClickListener {
	TextView mTvProfile, plumber_TxV, cleaner_TxV, pro_TxV, champion_Txv;
	LinearLayout sub_bottomLay;
	int mapInfo = 0;//1 - plumber, 2- cleanner

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.anb_layout);

		findViewByIds();
		setOnClickListeners();
		AsyctaskMapView asyctaskMapView = new AsyctaskMapView(this);
		asyctaskMapView.execute();
	}

	private void findViewByIds() {

		mTvProfile = (TextView) findViewById(R.id.anb_TvProfile);
		plumber_TxV = (TextView) findViewById(R.id.anb_TvPlumber);
		cleaner_TxV = (TextView) findViewById(R.id.anb_TvCleaner);
		pro_TxV = (TextView) findViewById(R.id.anb_TvPro);
		champion_Txv = (TextView) findViewById(R.id.anb_TvCha);
		sub_bottomLay = (LinearLayout) findViewById(R.id.anb_LlBottomBarWrapper_sub);
	}

	private void setOnClickListeners() {
		mTvProfile.setOnClickListener(this);
		plumber_TxV.setOnClickListener(this);
		cleaner_TxV.setOnClickListener(this);
		pro_TxV.setOnClickListener(this);
		champion_Txv.setOnClickListener(this);

	}

	@Override
	public void onBackPressed() {
		if(mapInfo != 0)
		{
			sub_bottomLay.setVisibility(View.GONE);
			mapInfo = 0;
		}
		else
		{
			super.onBackPressed();
		}
	}
	
	class AsyctaskMapView extends AsyncTask<Void, Void, Void> {
		ProgressDialog mPdDialog;
		boolean mBoolWasInternetPresentDuringDoInBackground = false;
		WeakReference<Context> mWrContext;

		public AsyctaskMapView(Context context) {
			mWrContext = new WeakReference<Context>(context);
			mPdDialog = new ProgressDialog(context);
		}

		@Override
		protected void onPreExecute() {

			mPdDialog = ProgressDialog.show(mWrContext.get(), "Alert",
					"Loading please wait..");
			mPdDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				if (NetworkApis.isOnline()) {
					mBoolWasInternetPresentDuringDoInBackground = true;
					List<NameValuePair> mNvp = new ArrayList<NameValuePair>();
					mNvp.add(new BasicNameValuePair("user_id", AppBase.getmMuUser().getmStrId().trim()));
					String mStsResponse = Utils.postData(AppBase.mStrBaseUrl
							+ "mapview", mNvp);
					HomerLogger.d("login response ==" + mStsResponse);
					//parseLoginResponse(mStsResponse);

				} else {
					mBoolWasInternetPresentDuringDoInBackground = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void notUsed) {
			try {

				mPdDialog.dismiss();

				/*if (mBoolWasInternetPresentDuringDoInBackground) {
					if (AppBase.getmMuUser().getmStrLoginSucces()
							.equals(ModelUser.mStrValueLoginSuccessSuccess)) {
						if (AppBase.getmMuUser().getmStrStatus().trim()
								.equals("1")) {
							navigateToActivity(ActivityNearby.class);
						} else {
							ActivityVerification
									.setmBoolLoginCredentialsAvail(true);
							navigateToActivity(ActivityVerification.class);
						}
					} else {
						AlertBoxUtils
								.getAlertDialogBox(mWrContext.get(),
										"Login Was not Succesfull .Please check your credentials and Try Again ")
								.show();
						;
					}
				} else {
					AlertBoxUtils
							.getAlertDialogBox(mWrContext.get(),
									"Please check your internet connection and try again. ")
							.show();
					;
				}*/
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void parseMapViewResponse(String response) {
		try {

			JSONObject json = new JSONObject(response);

			JSONObject jsonSucess, workerJson;
			JSONArray workerJArray;

			if (json.has("SUCCESS")) {
				jsonSucess = json.getJSONObject("SUCCESS");
				if (jsonSucess.has("worker_coordinates")) {
					workerJArray = jsonSucess
							.getJSONArray("worker_coordinates");
					for (int i = 0; i < workerJArray.length(); i++) {
						Worker worker = new Worker();
						workerJson = workerJArray.getJSONObject(i);
						if (workerJson.has("lat") && workerJson.has("lng")) {
							worker.setmStrLat(workerJson.getString("lat"));
							worker.setmStrLat(workerJson.getString("lng"));
							if (workerJson.has("title")) {
								worker.setmStrLat(workerJson.getString("title"));
							}
							if (workerJson.has("description")) {
								worker.setmStrLat(workerJson
										.getString("description"));
							}
							if (workerJson.has("marker_img_path")) {
								worker.setmStrLat(workerJson
										.getString("marker_img_path"));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v == mTvProfile) {
			navigateToActivity(ActivityAccountActionList.class);
		}
		if (v == plumber_TxV)
		{
			mapInfo = 1;
			sub_bottomLay.setVisibility(View.VISIBLE);
			pro_TxV.setText("Pro-Plumber");
			champion_Txv.setText("Champion-Plumber");
		}
		if (v == cleaner_TxV)
		{
			mapInfo = 2;
			sub_bottomLay.setVisibility(View.VISIBLE);
			pro_TxV.setText("Pro-Cleaner");
			champion_Txv.setText("Champion-Cleaner");
		}
		if (v == pro_TxV)
		{
			if(mapInfo == 1)
			{
				
			}
			else if(mapInfo == 2)
			{
				
			}
		}
		if (v == champion_Txv)
		{
			if(mapInfo == 1)
			{
				
			}
			else if(mapInfo == 2)
			{
				
			}
		}
		
	}

}
