package com.example.fbmodule;




import com.example.basecore.IEngine;

import cn.tim.annotation.DICreater;
import cn.tim.annotation.DIEngine;


/**
 * @author (ylc)
 * @datetime 2021-09-01 11:16 GMT+8
 * @email yanglichuan@ksjgs.com
 * @detail :
 */
@DIEngine
public class FBEngine implements IEngine {
    @Override
    public String getName(){
        return "fb";
    }
}
