package cn.tim.apt_demo;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.example.basecore.LoginManager;

import cn.tim.annotation.DILoginManager;
public class MainActivity extends AppCompatActivity {

    @DILoginManager
    LoginManager mLoginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        long l = System.currentTimeMillis();
        DILoginIn_MainActivity.inject(this);

        Log.e("ylc", "time: end "+ (System.currentTimeMillis()-l));
        Log.e("ylc", "time: end "+mLoginManager.list);

//        textView.setText("Hello, JavaPoet!");
//        textView2.setText("Tim");

//        Log.e("ylc", "onCreate: "+ mLoginManager.list);
    }
}