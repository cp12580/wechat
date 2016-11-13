package com.aaron.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/10/7.
 */
public class Util {
    public static void T(Context context, String toastMsg){
        Toast.makeText(context,toastMsg,Toast.LENGTH_SHORT).show();
    }
    public static void L(String log){
        Log.d("Aaron",log);
    }
}
