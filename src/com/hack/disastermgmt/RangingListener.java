package com.hack.disastermgmt;

import android.content.Context;
import android.util.Log;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import sos.Location;

import java.util.List;

public class RangingListener implements BeaconManager.RangingListener{
    protected static final String TAG =
            "EstimoteiBeacon";
    private Context context;
    
    public RangingListener(Context context){
	this.context=context;
	
    }
    @Override 
    public void onBeaconsDiscovered(Region region, final List<Beacon> beacons){
	Log.d(TAG, "Ranged beacons: " +
		beacons);
	Log.d(TAG,"Storing beacons in db");
	for(Beacon beacon:beacons){
	    double latitude=new GPSTracker(context).getLat();
	    double longitude=new GPSTracker(context).getLong();
	    Log.i(TAG, "Latitude is "+latitude);
	    Log.i(TAG, "Longitude is "+longitude);
	    long time=System.currentTimeMillis();
	    Log.i(TAG, "Time is "+time);
	    String guid=beacon.getProximityUUID()+"-"+beacon.getMajor()+"-"+beacon.getMinor();
	    Location record=new Location(latitude, longitude, time);
	    DatabaseHandler.addRecord(guid,record);
	}
    }

}
