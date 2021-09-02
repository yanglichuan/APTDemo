package com.example.ggmodule;




import androidx.annotation.Keep;

import com.example.basecore.IEngine;

import cn.tim.annotation.DILoginEngine;

/**
 * @author (ylc)
 * @datetime 2021-09-01 11:16 GMT+8
 * @email yanglichuan@ushareit.com
 * @detail :
 */
@Keep
@DILoginEngine
public class GGEngine implements IEngine {

    @Override
    public String getName(){
        return "gg";
    }
}
