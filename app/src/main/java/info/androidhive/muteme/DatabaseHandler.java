package info.androidhive.muteme;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "MuteMeHere";
	
	
	private static final String Location = "Location";
	private static final String Events = "Events";
	private static final String ProfileType = "ProfileType";
	private static final String Profile = "Profile";
	private static final String RecentProfile = "RecentProfile";
	
	

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}


	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db)
	{

		db.execSQL("CREATE TABLE IF NOT EXISTS Location (LocationID INTEGER PRIMARY KEY,  LocationName  TEXT, 	LocationXCordinate  TEXT, LocationYCordinate TEXT  )");  

		db.execSQL("CREATE TABLE IF NOT EXISTS Events (EventID INTEGER PRIMARY KEY,  EventName  TEXT, 	Description  TEXT, Date TEXT , Time TEXT , IsActive INTEGER )");  

		db.execSQL("CREATE TABLE IF NOT EXISTS ProfileType (TypeID INTEGER PRIMARY KEY,  TypeName  TEXT  )");

		db.execSQL("CREATE TABLE IF NOT EXISTS Profile (ProfileID INTEGER PRIMARY KEY,  ProfileName  TEXT, 	ProfileType INTEGER, LocationID INTEGER , StartTime TEXT , EndTime TEXT , Radius INTEGER, IsCallTune INTEGER, Volumn INTEGER, IsVibrate INTEGER , IsActiveProfile INTEGER  , IsMessegeTune INTEGER  )");  

		db.execSQL("CREATE TABLE IF NOT EXISTS RecentProfile (RecentID INTEGER PRIMARY KEY,  ProfileID INTEGER  )");

	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS Location");
		db.execSQL("DROP TABLE IF EXISTS Events");
		db.execSQL("DROP TABLE IF EXISTS ProfileType");
		db.execSQL("DROP TABLE IF EXISTS Profile");
		db.execSQL("DROP TABLE IF EXISTS RecentProfile");

		onCreate(db);
	}

	// ***************** Select Max **************************
	public long selectMaxID(String table, String col) {
		SQLiteDatabase db = this.getReadableDatabase();
		long lastId = (long) 0;
		try {
			String query = "SELECT MAX(" + col + ") from " + table ;//+ " order by "+ col + " DESC limit 1";
			Cursor c = db.rawQuery(query, null);
			if (c != null && c.moveToFirst()) {
				lastId = c.getLong(0); // The 0 is the column index, we only
										// have 1 column, so the index is 0
			}
			c.close();
			//db.close();
		} catch (Exception e) {
			lastId = (long) 0;
		}
		
		return ++lastId;

	}
	
	// ***************** Check Exist **************************
	private boolean CHK_EXIT(String table, String col, String val) {
		SQLiteDatabase db = this.getReadableDatabase();
		boolean isExist = false;

		try {
			
			String selectQuery = "SELECT  * FROM " + table + " Where "+ col +" = '"+val+"' ";
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor != null && cursor.moveToFirst() ) {
								
				isExist = true;				
							
			}
			
			cursor.close();	
			db.close();
		} catch (Exception e) {

		}
				
		return isExist;
	}
	
	
	// ******************** Adding Event ********************
		public void addEvent(EventsActivity obj) {
			SQLiteDatabase db = this.getWritableDatabase();
			Long id = selectMaxID(Events, "EventID");
			ContentValues values = new ContentValues();
			values.put("EventID", id);		
			values.put("EventName", obj.getEventName());
			values.put("Description", obj.getDescription());
			values.put("Date", obj.getDate());
			values.put("Time", obj.getTime());
			values.put("IsActive", obj.getIsActive());
			
			
			db.insert(Events, null, values);
			db.close();

		}
		
		
		// ******************** Update Event ********************
		public void UpdateEvent(EventsActivity obj) {
			SQLiteDatabase db = this.getWritableDatabase();
			
			ContentValues values = new ContentValues();
					
			values.put("EventName", obj.getEventName());
			values.put("Description", obj.getDescription());
			values.put("Date", obj.getDate());
			values.put("Time", obj.getTime());
			values.put("IsActive", obj.getIsActive());
			
			db.update(Events, values, "EventID = ?",  new String[] { String.valueOf(obj.getEventID() ) });
			
			db.close();

		}

	

		// **************** CHK Event EXIT *******************	
		public boolean CheckEventEXIT(EventsActivity obj) {
			
			return CHK_EXIT(Events, "EventName", obj.getEventName());
				
		}


		// **************** Get All Event *******************
		public ArrayList<EventsActivity> VewAllEvents() {

			ArrayList<EventsActivity> lstobj = new ArrayList<EventsActivity>();

			try {

				String selectQuery = "SELECT * FROM " + Events;
				SQLiteDatabase db = this.getReadableDatabase();
				Cursor cursor = db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst()) {
					do {

						EventsActivity object = new EventsActivity();
						object.setEventID(cursor.getInt(cursor.getColumnIndex("EventID")));
						object.setEventName(cursor.getString(cursor.getColumnIndex("EventName")));
						object.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
						object.setDate(cursor.getString(cursor.getColumnIndex("Date")));
						object.setTime(cursor.getString(cursor.getColumnIndex("Time")));
						object.setIsActive(  cursor.getInt(cursor.getColumnIndex("IsActive") ) == 1 ? true : false  );
												
						lstobj.add(object);

					} while (cursor.moveToNext());
				}

				cursor.close();
				db.close();

			} catch (Exception e) {
			}

			return lstobj;

		}
		
		
		// **************** Get Active Event *******************
				public ArrayList<EventsActivity> GetActiveEvents() {

					ArrayList<EventsActivity> lstobj = new ArrayList<EventsActivity>();

					try {

						String selectQuery = "SELECT * FROM " + Events + " Where  IsActive = 1 ";
						SQLiteDatabase db = this.getReadableDatabase();
						Cursor cursor = db.rawQuery(selectQuery, null);
						if (cursor.moveToFirst()) {
							do {

								EventsActivity object = new EventsActivity();
								object.setEventID(cursor.getInt(cursor.getColumnIndex("EventID")));
								object.setEventName(cursor.getString(cursor.getColumnIndex("EventName")));
								object.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
								object.setDate(cursor.getString(cursor.getColumnIndex("Date")));
								object.setTime(cursor.getString(cursor.getColumnIndex("Time")));
								
								object.setIsActive(  cursor.getInt(cursor.getColumnIndex("IsActive") ) == 1 ? true : false  );
														
								lstobj.add(object);

							} while (cursor.moveToNext());
						}

						cursor.close();
						db.close();

					} catch (Exception e) {
					}

					return lstobj;

				}
				
		
		
		
	// **************** Get Event By ID*******************
		
		public EventsActivity getEvent(int id) {
			SQLiteDatabase db = this.getReadableDatabase();
			EventsActivity object = new EventsActivity();
			

			try {

				String selectQuery = "SELECT  * FROM " + Events + " Where EventID = "+id+" ";
				Cursor cursor = db.rawQuery(selectQuery, null);				
				
				if (cursor != null && cursor.moveToFirst() ) {
					
					
					object.setEventID(cursor.getInt(cursor.getColumnIndex("EventID")));
					object.setEventName(cursor.getString(cursor.getColumnIndex("EventName")));
					object.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
					object.setDate(cursor.getString(cursor.getColumnIndex("Date")));
					object.setTime(cursor.getString(cursor.getColumnIndex("Time")));
					
					object.setIsActive(  cursor.getInt(cursor.getColumnIndex("IsActive") ) == 1 ? true : false  );
					
				}
				
				cursor.close();
				db.close();
				
			} catch (Exception e) {

			}

			return object;
		}

		
		
		//**************** Delete Event *******************
		public void DeleteEvent(int id) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(Events,  "EventID = ?", new String[] { String.valueOf(id) });
			db.close();
		}

		
		
		// ******************** Adding Location ********************
		public void AddLocation(LocationActivity obj) {
			SQLiteDatabase db = this.getWritableDatabase();
			Long id = selectMaxID(Location, "LocationID");
			ContentValues values = new ContentValues();
			values.put("LocationID", id);		
			values.put("LocationName", obj.getName());
			values.put("LocationXCordinate", obj.getLatitude());
			values.put("LocationYCordinate", obj.getLongitude());
			
			db.insert(Location, null, values);
			db.close();

		}
		

		// ******************** Update Location ********************
		public void UpdateLocation(LocationActivity obj) {
			SQLiteDatabase db = this.getWritableDatabase();
			
			ContentValues values = new ContentValues();
			values.put("LocationXCordinate", obj.getLatitude());
			values.put("LocationYCordinate", obj.getLongitude());
			
			db.update(Location, values, "LocationID = ?",  new String[] { String.valueOf(obj.getLocationId()) });
			
			db.close();

		}

		
		// **************** CHK Location EXIT *******************	
		public boolean CheckLocationEXIT(LocationActivity obj) {
			
			return CHK_EXIT(Location, "LocationName", obj.getName());
				
		}


		// **************** Get All Location *******************
		public ArrayList<LocationActivity> VewAllLocations() {

			ArrayList<LocationActivity> lstLoc = new ArrayList<LocationActivity>();

			try {

				String selectQuery = "SELECT * FROM " + Location;
				SQLiteDatabase db = this.getReadableDatabase();
				Cursor cursor = db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst()) {
					do {

						LocationActivity object = new LocationActivity();
						object.setLocationId(cursor.getInt(cursor.getColumnIndex("LocationID")));
						object.setName(cursor.getString(cursor.getColumnIndex("LocationName")));
						object.setLatitude(cursor.getString(cursor.getColumnIndex("LocationXCordinate")));
						object.setLongitude(cursor.getString(cursor.getColumnIndex("LocationYCordinate")));

						lstLoc.add(object);

					} while (cursor.moveToNext());
				}

				cursor.close();
				db.close();

			} catch (Exception e) {
			}

			return lstLoc;

		}
		
		
		// **************** Get Location By ID*******************
		
		public LocationActivity getLocation(int id) {
			SQLiteDatabase db = this.getReadableDatabase();
			LocationActivity object = new LocationActivity();
			

			try {

				String selectQuery = "SELECT  * FROM " + Location + " Where LocationID = "+id+" ";
				Cursor cursor = db.rawQuery(selectQuery, null);

				if (cursor != null && cursor.moveToFirst() ) {
					
					object.setLocationId(cursor.getInt(cursor
							.getColumnIndex("LocationID")));
					object.setName(cursor.getString(cursor
							.getColumnIndex("LocationName")));
					object.setLatitude(cursor.getString(cursor
							.getColumnIndex("LocationXCordinate")));
					object.setLongitude(cursor.getString(cursor
							.getColumnIndex("LocationYCordinate")));
														
					
				}

				cursor.close();
				db.close();
				
			} catch (Exception e) {

			}

			return object;
		}

				
		
		//**************** Delete Location *******************
		public void DeleteLocation(int id) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(Location,  "LocationID = ?", new String[] { String.valueOf(id) });
			db.close();
		}
		
				
		
		// ******************** Adding Profile ********************
		public void AddProfile(ProfileActivity obj) {
			SQLiteDatabase db = this.getWritableDatabase();
			Long id = selectMaxID(Profile, "ProfileID");
			ContentValues values = new ContentValues();
			values.put("ProfileID", id);		
			values.put("ProfileName", obj.getProfileName());
			values.put("ProfileType", obj.getProfileType());
			values.put("LocationID", obj.getLocationID());		
			values.put("StartTime", obj.getStartTime());
			values.put("EndTime", obj.getEndTime());
			values.put("Radius", obj.getRadius());
			values.put("IsCallTune", obj.getIsCallTune());
			values.put("Volumn", obj.getVolumn());
			values.put("IsVibrate", obj.getIsVibrate());
			values.put("IsActiveProfile", obj.getIsActiveProfile());
			values.put("IsMessegeTune", obj.getIsMessegeTune());
					
			db.insert(Profile, null, values);
			
			if(obj.getIsActiveProfile())
			{
				AddRecentProfile(id);
			}
			
			
			db.close();

		}
		

		// ******************** Update Profile ********************
		public void UpdateProfile(ProfileActivity obj) {
			SQLiteDatabase db = this.getWritableDatabase();
			
			ContentValues values = new ContentValues();
					
			values.put("ProfileName", obj.getProfileName());
			values.put("ProfileType", obj.getProfileType());
			values.put("LocationID", obj.getLocationID());		
			values.put("StartTime", obj.getStartTime());
			values.put("EndTime", obj.getEndTime());
			values.put("Radius", obj.getRadius());
			values.put("IsCallTune", obj.getIsCallTune());
			values.put("Volumn", obj.getVolumn());
			values.put("IsVibrate", obj.getIsVibrate());
			values.put("IsActiveProfile", obj.getIsActiveProfile());
			values.put("IsMessegeTune", obj.getIsMessegeTune());
		
			db.update(Profile, values, "ProfileID = ?",  new String[] { String.valueOf(obj.getProfileID()) });
		
			if(obj.getIsActiveProfile())
			{
				AddRecentProfile(obj.getProfileID());
			}
			
			
			db.close();

		}

		
		// **************** CHK Profile EXIT *******************	
		public boolean CheckProfileEXIT(ProfileActivity obj) {
			
			return CHK_EXIT(Profile, "ProfileName", obj.getProfileName());
				
		}
		
		

		
		// **************** Get All Profile *******************
		public ArrayList<ProfileActivity> VewAllProfile() {

			ArrayList<ProfileActivity> lstLoc = new ArrayList<ProfileActivity>();

			try {

				String selectQuery = "SELECT * FROM " + Profile;
				SQLiteDatabase db = this.getReadableDatabase();
				Cursor cursor = db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst()) {
					do {

						ProfileActivity object = new ProfileActivity();
						object.setProfileID(cursor.getInt(cursor.getColumnIndex("ProfileID")));
						object.setProfileName(cursor.getString(cursor.getColumnIndex("ProfileName")));
						object.setProfileType(cursor.getInt(cursor.getColumnIndex("ProfileType")));
						object.setLocationID(cursor.getInt(cursor.getColumnIndex("LocationID")));

						object.setStartTime(cursor.getString(cursor.getColumnIndex("StartTime")));

						object.setEndTime(cursor.getString(cursor.getColumnIndex("EndTime")));

						object.setRadius(cursor.getInt(cursor.getColumnIndex("Radius")));

						object.setIsCallTune(cursor.getInt(cursor.getColumnIndex("IsCallTune")));

						object.setVolumn(cursor.getInt(cursor.getColumnIndex("Volumn")));

						object.setIsVibrate(  cursor.getInt(cursor.getColumnIndex("IsVibrate") ) == 1 ? true : false  );
						
						object.setIsActiveProfile(  cursor.getInt(cursor.getColumnIndex("IsActiveProfile") ) == 1 ? true : false  );
						
						object.setIsMessegeTune(cursor.getInt(cursor.getColumnIndex("IsMessegeTune")));
						
						lstLoc.add(object);

					} while (cursor.moveToNext());
				}

				cursor.close();
				db.close();

			} catch (Exception e) {
			}

			return lstLoc;

		}
		

		

		// **************** Get Active Profile *******************
		public ArrayList<ProfileActivity> GetActiveProfile() {

			ArrayList<ProfileActivity> lstLoc = new ArrayList<ProfileActivity>();

			try {

				String selectQuery = "SELECT * FROM " + Profile + " Where IsActiveProfile = 1";
				SQLiteDatabase db = this.getReadableDatabase();
				Cursor cursor = db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst()) {
					do {

						ProfileActivity object = new ProfileActivity();
						object.setProfileID(cursor.getInt(cursor.getColumnIndex("ProfileID")));
						object.setProfileName(cursor.getString(cursor.getColumnIndex("ProfileName")));
						object.setProfileType(cursor.getInt(cursor.getColumnIndex("ProfileType")));
						object.setLocationID(cursor.getInt(cursor.getColumnIndex("LocationID")));
						object.setStartTime(cursor.getString(cursor.getColumnIndex("StartTime")));
						object.setEndTime(cursor.getString(cursor.getColumnIndex("EndTime")));
						object.setRadius(cursor.getInt(cursor.getColumnIndex("Radius")));
						object.setIsCallTune(cursor.getInt(cursor.getColumnIndex("IsCallTune")));
						object.setVolumn(cursor.getInt(cursor.getColumnIndex("Volumn")));
						object.setIsVibrate(  cursor.getInt(cursor.getColumnIndex("IsVibrate") ) == 1 ? true : false  );
						object.setIsActiveProfile(  cursor.getInt(cursor.getColumnIndex("IsActiveProfile") ) == 1 ? true : false  );
						object.setIsMessegeTune(cursor.getInt(cursor.getColumnIndex("IsMessegeTune")));
						
						lstLoc.add(object);

					} while (cursor.moveToNext());
				}

				cursor.close();
				db.close();

			} catch (Exception e) {
			}

			return lstLoc;

		}
		

		
		
		// **************** Get Profile By ID*******************
			
			public ProfileActivity getProfile(int id) {
				SQLiteDatabase db = this.getReadableDatabase();
				ProfileActivity object = new ProfileActivity();				
				try {

					String selectQuery = "SELECT  * FROM " + Profile + " Where ProfileID = "+id+" ";
					Cursor cursor = db.rawQuery(selectQuery, null);				
					
					if (cursor != null && cursor.moveToFirst() ) {
												
						object.setProfileID(cursor.getInt(cursor.getColumnIndex("ProfileID")));
						object.setProfileName(cursor.getString(cursor.getColumnIndex("ProfileName")));
						object.setProfileType(cursor.getInt(cursor.getColumnIndex("ProfileType")));
						object.setLocationID(cursor.getInt(cursor.getColumnIndex("LocationID")));
						object.setStartTime(cursor.getString(cursor.getColumnIndex("StartTime")));
						object.setEndTime(cursor.getString(cursor.getColumnIndex("EndTime")));
						object.setRadius(cursor.getInt(cursor.getColumnIndex("Radius")));
						object.setIsCallTune(cursor.getInt(cursor.getColumnIndex("IsCallTune")));
						object.setVolumn(cursor.getInt(cursor.getColumnIndex("Volumn")));
						object.setIsVibrate(  cursor.getInt(cursor.getColumnIndex("IsVibrate") ) == 1 ? true : false  );
						object.setIsActiveProfile(  cursor.getInt(cursor.getColumnIndex("IsActiveProfile") ) == 1 ? true : false  );
						object.setIsMessegeTune(cursor.getInt(cursor.getColumnIndex("IsMessegeTune")));
																									
						
					}

					cursor.close();
					db.close();
					
				} catch (Exception e) {

				}

				return object;
			}

		
		
		
		//**************** Delete Profile *******************
		public void DeleteProfile(int id) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(Profile,  "ProfileID = ?", new String[] { String.valueOf(id) });
			db.close();
		}
		
		
		
		
		// ******************** Adding Recent ********************
		
		public void AddRecentProfile(long ProfileId) {
			SQLiteDatabase db = this.getWritableDatabase();
			
			db.delete(RecentProfile,  "ProfileID = ?", new String[] { String.valueOf(ProfileId) });
			
			Long id = selectMaxID(RecentProfile, "RecentID");
			ContentValues values = new ContentValues();
			values.put("RecentID", id);		
			values.put("ProfileID", ProfileId);

			
			db.insert(RecentProfile, null, values);
			db.close();

		}
		

		

		
		// **************** Get All Recent *******************
		public ArrayList<ProfileActivity> VewAllRecent() {

			ArrayList<ProfileActivity> lstLoc = new ArrayList<ProfileActivity>();

			try {

				String selectQuery = "SELECT * FROM " + RecentProfile + " Order By RecentID Desc";
				SQLiteDatabase db = this.getReadableDatabase();
				Cursor cursor = db.rawQuery(selectQuery, null);
				if (cursor.moveToFirst()) {
					do {

						ProfileActivity object = new ProfileActivity();
						object = getProfile(cursor.getInt(cursor.getColumnIndex("ProfileID")) );						
						lstLoc.add(object);

					} while (cursor.moveToNext());
				}

				cursor.close();
				db.close();

			} catch (Exception e) {
			}

			return lstLoc;

		}
		

		
		
		
		
		
		
	

}
