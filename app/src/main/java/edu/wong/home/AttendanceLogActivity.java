package edu.wong.home;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.wong.activity.Login;
import edu.wong.activity.R;
import edu.wong.entity.Attendance;
import edu.wong.entity.Leave;
import edu.wong.util.ConnUtil;
import edu.wong.util.JSONUtil;
import edu.wong.util.PortUtil;
import okhttp3.Response;

public class AttendanceLogActivity extends AppCompatActivity {

    private ListView lv;
    private JSONObject object;
    private int id;
    private List<Map<String, Object>> list;

    private final String sPort = ":80";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_log);
        initView();

        showList();
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            list = (List<Map<String, Object>>) msg.obj;
            //填充数据
            setData(list);
        }
    };


    private void showList() {
        //获取链接
        PortUtil portUtil = new PortUtil(getApplicationContext());
        String port = portUtil.getPort();
        String url = "http://" + port + sPort+"/attend_system_api/AttendanceServlet?method=showAttendanceList";

        //获取要查询的id
        //如果是老师就查询自己的课程签到状况
        if (Login.uType == 2) {
            String info = getIntent().getStringExtra("info");
            try {
                JSONArray array = new JSONArray(info);
                object = new JSONObject(array.get(0).toString());
                id = object.getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //生成对象
        Attendance attendance = new Attendance();
        attendance.settId(id);

        toHttp(url, attendance);
    }

    private void toHttp(String url, final Attendance attendance) {
        ConnUtil connUtil = new ConnUtil() {
            @Override
            public void getOnResponse(Application application, Response response) {
                JSONArray json = JSONUtil.getJSONArray(response);
                Log.d("---------name", "getOnResponse: "+ json);
                list = new ArrayList<>();
                for (int i = 0; i < json.length(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    try {
                        JSONObject o = (JSONObject) json.get(i);
                        Iterator<String> keys = o.keys();
                        while (keys.hasNext()) {
                            String k = keys.next();
                            if (k.equals("state")) {
                                if ((int)o.get(k) == 0) {
                                    map.put(k, "未签到");
                                } else if ((int)o.get(k) == 1) {
                                    map.put(k, "已签到");
                                }
                                continue;
                            }
                            map.put(k, o.get(k));
                        }
                        list.add(map);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Message message = Message.obtain();
                message.obj = list;
                handler.sendMessage(message);
            }
        };
        String attendanceJSON = JSONUtil.getAttendanceJSON(attendance);
        connUtil.getStatus(url, attendanceJSON, getApplication());
    }

    private void setData(List<Map<String, Object>> list) {
        String[] from = {"sId", "state"};
        int[] to = {R.id.attend_id, R.id.attend_status,};
        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), list, R.layout.attendance_log, from, to);
        lv.setAdapter(adapter);
    }

    private void initView() {
        lv = findViewById(R.id.lv_showAttendance);
    }
}
