package info.androidhive.muteme;

import java.util.Calendar;
import java.util.Date;

import info.androidhive.muteme.R;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends Fragment {

	private String type;
	private EditText txtstart_time;
	private EditText txtend_time;
	private EditText txtlocation;
	private EditText txtradius;
	private RadioButton rdbtime;
	private RadioButton rdblocation;
	private TextView lblstart_time;
	private TextView lblend_time;
	private TextView lbllongitude;
	private SeekBar seekBarMsg;
	private SeekBar seekBarCall;
	private Button btnAdd;
	private Button btnclear;
	private Button btnView;
	private Button btnLocation;
	




	int ProfileID; 
	String ProfileName; 
	int ProfileType; 
	int LocationID; 
	String StartTime; 
	String EndTime; 
	int Radius;  
	int IsCallTune; 
	int Volumn; 
	boolean IsVibrate; 
	boolean IsActiveProfile; 
	int IsMessegeTune;
	private EditText txtName;
	private Preferences p;
	private CheckBox chkIsActive;
	private CheckBox chkIsVibrate;
	private DatabaseHandler db;
	private LocationActivity objLoc;
	private boolean isupdate;
	private ProfileActivity objProf;
	private CustomTimePicker customTimePicker;
	private CustomTimePicker customTimePicker2;

	
	
	public int getProfileID() {
		return ProfileID;
	}



	public void setProfileID(int profileID) {
		ProfileID = profileID;
	}



	public String getProfileName() {
		return ProfileName;
	}



	public void setProfileName(String profileName) {
		ProfileName = profileName;
	}



	public int getProfileType() {return ProfileType;}



	public void setProfileType(int profileType) {
		ProfileType = profileType;
	}



	public int getLocationID() {
		return LocationID;
	}



	public void setLocationID(int locationID) {
		LocationID = locationID;
	}



	public String getStartTime() {
		return StartTime;
	}



	public void setStartTime(String startTime) {
		StartTime = startTime;
	}



	public String getEndTime() {
		return EndTime;
	}



	public void setEndTime(String endTime) {
		EndTime = endTime;
	}



	public int getRadius() {
		return Radius;
	}



	public void setRadius(int radius) {
		Radius = radius;
	}



	public int getIsCallTune() {
		return IsCallTune;
	}



	public void setIsCallTune(int isCallTune) {
		IsCallTune = isCallTune;
	}



	public int getVolumn() {
		return Volumn;
	}



	public void setVolumn(int volumn) {
		Volumn = volumn;
	}



	public boolean getIsVibrate() {
		return IsVibrate;
	}



	public void setIsVibrate(boolean isVibrate) {
		IsVibrate = isVibrate;
	}



	public boolean getIsActiveProfile() {
		return IsActiveProfile;
	}



	public void setIsActiveProfile(boolean isActiveProfile) {
		IsActiveProfile = isActiveProfile;
	}




	public int getIsMessegeTune() {
		return IsMessegeTune;
	}



	public void setIsMessegeTune(int isMessegeTune) {
		IsMessegeTune = isMessegeTune;
	}

	
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
		try
		{
				//Toast.makeText(ProfileActivity.this.getActivity()," Profile  ", Toast.LENGTH_SHORT).show();
				
			//Clear();

			try
			{
				
				LocationID =  Integer.parseInt(p.getPrefVal("PROFILE", "0"));
				if(LocationID > 0)
				{
					objLoc =  db.getLocation(LocationID);
					txtlocation.setText(objLoc.getName());	
				}
				
				
				
				ProfileID =  Integer.parseInt(p.getPrefVal("PROFILE_MAIN", "0"));
				if(ProfileID > 0)
				{
					
					objProf =  db.getProfile(ProfileID);					
					txtName.setText(objProf.getProfileName());
					txtName.setEnabled(false);
					
					
					
					//Log.e("getProfileType = ", objProf.getProfileType()+"" );
				
					if(objProf.getProfileType() == 1)
					{
						rdbtime.setChecked(true);
						
						p.setPrefVal("TYPE", "time");
						txtstart_time.setText(objProf.getStartTime());
						txtend_time.setText(objProf.getEndTime());
						
					}
					else
					{
						rdblocation.setChecked(true);
						p.setPrefVal("TYPE", "loc");
						
						
						if(LocationID == 0)
						{
							p.setPrefVal("PROFILE", objProf.getLocationID()+"");
							LocationID = objProf.getLocationID();
							objLoc =  db.getLocation(LocationID);						
							txtlocation.setText(objLoc.getName());												
							txtradius.setText(objProf.getRadius()+"");
						}
						
						
					}
					rdbtime.setEnabled(false);
					rdblocation.setEnabled(false);
					
					
					//Toast.makeText(ProfileActivity.this.getActivity()," seekBarCall :" + objProf.getIsCallTune() + " , seekBarMsg :" + getIsMessegeTune(), Toast.LENGTH_SHORT).show();
							
					seekBarCall.setProgress(objProf.getIsCallTune());
					seekBarMsg.setProgress(objProf.getIsMessegeTune());
					chkIsActive.setChecked(objProf.getIsActiveProfile());
					chkIsVibrate.setChecked(objProf.IsVibrate);		
					Volumn = 0;
					
					
					isupdate = true;
					
				}
				else
				{
					isupdate = false;
				}
				
				
				
			}
			catch(Exception ex)
			{
				
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
		Clear();
		super.onDestroy();
	}

	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		
	    p = new Preferences(ProfileActivity.this.getActivity());
	    db = new DatabaseHandler(ProfileActivity.this.getActivity());
	    
		rdbtime = (RadioButton) rootView.findViewById(R.id.rdbtime);
		rdblocation = (RadioButton) rootView.findViewById(R.id.rdblocation);
		
		lblstart_time = (TextView) rootView.findViewById(R.id.lblstart_time);
		lblend_time = (TextView) rootView.findViewById(R.id.lblend_time);
		lbllongitude = (TextView) rootView.findViewById(R.id.lbllongitude);
				
		
		txtName = (EditText) rootView.findViewById(R.id.txtName);
		txtstart_time = (EditText) rootView.findViewById(R.id.txtstart_time);
		txtend_time = (EditText) rootView.findViewById(R.id.txtend_time);
		txtlocation = (EditText) rootView.findViewById(R.id.txtlocation);
		txtradius = (EditText) rootView.findViewById(R.id.txtradius);
		
		seekBarMsg = (SeekBar) rootView.findViewById(R.id.seekBarMsg);
		seekBarCall = (SeekBar) rootView.findViewById(R.id.seekBarCall);
		
		
		btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
		btnclear = (Button) rootView.findViewById(R.id.btnclear);
		btnView = (Button) rootView.findViewById(R.id.btnview2);
		btnLocation = (Button) rootView.findViewById(R.id.btnLocation);
					
		
		chkIsActive = (CheckBox) rootView.findViewById(R.id.chkIsActive);
		chkIsVibrate = (CheckBox) rootView.findViewById(R.id.chkIsVibrate);
				
		
		type = p.getPrefVal("TYPE", "time");
		
		if(type.equals("time"))
		{
			rdbtime.setChecked(true);
			rdblocation.setChecked(false);
			

			lbllongitude.setVisibility(View.GONE);		
			txtlocation.setVisibility(View.GONE);
			txtradius.setVisibility(View.GONE);		
			btnLocation.setVisibility(View.GONE);
		}
		else
		{
			rdbtime.setChecked(false);
			rdblocation.setChecked(true);
			
			lblstart_time.setVisibility(View.GONE);
			lblend_time.setVisibility(View.GONE);			
			txtstart_time.setVisibility(View.GONE);
			txtend_time.setVisibility(View.GONE);
		}
		
		
		
		btnLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),ViewLocationActivity.class);
				intent.putExtra("Type", "PROFILE");
				startActivity(intent); 
			}
		});
		
		
		
		
		
		rdbtime.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(rdbtime.isChecked())
				{
					rdblocation.setChecked(false);

					lbllongitude.setVisibility(View.GONE);		
					txtlocation.setVisibility(View.GONE);
					txtradius.setVisibility(View.GONE);
					btnLocation.setVisibility(View.GONE);
					
					lblstart_time.setVisibility(View.VISIBLE);
					lblend_time.setVisibility(View.VISIBLE);			
					txtstart_time.setVisibility(View.VISIBLE);
					txtend_time.setVisibility(View.VISIBLE);
				}
				
			}
		});
		
		
		rdblocation.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
			
				if(rdblocation.isChecked())
				{
					rdbtime.setChecked(false);
					

					lbllongitude.setVisibility(View.VISIBLE);		
					txtlocation.setVisibility(View.VISIBLE);
					txtradius.setVisibility(View.VISIBLE);
					btnLocation.setVisibility(View.VISIBLE);
					
					
					lblstart_time.setVisibility(View.GONE);
					lblend_time.setVisibility(View.GONE);			
					txtstart_time.setVisibility(View.GONE);
					txtend_time.setVisibility(View.GONE);	
				}
				
			}
		});
		
		
		
		
		btnclear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Clear();
			}
		});

		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(isupdate)
				{
					UpdateProfile();
				}
				else
				{
					AddProfile();
				}
								
				
				
				
			}
		});

		btnView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(getActivity(),ViewProfileActivity.class);
				intent.putExtra("Type", "PROFILE_MAIN");
				startActivity(intent); 			
				
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
  	    		 txtstart_time.setText(hour12 + ":" + min+ " " + AM_PM);  	    		
               }

  	                @Override
  	                public void onCancel() {

  	                }
  	            });

        
        
        
        customTimePicker2 = new CustomTimePicker(getActivity(),
  	            new CustomTimePicker.ICustomDateTimeListener() {

  			@Override
            public void onSet(Dialog dialog, Calendar calendarSelected,
                    Date dateSelected, int year, String monthFullName,
                    String monthShortName, int monthNumber, int date,
                    String weekDayFullName, String weekDayShortName,
                    int hour24, int hour12, int min, int sec,
                    String AM_PM) {
  	    		 txtend_time.setText(hour12 + ":" + min+ " " + AM_PM);  	    		
               }

  	                @Override
  	                public void onCancel() {

  	                }
  	            });

        
        
        customTimePicker.set24HourFormat(false);
        customTimePicker2.set24HourFormat(false);
        
        
        txtstart_time.setOnClickListener(new OnClickListener() {
   			
   			@Override
   			public void onClick(View v) {
   				// TODO Auto-generated method stub
   				customTimePicker.showDialog();
   			}
   		});
 		


        txtend_time.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			customTimePicker2.showDialog();
		}
	});
	

		
		
		
		
		


		
		return rootView;
	}
	
	
	
	private void Clear()
	{
		isupdate= false;				
		
		
		
		txtName.setEnabled(true);
		LocationID  = 0;
		txtlocation.setText("");		
		
		txtName.setText("");
		rdbtime.setEnabled(true);
		rdblocation.setEnabled(true);
		
		rdbtime.setChecked(true);		
		rdblocation.setChecked(false);
		
		
		txtstart_time.setText("");
		txtend_time.setText("");
		txtradius.setText("");		
		
		seekBarMsg.setProgress(50);
		seekBarCall.setProgress(50);
		chkIsActive.setChecked(true);
		chkIsVibrate.setChecked(false);		
		Volumn = 0;
		p.setPrefVal("PROFILE", "0");
		p.setPrefVal("PROFILE_MAIN", "0");
		type = p.setPrefVal("TYPE", "time");
		
	}
	
	

	private void AddProfile()
	{
		
		
		
		ProfileName = txtName.getText().toString();
		
		if(rdbtime.isChecked())
		{
			ProfileType = 1;				
			Radius =  Integer.parseInt("0");
		}
		else
		{
			ProfileType = 2;
			Radius =  Integer.parseInt( txtradius.getText().toString());			
		}

		
		StartTime = txtstart_time.getText().toString();
		EndTime = txtend_time.getText().toString();
					
		IsCallTune = seekBarCall.getProgress();
		IsMessegeTune= seekBarMsg.getProgress();
		
		IsActiveProfile = chkIsActive.isChecked();		
		IsVibrate = chkIsVibrate.isChecked();
				
		Volumn = 0;
		
		if(! db.CheckProfileEXIT(ProfileActivity.this)){
			db.AddProfile(ProfileActivity.this);
			Toast.makeText(ProfileActivity.this.getActivity(), "Profile Added", Toast.LENGTH_SHORT).show();
			Clear();
		}
		else
		{
			Toast.makeText(ProfileActivity.this.getActivity(), "Profile Already Created.", Toast.LENGTH_SHORT).show();
		}
		
				
	}
	
	
	
	private void UpdateProfile()
	{
		
		
	ProfileName = txtName.getText().toString();
		
		if(rdbtime.isChecked())
		{
			ProfileType = 1;
			Radius =  Integer.parseInt("0");
		}
		else
		{
			ProfileType = 2;
			Radius =  Integer.parseInt( txtradius.getText().toString());			
		}


		StartTime = txtstart_time.getText().toString();
		EndTime = txtend_time.getText().toString();
					
		IsCallTune = seekBarCall.getProgress();
		IsMessegeTune= seekBarMsg.getProgress();
		
		IsActiveProfile = chkIsActive.isChecked();		
		IsVibrate = chkIsVibrate.isChecked();
				
		Volumn = 0;
		
		
		db.UpdateProfile(ProfileActivity.this);
		Toast.makeText(ProfileActivity.this.getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
		Clear();
						

		
		
	}
	
	
	
	
}
