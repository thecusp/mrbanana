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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mrbanana.R;
import com.mrbanana.app.account.ActivityAccountActionList;
import com.mrbanana.app.nearby.ActivityNearby.AsyctaskMapView;
import com.mrbanana.app.nearby.ActivityNearby.AsyctaskNeedHelp;
import com.mrbanana.base.ActivityBase;
import com.mrbanana.base.AppBase;

public class ActivityBooked extends ActivityBase {
	TextView mTvCancel;
	private GoogleMap mMap;
	ArrayList<Worker> workerNearByArray;
	BitmapDescriptor home_icon;
	int mapInfo = 0;// 1 - plumber, 2- cleanner
	Worker mWoUser = new Worker();

	public String mStrNeedhelpIssue = "";
	public String mStrCancelResponse = "";
	private static AlertDialog alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ab_layout);
		home_icon = BitmapDescriptorFactory.fromResource(R.drawable.mark_red);
		findViewByIds();
		setOnClickListeners();
		addLocationMarkersToMap();
	}

	@SuppressLint("NewApi")
	private void findViewByIds() {

		mTvCancel = (TextView) findViewById(R.id.ab_lTvCancel);
		mMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.ab_lmap)).getMap();
		mMap.setMyLocationEnabled(true);
	}

	private void setOnClickListeners() {

		mTvCancel.setOnClickListener(this);

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
			mapInfo = 0;
		} else {
			super.onBackPressed();
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
				workerNearByArray = AppBase.getmArrLisWAvailTradesmen();
				mWoUser = AppBase.getmWUser();

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
				if (nearByPlacesLength > 0) {
					for (int counter = 0; counter < nearByPlacesLength; counter++) {
						objNearByPlaces = workerNearByArray.get(counter);
						objLatLong = new LatLng(
								Double.parseDouble(objNearByPlaces.getmStrLat()),
								Double.parseDouble(objNearByPlaces.getmStrLng()));
						if (counter == 0) {
							mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
									objLatLong, 13));
						}
						if (AppBase.getmStrTradesmanType().equals(
								AppBase.mStrValueTradesmantypeCleaner)) {
							mMap.addMarker(new MarkerOptions()
									.title(objNearByPlaces.getmStrtitle())
									.snippet(
											objNearByPlaces
													.getmStrWorker_type())
									.position(objLatLong)
									.icon(BitmapDescriptorFactory
											.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
						} else {
							mMap.addMarker(new MarkerOptions()
									.title(objNearByPlaces.getmStrtitle())
									.snippet(
											objNearByPlaces
													.getmStrWorker_type())
									.position(objLatLong)
									.icon(BitmapDescriptorFactory
											.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
						}
					}
				} else {
					AlertBoxUtils
							.getAlertDialogBox(this,
									"Sorry could not find any tardesman in the category you were looking for .")
							.show();
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

		if (v == mTvCancel) {
			AsyctaskCancel mAtcCanceltask = new AsyctaskCancel(this);
			mAtcCanceltask.execute();
		}
	}

	class AsyctaskCancel extends AsyncTask<Void, Void, Void> {
		ProgressDialog mPdDialog;
		boolean mBoolWasInternetPresentDuringDoInBackground = false;
		WeakReference<Context> mWrContext;

		public AsyctaskCancel(Context context) {
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

					mNvp.add(new BasicNameValuePair("booking_id", AppBase
							.getmStrBookingId()));
					String mStsResponse = Utils.postData(AppBase.mStrBaseUrl
							+ "cancelbooking", mNvp);

					HomerLogger.d("cancelbooking response ==" + mStsResponse);
					mStrCancelResponse = mStsResponse;
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

					if (checkCancelWasSuccessfullyExecuted()) {
						finish();
						;
					} else {

						AlertBoxUtils
								.getAlertDialogBox(
										mWrContext.get(),
										AppBase.retriveMsgsfromResponse(mStrCancelResponse))
								.show();
					}

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

	private Boolean checkCancelWasSuccessfullyExecuted() {

		try {
			JSONObject jsonob = new JSONObject(mStrCancelResponse);

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
