package com.fadisu.cpurun.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;

import com.fadisu.cpurun.R;
import com.fadisu.cpurun.fragment.BaseInfoFragment;
import com.fadisu.cpurun.fragment.BuildFragment;
import com.fadisu.cpurun.fragment.CpuInfoFragment;
import com.fadisu.cpurun.fragment.CpuRunTimeFragment;
import com.fadisu.cpurun.fragment.MoreFragment;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static String currentFragmentTag;

    private Fragment mMoreFragmen;
    private Fragment mBuildFragment;
    private Fragment mCpuInfoFragment;
    private Fragment mBaseInfoFragment;
    private Fragment mCpuRunTimeFragment;
    private FragmentManager mFragmentManager;

    private RadioButton mBaseRb;
    private RadioButton mMoreRb;
    private RadioButton mCpuInfo;
    private RadioButton mCpuTimeRb;
    private RadioButton mBuildInfoRb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initValues();
        initLisener();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initViews() {
        mMoreRb = (RadioButton) findViewById(R.id.rb_more);
        mBaseRb = (RadioButton) findViewById(R.id.rb_base_info);
        mCpuInfo = (RadioButton) findViewById(R.id.rb_cpu_info);
        mCpuTimeRb = (RadioButton) findViewById(R.id.rb_cpu_time);
        mBuildInfoRb = (RadioButton) findViewById(R.id.rb_build_info);
    }

    private void initValues() {
        currentFragmentTag = MainActivity.class.getSimpleName();

        mMoreFragmen = new MoreFragment();
        mBuildFragment = new BuildFragment();
        mCpuInfoFragment = new CpuInfoFragment();
        mBaseInfoFragment = new BaseInfoFragment();
        mCpuRunTimeFragment = new CpuRunTimeFragment();
        mFragmentManager = getSupportFragmentManager();

        changeFrament(mBaseInfoFragment, null, BaseInfoFragment.class.getSimpleName());
    }

    private void initLisener() {
        mBaseRb.setOnClickListener(this);
        mMoreRb.setOnClickListener(this);
        mCpuInfo.setOnClickListener(this);
        mCpuTimeRb.setOnClickListener(this);
        mBuildInfoRb.setOnClickListener(this);
    }


    /**
     * 设置显示的页面
     * <p/>
     * fragment传入点击的按钮对应的Fragment对象
     * bundle参数
     * tag标识符
     */
    public void changeFrament(Fragment fragment, Bundle bundle, String tag) {
        if (!currentFragmentTag.equals(tag)) {
            for (int i = 0, count = mFragmentManager.getBackStackEntryCount(); i < count; i++) {
                mFragmentManager.popBackStack();
            }
            currentFragmentTag = tag;
            FragmentTransaction fg = mFragmentManager.beginTransaction();
            fragment.setArguments(bundle);
            fg.add(R.id.main_fragment_root_view, fragment, tag);
            fg.addToBackStack(tag);
            fg.commit();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_base_info:
                changeFrament(mBaseInfoFragment, null, BaseInfoFragment.class.getSimpleName());
                break;
            case R.id.rb_cpu_time:
                changeFrament(mCpuRunTimeFragment, null, CpuRunTimeFragment.class.getSimpleName());
                break;
            case R.id.rb_cpu_info:
                changeFrament(mCpuInfoFragment, null, CpuInfoFragment.class.getSimpleName());
                break;
            case R.id.rb_build_info:
                changeFrament(mBuildFragment, null, BuildFragment.class.getSimpleName());
                break;
            case R.id.rb_more:
                changeFrament(mMoreFragmen, null, MoreFragment.class.getSimpleName());
                break;
        }
    }
}
