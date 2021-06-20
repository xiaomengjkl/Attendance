package edu.wong.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import edu.wong.entity.Attendance;
import edu.wong.entity.Leave;
import edu.wong.entity.TeacherCourse;
import okhttp3.Response;

public class JSONUtil {

    private static String json;
    private static String string;


    public static String getAttendanceJSON(Attendance attendance){
        JSONObject object = new JSONObject();
        try {
            object.put("sId",attendance.getsId());
            object.put("sLat",attendance.getsLat());
            object.put("sLon",attendance.getsLon());
            object.put("state",attendance.getState());
            object.put("cId",attendance.getsClassId());
            object.put("tId",attendance.gettId());
            object.put("code",attendance.getCode());
            json = object.toString();
        }catch (JSONException js){
            js.printStackTrace();
        }
        return json;
    }
    public static String getTAttendance(TeacherCourse ts){
        JSONObject object = new JSONObject();
        try {
            object.put("courseInfo",ts.getCourseInfo());
            object.put("Id",ts.getId());
            object.put("Lat",ts.getLat());
            object.put("Lon",ts.getLon());
            object.put("status",ts.getStatus());
            object.put("classId",ts.getClassId());
            json = object.toString();
        }catch (JSONException js){
            js.printStackTrace();
        }
        return json;
    }

    public static String getLeaveJSON(Leave leave) {
        JSONObject object = new JSONObject();
        try {
            object.put("id", leave.getId());
            object.put("start", leave.getStart());
            object.put("end", leave.getEnd());
            object.put("student_id", leave.getStudent_id());
            object.put("type", leave.getType());
            object.put("context", leave.getContext());
            object.put("status", leave.getStatus());
            object.put("remark", leave.getRemark());
            json = object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject getJSON(Response response) {
        JSONObject object = null;
        try {
            string = response.body().string();
//            Log.d("-----json", "getJSON: " + string);
            object = new JSONObject(string);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static JSONArray getJSONArray(Response response) {
        JSONArray array = null;
        try {
            string = response.body().string();
//            Log.d("-----json", "getJSON: " + string);
            array = new JSONArray(string);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }
}
