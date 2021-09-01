package cn.tim.apt_demo;

import com.example.basecore.IEngine;

import java.util.ArrayList;
import java.util.Set;

/**
 * @author (ylc)
 * @datetime 2021-09-01 10:51 GMT+8
 * @email yanglichuan@ksjgs.com
 * @detail :
 */
public class LoginManager {

    ArrayList<IEngine> list = new ArrayList<>();

    public void add(IEngine engine){
        list.add(engine);
    }

    public void addAll(Set<IEngine>engines){
        list.addAll(engines);
    }
}