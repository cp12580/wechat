package com.aaron.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.aaron.domain.User;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

/**
 * Created by Administrator on 2016/10/12.
 * 1.监听联系人变化，写入数据库
 * 2.连接断开服务器
 * 3.接收消息
 * 4.监听盆友圈动态
 *
 */
public class WeChatService extends Service {
    //
    //
    private String mCurrUsername;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mCurrUsername = intent.getStringExtra("username");
        AVIMClient.getInstance(User.getCurrentUser().getUsername()).open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e != null){
                    Log.d("Aaron",e.getMessage());
                }else {
                }
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AVIMClient.getInstance(User.getCurrentUser().getUsername()).close(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e != null){
                    Log.d("Aaron",e.getMessage());
                }
            }
        });
    }
}
