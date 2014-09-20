package com.hack.disastermgmt;

import java.util.List;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.BeaconManager.MonitoringListener;
import com.estimote.sdk.Region;

public class MonitoringListenerImpl implements MonitoringListener{
    protected static final String TAG =
	    "EstimoteiBeacon";
    private static final String
    ESTIMOTE_PROXIMITY_UUID =
    "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static String guid=ESTIMOTE_PROXIMITY_UUID+"-100-12";
    private static final Region ALL_ESTIMOTE_BEACONS =new Region("regionId", ESTIMOTE_PROXIMITY_UUID,
	    null, null);
    private BeaconManager beaconManager;
    

    public MonitoringListenerImpl(BeaconManager beaconManager){
	this.beaconManager=beaconManager;
    }
    @Override
    public void onEnteredRegion(Region region,List<Beacon> beacons)
    {

	Log.i(TAG,"Entered region");
	 
	        //---range for beacons---
	        try {
	            beaconManager.startRanging(
	                        ALL_ESTIMOTE_BEACONS);
	        } catch (RemoteException e) {
	            Log.e(TAG,
	                "Cannot start ranging", e);
	        }
	

    }

    @Override
    public void onExitedRegion(Region region) {

	Log.i(TAG,"Exited region");

	//---stop ranging for beacons---
	try {
	    beaconManager.stopRanging(
		    ALL_ESTIMOTE_BEACONS);
	    Log.i(TAG, DatabaseHandler.getRecords().toString());
	} catch (RemoteException e) {
	    Log.e(TAG,
		    e.getLocalizedMessage());
	}
    }


}
