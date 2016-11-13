package com.aaron.view;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.aaron.utils.AudioManager;
import com.aaron.wechat.R;

/**
 * Created by Administrator on 2016/11/1.
 */
public class PressToSpeekButton extends Button implements AudioManager.AudioStateListener {
    private static final int STATE_NORMAL = 0;
    private static final int STATE_RECODING = 1;
    private static final int STATE_CANCLE = 2;
    //记录当前状态
    private int mCurrentState = STATE_NORMAL;
    //是否开始录音标志
    private boolean isRecording = false;
    //判断在Button上滑动距离，以判断 是否取消
    private static final int DISTANCE_Y_CANCEL = 50;
    //录音管理工具类
    private AudioManager mAudioManager;
    //记录录音时间
    private float mTime;
    // 是否触发longClick
    private boolean mReady;
    //录音准备
    private static final int MSG_AUDIO_PREPARED = 0x110;
    //音量发生改变
    private static final int MSG_VOICE_CHANGED = 0x111;
    //取消提示对话框
    private static final int MSG_DIALOG_DIMISS = 0x112;

    private DialogManager mDialogManager;
    private Runnable getVoiceLevelRunn = new Runnable() {
        @Override
        public void run() {
            while (isRecording) {

                try {
                    Thread.sleep(100);
                    mHandler.sendEmptyMessage(MSG_VOICE_CHANGED);
                    mTime += 0.1f;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUDIO_PREPARED:
                    isRecording = true;
                    mDialogManager.showDialog();
                    new Thread(getVoiceLevelRunn).start();
                    break;
                case MSG_VOICE_CHANGED:
                    mDialogManager.updateLevel(mAudioManager.getVoiceLevel(7));
                    break;
                case MSG_DIALOG_DIMISS:
                    mDialogManager.dismissDialog();
                    break;
            }

        }
    };


    public PressToSpeekButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDialogManager = new DialogManager(getContext());
        String dir = Environment.getExternalStorageDirectory() + "/wechat/audios";
        mAudioManager = AudioManager.getInstance(dir);
        mAudioManager.setOnAudioStateListener(this);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mAudioManager.prepareAudio();
                mReady = true;
                return false;
            }
        });
    }

    public PressToSpeekButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PressToSpeekButton(Context context) {
        this(context, null);
    }


    // 录音完成后的回调
    public interface AudioFinishRecorderCallBack {
        void onFinish(float seconds, String filePath);
    }

    private AudioFinishRecorderCallBack finishRecorderCallBack;

    public void setFinishRecorderCallBack(AudioFinishRecorderCallBack listener) {
        finishRecorderCallBack = listener;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();//手指的状态
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                changeState(STATE_RECODING);
                break;
            case MotionEvent.ACTION_MOVE:
                if (moveToCancle(x, y)) {
                    changeState(STATE_CANCLE);
                } else {
                    changeState(STATE_RECODING);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!mReady){
                    reset();
                    return super.onTouchEvent(event);
                }
                if (!isRecording || mTime < 0.6f) {
                    mDialogManager.tooShort();
                    mAudioManager.cancel();
                    mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1000);
                }
                if (mCurrentState == STATE_RECODING) {
                    mDialogManager.dismissDialog();
                    mAudioManager.release();
                    if (finishRecorderCallBack != null) {
                        finishRecorderCallBack.onFinish(mTime, mAudioManager.getCurrentFilePath());
                    }
                } else if (mCurrentState == STATE_CANCLE) {
                    mDialogManager.dismissDialog();
                    mAudioManager.cancel();
                }

                reset();
                break;

        }
        return super.onTouchEvent(event);
    }

    private void reset() {
        isRecording = false;
        mTime = 0;
        mReady = false;
        changeState(STATE_NORMAL);
    }

    private boolean moveToCancle(int x, int y) {//滑动取消判断
        if (x < 0 || x > getWidth()) return true;
        if (y < -50 || y > getHeight() + 50) return true;
        return false;
    }

    private void changeState(int state) {
        if (mCurrentState != state) {
            mCurrentState = state;
            switch (state) {
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.chat_press_speak_normal);
                    setText("按住说话");
                    break;

                case STATE_RECODING:
                    setBackgroundResource(R.drawable.chat_press_speak_pressed);
                    setText("松开结束");
                    if (isRecording) {
                        mDialogManager.recoreding();
                    }
                    break;

                case STATE_CANCLE:
                    setBackgroundResource(R.drawable.chat_press_speak_pressed);
                    setText("松开手指 取消发送");
                    mDialogManager.moveToCancel();
                    break;
            }
        }
    }

    @Override
    public void wellPrepared() {
        mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }
}
