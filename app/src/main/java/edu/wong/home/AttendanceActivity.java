package edu.wong.home;

import android.app.Application;
import android.location.Location;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wong.activity.R;
import edu.wong.entity.Attendance;
import edu.wong.entity.TeacherCourse;
import edu.wong.util.ConnUtil;
import edu.wong.util.JSONUtil;
import edu.wong.util.PortUtil;
import edu.wong.util.gpsTest;
import okhttp3.Response;

public class AttendanceActivity extends AppCompatActivity {

    private final  String  TAG = "";

    private Button btn_loc;
    private final  String sPort = ":80";
    private EditText et;
    private EditText et_tid;
    private JSONObject object;
    private int id;
    private double latitude;
    private  double longitude;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        et_tid = findViewById( R.id.edit_tid);
        btn_loc = findViewById(R.id.btn_sAttendance);
        et = findViewById(R.id.edit_s_num);
        btn_loc.setOnClickListener(new View.OnClickListener(){
            int tId;
            @Override
            public void onClick(View v) {
                code = et.getText().toString();
                tId = Integer.valueOf(et_tid.getText().toString()) ;
                String info = getIntent().getStringExtra("info");
                try {
                    JSONArray array = new JSONArray(info);
                    object = new JSONObject(array.get(0).toString());
                    id = object.getInt("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                gpsTest g = new gpsTest(getApplicationContext());
                Location l = g.getLocation();
                if (l != null){
                    latitude = l.getLatitude();
                    longitude = l.getLongitude();
//                    Log.d(TAG,"lat"+latitude+"lon"+longitude);
//                    Toast.makeText(getApplicationContext(),"lat:"+latitude+"\n lon:"+longitude,Toast.LENGTH_SHORT).show();
                }
                Attendance sa = new Attendance();
                sa.setsId(id);
                sa.setsLat(latitude);
                sa.setsLon(longitude);
                sa.setState(1);
                sa.setCode(code);
                sa.settId(tId);
                PortUtil portUtil = new PortUtil(getApplicationContext());
                String port = portUtil.getPort();
                String url = "http://" + port +sPort+ "/attend_system_api/AttendanceServlet?method=updateAttendance";

                //发送数据
                toHttp(sa, url);
            }
        });

    }
    private void toHttp(Attendance ts, String url) {
        ConnUtil connUtil = new ConnUtil() {
            @Override
            public void getOnResponse(Application application, Response response) {
                JSONObject json = JSONUtil.getJSON(response);
                Log.d("----------", "toHttp: "+ json);
                try {
                    boolean status = json.getBoolean("status");
                    if (status) {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "签到成功", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "签到失败", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        String json = JSONUtil.getAttendanceJSON(ts);
//        Log.d("----------", "toHttp: "+ json);
        connUtil.getStatus(url, json, getApplication());
    }


}
