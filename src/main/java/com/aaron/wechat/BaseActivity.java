package com.aaron.wechat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

/**
 * Created by Administrator on 2016/10/7.
 */
public abstract class BaseActivity extends Activity {
    public ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        initData();
        initView();
    }

    protected abstract void initView();

    protected void initData(){};

    protected abstract int getLayoutID();

    public void showDialog(String title,String msg){
        mDialog = ProgressDialog.show(this,title,msg,true,false);
    }

    public void dismissDialog(){
        if (mDialog != null){
            mDialog.dismiss();
        }
    }


}
