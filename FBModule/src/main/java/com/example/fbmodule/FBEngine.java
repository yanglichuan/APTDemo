package com.example.fbmodule;




import androidx.annotation.Keep;

import com.example.basecore.IEngine;

import cn.tim.annotation.DIEngine;


/**
 * @author (ylc)
 * @datetime 2021-09-01 11:16 GMT+8
 * @email yanglichuan@ksjgs.com
 * @detail :
 */
@Keep
@DIEngine(pkg = "com.example.fbmodule.FBEngine")
public class FBEngine implements IEngine {
    @Override
    public String getName(){
        return "fb";
    }
}
