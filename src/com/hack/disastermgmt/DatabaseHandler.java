package com.hack.disastermgmt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import sos.Location;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler  {
 	static Map<String,List<Location>> store=new ConcurrentHashMap<String, List<Location>>();
 	// Adding new contact
 		public static void addRecord(String guid,Location location) {
 		   List<Location> tmp=null;
 		   boolean duplicate=false;
 			if(!store.containsKey(guid)){
 			  tmp=new ArrayList<Location>();
 			}else{
 			    tmp=store.get(guid);
 			}
 			for(Location l:tmp){
 			    if(l.getLatitude()==location.getLatitude() && l.getLongitude()==location.getLongitude()){
 				duplicate=true;
 				break;
 			    }
 			}
 			if(!duplicate){
 			 	tmp.add(location);
			    store.put(guid, tmp);
 			}
 		}
 		
 		public static List<Location> getRecords(String guid){
 		   List<Location> contactList = store.get(guid);
 			return contactList;
 		}
 		public static Map<String,List<Location>> getRecords(){
 		    return store;
 		}
}
