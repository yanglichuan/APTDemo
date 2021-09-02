package cn.tim.apt_demo;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import cn.tim.annotation.DIObject;
public class MainActivity extends AppCompatActivity {

    @DIObject
    LoginManager mLoginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        long l = System.currentTimeMillis();
        DiLoginInMainActivity.inject(this, this);

        Log.e("ylc", "time: end "+ (System.currentTimeMillis()-l));
        Log.e("ylc", "time: end "+mLoginManager.list);

//        textView.setText("Hello, JavaPoet!");
//        textView2.setText("Tim");

//        Log.e("ylc", "onCreate: "+ mLoginManager.list);
    }
}