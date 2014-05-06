package com.mrbanana.base;

import org.json.JSONException;
import org.json.JSONObject;

public class ModelJob {

	public static final String mStrKeyCleaningCount = "cleaning_count";
	public static final String mStrKeyPlumbingCount = "plumbing_count";
	public static final String mStrKeyCleaningList = "cleaning_list";
	public static final String mStrKeyPlumbingList = "plumbing_list";
	public static final String mStrKeyBookingId = "booking_id";
	public static final String mStrKeyFirstName = "first_name";
	public static final String mStrKeyLastname = "last_name";
	public static final String mStrKeyTradesmenType = "tradesman_type";
	public static final String mStrKeyTradesmenId = "tradesman_id";
	public static final String mStrKeyTradesmenLevel = "tradesman_level";
	public static final String mStrKeyPaymentStatus = "payment_status";
	public static final String mStrKeyJobStatus = "job_status";
	public static final String mStrKeyBookingTimeStamp = "booking_timestamp";
	public static final String mStrKeyAcceptedTimeStamp = "accepted_timestamp";

	String mStrBookingId = "";
	String mStrFirstName = "";
	String mStrLastName = "";
	String mStrTradesmanType = "";
	String mStrTradesmanLevel = "";
	String mStrTradesmanId = "";
	String mStrPaymentStatus = "";
	String mStrJobStatus = "";
	String mStrBookingTimeStamp = "";
	String mStrAcceptedTimeStamp = "";

	String mStrJsonObject = "";

	public String getmStrBookingId() {
		return mStrBookingId;
	}

	public void setmStrBookingId(String mStrBookingId) {
		this.mStrBookingId = mStrBookingId;
	}

	public String getmStrFirstName() {
		return mStrFirstName;
	}

	public void setmStrFirstName(String mStrFirstName) {
		this.mStrFirstName = mStrFirstName;
	}

	public String getmStrLastName() {
		return mStrLastName;
	}

	public void setmStrLastName(String mStrLastName) {
		this.mStrLastName = mStrLastName;
	}

	public String getmStrTradesmanType() {
		return mStrTradesmanType;
	}

	public void setmStrTradesmanType(String mStrTradesmanType) {
		this.mStrTradesmanType = mStrTradesmanType;
	}

	public String getmStrTradesmanLevel() {
		return mStrTradesmanLevel;
	}

	public void setmStrTradesmanLevel(String mStrTradesmanLevel) {
		this.mStrTradesmanLevel = mStrTradesmanLevel;
	}

	public String getmStrTradesmanId() {
		return mStrTradesmanId;
	}

	public void setmStrTradesmanId(String mStrTradesmanId) {
		this.mStrTradesmanId = mStrTradesmanId;
	}

	public String getmStrPaymentStatus() {
		return mStrPaymentStatus;
	}

	public void setmStrPaymentStatus(String mStrPaymentStatus) {
		this.mStrPaymentStatus = mStrPaymentStatus;
	}

	public String getmStrJobStatus() {
		return mStrJobStatus;
	}

	public void setmStrJobStatus(String mStrJobStatus) {
		this.mStrJobStatus = mStrJobStatus;
	}

	public String getmStrBookingTimeStamp() {
		return mStrBookingTimeStamp;
	}

	public void setmStrBookingTimeStamp(String mStrBookingTimeStamp) {
		this.mStrBookingTimeStamp = mStrBookingTimeStamp;
	}

	public String getmStrAcceptedTimeStamp() {
		return mStrAcceptedTimeStamp;
	}

	public void setmStrAcceptedTimeStamp(String mStrAcceptedTimeStamp) {
		this.mStrAcceptedTimeStamp = mStrAcceptedTimeStamp;
	}

	public String getmStrJsonObject() {
		return mStrJsonObject;
	}

	public void setmStrJsonObject(String mStrJsonObject) {
		this.mStrJsonObject = mStrJsonObject;
	}

	public static String getMstrkeycleaningcount() {
		return mStrKeyCleaningCount;
	}

	public static String getMstrkeyplumbingcount() {
		return mStrKeyPlumbingCount;
	}

	public static String getMstrkeycleaninglist() {
		return mStrKeyCleaningList;
	}

	public static String getMstrkeyplumbinglist() {
		return mStrKeyPlumbingList;
	}

	public static String getMstrkeybookingid() {
		return mStrKeyBookingId;
	}

	public static String getMstrkeyfirstname() {
		return mStrKeyFirstName;
	}

	public static String getMstrkeylastname() {
		return mStrKeyLastname;
	}

	public static String getMstrkeytradesmentype() {
		return mStrKeyTradesmenType;
	}

	public static String getMstrkeytradesmenid() {
		return mStrKeyTradesmenId;
	}

	public static String getMstrkeytradesmenlevel() {
		return mStrKeyTradesmenLevel;
	}

	public static String getMstrkeypaymentstatus() {
		return mStrKeyPaymentStatus;
	}

	public static String getMstrkeyjobstatus() {
		return mStrKeyJobStatus;
	}

	public static String getMstrkeybookingtimestamp() {
		return mStrKeyBookingTimeStamp;
	}

	public static String getMstrkeyacceptedtimestamp() {
		return mStrKeyAcceptedTimeStamp;
	}

	public void fromJsonString(String mStrJson) {

		try {
			JSONObject json = new JSONObject(mStrJson);
			fromJsonOb(json);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void fromJsonOb(JSONObject json) {

		try {
			setmStrBookingId(json.getString(mStrKeyBookingId));
			setmStrFirstName(json.getString(mStrKeyFirstName));
			setmStrLastName(json.getString(mStrKeyLastname));
			setmStrTradesmanType(json.getString(mStrKeyTradesmenType));
			setmStrTradesmanLevel(json.getString(mStrKeyTradesmenLevel));
			setmStrTradesmanId(json.getString(mStrKeyTradesmenId));
			setmStrPaymentStatus(json.getString(mStrKeyPaymentStatus));
			setmStrJobStatus(json.getString(mStrKeyJobStatus));
			setmStrBookingTimeStamp(json.getString(mStrKeyBookingTimeStamp));
			setmStrAcceptedTimeStamp(json.getString(mStrKeyAcceptedTimeStamp));

			setmStrJsonObject(json.toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
