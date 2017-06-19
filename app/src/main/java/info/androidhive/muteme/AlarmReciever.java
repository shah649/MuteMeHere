package info.androidhive.muteme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReciever extends BroadcastReceiver {

	private final String SOMEACTION = "com.finalproject.alarm.ACTION";
	@Override
	public void onReceive(Context context, Intent intent)
	{
		String action = intent.getAction();
		if (SOMEACTION.equals(action)) {
			
			Intent intent1 = new Intent(context, IntentServices.class);
			context.startService(intent1);
			
		}
	}
}
