package cn.tim.apt_demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basecore.LoginManager;

import cn.tim.annotation.DILoginManager;
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        long l = System.currentTimeMillis();
//        DILoginIn_MainActivity.inject(this);

        LoginManager mLoginManager = new LoginManager();
        Log.e("ylc", "time: end "+ (System.currentTimeMillis()-l));
        Log.e("ylc", "time: end "+mLoginManager.getList());


        findViewById(R.id.text2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, MainActivity2.class));
            }
        });



//        textView.setText("Hello, JavaPoet!");
//        textView2.setText("Tim");

//        Log.e("ylc", "onCreate: "+ mLoginManager.list);
    }
}