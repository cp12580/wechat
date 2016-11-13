package com.aaron.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.utils.Util;
import com.aaron.wechat.R;

/**
 * Created by Administrator on 2016/11/3.
 */
public class DialogManager {
    private Dialog mDialog;
    private ImageView mIvState;
    private ImageView mIvLevel;
    private TextView mTvLable;
    private Context mContext;


    public DialogManager(Context context){
        mContext = context;
    }

    public void showDialog(){
        mDialog = new Dialog(mContext, R.style.theme_audio_dialog);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_audio,null);
        mDialog.setContentView(view);

        mIvState = (ImageView) mDialog.findViewById(R.id.iv_record_state);
        mIvLevel = (ImageView) mDialog.findViewById(R.id.iv_record_level);
        mTvLable = (TextView) mDialog.findViewById(R.id.tv_record_lable);

        mDialog.show();

    }
    public void recoreding(){
        if (mDialog != null && mDialog.isShowing()){
            mIvState.setVisibility(View.VISIBLE);
            mIvLevel.setVisibility(View.VISIBLE);
            mTvLable.setVisibility(View.VISIBLE);

            mIvState.setImageResource(R.drawable.recorder);
            mTvLable.setText("手指上划取消发送");
            Util.L("show");
        }

    }
    public void moveToCancel(){
        if (mDialog != null && mDialog.isShowing()){
            mIvState.setVisibility(View.VISIBLE);
            mIvLevel.setVisibility(View.GONE);
            mTvLable.setVisibility(View.VISIBLE);

            mIvState.setImageResource(R.drawable.cancel);
            mTvLable.setText("松开手指取消发送");

        }
    }
    public void tooShort(){
        if (mDialog != null && mDialog.isShowing()){
            mIvState.setVisibility(View.VISIBLE);
            mIvLevel.setVisibility(View.GONE);
            mTvLable.setVisibility(View.VISIBLE);

            mIvState.setImageResource(R.drawable.voice_to_short);
            mTvLable.setText("录音时间太短");

        }
    }
    public void dismissDialog(){
        if (mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
            mDialog = null;

        }
    }

    public void updateLevel(int level){
        if (mDialog != null && mDialog.isShowing()){
//            mIvState.setVisibility(View.VISIBLE);
//            mIvLevel.setVisibility(View.VISIBLE);
//            mTvLable.setVisibility(View.VISIBLE);

            mIvState.setImageResource(R.drawable.recorder);
            mTvLable.setText("手指上划取消发送");
            int resId = mContext.getResources().getIdentifier("v" + level, "drawable", mContext.getPackageName());
            mIvLevel.setImageResource(resId);
        }
    }
}

