package com.mrbanana.base;

import homemade.apps.framework.homerlibs.utils.sharedpref.SharedPrefrenceHelper;
import android.content.Context;
import android.os.Handler;

public class AppBase {

	// Constants

	public final static String mStrPrefNameBase = "MrBananaPref";

	public final static String mStrPrefKeyTourHasBeenShown = "tourHasBeenShown";

	// variables

	/**
	 * mBoolTourHasBeenShown is a boolean variable that keeps track of whether
	 * client tour has been shown or not... it does this by keeping a refrence
	 * of it's value in shared prefrence
	 * 
	 * hence its getter and setter read and update the shared prefrence
	 * respectively if required.
	 */
	private static Boolean mBoolTourHasBeenShown;

	/**
	 * getter function for mBoolTourHasBeenShown
	 * 
	 * @param context
	 * @return
	 */
	public static Boolean getmBoolTourHasBeenShown(Context context) {

		if (mBoolTourHasBeenShown == null)
			mBoolTourHasBeenShown = SharedPrefrenceHelper.getPref(context,
					mStrPrefNameBase).getBoolean(mStrPrefKeyTourHasBeenShown,
					false);

		return mBoolTourHasBeenShown;
	}

	/**
	 * setter function for mBoolTourHasBeenShown note: it also updates the new
	 * value in the shared pref.
	 * 
	 * @param mBoolTourHasBeenShown
	 * @param context
	 */
	public static void setmBoolTourHasBeenShown(Boolean mBoolTourHasBeenShown,
			Context context) {
		// / update the value in shared prefence also ....
		SharedPrefrenceHelper.getPref(context, mStrPrefNameBase).edit()
				.putBoolean(mStrPrefKeyTourHasBeenShown, mBoolTourHasBeenShown)
				.commit();

		// setting the value of the req boolean to null so that when we call its
		// getter it
		// refetches the value from pref which has been changed in the above
		// line
		AppBase.mBoolTourHasBeenShown = null;

		getmBoolTourHasBeenShown(context);
	}

	private static ModelUser mMuUser = new ModelUser();

	public static ModelUser getmMuUser() {
		return mMuUser;
	}

	public static void setmMuUser(ModelUser mMuUserr) {
		mMuUser = mMuUserr;
	}

	public static final String mStrBaseUrl = "http://mrbananaapp.com/beta/androidfeed/";

	private static boolean mBoolCanResendSms = true;

	public static boolean getmBoolCanResendSms() {
		return mBoolCanResendSms;
	}

	public static void setmBoolCanResendSmsValueToFalse() {
		AppBase.mBoolCanResendSms = false;
		resetCanResendSmsValueToTrue();

	}

	public static void resetCanResendSmsValueToTrue() {

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				try {
					AppBase.mBoolCanResendSms = true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, 3000);
	}
}
