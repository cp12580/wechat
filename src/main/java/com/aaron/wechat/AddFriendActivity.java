package com.aaron.wechat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.utils.Util;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/9.
 */
public class AddFriendActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.iv_back_add_friend)
    ImageView mIvBackAddFriend;
    @BindView(R.id.et_search_add_friend)
    EditText mEtSearchAddFriend;
    @BindView(R.id.tv_search_add_friend)
    TextView mTvSearchAddFriend;
    @BindView(R.id.re_search_add_friend)
    RelativeLayout mReSearchAddFriend;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mIvBackAddFriend.setOnClickListener(this);
        mReSearchAddFriend.setOnClickListener(this);
        mEtSearchAddFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {
                if (sequence.length() >0){
                    mReSearchAddFriend.setVisibility(View.VISIBLE);
                    mTvSearchAddFriend.setText(mEtSearchAddFriend.getText().toString());
                }else {
                    mTvSearchAddFriend.setText("");
                    mReSearchAddFriend.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_add_friend;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back_add_friend:
                finish();
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                break;
            case R.id.re_search_add_friend:
                String findUserName = mEtSearchAddFriend.getText().toString();
                if (findUserName != null){
                    searchUser(findUserName);
                }
                break;
        }
    }

    private void searchUser(final String userName){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在查找联系人...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        AVQuery<AVUser> query = AVUser.getQuery(AVUser.class);
        query.whereEqualTo("username",userName);
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                dialog.dismiss();
                if (e == null){
                    ArrayList<AVUser> users = new ArrayList<AVUser>();
                    for (AVUser user : list) {
                        users.add(user);
                    }
                    if (users.size() == 0){
                        Util.T(AddFriendActivity.this,"用户名不存在");
                    }else if (users.get(0).getObjectId().equals(AVUser.getCurrentUser().getObjectId())){
                        Util.T(AddFriendActivity.this,"不能添加自己");
                    }else {
                        Intent intent = new Intent(AddFriendActivity.this,UserInfoActivity.class);
                        intent.putExtra("objectId",users.get(0).getObjectId());
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    }
                }else {
                    Util.T(AddFriendActivity.this,e.getMessage());
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
}
