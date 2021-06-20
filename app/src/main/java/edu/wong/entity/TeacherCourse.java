package edu.wong.entity;

public class TeacherCourse {
    public int getId() {
        return tId;
    }

    public void setId(int id) {
        this.tId = id;
    }

    private int tId;
    private String courseInfo;
    private double lat;
    private double lon;
    private int classId;
    private int status;
    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

//1 为开启中 0为未开启

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCourseInfo() {
        return courseInfo;
    }

    public void setCourseInfo(String courseInfo) {
        this.courseInfo = courseInfo;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
