package cn.tim.apt_demo;




import com.example.basecore.IEngine;

import cn.tim.annotation.DILoginEngine;


/**
 * @author (ylc)
 * @datetime 2021-09-01 11:16 GMT+8
 * @email yanglichuan@ushareit.com
 * @detail :
 */
@DILoginEngine
public class AliEngine implements IEngine {
    @Override
    public String getName(){
        return "fb";
    }
}
