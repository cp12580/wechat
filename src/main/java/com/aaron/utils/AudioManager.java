package com.aaron.utils;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Administrator on 2016/11/4.
 */
public class AudioManager {
    private MediaRecorder mMediaRecorder;
    //录音文件
    private String mDir;
    //当前录音文件目录
    private String mCurrentFilePath;
    //单例模式
    private static AudioManager mInstance;
    //是否准备好
    private boolean isPrepare;

    //私有构造方法
    private AudioManager(String dir) {
        mDir = dir;
    }

    //对外公布获取实例的方法
    public static AudioManager getInstance(String dir) {
        if (mInstance == null) {
            synchronized (AudioManager.class) {
                if (mInstance == null) {
                    mInstance = new AudioManager(dir);
                }
            }
        }
        return mInstance;
    }

    public void prepareAudio() {
        isPrepare = false;
        File dir = new File(mDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = generateFileName();
        File file = new File(dir, fileName);
        mCurrentFilePath = file.getAbsolutePath();
        mMediaRecorder = new MediaRecorder();
        try {
            mMediaRecorder.setOutputFile(file.getAbsolutePath());
            // 设置MediaRecorder的音频源为麦克风
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置音频格式为RAW_AMR
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            // 设置音频编码为AMR_NB
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            // 准备录音

            mMediaRecorder.prepare();
            isPrepare = true;
            if (mAudioStateListener != null){
                mAudioStateListener.wellPrepared();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //获取音量
    public int getVoiceLevel(int maxlevel) {
        if (isPrepare) {
            Util.L(mMediaRecorder.getMaxAmplitude() + "");
            try {
                // getMaxAmplitude返回的数值最大是32767
                return maxlevel * mMediaRecorder.getMaxAmplitude() / 32768 + 1;//返回结果1-7之间
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 1;
    }

    public void release() {
        if (mMediaRecorder != null) {
            try {
                mMediaRecorder.stop();
            } catch (IllegalStateException e) {
                // TODO 如果当前java状态和jni里面的状态不一致，
                //e.printStackTrace();
                mMediaRecorder = null;
                mMediaRecorder = new MediaRecorder();
            }
            mMediaRecorder.reset();
            mMediaRecorder = null;
        }
    }

    public void cancel() {
        release();
        if (mCurrentFilePath != null) {
            //取消录音后删除对应文件
            File file = new File(mCurrentFilePath);
            file.delete();
            mCurrentFilePath = null;
        }

    }

    private String generateFileName() {
        //随机生成不同的UUID
        return UUID.randomUUID().toString() + ".amr";
    }

//     获取当前文件路径
    public String getCurrentFilePath() {

        return mCurrentFilePath;
    }

     // 录音准备工作完成回调接口
    public interface AudioStateListener {
        void wellPrepared();
    }

    public AudioStateListener mAudioStateListener;

    public void setOnAudioStateListener(AudioStateListener listener) {
        mAudioStateListener = listener;
    }

}
