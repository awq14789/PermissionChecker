package cn.yayi365.permissiontest;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import cn.yayi365.permission.Permission;
import cn.yayi365.permission.PermissionChecker;
import cn.yayi365.permission.PermissionUtil;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    @Permission("TEST1")
    TextView textView1;

    @Permission()
    TextView textView2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            new PermissionChecker.Builder(MainActivity.this)
                    .currentPermission("" + msg.what)
                    .permissionUtil(new PermissionUtil() {
                        @Override
                        public boolean checkPermission(String permission, String currentPermission) {
                            Log.d(TAG, "checkPermission: permission-->" + permission + "  currentPermission-->" + currentPermission);
                            return !TextUtils.isEmpty(permission);
                        }

                        @Override
                        public void onPermissionResult(Object obj, boolean isHasPermission) {
                            if (null != obj && obj instanceof View) {
                                ((View) obj).setVisibility(isHasPermission ? View.VISIBLE : View.GONE);
                            }
                        }
                    })
                    .check();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = (TextView) findViewById(R.id.tv_test1);
        textView2 = (TextView) findViewById(R.id.tv_test2);

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);
            }
        }.start();
    }
}
