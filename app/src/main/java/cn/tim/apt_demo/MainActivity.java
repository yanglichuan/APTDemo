package cn.tim.apt_demo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import cn.tim.annotation.DIActivity;
import cn.tim.annotation.DIObject;
import cn.tim.annotation.DIView;

//@DIActivity
public class MainActivity extends AppCompatActivity {

    @DIActivity
    static class DDD{

        @DIObject
        LoginManager mLoginManager;

        public void run(Context context){
            DILogin.inject(context, this);
        }
    }

    @DIView(value = R.id.text)
    TextView textView;

    @DIView(value = R.id.text1)
    TextView textView1;

    @DIView(value = R.id.text2)
    TextView textView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DDD ddd = new DDD();
        ddd.run(this);


        Log.e("ylc", "onCreate: "+ ddd.mLoginManager.list);


//        textView.setText("Hello, JavaPoet!");
//        textView2.setText("Tim");


//        Log.e("ylc", "onCreate: "+ mLoginManager.list);
    }
}