package com.example.werewolfkiller.activity.poisearch;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;

public class Utils {
    /**
     * 隐藏键盘
     */
    public static void myToast(Activity activity, String res){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(),res, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void hideKeyBoard(Activity activity) {
        if (null == activity) {
            return;
        }
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View v = activity.getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
    private static final double EARTH_RADIUS = 6371393;

    /**
     * 通过AB点经纬度获取距离 整数
     * @param pointA A点(经，纬)
     * @param pointB B点(经，纬)
     * @return 距离(单位：米)
     */
    public static long getDistance(LatLng pointA, LatLng pointB) {
        if (pointA==null){
            pointA=new LatLng(0,0);
        }
        // 经纬度（角度）转弧度。弧度用作参数，以调用Math.cos和Math.sin
        // A经弧度
        double radiansAX = Math.toRadians(pointA.latitude);
        // A纬弧度
        double radiansAY = Math.toRadians(pointA.longitude);
        // B经弧度
        double radiansBX = Math.toRadians(pointB.latitude);
        // B纬弧度
        double radiansBY = Math.toRadians(pointB.longitude);
        // 公式中“cosβ1cosβ2cos（α1-α2）+sinβ1sinβ2”的部分，得到∠AOB的cos值
        double cos = Math.cos(radiansAY) * Math.cos(radiansBY) * Math.cos(radiansAX - radiansBX)
                + Math.sin(radiansAY) * Math.sin(radiansBY);
        // 反余弦值
        double acos = Math.acos(cos);
        // 最终结果
        double h = EARTH_RADIUS * acos;

        //四舍五入
        long f1 = Math.round(h);
        //保留小数后两位
       /* BigDecimal b = new BigDecimal(h);
        double f1 = b.setScale(2, RoundingMode.HALF_UP).doubleValue();*/
        return f1;
    }

    /**
     * 转化为弧度(rad)
     * */
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }


    public static double getDistance2(LatLng lat1, LatLng lat2) {
        if (lat1==null){
            lat1=new LatLng(0,0);
        }

        double radLat1 = rad(lat1.latitude);
        double radLat2 = rad(lat2.latitude);

        double radLon1 = rad(lat1.longitude);
        double radLon2 = rad(lat2.longitude);

        if (radLat1 < 0)
            radLat1 = Math.PI / 2 + Math.abs(radLat1);// south
        if (radLat1 > 0)
            radLat1 = Math.PI / 2 - Math.abs(radLat1);// north
        if (radLon1 < 0)
            radLon1 = Math.PI * 2 - Math.abs(radLon1);// west
        if (radLat2 < 0)
            radLat2 = Math.PI / 2 + Math.abs(radLat2);// south
        if (radLat2 > 0)
            radLat2 = Math.PI / 2 - Math.abs(radLat2);// north
        if (radLon2 < 0)
            radLon2 = Math.PI * 2 - Math.abs(radLon2);// west
        double x1 = EARTH_RADIUS * Math.cos(radLon1) * Math.sin(radLat1);
        double y1 = EARTH_RADIUS * Math.sin(radLon1) * Math.sin(radLat1);
        double z1 = EARTH_RADIUS * Math.cos(radLat1);

        double x2 = EARTH_RADIUS * Math.cos(radLon2) * Math.sin(radLat2);
        double y2 = EARTH_RADIUS * Math.sin(radLon2) * Math.sin(radLat2);
        double z2 = EARTH_RADIUS * Math.cos(radLat2);

        double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)+ (z1 - z2) * (z1 - z2));
        //余弦定理求夹角
        double theta = Math.acos((EARTH_RADIUS * EARTH_RADIUS + EARTH_RADIUS * EARTH_RADIUS - d * d) / (2 * EARTH_RADIUS * EARTH_RADIUS));

        double tmp=theta * EARTH_RADIUS;

        double dist = Math.round(tmp);

        return dist;
    }



}
