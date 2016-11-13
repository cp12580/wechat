package com.aaron.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.dao.ContactInfo;
import com.aaron.utils.Util;
import com.aaron.wechat.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2016/10/8.
 */
public class ContactListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    private int mHeaderCount = 1;
    private Context mContext;
    private List<ContactInfo> mUsers;


    public ContactListAdapter(Context context,List<ContactInfo> users){
        mContext = context;
        mUsers = users;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER){
            View headView = View.inflate(mContext,R.layout.contact_fragment_headview,null);
            RelativeLayout reNewFriends = (RelativeLayout) headView.findViewById(R.id.re_newfriends_headview);
            RelativeLayout reChatRoom = (RelativeLayout) headView.findViewById(R.id.re_chatroom_headview);
            reNewFriends.setOnClickListener(this);
            reChatRoom.setOnClickListener(this);
            return new ContactHeadViewHolder(headView);
        }else {
            View contactView = View.inflate(mContext,R.layout.item_contact_list,null);
            RelativeLayout reContactItem = (RelativeLayout) contactView.findViewById(R.id.re_contact_item);
            reContactItem.setOnClickListener(this);
            return new ContactViewHolder(contactView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContactViewHolder){
            String name = null;
            if (mUsers.get(position - 1).getBeizhu() != null){
                name = mUsers.get(position - 1).getBeizhu();
            }else if (mUsers.get(position - 1).getNickname() != null){
                name = mUsers.get(position - 1).getNickname();
            }else {
                name = mUsers.get(position - 1).getUsername();
            }
            ((ContactViewHolder)holder).tv.setText(name);
            Glide.with(mContext).load(mUsers.get(position -1).getAvatarUrl())
                    .placeholder(R.drawable.default_useravatar).error(R.drawable.default_useravatar)
                    .into(((ContactViewHolder)holder).iv);
            ((ContactViewHolder) holder).reItem.setTag(mUsers.get(position - 1).getUsername());
        }
        if (holder instanceof ContactHeadViewHolder){

        }

    }

    @Override
    public int getItemCount() {
        return mHeaderCount + mUsers.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderCount != 0 && position < mHeaderCount){
            return ITEM_TYPE_HEADER;
        }else {
            return ITEM_TYPE_CONTENT;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.re_newfriends_headview:
                Util.T(mContext,"new Friend");
                break;
            case R.id.re_chatroom_headview:
                Util.T(mContext,"chatroom");
                break;
            case R.id.re_contact_item:
                if (listner != null){
                    listner.onItemClick(view, (String) view.getTag());
                }
                break;
        }
    }

    private OnContactItemClickListener listner;

    public interface OnContactItemClickListener{
        public void onItemClick(View v,String username);
    }
    public void setOnContactClickListener(OnContactItemClickListener lis){
        listner = lis;
    }


    class ContactViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tv;
        RelativeLayout reItem;

        public ContactViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv_avatar_item_contact);
            tv = (TextView) itemView.findViewById(R.id.tv_name_item_contact);
            reItem = (RelativeLayout) itemView.findViewById(R.id.re_contact_item);
        }
    }

    class ContactHeadViewHolder extends RecyclerView.ViewHolder{

        public ContactHeadViewHolder(View itemView) {
            super(itemView);

        }
    }
}
