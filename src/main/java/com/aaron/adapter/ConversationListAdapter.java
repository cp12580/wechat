package com.aaron.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.dao.ConversationItem;
import com.aaron.event.ConversationItemEvent;
import com.aaron.wechat.R;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/18.
 */
public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListAdapter.ConversationViewHolder>
implements View.OnClickListener{

    private Context mContext;
    private List<ConversationItem> mConversationList;
    public ConversationListAdapter(Context c){
        mContext = c;
        mConversationList = new ArrayList<>();
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_conversation_list,parent,false);
        view.setOnClickListener(this);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, final int position) {
        if (mConversationList.get(position).getBeizhu() != null){
            holder.mTvNameConversation.setText(mConversationList.get(position).getBeizhu());
        }else if (mConversationList.get(position).getNickname() != null){
            holder.mTvNameConversation.setText(mConversationList.get(position).getNickname());
        }else {
            holder.mTvNameConversation.setText(mConversationList.get(position).getUsername());
        }

        holder.mTvContentConversation.setText(mConversationList.get(position).getLastMessage());
        holder.mTvTimeConversation.setText(millisecsToDateString(mConversationList.get(position).getUpdateTime()));
        if (mConversationList.get(position).getUnreadCount() == 0){
            holder.mTvUnreadConversation.setVisibility(View.INVISIBLE);
        }else {
            holder.mTvUnreadConversation.setVisibility(View.VISIBLE);
            holder.mTvUnreadConversation.setText(mConversationList.get(position).getUnreadCount() +"");
        }
        Glide.with(mContext).load(mConversationList.get(position).getAvatarUrl())
                .placeholder(R.drawable.default_useravatar).into(holder.mIvAvatarConversation);
        holder.mRelativeLayout.setTag(mConversationList.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return mConversationList.size();
    }

    @Override
    public void onClick(View view) {
        ConversationItemEvent event = new ConversationItemEvent(view);
        EventBus.getDefault().post(event);
    }


    class ConversationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.re_parent_conversation)
        RelativeLayout mRelativeLayout;
        @BindView(R.id.iv_avatar_conversation)
        ImageView mIvAvatarConversation;
        @BindView(R.id.tv_name_conversation)
        TextView mTvNameConversation;
        @BindView(R.id.tv_time_conversation)
        TextView mTvTimeConversation;
        @BindView(R.id.msg_state_conversation)
        ImageView mMsgStateConversation;
        @BindView(R.id.tv_content_conversation)
        TextView mTvContentConversation;
        @BindView(R.id.tv_unread_conversation)
        TextView mTvUnreadConversation;
        public ConversationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void refresh(List<ConversationItem> converList){
        mConversationList.clear();
        mConversationList.addAll(converList);
        notifyDataSetChanged();
    }

    public static String millisecsToDateString(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        return format.format(new Date(timestamp));
    }
}
