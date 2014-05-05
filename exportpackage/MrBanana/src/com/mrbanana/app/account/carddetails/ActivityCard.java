package com.mrbanana.app.account.carddetails;

import homemade.apps.framework.homerlibs.utils.AlertBoxUtils;
import homemade.apps.framework.homerlibs.utils.HomerLogger;
import homemade.apps.framework.homerlibs.utils.NetworkApis;
import homemade.apps.framework.homerlibs.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.mrbanana.app.account.profile.ActivityProfile;
import com.mrbanana.app.nearby.ActivityNearby;
import com.mrbanana.app.registration_and_login.ActivityVerification;
import com.mrbanana.base.ActivityBase;
import com.mrbanana.base.AppBase;
import com.mrbanana.base.ModelUser;

public class ActivityCard extends ActivityBase {

	String mStrHasCreditCardResponse = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AsyctaskHasCreditCard mAthccHasCreditCardtask = new AsyctaskHasCreditCard(
				this);
		mAthccHasCreditCardtask.execute();
	}

	class AsyctaskHasCreditCard extends AsyncTask<Void, Void, Void> {
		ProgressDialog mPdDialog;
		boolean mBoolWasInternetPresentDuringDoInBackground = false;
		WeakReference<Context> mWrContext;

		public AsyctaskHasCreditCard(Context context) {
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
					mNvp.add(new BasicNameValuePair("user_id", AppBase
							.getmMuUser().getmStrId()));

					String mStsResponse = Utils.postData(AppBase.mStrBaseUrl
							+ "hascreditcard", mNvp);

					HomerLogger.d("hascreditcard response ==" + mStsResponse);

					parseHasCreditCardResponse(mStsResponse);

					mStrHasCreditCardResponse = mStsResponse;

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

				if (mBoolWasInternetPresentDuringDoInBackground) {
					if (hasCreditCard(mStrHasCreditCardResponse)) {
						navigateToActivity(ActivityCardDetails.class);
					} else {
						navigateToActivity(ActivityCardEdit.class);
					}
				} else {
					AlertBoxUtils
							.getAlertDialogBox(mWrContext.get(),
									"Please check your internet connection and try again. ")
							.show();
					;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			finish();
		}
	}

	public void parseHasCreditCardResponse(String mStsResponse) {
		AppBase.getmMuUser()
				.setCCDetailsFromhasCreditCradResponse(mStsResponse);
	}

	public Boolean hasCreditCard(String mStsResponse) {

		try {
			JSONObject jsonob = new JSONObject(mStrHasCreditCardResponse);

			if (jsonob.has("ERROR")) {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

}
