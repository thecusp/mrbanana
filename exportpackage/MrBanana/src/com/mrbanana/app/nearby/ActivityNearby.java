package com.mrbanana.app.nearby;

import homemade.apps.framework.homerlibs.utils.AlertBoxUtils;
import homemade.apps.framework.homerlibs.utils.HomerLogger;
import homemade.apps.framework.homerlibs.utils.NetworkApis;
import homemade.apps.framework.homerlibs.utils.Utils;

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
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.mrbanana.base.ActivityBase;
import com.mrbanana.base.AppBase;

@SuppressLint("NewApi")
public class ActivityNearby extends ActivityBase implements OnClickListener {
	TextView mTvProfile, plumber_TxV, cleaner_TxV, pro_TxV, champion_Txv;
	LinearLayout sub_bottomLay;
	private GoogleMap mMap;
	ArrayList<Worker> workerNearByArray;
	BitmapDescriptor home_icon;
	int mapInfo = 0;// 1 - plumber, 2- cleanner

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

		mTvProfile = (TextView) findViewById(R.id.anb_TvProfile);
		plumber_TxV = (TextView) findViewById(R.id.anb_TvPlumber);
		cleaner_TxV = (TextView) findViewById(R.id.anb_TvCleaner);
		pro_TxV = (TextView) findViewById(R.id.anb_TvPro);
		champion_Txv = (TextView) findViewById(R.id.anb_TvCha);
		sub_bottomLay = (LinearLayout) findViewById(R.id.anb_LlBottomBarWrapper_sub);
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		mMap.setMyLocationEnabled(true);
	}

	private void setOnClickListeners() {
		mTvProfile.setOnClickListener(this);
		plumber_TxV.setOnClickListener(this);
		cleaner_TxV.setOnClickListener(this);
		pro_TxV.setOnClickListener(this);
		champion_Txv.setOnClickListener(this);
		mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				System.out.println("Title:"+marker.getTitle());
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
			super.onBackPressed();
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
					AlertBoxUtils.getAlertDialogBox(mWrContext.get(),"Please check your internet connection and try again. ").show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void getLatLongFromAddress(String youraddress) {
		
		double lng, lat;
	    String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
	                  youraddress + "&sensor=false";
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

	        lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
	            .getJSONObject("geometry").getJSONObject("location")
	            .getDouble("lng");

	        lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
	            .getJSONObject("geometry").getJSONObject("location")
	            .getDouble("lat");

	        Log.d("latitude", ""+lat);
	        Log.d("longitude", ""+lng);
	    } catch (JSONException e) {
	        e.printStackTrace();
	    }

	}
	
	private void addLocationMarkersToMap() {
		try {
			Worker objNearByPlaces;
			LatLng objLatLong;
			if (workerNearByArray != null) {
				int nearByPlacesLength = workerNearByArray.size();
				for (int counter = 0; counter < nearByPlacesLength; counter++) {
					objNearByPlaces = workerNearByArray.get(counter);
					objLatLong = new LatLng(Double.parseDouble(objNearByPlaces
							.getmStrLat()), Double.parseDouble(objNearByPlaces
							.getmStrLng()));
					if (counter == 0)
					{
						 mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(objLatLong, 13));
					}
					if(objNearByPlaces.getmStrWorker_type().equalsIgnoreCase(Worker.CLEANER_TYPE))
					{
						mMap.addMarker(new MarkerOptions().title(
								objNearByPlaces.getmStrtitle())
								.snippet(objNearByPlaces.getmStrWorker_type())
								.position(objLatLong)
								.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
					}
					else
					{
						mMap.addMarker(new MarkerOptions().title(
								objNearByPlaces.getmStrtitle())
								.snippet(objNearByPlaces.getmStrWorker_type())
								.position(objLatLong)
								.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
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
							if (workerJson.has("tradesman_type")) {
								worker.setmStrWorker_type(workerJson
										.getString("tradesman_type"));
							}
						}
						workerNearByArray.add(worker);
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
		if (v == plumber_TxV) {
			mapInfo = 1;
			sub_bottomLay.setVisibility(View.VISIBLE);
			pro_TxV.setText("Pro-Plumber");
			champion_Txv.setText("Champion-Plumber");
		}
		if (v == cleaner_TxV) {
			mapInfo = 2;
			sub_bottomLay.setVisibility(View.VISIBLE);
			pro_TxV.setText("Pro-Cleaner");
			champion_Txv.setText("Champion-Cleaner");
		}
		if (v == pro_TxV) {
			if (mapInfo == 1) {

			} else if (mapInfo == 2) {

			}
		}
		if (v == champion_Txv) {
			if (mapInfo == 1) {

			} else if (mapInfo == 2) {

			}
		}

	}

}
