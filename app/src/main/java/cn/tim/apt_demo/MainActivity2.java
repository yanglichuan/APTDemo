package cn.tim.apt_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.basecore.LoginManager;

import cn.tim.annotation.DILoginManager;

public class MainActivity2 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);



        LoginManager mLoginManager = new LoginManager();
        Log.e("ylc", "time: end "+mLoginManager.getList());

    }
}