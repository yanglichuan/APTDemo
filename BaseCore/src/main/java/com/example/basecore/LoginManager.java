package com.example.basecore;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author (ylc)
 * @datetime 2021-09-01 10:51 GMT+8
 * @email yanglichuan@ushareit.com
 * @detail :
 */
public class LoginManager {

    public LoginManager() {
        initEngine();
    }

    /**
     * asm会自动注入
     */
    private void initEngine() {

    }

    public void asmCall(String engine){
        try{
            IEngine iEngine = (IEngine) Class.forName(engine).newInstance();
            add(iEngine);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
//
//    public void asmCall(String engine){
//        try{
//            IEngine iEngine = (IEngine) Class.forName(engine).getDeclaredMethod("get").invoke(null);
//            add(iEngine);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }

    public ArrayList<IEngine> getList() {
        return list;
    }

    private ArrayList<IEngine> list = new ArrayList<>();

    private void add(IEngine engine) {
        list.add(engine);
    }
}
