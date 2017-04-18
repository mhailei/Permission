package com.mhailei.permission;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class PermissionActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mCallPhone;
    private Button mDownload;
    private FileOutputStream mFos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x.Ext.init(getApplication());
        initView();
    }

    //初始化UI
    private void initView() {
        mCallPhone = ((Button) findViewById(R.id.btn_call_phone));
        mDownload = ((Button) findViewById(R.id.btn_download));

        mCallPhone.setOnClickListener(this);
        mDownload.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_call_phone:
                callPhone();
                break;
            case R.id.btn_download:

                break;
            default:
                break;
        }
    }

    //拨打电话
    private void callPhone() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //TO
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            doCallPhone();
        }

    }

    private void doCallPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + "10086");
        intent.setData(data);
        startActivity(intent);
    }

    private void sdCardPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            doSDCardPermission();
        }

    }

    private void doSDCardPermission() {
        x.http().get(new RequestParams("http://img02.tooopen.com/images/20140504/sy_60294738471.jpg"), new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                String path = Environment.getExternalStorageDirectory().getPath();
                try {
                    FileOutputStream mFos = new FileOutputStream(new File(path));
                    mFos.write(result.getBytes());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();

                } finally {
                    try {
                        mFos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                return false;
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                //打电话权限回调处理
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doCallPhone();
                } else {
                    //提示用户权限未被授予

                }
                break;

            case 0:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doSDCardPermission();
                } else {
                    //提示未授权
                }
                break;
            default:
                break;
        }


    }
}
