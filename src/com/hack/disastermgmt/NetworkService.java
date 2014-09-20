package com.hack.disastermgmt;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import sos.Location;
import sos.emergency_helper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class NetworkService extends Service {
    Context context = this;
    protected static final String TAG =
            "EstimoteiBeacon";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Worker()).start();
        return Service.START_STICKY;
    }


    class Worker implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Map<String, List<Location>> records = DatabaseHandler.getRecords();
                    Log.i(TAG, "About to upload" + records.toString());
                    if (!records.isEmpty()) {
                        uploadRecords(records);
                        Log.i(TAG, "Uploaded");
                    }
                    Thread.sleep(TimeUnit.SECONDS.toMillis(1));

                } catch (Exception e) {
                    Log.i(TAG, "Exception" + e.getCause());
                }
            }
        }


        private void uploadRecords(Map<String, List<Location>> records) {
            TTransport transport;
            try {
                transport = new TSocket("100.96.51.84", 7911);
                TProtocol protocol = new TBinaryProtocol(transport);
                transport.open();
                emergency_helper.Client client = new emergency_helper.Client(protocol);
                for (String key : records.keySet()) {
                    client.uploadLocationData(key, records.get(key));
                }
                transport.close();
            } catch (Exception e) {
                Log.i(TAG, "ERROR");
            }
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

}

