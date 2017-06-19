package info.androidhive.muteme;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import info.androidhive.muteme.R;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class EventsActivity extends Fragment {

	private EditText txtName;
	private EditText txtdesc;
	private Button btnClear;
	private Button btnAdd;
	private DatabaseHandler dbHandler;
	ArrayList<EventsActivity> lstEvents;
	PendingIntent pendingIntent;
	AlarmManager alarmManager;
	
	private EventsActivity objEvent;
	private boolean IsActive;
	private int eventID;
	private String eventName;
	private String description;
	private String date;
	private String time;
	private EditText txtDate;
	private EditText txtTime;
	private CheckBox chkIsActive;
	private Button btnView;
	private boolean isupdate;
	private Preferences p;	

	CustomDatePicker customDatePicker;
	CustomTimePicker customTimePicker;
	

	
	public boolean getIsActive() {
		return IsActive;
	}


	public void setIsActive(boolean IsActive) {
		this.IsActive = IsActive;
	}

	
	public int getEventID() {
		return eventID;
	}


	public void setEventID(int eventID) {
		this.eventID = eventID;
	}


	public String getEventName() {
		return eventName;
	}


	public void setEventName(String eventName) {
		this.eventName = eventName;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		//Toast.makeText(LocationActivity.this.getActivity()," resumed ", Toast.LENGTH_SHORT).show();
		
		try
		{
			eventID =  Integer.parseInt(p.getPrefVal("EVENT", "0"));
			if(eventID > 0)
			{
				txtName.setEnabled(false);
				objEvent =  dbHandler.getEvent(eventID);
				txtName.setText(objEvent.getEventName());
				txtDate.setText(objEvent.getDate());
				txtTime.setText(objEvent.getTime());
				txtdesc.setText(objEvent.getDescription());
				chkIsActive.setChecked( objEvent.getIsActive());
				isupdate = true;
			}
		}
		catch(Exception ex)
		{
			
		}
		
		super.onResume();
	}
	
	



	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		clear();
		super.onDestroy();
	}

	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_event, container, false);
		dbHandler = new DatabaseHandler(this.getActivity());
		p = new Preferences(EventsActivity.this.getActivity());
		
		txtName = (EditText) rootView.findViewById(R.id.txtName);
		txtDate = (EditText) rootView.findViewById(R.id.txtDate);
		txtTime = (EditText) rootView.findViewById(R.id.txtTime);		
		txtdesc = (EditText) rootView.findViewById(R.id.txtdesc);
		chkIsActive = (CheckBox) rootView.findViewById(R.id.chkIsActive);
				
		btnClear = (Button) rootView.findViewById(R.id.btnClear);
		btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
		btnView = (Button) rootView.findViewById(R.id.btnView);
		
		txtDate.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
		 
		
		
		btnClear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				clear();
				
			}
		});
		
		
		btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(isupdate)
				{
					UpdateEvent();
				}
				else
				{
					AddEvent();	
				}
				
			
				//Log.v("obj", "hye i am clicked");
				
			}
		});
		

		
         btnView.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(getActivity(),ViewEventsActivity.class);
				intent.putExtra("Type", "EVENT");
				startActivity(intent);
				
			}
		});
		
         
         
         
         customDatePicker = new CustomDatePicker(getActivity(),
   	            new CustomDatePicker.ICustomDateTimeListener() {

   	                @Override
   	                public void onSet(Dialog dialog, Calendar calendarSelected,
   	                        Date dateSelected, int year, String monthFullName,
   	                        String monthShortName, int monthNumber, int date,
   	                        String weekDayFullName, String weekDayShortName) {
   	                	txtDate.setText(calendarSelected.get(Calendar.DAY_OF_MONTH)
   	                                    + "/" + (monthNumber+1) + "/" + year);  	  				
   	                }

   	                @Override
   	                public void onCancel() {

   	                }
   	            });
   		
         
         
         customTimePicker = new CustomTimePicker(getActivity(),
   	            new CustomTimePicker.ICustomDateTimeListener() {

   			@Override
             public void onSet(Dialog dialog, Calendar calendarSelected,
                     Date dateSelected, int year, String monthFullName,
                     String monthShortName, int monthNumber, int date,
                     String weekDayFullName, String weekDayShortName,
                     int hour24, int hour12, int min, int sec,
                     String AM_PM) {
   	    		 txtTime.setText(hour12 + ":" + min+ " " + AM_PM);  	    		
                }

   	                @Override
   	                public void onCancel() {

   	                }
   	            });

         
         
         
         
         
         
         
         customDatePicker.setDate(Calendar.getInstance());
         customTimePicker.set24HourFormat(false);
        
         txtDate.setOnClickListener(new OnClickListener() {
   			
   			@Override
   			public void onClick(View v) {
   				// TODO Auto-generated method stub
   				customDatePicker.showDialog();
   			}
   		});
   		
   		txtTime.setOnClickListener(new OnClickListener() {
   			
   			@Override
   			public void onClick(View v) {
   				// TODO Auto-generated method stub
   				customTimePicker.showDialog();
   			}
   		});
 		         
		
		

		return rootView;
	}
	
	
	
	private void clear()
	{
		isupdate = false;
		txtName.setEnabled(true);
		
		p.setPrefVal("EVENT", "0");
		txtName .setText("");
		txtDate .setText("");
		txtTime .setText("");
		txtdesc.setText("");		
		chkIsActive.setChecked(true);
	}
	
	
	
	

	private void AddEvent()
	{
		
		try {

			// Adding values to variables then in database
			
			eventName = txtName.getText().toString();
			date = txtDate.getText().toString();
			time = txtTime.getText().toString();
			description = txtdesc.getText().toString();
			IsActive =   chkIsActive.isChecked();
			
			
			
			// checking for existing location
			if(! dbHandler.CheckEventEXIT(EventsActivity.this)){
				
				dbHandler.addEvent(EventsActivity.this);
				
				clear();
				
				// Display a success messege
				Toast.makeText(EventsActivity.this.getActivity(),
						"Even Added !", Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(EventsActivity.this.getActivity(),
						" Even already exists. ", Toast.LENGTH_SHORT).show();
				txtName.selectAll();
				
			}
				
			

		} catch (Exception ex) {
			Toast.makeText(EventsActivity.this.getActivity(),
					ex.getMessage(), Toast.LENGTH_SHORT).show();
		}

		
		
	}
	
	private void UpdateEvent()
	{
		
		try {

			// Adding values to variables then in database
			
			eventName = txtName.getText().toString();
			date = txtDate.getText().toString();
			time = txtTime.getText().toString();
			description = txtdesc.getText().toString();
			IsActive =   chkIsActive.isChecked();
			
			
			
			// checking for existing location
				
				dbHandler.UpdateEvent(EventsActivity.this);
				
				clear();
				
				// Display a success messege
				Toast.makeText(EventsActivity.this.getActivity(),
						"Even Updated !", Toast.LENGTH_SHORT).show();
			

		} catch (Exception ex) {
			Toast.makeText(EventsActivity.this.getActivity(),
					ex.getMessage(), Toast.LENGTH_SHORT).show();
		}

		
	}
	
	
	}
	
	
  
