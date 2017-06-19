package info.androidhive.muteme;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class IntentServices extends IntentService {

	public IntentServices() {
		super("IntentServices");
	}
	
	@Override
	public void onHandleIntent(Intent intent) {
		
		generateNotification(getApplicationContext(),"Mute Me Here");
	}
	
	@SuppressWarnings("deprecation")
	private void generateNotification(Context context, String message) {
		  int icon = R.drawable.ic_launcher;
		  long when = System.currentTimeMillis();
		  NotificationManager notificationManager = (NotificationManager) context
		    .getSystemService(Context.NOTIFICATION_SERVICE);
		  Notification notification = new Notification(icon, message, when);
		  String title =ServiceTest.eventName;
		  String subTitle = ServiceTest.eventSubTitle;

		  Intent notificationIntent = new Intent(context, MainActivity.class);
		  
		  notificationIntent.setAction("MyIntent");
		  
		  PendingIntent intent = PendingIntent.getActivity(context, 0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		  notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
		 
		  notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		  notification.setLatestEventInfo(context, title, subTitle, intent);
		  notification.defaults |= Notification.DEFAULT_SOUND;
		  notification.flags |= Notification.FLAG_AUTO_CANCEL;
		  notification.defaults |= Notification.DEFAULT_VIBRATE;
		  
		  notificationManager.notify(0, notification);
	}	
}
