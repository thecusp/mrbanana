package com.mrbanana.app.needhelp;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import homemade.apps.framework.homerlibs.utils.AlertBoxUtils;
import homemade.apps.framework.homerlibs.utils.HomerLogger;
import homemade.apps.framework.homerlibs.utils.NetworkApis;
import homemade.apps.framework.homerlibs.utils.Utils;
import homemade.apps.framework.homerlibs.utils.Validation;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mrbanana.R;
import com.mrbanana.app.account.profile.ActivityProfile;
import com.mrbanana.app.clienttour.ActivityClientTour;
import com.mrbanana.app.nearby.ActivityNearby;
import com.mrbanana.app.registration_and_login.ActivityVerification;
import com.mrbanana.base.ActivityBase;
import com.mrbanana.base.AppBase;
import com.mrbanana.base.ModelUser;

public class ActivityNeedHelp extends ActivityBase {

	RelativeLayout mRlClientTour, mRlFeedback, mRlNeedHelp;
	TextView mTvBack;
	public String mStrLogoutResponse = "";

	public String mStrNeedhelpIssue = "";
	public String mStrSendFeedbackIssue = "";

	public String mStrNeedHelpResponse = "";
	public String mStrSendFeedbackResponse = "";

	private static AlertDialog alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.anh_layout);

		findViewByIds();
		setOnClickListeners();

	}

	private void findViewByIds() {

		mRlClientTour = (RelativeLayout) findViewById(R.id.anh_lLlClientTour);

		mRlFeedback = (RelativeLayout) findViewById(R.id.anh_lLlFeedback);

		mRlNeedHelp = (RelativeLayout) findViewById(R.id.anh_lLlNeedHelp);

		mTvBack = (TextView) findViewById(R.id.anh_lTvBack);

	}

	private void setOnClickListeners() {

		mRlClientTour.setOnClickListener(this);

		mRlFeedback.setOnClickListener(this);

		mRlNeedHelp.setOnClickListener(this);

		mTvBack.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		if (v == mRlClientTour) {
			navigateToActivity(ActivityClientTour.class);
		}
		if (v == mRlNeedHelp) {
			// navigateToActivity(ActivitySendFeedBack.class);
			getAlertDialogBoxForCommentBoxNeedHelp(this).show();

		}
		if (v == mRlFeedback) {
			getAlertDialogBoxForCommentBoxSendFeedback(this).show();
			// navigateToActivity(ActivitySendFeedBack.class);
		}

		if (v == mTvBack) {
			onBackPressed();
		}
	}

	public AlertDialog getAlertDialogBoxForCommentBoxNeedHelp(
			ActivityNeedHelp context) {
		final AsyctaskNeedHelp mAtnhNeedHelpTask = new AsyctaskNeedHelp(this);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Comment Box");
		final EditText et = new EditText(context);
		et.setHint("Enter Text Here .");

		builder.setView(et)
				.setPositiveButton("Submit",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(final DialogInterface dialog,
									int which) {
								if (Validation.hasText(et)) {
									dialog.dismiss();

									mStrNeedhelpIssue = et.getText().toString()
											.trim();
									mAtnhNeedHelpTask.execute();

								} else {
									et.setHint("Please enter text before proceeding ");
								}

							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();

							}
						});

		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {

			}

		});

		alert = builder.create();

		return alert;
	}

	class AsyctaskNeedHelp extends AsyncTask<Void, Void, Void> {
		ProgressDialog mPdDialog;
		boolean mBoolWasInternetPresentDuringDoInBackground = false;
		WeakReference<Context> mWrContext;

		public AsyctaskNeedHelp(Context context) {
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

					mNvp.add(new BasicNameValuePair("issue_message",
							mStrNeedhelpIssue));
					String mStsResponse = Utils.postData(AppBase.mStrBaseUrl
							+ "needhelp", mNvp);

					HomerLogger.d("needhelp response ==" + mStsResponse);
					mStrNeedHelpResponse = mStsResponse;
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

					// if (checkNeedHelpWasSuccessfullyExecuted()) {
					//
					// ;
					// } else {
					// AlertBoxUtils
					// .getAlertDialogBox(mWrContext.get(),
					// "Need Help could not be contacted for some reason .Try Again ")
					// .show();
					// ;
					// }

					AlertBoxUtils.getAlertDialogBox(mWrContext.get(),
							mStrNeedHelpResponse).show();
				} else {
					AlertBoxUtils
							.getAlertDialogBox(mWrContext.get(),
									"Please check your internet connection and try again. ")
							.show();
					;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private Boolean checkNeedHelpWasSuccessfullyExecuted() {

		try {
			JSONObject jsonob = new JSONObject(mStrNeedHelpResponse);

			if (jsonob.has("ERROR")) {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	public AlertDialog getAlertDialogBoxForCommentBoxSendFeedback(
			ActivityNeedHelp context) {
		final AsyctaskSendfeedback mAtnhSendFeedbackTask = new AsyctaskSendfeedback(
				this);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Comment Box");
		final EditText et = new EditText(context);
		et.setHint("Enter Text Here .");

		builder.setView(et)
				.setPositiveButton("Submit",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(final DialogInterface dialog,
									int which) {
								if (Validation.hasText(et)) {
									dialog.dismiss();

									mStrSendFeedbackIssue = et.getText()
											.toString().trim();
									mAtnhSendFeedbackTask.execute();

								} else {
									et.setHint("Please enter text before proceeding ");
								}

							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();

							}
						});

		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {

			}

		});

		alert = builder.create();

		return alert;
	}

	class AsyctaskSendfeedback extends AsyncTask<Void, Void, Void> {
		ProgressDialog mPdDialog;
		boolean mBoolWasInternetPresentDuringDoInBackground = false;
		WeakReference<Context> mWrContext;

		public AsyctaskSendfeedback(Context context) {
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

					mNvp.add(new BasicNameValuePair("issue_message",
							mStrSendFeedbackIssue));
					String mStsResponse = Utils.postData(AppBase.mStrBaseUrl
							+ "feedback", mNvp);

					HomerLogger.d("feedback response ==" + mStsResponse);
					mStrSendFeedbackResponse = mStsResponse;
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

					// if (checkSendFeedBackWasSuccessfullyExecuted()) {
					//
					// } else {
					// AlertBoxUtils
					// .getAlertDialogBox(mWrContext.get(),
					// "Send feedback could not be contacted for some reason .Try Again ")
					// .show();
					// ;
					// }

					AlertBoxUtils.getAlertDialogBox(mWrContext.get(),
							mStrSendFeedbackResponse).show();
				} else {
					AlertBoxUtils
							.getAlertDialogBox(mWrContext.get(),
									"Please check your internet connection and try again. ")
							.show();
					;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private Boolean checkSendFeedBackWasSuccessfullyExecuted() {

		try {
			JSONObject jsonob = new JSONObject(mStrSendFeedbackResponse);

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
