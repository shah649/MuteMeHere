package info.androidhive.muteme;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class ViewProfileActivity extends Activity {

	
	private ListView view;
	DatabaseHandler db;
	ArrayList<ProfileActivity> detailed_arr = new ArrayList<ProfileActivity>();
	private Adapter_Class lstAdapter;
	private AlertDialog.Builder Notify;
	private ProfileActivity obj;
	private String ActivityType;
	private Preferences p;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_profile);
		
		
		
		
	p = new Preferences(this);
	db = new DatabaseHandler(this);
	view = (ListView) findViewById(R.id.list);	
	view.setItemsCanFocus(false);
	
	
	try {		
		ActivityType = getIntent().getStringExtra("Type");			
	} catch (Exception e) {
		// TODO: handle exception
		}
	
	
	
	view.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			
			obj =  detailed_arr.get(arg2);			
			//Log.v("obj value", obj.getLocationId() +"");
			p.setPrefVal(ActivityType,obj.getProfileID()+"" );			
			p.setPrefVal("PROFILE", "0");
			ViewProfileActivity.this.finish();
			
			
		}
		
	});
	
	
	view.setOnItemLongClickListener(new OnItemLongClickListener() {
		
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub		
			createDialog();
			Notify.show();
			obj =  detailed_arr.get(arg2);			
			//Log.v("obj value", obj.getName() +"");
			return true;
		}
	});
	

		
	
	Set_Referash_Data();
		
		
	}

	class Holder_Class {
		TextView name;
		TextView lat;
		Button lng;		
	}

	


	class Adapter_Class extends ArrayAdapter<ProfileActivity> {
		Activity activity;
		int layoutResourceId;
		ProfileActivity act;
		ArrayList<ProfileActivity> data = new ArrayList<ProfileActivity>();
		//private TextView user_ID;

		public Adapter_Class(Activity act, int layoutResourceId,
				ArrayList<ProfileActivity> data) {
			super(act, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.activity = act;
			this.data = data;
			// notifyDataSetChanged();
		}
		

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			Holder_Class holder = null;

			if (row == null) {
				LayoutInflater inflater = LayoutInflater.from(activity);

				row = inflater.inflate(layoutResourceId, parent, false);
				holder = new Holder_Class();
				holder.name = (TextView) row.findViewById(R.id.txtMain);
				holder.lat = (TextView) row.findViewById(R.id.txtSub);
				
				row.setTag(holder);

			} else {
				holder = (Holder_Class) row.getTag();
			}

			act = data.get(position);
			
			holder.name.setText(act.getProfileName());
			
			if(act.getProfileType() == 1)
			{
				holder.lat.setText("Time Based");	
			}
			else
			{
				holder.lat.setText("Location Based");
			}			
			return row;
		}
	}
	
	


	public void Set_Referash_Data() {
		detailed_arr.clear();
		
		ArrayList<ProfileActivity> arr = db.VewAllProfile();

		for (int i = 0; i < arr.size(); i++) {
			ProfileActivity obj = new ProfileActivity();
			obj.setProfileID(arr.get(i).getProfileID());
			obj.setProfileName(arr.get(i).getProfileName());
			obj.setProfileType(arr.get(i).getProfileType());
			obj.setLocationID(arr.get(i).getLocationID());
			obj.setStartTime(arr.get(i).getStartTime());
			obj.setEndTime(arr.get(i).getEndTime());
			obj.setRadius(arr.get(i).getRadius());
			obj.setIsCallTune(arr.get(i).getIsCallTune());
			obj.setVolumn(arr.get(i).getVolumn());
			obj.setIsVibrate(arr.get(i).getIsVibrate());
			obj.setIsActiveProfile(arr.get(i).getIsActiveProfile());
			obj.setIsMessegeTune(arr.get(i).getIsMessegeTune());
			
			
			
			detailed_arr.add(obj);
		}

		db.close();
		lstAdapter = new Adapter_Class(ViewProfileActivity.this, R.layout.list_item,
				detailed_arr);
		view.setAdapter(lstAdapter);
		lstAdapter.notifyDataSetChanged();
		
	}


	

	public void createDialog()

	{
		Notify = new AlertDialog.Builder(this);

		Notify.setTitle("Remove Profile ?");

		Notify.setPositiveButton("Remove",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						db.DeleteProfile(obj.getProfileID());
						detailed_arr.remove(obj);
												
						lstAdapter.notifyDataSetChanged();
						Log.v("obj to delete", obj.getProfileID()+"");
					}

				});
	}
	
		
	

}
