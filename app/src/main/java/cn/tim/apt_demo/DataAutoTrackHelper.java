package cn.tim.apt_demo;

import android.app.Activity;
import android.view.View;

import androidx.annotation.Keep;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yxhuang
 * Date: 2020/9/29
 * Description: 用于数据的埋点
 */
@Keep
public class DataAutoTrackHelper {

    @Keep
    public static void trackViewOnClick(View view) {
        try {
            JSONObject jsonObject = new JSONObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
