package com.hack.disastermgmt;

public class Record {

    private  String guid,location,time;

    public Record(){};
    @Override
    public String toString() {
	return "Record [guid=" + guid + ", location=" + location + ", time="
		+ time + "]";
    }
    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGuid() {
        return guid;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public Record(String guid, String location, String time) {
	super();
	this.guid = guid;
	this.location = location;
	this.time = time;
    }
}
