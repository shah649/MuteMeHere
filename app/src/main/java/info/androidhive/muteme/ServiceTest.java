package info.androidhive.muteme;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class ServiceTest extends Service implements LocationListener {

	private DatabaseHandler db;
	ArrayList<EventsActivity> lstEvents;
	PendingIntent pendingIntent;
	AlarmManager alarmManager;
	LocationManager locationManager;
	String provider;
	public static String eventName;
	public static String eventSubTitle;
	private ArrayList<ProfileActivity> lstProfile;	
	private LocationActivity loc;
	private AudioManager manager;
	private Timer mTimer;
	private int callVolumn;
	private int msgVolumn;
	private int callVolumnScale;
	private int msgVolumnScale;
	
	private boolean isGPSEnabled;
	private boolean isNetworkEnabled;
	Location location; // location

	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mTimer = new Timer();
		//// 60 sec
		mTimer.schedule(timerTask, 1000, 60 * 1000);

		
		Intent intentsOpen = new Intent(ServiceTest.this, AlarmReciever.class);
		intentsOpen.setAction("com.finalproject.alarm.ACTION");
		pendingIntent = PendingIntent.getBroadcast(this, 111, intentsOpen, 0);
		alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		
		manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);					
		callVolumnScale = (100/ manager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL));
		msgVolumnScale = (100/ manager.getStreamMaxVolume(AudioManager.STREAM_RING));					
		
		

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {

			/*Log.v("hue", "hye i am start");*/
		} catch (Exception e) {
			
			/*Log.v("Error onStartCommand", e.getMessage());*/
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	

	TimerTask timerTask = new TimerTask() {
		

		@Override
		public void run() {

			try {
				
				/*Log.e("Log", "Running");*/				
				db = new DatabaseHandler(ServiceTest.this);
				lstEvents = new ArrayList<EventsActivity>();
				lstEvents.clear();
				lstEvents = db.GetActiveEvents();

				Calendar cal = Calendar.getInstance();
				int hour = cal.get(cal.HOUR);
				int min = cal.get(cal.MINUTE);
				int status = cal.get(cal.AM_PM);
				String am_pm;

				if (status == 0) {
					am_pm = "AM";
				} else {
					am_pm = "PM";
				}

				if (hour == 0) {
					hour = 12;
				}

				for (int i = 0; i < lstEvents.size(); i++) {

					String dateTimeFromDb = lstEvents.get(i).getDate() + " "
							+ lstEvents.get(i).getTime();

					int date1 = cal.get(cal.DATE);
					int month = cal.get(cal.MONTH);
					int year = cal.get(cal.YEAR);

					String currentDateAndTime = date1 + "/" + (month + 1) + "/"
							+ year + " " + hour + ":" + min + " " + am_pm;

					if (dateTimeFromDb.equals(currentDateAndTime)) {
						Log.e("log", "i m here");
						eventName = lstEvents.get(i).getEventName();
						eventSubTitle = lstEvents.get(i).getDescription();
						alarmManager.set(AlarmManager.RTC_WAKEUP,
								cal.getTimeInMillis(), pendingIntent);
					}

				}

				/* //// Profile work */
				lstProfile = new ArrayList<ProfileActivity>();
				lstProfile.clear();
				lstProfile = db.GetActiveProfile();
				
				/* Log.e("log", "db.GetActiveProfile();"); */
				
				for (int i = 0; i < lstProfile.size(); i++) {
					
					/* Log.e("log", "lstProfile.size()"); */

					
				    callVolumn = lstProfile.get(i).getIsCallTune();
				    msgVolumn = lstProfile.get(i).getIsMessegeTune();

				    

					
					int type = lstProfile.get(i).getProfileType();
					/* Log.e("log", type+""); */
					
					if (type == 1) {
						
						/* time prof start */ 
						Log.e("log", "i m time prfile");
						
						try {
							
							String[] sTime = lstProfile.get(i).getStartTime()
									.split(":");
							String[] eTime = lstProfile.get(i).getEndTime()
									.split(":");

							int sHour = Integer.parseInt(sTime[0].toString());
							int sMin = Integer.parseInt(sTime[1].split(" ")[0].toString());

							int eHour = Integer.parseInt(eTime[0].toString());
							int eMin = Integer.parseInt(eTime[1].split(" ")[0].toString());

							if (hour >= sHour && hour <= eHour) {

								/* Log.e("log", "sHour" + sHour); */

								if (hour == sHour && hour == eHour) {

									if (min >= sMin && min <= eMin) {
										/* settting volumns  */
										setProfile();
										return;

									}

								} else if (hour == sHour) {
									if (min >= sMin) {
										/* settting volumns  */
										setProfile();
										return;
									}
								}

								else if (hour == eHour) {
									if (min <= eMin) {
										/* settting volumns  */
										setProfile();
										return;
									}
								} else {

									/* settting volumns */
									setProfile();

									eventName = " Time Profile Activated : " + hour
											+ " - " + min;
									eventSubTitle = "Service Running";
									alarmManager.set(AlarmManager.RTC_WAKEUP,
											cal.getTimeInMillis(), pendingIntent);
									
									return;

								}

							}	
							
							
							
							
						} catch (Exception e) {
							/* TODO: handle exception */
						}
						

					}
					else
					{


						try {
						
							locationManager = (LocationManager) ServiceTest.this.getSystemService(Context.LOCATION_SERVICE);
							
							loc = new LocationActivity();
							int locationID = lstProfile.get(i).getLocationID();
							float radius = lstProfile.get(i).getRadius();
							loc = db.getLocation(locationID);


						         
						        	
					 	           locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,ServiceTest.this);
					 	           
					 	      		
							              
							                    if (locationManager != null) {
							                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							                        if (location == null) {
							                        								    
							                        	//Log.e("Log", "GPSLocationis null");							                        	
										                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);										                   
										                }
									        		 
							                        }
							                    
							              
						        
						        
								if (location != null) {



									double lat = Double.parseDouble(loc.getLatitude());
									double lng = Double.parseDouble(loc.getLongitude());
									double distance = CoordDistance(location.getLatitude() , location.getLongitude() , lat , lng); 
									

									
									if(distance <= radius )
									{
										
										setProfile();
									}
								}

									
									
							

						}

						catch (Exception ex) {
							//Log.v("Log : Location profile Error", ex.getMessage());
						}
						
						
						
						
						
					// location profile end	
					}
					
					
				

				}
				
				Looper.prepare();

			} catch (Exception ex) {
				//Log.v("Log : Run Error", ex.getMessage());
			}

		}

	};
	
	
	double CoordDistance(double latitude1, double longitude1, double latitude2, double longitude2)
	{
		try
		{
		
		/*earth radius : 3958.75 Miles , 6371.0 KM  ,  6371000 meters */			
	    return 6371000 * Math.acos(
	        Math.sin(latitude1) * Math.sin(latitude2)
	        + Math.cos(latitude1) * Math.cos(latitude2) * Math.cos(longitude2 - longitude1));
		}
		catch(Exception ex)
		{
			return 999999999; 			
		}
		
		
	    
	}

	public void setProfile() {

		final Vibrator vibrator = (Vibrator) ServiceTest.this.getSystemService(Context.VIBRATOR_SERVICE);

		manager.setStreamVolume(AudioManager.STREAM_RING,  msgVolumn/msgVolumnScale, 0);
		manager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, callVolumn/callVolumnScale, 0);


		long pattern[] = { 0, 200, 100, 300, 400 };

		// start vibration with repeated count, use -1 if you
		// don't want to repeat the vibration
		vibrator.vibrate(pattern, -1);

	}

	public void onDestroy() {
		try {
			mTimer.cancel();
			timerTask.cancel();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}
}
