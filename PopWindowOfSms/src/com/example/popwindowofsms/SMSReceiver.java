package com.example.popwindowofsms;

import java.io.UnsupportedEncodingException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
	byte[] messageBytes;
	String address;
	public SMSReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO: This method is called when the BroadcastReceiver is receiving
		// an Intent broadcast.
		//throw new UnsupportedOperationException("Not yet implemented");
		if("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction()))
		{
			StringBuilder sb = new StringBuilder();
			Bundle bundle = intent.getExtras();
			if(bundle != null)
			{
				Object[] objArray = (Object[])bundle.get("pdus");
				SmsMessage[] Sms = new SmsMessage[objArray.length];
				for(int i=0; i<objArray.length; i++)
				{
					Sms[i] = SmsMessage.createFromPdu((byte[]) objArray[i]);
				}
				for(SmsMessage currentMessage : Sms)
				{
					sb.append("ю╢вт:");
					sb.append(currentMessage.getDisplayOriginatingAddress());
					address = currentMessage.getDisplayOriginatingAddress();
					sb.append("\n");
					sb.append(currentMessage.getDisplayMessageBody());
				}
			}
			Intent mainIntent = new Intent(context, PopWindowActivity.class);
			mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(mainIntent);
			messageBytes = sb.toString().getBytes();
			/*try {
				String message = new String(bytes, "utf-8");
				Toast.makeText(context, message, Toast.LENGTH_LONG).show();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
		}
		
	}
	public byte[] getMessage(){
		return messageBytes;
	}
	public String getAddress(){
		return address;
	}
}
