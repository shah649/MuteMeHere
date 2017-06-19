package info.androidhive.muteme;

import info.androidhive.muteme.R;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LocationActivity extends Fragment implements LocationListener{

	private EditText txtlocation;
	private EditText txtlat;
	private EditText txtlong;
	private Button btnClear;
	private Button btnAdd;

	private int locationId;
	private String name;
	private String latitude;
	private String longitude;
	private DatabaseHandler dbHandler;
	private Button btnview;
	private View rootView;	
	Boolean isupdate;
	private LocationActivity objLoc;
	private Preferences p;
	private Button btnLocation;
	LocationManager locationManager ;
	String provider;
	private boolean isGPSEnabled;
	private boolean isNetworkEnabled;
	Location location; // location

	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	
	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLatitude() {return latitude;}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}



	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		//Toast.makeText(LocationActivity.this.getActivity()," location ", Toast.LENGTH_SHORT).show();
		
		try
		{
			
			txtlocation.requestFocus();
			
			locationId =  Integer.parseInt(p.getPrefVal("LOCATION", "0"));
			if(locationId > 0)
			{
				txtlocation.setEnabled(false);
				objLoc =  dbHandler.getLocation(locationId);
				txtlocation.setText(objLoc.getName());
				txtlat.setText(objLoc.getLatitude());
				txtlong.setText(objLoc.getLongitude());
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

		p = new Preferences(LocationActivity.this.getActivity());
		
	    rootView = inflater.inflate(R.layout.fragment_area, container, false);
		txtlocation = (EditText) rootView.findViewById(R.id.txtlocation);
		txtlat = (EditText) rootView.findViewById(R.id.txtlat);
		txtlong = (EditText) rootView.findViewById(R.id.txtlong);
		txtlocation.requestFocus();
		txtlocation.selectAll();
		
		dbHandler = new DatabaseHandler(this.getActivity());
		
		btnClear = (Button) rootView.findViewById(R.id.btnClear);
		btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
		btnview = (Button) rootView.findViewById(R.id.btnview);
		btnLocation = (Button) rootView.findViewById(R.id.btnLocation);
		
		
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
				// TODO Auto-generated method stub
				if(isupdate)
				{
					updatelocation();
				}
				else
				{
					Addlocation();
				}
				
						
				
			}
		});
		
		
		btnview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),ViewLocationActivity.class);
				intent.putExtra("Type", "LOCATION");
				startActivity(intent); 
				
			}
		});
		
		
		
		btnLocation.setOnClickListener(new OnClickListener() {
			


			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				
				
				// Getting LocationManager object
		        locationManager = (LocationManager) LocationActivity.this.getActivity().getSystemService(Context.LOCATION_SERVICE);        

		        
		     // getting GPS status 
		        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	 
		        // getting network status 
		        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		        
		        if (!isGPSEnabled && !isNetworkEnabled)// no network provider is enabled
					{Toast.makeText(LocationActivity.this.getActivity(), "No network provider available", Toast.LENGTH_SHORT).show();}
		        else
		        	{
						locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,LocationActivity.this);

						if (isGPSEnabled)
						{
							if (locationManager != null)
							{
								location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

			                         if (location != null)
										{

			                        	Toast.makeText(LocationActivity.this.getActivity(), "Location from GPS", Toast.LENGTH_SHORT).show();

			                			txtlong.setText("" + location.getLongitude());
			                			

			                			txtlat.setText("" + location.getLatitude() );
			                        	
			                        	}
			                        	else
			                        	{
			                        	
			                       		 if (isNetworkEnabled)
										 	{
			                       	 
			                       			if (locationManager != null)
												{
						                   			 location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						                    if (location != null)
												{

						                    	Toast.makeText(LocationActivity.this.getActivity(), "Location from Network", Toast.LENGTH_SHORT).show();

						            			txtlong.setText("" + location.getLongitude());
						            			txtlat.setText("" + location.getLatitude() );
						                    }
						                    else
						                    {
						                    	Toast.makeText(LocationActivity.this.getActivity() , "Location can't be retrieved", Toast.LENGTH_SHORT).show();
						                    }
						                }
					        		 
			                       		 
			                       		 
			                       		 
			                       	 }
			                       	 else
			                       	 {
			                       		Toast.makeText(LocationActivity.this.getActivity() , "Location can't be retrieved", Toast.LENGTH_SHORT).show();
			                       	 }
			                        	
			                        	
			                        	
			                        	
			                        	
			                        	
			                        }
			                    }
			              }
		        	 else
		        	 {
		        		 
		        		 
		        		 
		        		 if (locationManager != null) {
			                    location = locationManager
			                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			                    if (location != null) {
			                    	Toast.makeText(LocationActivity.this.getActivity(), "Location from Network", Toast.LENGTH_SHORT).show();

			            			txtlong.setText("" + location.getLongitude());
			            			txtlat.setText("" + location.getLatitude() );
			                    	
			                    }
			                   else
			                    {
			                    	Toast.makeText(LocationActivity.this.getActivity() , "Location can't be retrieved", Toast.LENGTH_SHORT).show();
			                    }
			                }
		        		 
		        		 
		        		 
		        		 
		        	 }
		        	 
		        	
		        	
		        	
		        	
		        }
				
				
			}
		});
		
		
		
		
		return rootView;
	}
	
	
	private void clear()
	{		
		isupdate = false;
		txtlocation.setEnabled(true);
		txtlat.setText("");
		txtlocation.setText("");
		txtlong.setText("");		
		p.setPrefVal("LOCATION", "0");
	}
	
	
	private void Addlocation()
	{
		
		try {

			// Adding values to variables then in database
			
			name = txtlocation.getText().toString();
			latitude = txtlat.getText().toString();
			longitude = txtlong.getText().toString();
			
			// checking for existing location
			if(! dbHandler.CheckLocationEXIT(LocationActivity.this)){
				dbHandler.AddLocation(LocationActivity.this);
				
				// Display a success messege
				Toast.makeText(LocationActivity.this.getActivity(),
						"Location Added !", Toast.LENGTH_SHORT).show();
					clear();
			}
			else{
				Toast.makeText(LocationActivity.this.getActivity(),
						" Location already exists. ", Toast.LENGTH_SHORT).show();
				txtlocation.selectAll();
				
			}
				
			

		} catch (Exception ex) {
			Toast.makeText(LocationActivity.this.getActivity(),
					ex.getMessage(), Toast.LENGTH_SHORT).show();
		}

		
		
	}
	
	

	private void updatelocation()
	{
		
		try {

			// Adding values to variables then in database
			
			name = txtlocation.getText().toString();
			latitude = txtlat.getText().toString();
			longitude = txtlong.getText().toString();
			
				//Update db
				dbHandler.UpdateLocation(LocationActivity.this);
				
				// Display a success messege
				Toast.makeText(LocationActivity.this.getActivity(),
						"Location Updated !", Toast.LENGTH_SHORT).show();
					clear();
			

		} catch (Exception ex) {
			Toast.makeText(LocationActivity.this.getActivity(),
					ex.getMessage(), Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
		try
		{

			
		}
		catch(Exception ex)
		{
			
		}
		
			
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
		
}
