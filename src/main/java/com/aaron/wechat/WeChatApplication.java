package com.aaron.wechat;

import android.app.Application;

import com.aaron.domain.AddRequest;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;

/**
 * Created by Administrator on 2016/10/7.
 */
public class WeChatApplication extends Application {
    private static final String APP_ID ="0E8qbRpr6G42WBKeDe97FTKu-gzGzoHsz" ;
    private static final String APP_KEY ="dQIQLpvg0dASNalRkcTpGjOF" ;
    private static WeChatApplication app;
    @Override
    public void onCreate() {
        super.onCreate();
        AVObject.registerSubclass(AddRequest.class);
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,APP_ID, APP_KEY);
    }
    public static WeChatApplication getInstance(){
        if (app == null){
            synchronized (WeChatApplication.class){
                if (app == null){
                    app = new WeChatApplication();
                }
            }
        }
        return app;
    }
}
