package info.androidhive.muteme;


import java.util.ArrayList;

import android.os.Bundle;
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
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class ViewEventsActivity extends Activity {

	
	private ListView view;
	DatabaseHandler db;
	ArrayList<EventsActivity> detailed_arr = new ArrayList<EventsActivity>();
	private Adapter_Class lstAdapter;
	private AlertDialog.Builder Notify;
	private EventsActivity obj;
	private String ActivityType;
	private Preferences p;

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_events);
		
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
				p.setPrefVal(ActivityType,obj.getEventID()+"" );			
				ViewEventsActivity.this.finish();			
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

	

	class Adapter_Class extends ArrayAdapter<EventsActivity> {
		Activity activity;
		int layoutResourceId;
		EventsActivity act;
		ArrayList<EventsActivity> data = new ArrayList<EventsActivity>();
		//private TextView user_ID;

		public Adapter_Class(Activity act, int layoutResourceId,
				ArrayList<EventsActivity> data) {
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
			
			holder.name.setText(act.getEventName());
			holder.lat.setText(act.getDate());
			
			return row;

		}


	}
	

	
	public void Set_Referash_Data() {
		detailed_arr.clear();
		
		ArrayList<EventsActivity> arr = db.VewAllEvents();

		for (int i = 0; i < arr.size(); i++) {
			EventsActivity obj = new EventsActivity();
			
			obj.setEventID(arr.get(i).getEventID());
			obj.setEventName(arr.get(i).getEventName());
			obj.setDate(arr.get(i).getDate());
			obj.setTime(arr.get(i).getTime());
						
			detailed_arr.add(obj);
		}

		db.close();
		lstAdapter = new Adapter_Class(ViewEventsActivity.this, R.layout.list_item,
				detailed_arr);
		view.setAdapter(lstAdapter);
		lstAdapter.notifyDataSetChanged();
		
	}

	

	


	public void createDialog()

	{
		Notify = new AlertDialog.Builder(this);
		Notify.setTitle("Remove Event ?");
		Notify.setPositiveButton("Remove",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						db.DeleteEvent(obj.getEventID());
						detailed_arr.remove(obj);
												
						lstAdapter.notifyDataSetChanged();
						Log.v("obj to delete", obj.getEventID()+"");
					}

				});
	}
	
	



}
