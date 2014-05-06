package com.mrbanana.base;

import homemade.apps.framework.homerlibs.utils.sharedpref.SharedPrefrenceHelper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.mrbanana.app.nearby.Worker;

public class AppBase {

	// Constants

	public final static String mStrPrefNameBase = "MrBananaPref";

	public final static String mStrPrefKeyTourHasBeenShown = "tourHasBeenShown";

	public final static String mStrPrefKeyUserIsLoggedIn = "userIsLoggedIn";

	public final static String mStrPrefKeyLoginResponse = "LoginResponse";

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

	private static Boolean mBoolUserIsLoggedIn = null;

	/**
	 * getter function for mBoolTourHasBeenShown
	 * 
	 * @param context
	 * @return
	 */
	public static Boolean getmBoolUserIsLoggedIn(Context context) {

		if (mBoolUserIsLoggedIn == null)
			mBoolUserIsLoggedIn = SharedPrefrenceHelper.getPref(context,
					mStrPrefNameBase).getBoolean(mStrPrefKeyUserIsLoggedIn,
					false);

		return mBoolUserIsLoggedIn;
	}

	/**
	 * setter function for mBoolTourHasBeenShown note: it also updates the new
	 * value in the shared pref.
	 * 
	 * @param mBoolTourHasBeenShown
	 * @param context
	 */
	public static void setmBoolUserIsLoggedIn(Boolean mBoolUserIsLoggedIn,
			Context context) {
		// / update the value in shared prefence also ....
		SharedPrefrenceHelper.getPref(context, mStrPrefNameBase).edit()
				.putBoolean(mStrPrefKeyUserIsLoggedIn, mBoolUserIsLoggedIn)
				.commit();

		// setting the value of the req boolean to null so that when we call its
		// getter it
		// refetches the value from pref which has been changed in the above
		// line
		AppBase.mBoolUserIsLoggedIn = null;

		getmBoolUserIsLoggedIn(context);
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

	public static String retriveMsgsfromResponse(String str) {
		String result = "";
		try {
			JSONObject json = new JSONObject(str);

			if (json.has("ERROR")) {

				result = json.getString("ERROR");

			} else if (json.has("SUCCESS")) {

				result = json.getString("SUCCESS");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static ArrayList<ModelJob> getmArrLisOfMjJobListPlumberJobs() {
		return mArrLisOfMjJobListPlumberJobs;
	}

	public static void setmArrLisOfMjJobListPlumberJobs(
			ArrayList<ModelJob> mArrLisOfMjJobListPlumberJobs) {
		AppBase.mArrLisOfMjJobListPlumberJobs = mArrLisOfMjJobListPlumberJobs;
	}

	public static ArrayList<ModelJob> getmArrLisOfMjJobListCleanerJobs() {
		return mArrLisOfMjJobListCleanerJobs;
	}

	public static void setmArrLisOfMjJobListCleanerJobs(
			ArrayList<ModelJob> mArrLisOfMjJobListCleanerJobs) {
		AppBase.mArrLisOfMjJobListCleanerJobs = mArrLisOfMjJobListCleanerJobs;
	}

	private static ArrayList<ModelJob> mArrLisOfMjJobListPlumberJobs = new ArrayList<ModelJob>();
	private static ArrayList<ModelJob> mArrLisOfMjJobListCleanerJobs = new ArrayList<ModelJob>();

	private static String mStrTradesmanType = "";
	private static String mStrTradesmanLevel = "";

	public static final String mStrValueTradesmantypeCleaner = "cleaner";
	public static final String mStrValueTradesmantypePlumber = "plumber";

	public static final String mStrValueTradesmantypeLevelchanp = "champion";
	public static final String mStrValueTradesmantypeLevelpro = "pro";

	public static String getmStrTradesmanType() {
		return mStrTradesmanType;
	}

	public static void setmStrTradesmanType(String mStrTradesmanType) {
		AppBase.mStrTradesmanType = mStrTradesmanType;
	}

	public static String getmStrTradesmanLevel() {
		return mStrTradesmanLevel;
	}

	public static void setmStrTradesmanLevel(String mStrTradesmanLevel) {
		AppBase.mStrTradesmanLevel = mStrTradesmanLevel;
	}

	public static String getmStrBookingId() {
		return mStrBookingId;
	}

	public static void setmStrBookingId(String mStrBookingId) {
		AppBase.mStrBookingId = mStrBookingId;
	}

	private static String mStrBookingId = "";

	private static ArrayList<Worker> mArrLisWAvailTradesmen = new ArrayList<Worker>();

	public static ArrayList<Worker> getmArrLisWAvailTradesmen() {
		return mArrLisWAvailTradesmen;
	}

	public static void setmArrLisWAvailTradesmen(
			ArrayList<Worker> mArrLisWAvailTradesmen) {
		AppBase.mArrLisWAvailTradesmen = mArrLisWAvailTradesmen;
	}

	private static Worker mWUser = new Worker();

	public static Worker getmWUser() {
		return mWUser;
	}

	public static void setmWUser(Worker mWUser) {
		AppBase.mWUser = mWUser;
	}

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";
	private static final String API_KEY = "AIzaSyCXH2zZy6zlDjWxIRjkE77ZDYj2ckBgcSQ";

	public static ArrayList<String> autocomplete(String input) {

		ArrayList<String> resultList = null;

		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE
					+ TYPE_AUTOCOMPLETE + OUT_JSON);
			sb.append("?sensor=false&key=" + API_KEY);
			// sb.append("&components=country:uk");
			sb.append("&input=" + URLEncoder.encode(input, "utf8"));

			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			// Load the results into a StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			Log.e("", "Error processing Places API URL", e);
			return resultList;
		} catch (IOException e) {
			Log.e("", "Error connecting to Places API", e);
			return resultList;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		try {
			// Create a JSON object hierarchy from the results
			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

			// Extract the Place descriptions from the results
			resultList = new ArrayList<String>(predsJsonArray.length());
			for (int i = 0; i < predsJsonArray.length(); i++) {
				resultList.add(predsJsonArray.getJSONObject(i).getString(
						"description"));
			}

		} catch (JSONException e) {
			Log.e("MainActivty", "Cannot process JSON results", e);
		}

		return resultList;
	}
}
