package cn.tim.apt_demo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basecore.util.ContextHolder;

import cn.tim.annotation.DIActivity;
import cn.tim.annotation.DIObject;
import cn.tim.annotation.DIView;

public class MainActivity extends AppCompatActivity {

    static class DDD{

        @DIObject
        LoginManager mLoginManager;

        public void run(Context context){
            DiLoginInDDD.inject(context, this);
        }
    }

    @DIView(value = R.id.text)
    TextView textView;

    @DIView(value = R.id.text1)
    TextView textView1;

    @DIView(value = R.id.text2)
    TextView textView2;

    @DIObject
    LoginManager mLoginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContextHolder.mContext = this.getApplicationContext();




        long l = System.currentTimeMillis();
        DiLoginInMainActivity.inject(this, this);


        Log.e("ylc", "time: "+ (System.currentTimeMillis()-l));
        Log.e("ylc", "onCreate222: "+ mLoginManager.list);



        l = System.currentTimeMillis();

        try {
//            LoginManager loginManager = (LoginManager) Class.forName("cn.tim.apt_demo.LoginManager").newInstance();
//            java.util.Set<String> fileNameByPackageName = com.example.basecore.util.ClassUtils.getFileNameByPackageName(this, "com.ushareit.login.apt");

            Class.forName("com.ushareit.login.apt.Provider_FBEngine").newInstance();
            Class.forName("com.ushareit.login.apt.Provider_GGEngine").newInstance();



        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("ylc", "time: end "+ (System.currentTimeMillis()-l));





//        textView.setText("Hello, JavaPoet!");
//        textView2.setText("Tim");


//        Log.e("ylc", "onCreate: "+ mLoginManager.list);
    }
}