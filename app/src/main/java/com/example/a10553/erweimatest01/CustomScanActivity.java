package com.example.a10553.erweimatest01;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomScanActivity extends Activity implements DecoratedBarcodeView.TorchListener {

    @BindView(R.id.btn_switch)
    Button btnSwitch;
    @BindView(R.id.dbv_custom)
    DecoratedBarcodeView dbvCustom;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scan);

        ButterKnife.bind(this);

        dbvCustom.setTorchListener(this);

        // 如果没有闪光灯功能，就去掉相关按钮
        if(!hasFlash()) {
            btnSwitch.setVisibility(View.GONE);
        }

        //重要代码，初始化捕获
        captureManager = new CaptureManager(this,dbvCustom);
        captureManager.initializeFromIntent(getIntent(),savedInstanceState);
        captureManager.decode();
    }

    // torch 手电筒
    @Override
    public void onTorchOn() {
        Toast.makeText(this,"torch on",Toast.LENGTH_LONG).show();
        isLightOn = true;
    }

    @Override
    public void onTorchOff() {
        Toast.makeText(this,"torch off", Toast.LENGTH_LONG).show();
        isLightOn = false;
    }

    // 判断是否有闪光灯功能
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    // 点击切换闪光灯
    @OnClick(R.id.btn_switch)
    public void swichLight(){
        if(isLightOn){
            dbvCustom.setTorchOff();
        }else{
            dbvCustom.setTorchOn();
        }
    }

    private CaptureManager captureManager;
    private boolean isLightOn = false;

    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        captureManager.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return dbvCustom.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

}
