package edu.wong.home;

import android.app.Application;
import android.location.Location;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wong.activity.R;
import edu.wong.entity.Leave;
import edu.wong.entity.TeacherCourse;
import edu.wong.util.ConnUtil;
import edu.wong.util.JSONUtil;
import edu.wong.util.PortUtil;
import edu.wong.util.gpsTest;
import okhttp3.Response;

public class TAttendanceActivity extends AppCompatActivity {

    private TextView tv_code;
    private EditText edit_course;
    private Button btn_close;
    private Button btn_loc;
    private String s ;
    private final String TAG = "lll";
    private final  String sPort = ":80";
    private double latitude;
    private double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tattendance);
        tv_code = findViewById(R.id.tv_code);
        edit_course = findViewById(R.id.edit_t_info);
        btn_close = findViewById(R.id.btn_close); //关闭签到按钮

        btn_loc = findViewById(R.id.btn_tloc);
        btn_loc.setOnClickListener(new View.OnClickListener() {
            private int id;
            private JSONObject object;
            private int class_id;
            @Override
            public void onClick(View v) {
                s = edit_course.getText().toString();
                String info = getIntent().getStringExtra("info");
                try {
                    JSONArray array = new JSONArray(info);
                    object = new JSONObject(array.get(0).toString());

                    id = object.getInt("id");
                    class_id = object.getInt("class_id");

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
                TeacherCourse ts = new TeacherCourse();
                ts.setCourseInfo(s);
                ts.setId(id);
                ts.setLat(latitude);
                ts.setLon(longitude);
                ts.setStatus(1);
                ts.setClassId(class_id);

                PortUtil portUtil = new PortUtil(getApplicationContext());
                String port = portUtil.getPort();
                String url = "http://" + port +sPort+ "/attend_system_api/TAttendanceServlet?method=AddTeacherCourse";

                //发送数据
                toHttp(ts, url);
        }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            private int class_id;
            private JSONObject object;
            private int id;
            @Override
            public void onClick(View v) {
                String info = getIntent().getStringExtra("info");
                try {
                    JSONArray array = new JSONArray(info);
                    object = new JSONObject(array.get(0).toString());
                    id = object.getInt("id");
                    class_id = object.getInt("class_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                TeacherCourse ts = new TeacherCourse();
                ts.setId(id);
                ts.setClassId(class_id);
                PortUtil portUtil = new PortUtil(getApplicationContext());
                String port = portUtil.getPort();
                String url = "http://" + port +sPort+ "/attend_system_api/TAttendanceServlet?method=closeLocation";

                //发送数据
                toHttp1(ts, url);
            }
        });

    }
    //开启
    private void toHttp(TeacherCourse ts, String url) {
        ConnUtil connUtil = new ConnUtil() {
            @Override
            public void getOnResponse(Application application, Response response) {
                JSONObject json = JSONUtil.getJSON(response);
                Log.d("----------", "toHttp: "+ json);
                try {
                    String code = json.getString("code");
                    tv_code.setText(code);
                    boolean status = json.getBoolean("status");
                    if (status) {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "开启成功", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "开启失败，请稍后重试", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        String json = JSONUtil.getTAttendance(ts);
//        Log.d("----------", "toHttp: "+ json);
        connUtil.getStatus(url, json, getApplication());
    }
    //关闭
    private void toHttp1(TeacherCourse ts, String url) {
        ConnUtil connUtil = new ConnUtil() {
            @Override
            public void getOnResponse(Application application, Response response) {
                JSONObject json = JSONUtil.getJSON(response);
                Log.d("----------", "toHttp: "+ json);
                try {
                    boolean status = json.getBoolean("status");
                    if (status) {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "关闭成功", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "关闭失败", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        String json = JSONUtil.getTAttendance(ts);
//        Log.d("----------", "toHttp: "+ json);
        connUtil.getStatus(url, json, getApplication());
    }

}

