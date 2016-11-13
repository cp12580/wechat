package com.aaron.fragment;

import android.view.View;

import com.aaron.wechat.R;

/**
 * Created by Administrator on 2016/10/3.
 */
public class FindFragment extends BaseFragment {
    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_find,null);
        return view;
    }
}