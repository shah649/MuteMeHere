
package info.androidhive.muteme;

import java.util.ArrayList;

import info.androidhive.muteme.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewLocationActivity extends Activity {

	private ListView view;
	DatabaseHandler db;
	ArrayList<LocationActivity> detailed_arr = new ArrayList<LocationActivity>();
	private Adapter_Class lstAdapter;
	private AlertDialog.Builder Notify;
	private LocationActivity obj;
	private String ActivityType;
	private Preferences p;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_location);
			
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
			p.setPrefVal(ActivityType,obj.getLocationId()+"" );			
			ViewLocationActivity.this.finish();			
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

	
	
	


	public void createDialog()

	{
		Notify = new AlertDialog.Builder(this);

		Notify.setTitle("Remove Location ?");

		Notify.setPositiveButton("Remove",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						db.DeleteLocation(obj.getLocationId());
						detailed_arr.remove(obj);
												
						lstAdapter.notifyDataSetChanged();
						Log.v("obj to delete", obj.getLocationId()+"");
					}

				});
	}
	
	

	public void Set_Referash_Data() {
		detailed_arr.clear();
		
		ArrayList<LocationActivity> arr = db.VewAllLocations();

		for (int i = 0; i < arr.size(); i++) {
			LocationActivity obj = new LocationActivity();
			obj.setLocationId(arr.get(i).getLocationId());
			obj.setName(arr.get(i).getName());
			obj.setLatitude(arr.get(i).getLatitude());
			obj.setLongitude(arr.get(i).getLongitude());
			detailed_arr.add(obj);
		}

		db.close();
		lstAdapter = new Adapter_Class(ViewLocationActivity.this, R.layout.list_item,
				detailed_arr);
		view.setAdapter(lstAdapter);
		lstAdapter.notifyDataSetChanged();
		
	}

	
	

	class Holder_Class {
		TextView name;
		TextView lat;
		Button lng;		
	}

	

	
	class Adapter_Class extends ArrayAdapter<LocationActivity> {
		Activity activity;
		int layoutResourceId;
		LocationActivity act;
		ArrayList<LocationActivity> data = new ArrayList<LocationActivity>();
		//private TextView user_ID;

		public Adapter_Class(Activity act, int layoutResourceId,
				ArrayList<LocationActivity> data) {
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
			
			holder.name.setText(act.getName());
			holder.lat.setText(act.getLatitude() + " , " + act.getLongitude()  );
			
			return row;

		}


	}
	
	
	
	
	

}
