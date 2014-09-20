package com.hack.LifeAfterLife;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.hack.disastermgmt.MonitoringListenerImpl;
import com.hack.disastermgmt.NetworkService;
import com.hack.disastermgmt.RangingListener;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import sos.User;
import sos.emergency_helper;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends Activity {
    private static final String IOS_GUID =
            "b9407f30-f5f8-466e-aff9-25556b57fe6d-100-12";

    private static final String GUID =
            "GUID";

    EditText name, age, contact, emergency_name, emergency_contact;
    Spinner gender;
    Button btnSubmit;

    private static final String
            ESTIMOTE_PROXIMITY_UUID =
            "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final Region ALL_ESTIMOTE_BEACONS =
            new Region("regionId", ESTIMOTE_PROXIMITY_UUID,
                    null, null);

    protected static final String TAG =
            "EstimoteiBeacon";
    private Context context;
    BeaconManager beaconManager;

    private void startMonitoringBeacon() {
        //---called when beacons are found---
        beaconManager.setRangingListener(new RangingListener(context));
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            beaconManager.stopRanging(
                    ALL_ESTIMOTE_BEACONS);
        } catch (RemoteException e) {
            Log.e(TAG, "Cannot stop", e);
        }
    }

    public static boolean isAppInForeground(
            Context context) {
        List<ActivityManager.RunningTaskInfo> task = ((ActivityManager)
                context.getSystemService(
                        Context.ACTIVITY_SERVICE))
                .getRunningTasks(1);
        if (task.isEmpty()) {
            return false;
        }
        return task
                .get(0)
                .topActivity
                .getPackageName()
                .equalsIgnoreCase(
                        context.getPackageName());
    }

    @Override
    protected void onStart() {
        super.onStart();

        beaconManager.connect(new
                                      BeaconManager.ServiceReadyCallback() {
                                          @Override
                                          public void onServiceReady() {
                                              try {
                                                  beaconManager.startMonitoring(
                                                          ALL_ESTIMOTE_BEACONS);
                                              } catch (RemoteException e) {
                                                  Log.d(TAG,
                                                          "Error while starting monitoring");
                                              }
                                          }
                                      });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context=this;
        Log.i(TAG, "CREATED Main Activity");
        beaconManager = new BeaconManager(this);
        //---by default you scan 5s and then wait 25s
        // for this demo, you will scan more
        // frequently---
        beaconManager.setBackgroundScanPeriod(
                TimeUnit.SECONDS.toMillis(1), 0);
        beaconManager.setMonitoringListener(new
                MonitoringListenerImpl(beaconManager));
        startMonitoringBeacon();

        // use this to start and trigger a service
        Intent i= new Intent(context, NetworkService.class);
        context.startService(i);

        setContentView(R.layout.registration);

        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        gender = (Spinner) findViewById(R.id.gender);
        contact = (EditText) findViewById(R.id.contact);
        emergency_name = (EditText) findViewById(R.id.emergency_name);
        emergency_contact = (EditText) findViewById(R.id.emergency_contact);


        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String _name = name.getText().toString();
                String _age = age.getText().toString();
                String _gender = gender.getSelectedItem().toString();
                String _contact = contact.getText().toString();
                String _emergency_name = emergency_name.getText().toString();
                String _emergency_contact = emergency_contact.getText().toString();


                // check if any of the fields are vaccant
                if (_name.equals("") || _contact.equals("") || _emergency_contact.equals("")) {
                    Toast.makeText(getApplicationContext(), "Mandatory field can't be empty", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    // Create user object and call register api
                    //    loginDataBaseAdapter.insertEntry(userName, password);
                    Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
                }

                sos.User user = new User();
                user.setName(_name);
                user.setAge(Integer.valueOf(_age));
                user.setGender(_gender);
                user.setEmergency_contact(_emergency_contact);
                user.setEmergency_name(_emergency_name);
                user.setGuid(IOS_GUID);
                user.setSelf_contact(_contact);

                Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();

                Long returnVal = null;
                try {
                    returnVal = new UserRegisterTask().execute(user).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if(returnVal == Long.valueOf(1l)){
                    Intent intent = new Intent(RegistrationActivity.this, SearchLocationsActivity.class);
                    intent.putExtra(GUID, IOS_GUID);
                    startActivity(intent);
                }
            }
        }
        );
    }

    private class UserRegisterTask extends AsyncTask<User, Integer, Long> {
        protected Long doInBackground(User... user) {

            try {
                Log.i("register_log", "trying registration");
                TTransport transport = new TSocket("100.96.51.84", 7911);
                TProtocol protocol = new TBinaryProtocol(transport);
                emergency_helper.Client client = new emergency_helper.Client(protocol);
                transport.open();
                client.register_user(user[0]);
                transport.close();
            } catch (TTransportException e) {
                Log.i("register_log", "Transport error");

                e.printStackTrace();
            } catch (TException e) {
                Log.i("register_log", "TException error");

                e.printStackTrace();
            }
            return Long.valueOf(1);

        }

        protected void onPostExecute(Long result) {
            Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.disconnect();

    }
}


