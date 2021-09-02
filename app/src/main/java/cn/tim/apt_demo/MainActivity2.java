package cn.tim.apt_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.basecore.LoginManager;

import cn.tim.annotation.DILoginManager;

public class MainActivity2 extends AppCompatActivity {

    @DILoginManager
    LoginManager TransformLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);


        DILoginIn_MainActivity2.inject(this);



        Log.e("ylc", "time: end "+mLoginManager.list);

    }
}