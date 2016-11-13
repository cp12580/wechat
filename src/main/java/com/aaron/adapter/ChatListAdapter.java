package com.aaron.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.dao.ContactInfo;
import com.aaron.dao.DBManager;
import com.aaron.domain.User;
import com.aaron.event.PreviewImageEvent;
import com.aaron.wechat.R;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avos.avoscloud.im.v2.messages.AVIMVideoMessage;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/18.
 */
public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  implements View.OnClickListener{
    private final int SEND = 100;
    private final int SEND_MSG = 101;
    private final int SEND_IMAGE = 102;
    private final int SEND_VOICE = 103;
    private final int SEND_LOCATION = 104;
    private final int SEND_VIDEO = 105;


    private final int RECEIVE = 200;
    private final int RECEIVE_MSG = 201;
    private final int RECEIVE_IMAGE = 202;
    private final int RECEIVE_VOICE = 203;
    private final int RECEIVE_LOCATION = 204;
    private final int RECEIVE_VIDEO = 205;

    private final int ITEM_UNKNOWN = 300;
    // 时间间隔最小为十分钟
    private final static long TIME_INTERVAL = 1000 * 60 * 3;

    private List<AVIMMessage> messageList;
    private Context mContext;
    private AVIMConversation mConversation;
    private ContactInfo mContactInfo;
    private ContactInfo meInfo;
    private int mode = 1;//1是单聊2是群聊


    public ChatListAdapter(Context c, ContactInfo contactInfo) {
        mContext = c;
        mContactInfo = contactInfo;
        messageList = new ArrayList<>();
        meInfo = DBManager.getDbManager(c, User.getCurrentUser().getUsername()).
                queryContactInfoByUsername(User.getCurrentUser().getUsername());
    }

    public void setConversation(AVIMConversation conversation) {
        mConversation = conversation;
    }

    public void setMessages(List<AVIMMessage> messages) {
        messageList.clear();
        messages.addAll(messages);
        notifyDataSetChanged();
    }

    /**
     * 添加多条消息记录
     *
     * @param messages
     */
    public void addMessageList(List<AVIMMessage> messages) {
        messageList.addAll(0, messages);
        notifyDataSetChanged();
    }


    /**
     * 添加消息记录
     *
     * @param message
     */
    public void addMessage(AVIMMessage message) {
        messageList.addAll(Arrays.asList(message));
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case SEND_MSG:
                View sendMsgView = LayoutInflater.from(mContext).inflate(R.layout.row_send_message, parent, false);
                return new SendMsgViewHolder(sendMsgView);
            case SEND_IMAGE:
                View sendImgView = LayoutInflater.from(mContext).inflate(R.layout.row_send_picture, parent, false);
                ImageView ivSendimg  = (ImageView) sendImgView.findViewById(R.id.iv_picture_sendimg);
                ivSendimg.setOnClickListener(this);
                return new SendImgViewHolder(sendImgView);
            case SEND_VOICE:

            case SEND_LOCATION:

                break;
            case SEND_VIDEO:

                break;
            case RECEIVE_MSG:
                View recMsgView = LayoutInflater.from(mContext).inflate(R.layout.row_received_message, parent, false);
                return new ReceiveMsgViewHolder(recMsgView);
            case RECEIVE_IMAGE:
                View receImgView = LayoutInflater.from(mContext).inflate(R.layout.row_received_picture, parent, false);
                return new ReceImgViewHolder(receImgView);
            case RECEIVE_VOICE:

                break;
            case RECEIVE_LOCATION:

                break;
            case RECEIVE_VIDEO:

                break;
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        AVIMMessage message = messageList.get(position);
        if (message != null && fromMe(message)) {
            if (message instanceof AVIMTextMessage) {
                return SEND_MSG;
            } else if (message instanceof AVIMImageMessage) {
                return SEND_IMAGE;
            } else if (message instanceof AVIMAudioMessage) {
                return SEND_VOICE;
            } else if (message instanceof AVIMLocationMessage) {
                return SEND_LOCATION;
            } else if (message instanceof AVIMVideoMessage) {
                return SEND_VIDEO;
            }
        } else {
            if (message instanceof AVIMTextMessage) {
                return RECEIVE_MSG;
            } else if (message instanceof AVIMImageMessage) {
                return RECEIVE_IMAGE;
            } else if (message instanceof AVIMAudioMessage) {
                return RECEIVE_VOICE;
            } else if (message instanceof AVIMLocationMessage) {
                return RECEIVE_LOCATION;
            } else if (message instanceof AVIMVideoMessage) {
                return RECEIVE_VIDEO;
            }
        }

        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AVIMMessage message = messageList.get(position);
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case SEND_MSG:
                AVIMTextMessage msg = (AVIMTextMessage) message;
                if (showTime(position)) {
                    ((SendMsgViewHolder) holder).mTimestampSendmsg.setVisibility(View.VISIBLE);
                    ((SendMsgViewHolder) holder).mTimestampSendmsg.setText(millisecsToDateString(msg.getTimestamp()));
                } else {
                    ((SendMsgViewHolder) holder).mTimestampSendmsg.setVisibility(View.GONE);
                }
                ((SendMsgViewHolder) holder).mTvChatcontentSendmsg.setText(msg.getText());
                switch (message.getMessageStatus()) {
                    case AVIMMessageStatusFailed:
                        ((SendMsgViewHolder) holder).mMsgStatusSendmsg.setVisibility(View.VISIBLE);
                        ((SendMsgViewHolder) holder).mPbSendingSendmsg.setVisibility(View.GONE);
                        break;
                    case AVIMMessageStatusSent:
                        ((SendMsgViewHolder) holder).mPbSendingSendmsg.setVisibility(View.GONE);
                        ((SendMsgViewHolder) holder).mMsgStatusSendmsg.setVisibility(View.GONE);
                        break;
                    case AVIMMessageStatusSending:
                        ((SendMsgViewHolder) holder).mPbSendingSendmsg.setVisibility(View.VISIBLE);
                        ((SendMsgViewHolder) holder).mMsgStatusSendmsg.setVisibility(View.GONE);
                        break;
                }

                if (null != meInfo.getAvatarUrl()) {
                    Glide.with(mContext).load(meInfo.getAvatarUrl()).placeholder(R.drawable.default_useravatar)
                            .into(((SendMsgViewHolder) holder).mIvAvatarSendmsg);
                } else {
                    ((SendMsgViewHolder) holder).mIvAvatarSendmsg.setImageResource(R.drawable.default_useravatar);
                }

                break;
            case RECEIVE_MSG:
                AVIMTextMessage recmsg = (AVIMTextMessage) message;
                if (mode == 1) {
                    ((ReceiveMsgViewHolder) holder).mTvUsernameRecmsg.setVisibility(View.GONE);
                } else {
                    ((ReceiveMsgViewHolder) holder).mTvUsernameRecmsg.setVisibility(View.VISIBLE);
                    ((ReceiveMsgViewHolder) holder).mTvUsernameRecmsg.setText(recmsg.getFrom());
                }
                if (showTime(position)) {
                    ((ReceiveMsgViewHolder) holder).mTimestampRecmsg.setVisibility(View.VISIBLE);
                    ((ReceiveMsgViewHolder) holder).mTimestampRecmsg.setText(millisecsToDateString(recmsg.getTimestamp()));
                } else {
                    ((ReceiveMsgViewHolder) holder).mTimestampRecmsg.setVisibility(View.GONE);
                }
                ((ReceiveMsgViewHolder) holder).mTvChatcontentRecmsg.setText(recmsg.getText());

                Glide.with(mContext).load(mContactInfo.getAvatarUrl()).placeholder(R.drawable.default_useravatar)
                        .into(((ReceiveMsgViewHolder) holder).mIvAvatarRecmsg);
                break;
            case SEND_IMAGE:
                final AVIMImageMessage sendImg = (AVIMImageMessage) message;
                if (showTime(position)) {
                    ((SendImgViewHolder) holder).mTimestampSendimg.setVisibility(View.VISIBLE);
                    ((SendImgViewHolder) holder).mTimestampSendimg.setText(millisecsToDateString(sendImg.getTimestamp()));
                } else {
                    ((SendImgViewHolder) holder).mTimestampSendimg.setVisibility(View.GONE);
                }
                switch (message.getMessageStatus()) {
                    case AVIMMessageStatusFailed:
                        ((SendImgViewHolder) holder).mMsgStatusSendimg.setVisibility(View.VISIBLE);
                        ((SendImgViewHolder) holder).mPbSendingSendimg.setVisibility(View.GONE);
                        break;
                    case AVIMMessageStatusSent:
                        ((SendImgViewHolder) holder).mPbSendingSendimg.setVisibility(View.GONE);
                        ((SendImgViewHolder) holder).mMsgStatusSendimg.setVisibility(View.GONE);
                        break;
                    case AVIMMessageStatusSending:
                        ((SendImgViewHolder) holder).mPbSendingSendimg.setVisibility(View.VISIBLE);
                        ((SendImgViewHolder) holder).mMsgStatusSendimg.setVisibility(View.GONE);
                        break;
                }
                String imgPath = sendImg.getText();
                File file = new File(imgPath);
                if (file.exists()) {
                    Glide.with(mContext).load(imgPath)
                            .into(((SendImgViewHolder) holder).mIvPictureSendimg);
                } else {
                    Glide.with(mContext).load(sendImg.getFileUrl())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(((SendImgViewHolder) holder).mIvPictureSendimg);
                }
//                Glide.with(mContext).load(sendImg.getFileUrl())
//                            .into(((SendImgViewHolder) holder).mIvPictureSendimg);
                Glide.with(mContext).load(meInfo.getAvatarUrl()).placeholder(R.drawable.default_useravatar)
                        .into(((SendImgViewHolder) holder).mIvAvatarSendimg);
                ((SendImgViewHolder) holder).mIvPictureSendimg.setTag(sendImg.getFileUrl());
                break;
            case RECEIVE_IMAGE:
                AVIMImageMessage receImg = (AVIMImageMessage) message;
                if (mode == 1) {
                    ((ReceImgViewHolder) holder).mTvUsernameRecimg.setVisibility(View.GONE);
                } else {
                    ((ReceImgViewHolder) holder).mTvUsernameRecimg.setVisibility(View.VISIBLE);
                    ((ReceImgViewHolder) holder).mTvUsernameRecimg.setText(receImg.getFrom());
                }
                if (showTime(position)) {
                    ((ReceImgViewHolder) holder).mTimestampRecimg.setVisibility(View.VISIBLE);
                    ((ReceImgViewHolder) holder).mTimestampRecimg.setText(millisecsToDateString(receImg.getTimestamp()));
                } else {
                    ((ReceImgViewHolder) holder).mTimestampRecimg.setVisibility(View.GONE);
                }
                switch (message.getMessageStatus()) {
                    case AVIMMessageStatusSent:
                        ((ReceImgViewHolder) holder).mPbReceimg.setVisibility(View.GONE);
                        break;
                    case AVIMMessageStatusSending:
                        ((ReceImgViewHolder) holder).mPbReceimg.setVisibility(View.VISIBLE);
                        break;
                }
                String receImgPath = receImg.getText();
                File receFile = new File(receImgPath);
                if (receFile.exists()) {
                    Glide.with(mContext).load(receImgPath)
                            .into(((ReceImgViewHolder) holder).mIvPictureRecimg);
                } else {
                    Glide.with(mContext).load(receImg.getFileUrl())
                            .into(((ReceImgViewHolder) holder).mIvPictureRecimg);
                }
                Glide.with(mContext).load(mContactInfo.getAvatarUrl()).placeholder(R.drawable.default_useravatar)
                        .into(((ReceImgViewHolder) holder).mIvAvatarRecimg);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    /**
     * 因为 RecyclerView 中的 item 缓存默认最大为 5，造成会重复的 create item 而卡顿
     * 所以这里根据不同的类型设置不同的缓存值，经验值，不同 app 可以根据自己的场景进行更改
     */
    public void resetRecycledViewPoolSize(RecyclerView recyclerView) {
        recyclerView.getRecycledViewPool().setMaxRecycledViews(SEND_MSG, 25);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(SEND_IMAGE, 10);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(SEND_VOICE, 15);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(SEND_LOCATION, 10);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(SEND_VIDEO, 10);

        recyclerView.getRecycledViewPool().setMaxRecycledViews(RECEIVE_MSG, 25);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(RECEIVE_IMAGE, 10);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(RECEIVE_VOICE, 15);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(RECEIVE_LOCATION, 10);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(SEND_VIDEO, 10);
    }

    public boolean fromMe(AVIMMessage message) {
        if (message != null) {
            return message.getFrom().equals(User.getCurrentUser().getUsername());
        }
        return false;
    }

    /**
     * send text message
     *
     * @param msg
     */
    public void sendMessage(AVIMMessage msg) {
        addMessage(msg);
    }

    /**
     * 是否显示时间
     *
     * @param positon
     * @return
     */
    public boolean showTime(int positon) {
        if (positon == 0) {
            return true;
        }
        long newTime = messageList.get(positon).getTimestamp();
        long oldTime = messageList.get(positon - 1).getTimestamp();
        if (newTime - oldTime > TIME_INTERVAL) {
            return true;
        } else {
            return false;
        }
    }

    public static String millisecsToDateString(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        return format.format(new Date(timestamp));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_picture_sendimg:
                String imgUri = (String) view.getTag();
                EventBus.getDefault().post(new PreviewImageEvent(imgUri));
                break;
        }
    }


    class SendMsgViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.timestamp_sendmsg)
        public TextView mTimestampSendmsg;
        @BindView(R.id.iv_avatar_sendmsg)
        public ImageView mIvAvatarSendmsg;
        @BindView(R.id.tv_chatcontent_sendmsg)
        public TextView mTvChatcontentSendmsg;
        @BindView(R.id.msg_status_sendmsg)
        public ImageView mMsgStatusSendmsg;
        @BindView(R.id.pb_sending_sendmsg)
        public ProgressBar mPbSendingSendmsg;

        public SendMsgViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    class ReceiveMsgViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.timestamp_recmsg)
        public TextView mTimestampRecmsg;
        @BindView(R.id.iv_avatar_recmsg)
        public ImageView mIvAvatarRecmsg;
        @BindView(R.id.tv_chatcontent_recmsg)
        public TextView mTvChatcontentRecmsg;
        @BindView(R.id.tv_username_recmsg)
        public TextView mTvUsernameRecmsg;

        public ReceiveMsgViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class SendImgViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.timestamp_sendimg)
        TextView mTimestampSendimg;
        @BindView(R.id.iv_avatar_sendimg)
        ImageView mIvAvatarSendimg;
        @BindView(R.id.iv_picture_sendimg)
        ImageView mIvPictureSendimg;
        @BindView(R.id.rl_picture)
        RelativeLayout mRlPicture;
        @BindView(R.id.pb_sending_sendimg)
        ProgressBar mPbSendingSendimg;
        @BindView(R.id.msg_status_sendimg)
        ImageView mMsgStatusSendimg;

        public SendImgViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ReceImgViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.timestamp_recimg)
        TextView mTimestampRecimg;
        @BindView(R.id.iv_avatar_recimg)
        ImageView mIvAvatarRecimg;
        @BindView(R.id.iv_picture_recimg)
        ImageView mIvPictureRecimg;
        @BindView(R.id.tv_username_recimg)
        TextView mTvUsernameRecimg;
        @BindView(R.id.progressBar_receimg)
        ProgressBar mPbReceimg;

        public ReceImgViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
