package com.mrbanana.app.nearby;

import homemade.apps.framework.homerlibs.utils.AlertBoxUtils;
import homemade.apps.framework.homerlibs.utils.HomerLogger;
import homemade.apps.framework.homerlibs.utils.NetworkApis;
import homemade.apps.framework.homerlibs.utils.Utils;
import homemade.apps.framework.homerlibs.utils.Validation;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mrbanana.R;
import com.mrbanana.app.account.ActivityAccountActionList;
import com.mrbanana.app.needhelp.ActivityNeedHelp;
import com.mrbanana.base.ActivityBase;
import com.mrbanana.base.AppBase;

@SuppressLint("NewApi")
public class ActivityNearby extends ActivityBase implements OnClickListener {
	TextView mTvProfile, plumber_TxV, cleaner_TxV, pro_TxV, champion_Txv,
			mTvNeedHelp;
	LinearLayout sub_bottomLay;
	private GoogleMap mMap;
	ArrayList<Worker> workerNearByArray;
	BitmapDescriptor home_icon;
	int mapInfo = 0;// 1 - plumber, 2- cleanner
	Worker mWoUser = new Worker();

	public String mStrNeedhelpIssue = "";
	public String mStrNeedHelpResponse = "";

	public String mStrBookNowResponse = "";
	private static AlertDialog alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.anb_layout);
		home_icon = BitmapDescriptorFactory.fromResource(R.drawable.mark_red);
		findViewByIds();
		setOnClickListeners();
		AsyctaskMapView asyctaskMapView = new AsyctaskMapView(this);
		asyctaskMapView.execute();
	}

	@SuppressLint("NewApi")
	private void findViewByIds() {

		mTvNeedHelp = (TextView) findViewById(R.id.anb_TvHelp);
		mTvProfile = (TextView) findViewById(R.id.anb_TvProfile);
		plumber_TxV = (TextView) findViewById(R.id.anb_TvPlumber);
		cleaner_TxV = (TextView) findViewById(R.id.anb_TvCleaner);
		pro_TxV = (TextView) findViewById(R.id.anb_TvPro);
		champion_Txv = (TextView) findViewById(R.id.anb_TvCha);
		sub_bottomLay = (LinearLayout) findViewById(R.id.anb_LlBottomBarWrapper_sub);
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		mMap.setMyLocationEnabled(true);
	}

	private void setOnClickListeners() {

		mTvNeedHelp.setOnClickListener(this);
		mTvProfile.setOnClickListener(this);
		plumber_TxV.setOnClickListener(this);
		cleaner_TxV.setOnClickListener(this);
		pro_TxV.setOnClickListener(this);
		champion_Txv.setOnClickListener(this);
		mMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				System.out.println("Title:" + marker.getTitle());
				return false;
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (mapInfo != 0) {
			sub_bottomLay.setVisibility(View.GONE);
			mapInfo = 0;
		} else {
			AlertBoxUtils.getAlertDialogBoxForExit(this,
					"Are you sure you want to exit the app ?").show();
		}
	}

	class AsyctaskMapView extends AsyncTask<Void, Void, String> {
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
		protected String doInBackground(Void... params) {
			String mStsResponse = "";
			try {

				if (NetworkApis.isOnline()) {
					mBoolWasInternetPresentDuringDoInBackground = true;
					List<NameValuePair> mNvp = new ArrayList<NameValuePair>();
					mNvp.add(new BasicNameValuePair("user_id", AppBase
							.getmMuUser().getmStrId().trim()));
					mStsResponse = Utils.postData(AppBase.mStrBaseUrl
							+ "mapview", mNvp);
					HomerLogger.d("login response ==" + mStsResponse);
					// parseLoginResponse(mStsResponse);
					parseMapViewResponse(mStsResponse);

				} else {
					mBoolWasInternetPresentDuringDoInBackground = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mStsResponse;
		}

		@Override
		protected void onPostExecute(String response) {
			try {
				// parseMapViewResponse(response);
				mPdDialog.dismiss();
				addLocationMarkersToMap();
				if (!mBoolWasInternetPresentDuringDoInBackground) {
					AlertBoxUtils
							.getAlertDialogBox(mWrContext.get(),
									"Please check your internet connection and try again. ")
							.show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void getLatLongFromAddress(String youraddress) {

		double lng, lat;
		String uri = "http://maps.google.com/maps/api/geocode/json?address="
				+ youraddress + "&sensor=false";
		HttpGet httpGet = new HttpGet(uri);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());

			lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lng");

			lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lat");

			Log.d("latitude", "" + lat);
			Log.d("longitude", "" + lng);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void addLocationMarkersToMap() {
		try {
			try {
				// first plot user
				LatLng objLatLongUser = new LatLng(Double.parseDouble(mWoUser
						.getmStrLat()),
						Double.parseDouble(mWoUser.getmStrLng()));
				mMap.addMarker(new MarkerOptions()
						.title(mWoUser.getmStrtitle())
						.snippet(mWoUser.getmStrWorker_type())
						.position(objLatLongUser)
						.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Worker objNearByPlaces;
			LatLng objLatLong;

			if (workerNearByArray != null) {
				int nearByPlacesLength = workerNearByArray.size();
				for (int counter = 0; counter < nearByPlacesLength; counter++) {
					objNearByPlaces = workerNearByArray.get(counter);
					objLatLong = new LatLng(Double.parseDouble(objNearByPlaces
							.getmStrLat()), Double.parseDouble(objNearByPlaces
							.getmStrLng()));
					if (counter == 0) {
						mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
								objLatLong, 13));
					}
					if (objNearByPlaces.getmStrWorker_type().equalsIgnoreCase(
							Worker.CLEANER_TYPE)) {
						mMap.addMarker(new MarkerOptions()
								.title(objNearByPlaces.getmStrtitle())
								.snippet(objNearByPlaces.getmStrWorker_type())
								.position(objLatLong)
								.icon(BitmapDescriptorFactory
										.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
					} else {
						mMap.addMarker(new MarkerOptions()
								.title(objNearByPlaces.getmStrtitle())
								.snippet(objNearByPlaces.getmStrWorker_type())
								.position(objLatLong)
								.icon(BitmapDescriptorFactory
										.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
					}
				}
				mMap.setInfoWindowAdapter(new InfoWindowAdapter() {

					@Override
					public View getInfoWindow(Marker arg0) {
						return null;
					}

					@Override
					public View getInfoContents(Marker marker) {
						View v = getLayoutInflater().inflate(
								R.layout.google_map_marker, null);
						TextView infoTitle = (TextView) v
								.findViewById(R.id.infoTitle);
						TextView infoDesc = (TextView) v
								.findViewById(R.id.infoDesc);
						infoTitle.setText(marker.getTitle().toString());
						infoDesc.setText(marker.getSnippet().toString());
						return v;
					}
				});
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void parseMapViewResponse(String response) {
		try {

			JSONObject json = new JSONObject(response);
			workerNearByArray = new ArrayList<Worker>();
			JSONObject jsonSucess, workerJson;
			JSONArray workerJArray;

			if (json.has("SUCCESS")) {
				jsonSucess = json.getJSONObject("SUCCESS");
				if (jsonSucess.has("worker_coordinates")) {
					String mStrJsonSucces = jsonSucess
							.getString("worker_coordinates");

					workerJArray = new JSONArray(mStrJsonSucces);
					for (int i = 0; i < workerJArray.length(); i++) {
						Worker worker = new Worker();
						workerJson = workerJArray.getJSONObject(i);
						if (workerJson.has("lat") && workerJson.has("lng")) {
							worker.setmStrLat(workerJson.getString("lat"));
							worker.setmStrLng(workerJson.getString("lng"));
							if (workerJson.has("title")) {
								worker.setmStrtitle(workerJson
										.getString("title"));
							}
							if (workerJson.has("description")) {
								worker.setmStrDescription(workerJson
										.getString("description"));
							}
							if (workerJson.has("marker_img_path")) {
								worker.setmStrImg_Path(workerJson
										.getString("marker_img_path"));
							}
							if (workerJson.has("tradesman_type")) {
								worker.setmStrWorker_type(workerJson
										.getString("tradesman_type"));
							}
						}
						workerNearByArray.add(worker);
					}
				}

				try {
					if (jsonSucess.has("user_coordinates")) {
						JSONObject mJsonObUserCord1 = jsonSucess
								.getJSONObject("user_coordinates");
						JSONObject mJsonObUserCord2 = mJsonObUserCord1
								.getJSONObject("user_coordinates");

						mWoUser.setmStrtitle(mJsonObUserCord2
								.getString("title"));
						mWoUser.setmStrImg_Path(mJsonObUserCord2
								.getString("marker_img_path"));
						mWoUser.setmStrLat(mJsonObUserCord2.getString("lat"));
						mWoUser.setmStrLng(mJsonObUserCord2.getString("lng"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {

		if (v == mTvNeedHelp) {
			// navigateToActivity(ActivityNeedHelp.class);
			getAlertDialogBoxForCommentBoxNeedHelp(this).show();
		}

		if (v == mTvProfile) {
			navigateToActivity(ActivityAccountActionList.class);
		}
		if (v == plumber_TxV) {

			AppBase.setmStrTradesmanType(AppBase.mStrValueTradesmantypePlumber);
			mapInfo = 1;
			sub_bottomLay.setVisibility(View.VISIBLE);
			pro_TxV.setText("Pro-Plumber");

			champion_Txv.setText("Champion-Plumber");
			champion_Txv
					.setBackgroundResource(R.drawable.selector_anb_btn_green);
			pro_TxV.setBackgroundResource(R.drawable.selector_anb_btn_green);
		}
		if (v == cleaner_TxV) {

			AppBase.setmStrTradesmanType(AppBase.mStrValueTradesmantypeCleaner);
			mapInfo = 2;
			sub_bottomLay.setVisibility(View.VISIBLE);
			pro_TxV.setText("Pro-Cleaner");
			champion_Txv.setText("Champion-Cleaner");

			champion_Txv
					.setBackgroundResource(R.drawable.selector_anb_headerbtn);
			pro_TxV.setBackgroundResource(R.drawable.selector_anb_headerbtn);
		}
		if (v == pro_TxV) {
			// if (mapInfo == 1) {
			//
			// } else if (mapInfo == 2) {
			//
			// }
			AppBase.setmStrTradesmanLevel(AppBase.mStrValueTradesmantypeLevelpro);
			// AlertBoxUtils.getAlertDialogBox(this, "book a pro").show();

			AsyctaskBookNow mAtbnBookNowTask = new AsyctaskBookNow(this);
			mAtbnBookNowTask.execute();
		}
		if (v == champion_Txv) {
			// if (mapInfo == 1) {
			//
			// } else if (mapInfo == 2) {
			//
			// }
			AppBase.setmStrTradesmanLevel(AppBase.mStrValueTradesmantypeLevelchanp);
			// AlertBoxUtils.getAlertDialogBox(this, "book a championp").show();
			AsyctaskBookNow mAtbnBookNowTask = new AsyctaskBookNow(this);
			mAtbnBookNowTask.execute();

		}

	}

	public AlertDialog getAlertDialogBoxForCommentBoxNeedHelp(Activity context) {
		final AsyctaskNeedHelp mAtnhNeedHelpTask = new AsyctaskNeedHelp(this);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Comment Box");
		final EditText et = new EditText(context);
		et.setHint("Enter Text Here .");
		et.setHeight(Utils.getDipValuefor(this, 200));
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

	class AsyctaskBookNow extends AsyncTask<Void, Void, String> {
		ProgressDialog mPdDialog;
		boolean mBoolWasInternetPresentDuringDoInBackground = false;
		WeakReference<Context> mWrContext;

		public AsyctaskBookNow(Context context) {
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
		protected String doInBackground(Void... params) {
			String mStsResponse = "";
			try {

				if (NetworkApis.isOnline()) {
					mBoolWasInternetPresentDuringDoInBackground = true;
					List<NameValuePair> mNvp = new ArrayList<NameValuePair>();

					mNvp.add(new BasicNameValuePair("user_id", AppBase
							.getmMuUser().getmStrId().trim()));

					mNvp.add(new BasicNameValuePair("tradesman_type", AppBase
							.getmStrTradesmanType()));

					mNvp.add(new BasicNameValuePair("tradesman_level", AppBase
							.getmStrTradesmanLevel()));

					mStsResponse = Utils.postData(AppBase.mStrBaseUrl
							+ "booknow", mNvp);
					HomerLogger.d("booknow response ==" + mStsResponse);
					// parseLoginResponse(mStsResponse);

					mStrBookNowResponse = mStsResponse;
					parseBookNowResponse();
				} else {
					mBoolWasInternetPresentDuringDoInBackground = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mStsResponse;
		}

		@Override
		protected void onPostExecute(String response) {
			try {
				// parseMapViewResponse(response);
				mPdDialog.dismiss();

				if (!mBoolWasInternetPresentDuringDoInBackground) {
					AlertBoxUtils
							.getAlertDialogBox(mWrContext.get(),
									"Please check your internet connection and try again. ")
							.show();
				} else {
					if (checkBookNowWasSuccessfullyExecuted()) {
						navigateToActivity(ActivityBooked.class);
					} else {
						AlertBoxUtils
								.getAlertDialogBox(
										mWrContext.get(),
										AppBase.retriveMsgsfromResponse(mStrBookNowResponse))
								.show();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Boolean checkBookNowWasSuccessfullyExecuted() {

		try {
			JSONObject jsonob = new JSONObject(mStrBookNowResponse);

			if (jsonob.has("ERROR")) {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	public void parseBookNowResponse() {
		try {

			JSONObject json = new JSONObject(mStrBookNowResponse);
			workerNearByArray = new ArrayList<Worker>();
			JSONObject jsonSucess, workerJson;
			JSONArray workerJArray;

			if (json.has("SUCCESS")) {
				jsonSucess = json.getJSONObject("SUCCESS");

				AppBase.setmStrBookingId(jsonSucess.getString("booking_id"));
				if (jsonSucess.has("available_tradesman")) {
					String mStrJsonSucces = jsonSucess
							.getString("available_tradesman");

					workerJArray = new JSONArray(mStrJsonSucces);
					for (int i = 0; i < workerJArray.length(); i++) {
						Worker worker = new Worker();
						workerJson = workerJArray.getJSONObject(i);
						if (workerJson.has("lat") && workerJson.has("lng")) {
							worker.setmStrLat(workerJson.getString("lat"));
							worker.setmStrLng(workerJson.getString("lng"));
							if (workerJson.has("title")) {
								worker.setmStrtitle(workerJson
										.getString("title"));
							}
							if (workerJson.has("description")) {
								worker.setmStrDescription(workerJson
										.getString("description"));
							}
							if (workerJson.has("marker_img_path")) {
								worker.setmStrImg_Path(workerJson
										.getString("marker_img_path"));
							}
							if (workerJson.has("tradesman_type")) {
								worker.setmStrWorker_type(workerJson
										.getString("tradesman_type"));
							}
						}
						AppBase.getmArrLisWAvailTradesmen().add(worker);
					}
				}

				try {
					// if (jsonSucess.has("user_coordinates")) {
					// JSONObject mJsonObUserCord1 = jsonSucess
					// .getJSONObject("user_coordinates");
					// JSONObject mJsonObUserCord2 = mJsonObUserCord1
					// .getJSONObject("user_coordinates");
					//
					// mWoUser.setmStrtitle(mJsonObUserCord2
					// .getString("title"));
					// mWoUser.setmStrImg_Path(mJsonObUserCord2
					// .getString("marker_img_path"));
					// mWoUser.setmStrLat(mJsonObUserCord2.getString("lat"));
					// mWoUser.setmStrLng(mJsonObUserCord2.getString("lng"));
					// }

					AppBase.setmWUser(mWoUser);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
