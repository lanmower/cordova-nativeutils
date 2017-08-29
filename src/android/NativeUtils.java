package org.flybuy.nativeutils;

import org.apache.cordova.*;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

public class NativeUtils extends CordovaPlugin{
	private static final String TAG = "NativeUtils";
	
	public static final String ACTION_GPS_STATE = "getGPSState";
	public static final String ACTION_NAV_TO_GPS_SETTINGS = "navToGPSSettings";
	public static final String ACTION_REQUIRE_GPS = "requireGPS";
	
	private Boolean dialog = false;
	private final String DIALOG_TEXT = "GPS is Disabled. Enable It To Continue.";
	private final String DIALOG_POS_TEXT = "Enable GPS";
	private final String DIALOG_NEG_TEXT = "Cancel";
	
	LocationManager locationManager;
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.cordova.CordovaPlugin#execute(java.lang.String, org.json.JSONArray, org.apache.cordova.CallbackContext)
	 * Args: 
	 * 0: Dialog: Show a Dialog if Disabled (Boolean)
	 */
	public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
		Log.e(TAG, "Executing Action: " + action + " With Data: " + data);
		
		Boolean result = false;
		this.dialog = data.getBoolean(0);
		
		Activity myActivity = this.cordova.getActivity();
		
		if(ACTION_GPS_STATE.equalsIgnoreCase(action)) {
            Log.e(TAG, "Getting GPS State");
			locationManager = (LocationManager) myActivity.getSystemService(Context.LOCATION_SERVICE);
			
			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.e(TAG, "GPS ENABLED");
	            //Toast.makeText(myActivity, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
	            
	            callbackContext.success("Enabled");
	            result = true;
	        } else {
                 callbackContext.success("Disabled");
                
                 Log.e(TAG, "GPS DISABLED");
                 
	        	//Toast.makeText(myActivity, "GPS is Not Enabled on your device", Toast.LENGTH_SHORT).show();
	        	
	        	if(this.dialog) {
	        		showGPSDisabledAlertToUser();
	        	}
	            
	        	result = true;
	        }
		}
		
		return result;
	}
	private void showGPSDisabledAlertToUser(){
		final Activity myActivity = this.cordova.getActivity();
		
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(myActivity);
        alertDialogBuilder.setMessage(this.DIALOG_TEXT)
        .setCancelable(false)
        .setPositiveButton(this.DIALOG_POS_TEXT,
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Intent callGPSSettingIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                myActivity.startActivity(callGPSSettingIntent);
            }
        });
        alertDialogBuilder.setNegativeButton(this.DIALOG_NEG_TEXT,
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
