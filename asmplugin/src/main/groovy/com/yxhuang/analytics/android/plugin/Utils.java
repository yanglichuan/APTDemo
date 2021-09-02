package com.yxhuang.analytics.android.plugin;

/**
 * created by charlie on 2021/7/12
 */
public class  Utils {


    public static boolean ignorePackageNames(String className) {
        //命中白名单返回false
//        for (packageName in whitePackageNames) {
//            if (className.startsWith(packageName, true)) {
//                return false
//            }
//        }


        for (int i = 0; i < blackPackageNames.length; i++) {
            if (className.toLowerCase().startsWith(blackPackageNames[i].toLowerCase())) {
                System.out.println("过滤 $className, $packageName");
                LogUtil.e("bbbb", "过滤 $className, $packageName");
                return true;
            }
        }



        return false;
    }


    /**
     * 白名单
     */
//    private val whitePackageNames = arrayOf("")


    /**
     * 黑名单
     */
    private static String[] blackPackageNames = new String[]{
            "kotlin",
            "java",
            "android",
            "androidx",
            "com/google"
    };


}