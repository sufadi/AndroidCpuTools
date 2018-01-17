package com.fadisu.cpurun.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;

import com.fadisu.cpurun.R;
import com.fadisu.cpurun.fragment.BuildFragment;
import com.fadisu.cpurun.fragment.MoreFragment;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static String currentFragmentTag;

    private Fragment mBuildFragment;
    private Fragment mMoreFragmen;
    private FragmentManager mFragmentManager;

    private RadioButton mMoreRb;
    private RadioButton mBuildInfoRb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initValues();
        initLisener();
    }

    private void initViews() {
        mMoreRb = (RadioButton) findViewById(R.id.rb_more);
        mBuildInfoRb = (RadioButton) findViewById(R.id.rb_build_info);
    }

    private void initValues() {
        currentFragmentTag = MainActivity.class.getSimpleName();

        mBuildFragment = new BuildFragment();
        mMoreFragmen = new MoreFragment();
        mFragmentManager = getSupportFragmentManager();

        changeFrament(mBuildFragment, null, BuildFragment.class.getSimpleName());
    }

    private void initLisener() {
        mMoreRb.setOnClickListener(this);
        mBuildInfoRb.setOnClickListener(this);
    }


    /**
     * 设置显示的页面
     *
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
        switch (view.getId()){
            case R.id.rb_build_info:
                changeFrament(mBuildFragment, null, BuildFragment.class.getSimpleName());
                break;
            case R.id.rb_more:
                changeFrament(mMoreFragmen, null, MoreFragment.class.getSimpleName());
                break;
        }
    }
}
