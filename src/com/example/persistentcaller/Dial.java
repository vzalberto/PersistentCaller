package com.example.persistentcaller;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class Dial extends Activity {
	TextView info;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dial);
		PhoneCallListener phoneListener = new PhoneCallListener();
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
		call();
	}
	
	private void call(){
		try{
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:030"));
			startActivity(callIntent);	
		}catch(ActivityNotFoundException e){
			Log.e("PersistentCaller", "Call failed", e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dial, menu);
		return true;
	}
	
	private class PhoneCallListener extends PhoneStateListener{
		
		private static final String TAG = "PhoneCallListener";
		
		@Override
		public void onCallStateChanged(int state, String incomingNumber){
			boolean hasPhoneCalled = false;
			Log.i(TAG, "Call forwarding indicator has changed.");
			
			//do stuff
			switch(state){
			case TelephonyManager.CALL_STATE_RINGING:
				Log.i(TAG, "Phone is ringing. Number: " + incomingNumber);
				break;
				
			case TelephonyManager.CALL_STATE_OFFHOOK:
				Log.i(TAG, "Phone is offhook");
				hasPhoneCalled = true;
				break;
				
			case TelephonyManager.CALL_STATE_IDLE:
				Log.i(TAG, "Phone is idle");
				
				if(hasPhoneCalled){
					Log.i(TAG, "Restarting activity...");
					Intent i = getBaseContext().getPackageManager()
							.getLaunchIntentForPackage(
								getBaseContext().getPackageName());
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
					
					hasPhoneCalled = false;
				}
				break;
			}
		}
		
		public void onCallForwardingIndicatorChanged(boolean cfi){
			//restart activity
		}
	}

}