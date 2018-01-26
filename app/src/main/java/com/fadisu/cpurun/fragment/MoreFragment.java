package com.fadisu.cpurun.fragment;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fadisu.cpurun.R;

public class MoreFragment extends Fragment {

    private Context mContext;

    private View mView;
    private TextView mInfoTv;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_more, container, false);
        initViews();
        initValues();
        initListeners();
        return mView;
    }

    private void initViews() {
        mInfoTv = (TextView) mView.findViewById(R.id.tv_info);
    }

    private void initValues() {
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            mInfoTv.setText(getString(R.string.more_version_code) + packageInfo.versionCode
                    + getString(R.string.more_version_name) + packageInfo.versionName
                    + getString(R.string.more_source_code) + "https://github.com/sufadi/AndroidCpuTools"
                    + getString(R.string.more_blog) + "http://blog.csdn.net/su749520/article/details/79026493");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void initListeners() {

    }
}
