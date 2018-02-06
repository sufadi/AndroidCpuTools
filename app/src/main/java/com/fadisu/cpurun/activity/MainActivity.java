package com.fadisu.cpurun.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;

import com.fadisu.cpurun.MyApplication;
import com.fadisu.cpurun.R;
import com.fadisu.cpurun.fragment.BuildFragment;
import com.fadisu.cpurun.fragment.CoreInfoFragment;
import com.fadisu.cpurun.fragment.CpuInfoFragment;
import com.fadisu.cpurun.fragment.CpuRunTimeFragment;
import com.fadisu.cpurun.fragment.CpuSceneFragment;
import com.fadisu.cpurun.fragment.CpuSettingsFragment;
import com.fadisu.cpurun.fragment.CpuStatusFragment;
import com.fadisu.cpurun.fragment.CpuVoltageFragment;
import com.fadisu.cpurun.fragment.DisplayFragment;
import com.fadisu.cpurun.fragment.MemInfoFragment;
import com.fadisu.cpurun.fragment.MoreFragment;
import com.fadisu.cpurun.fragment.PropFragment;
import com.fadisu.cpurun.fragment.SystemFragment;
import com.fadisu.cpurun.fragment.TemperatureFragment;
import com.fadisu.cpurun.util.CpuSettingsUtils;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private boolean isFloatWindowVisible;

    private static String currentFragmentTag;

    private Fragment mMoreFragmen;
    private Fragment mPropFragment;
    private Fragment mBuildFragment;
    private Fragment mSystemFragment;
    private Fragment mDisplayFragment;
    private Fragment mCpuInfoFragment;
    private Fragment mMemInfoFragment;
    private Fragment mBaseInfoFragment;
    private Fragment mCpuStatusFragment;
    private Fragment mCpuSceneFragment;
    private Fragment mCpuRunTimeFragment;
    private Fragment mCpuVoltageFragment;
    private Fragment mTemperatureFragment;
    private Fragment mCpuSettingsFragment;
    private FragmentManager mFragmentManager;

    private RadioButton mSysRb;
    private RadioButton mPropRb;
    private RadioButton mBaseRb;
    private RadioButton mMoreRb;
    private RadioButton mCpuInfo;
    private RadioButton mCpuStatus;
    private RadioButton mMeminfoRb;
    private RadioButton mCpuTimeRb;
    private RadioButton mDisplayRb;
    private RadioButton mBuildInfoRb;
    private RadioButton mCpuSceneFreq;
    private RadioButton mCpuVoltageRb;
    private RadioButton mTemperatureRb;
    private RadioButton mCpuSettingsRb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initValues();
        initLisener();

        MyApplication.startService(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyApplication.stopService(this);
        CpuSettingsUtils.getInstance(this).userUnreg();
        finish();
    }

    private void initViews() {
        mMoreRb = (RadioButton) findViewById(R.id.rb_more);
        mPropRb = (RadioButton) findViewById(R.id.rb_prop);
        mSysRb = (RadioButton) findViewById(R.id.rb_sys_info);
        mBaseRb = (RadioButton) findViewById(R.id.rb_base_info);
        mCpuInfo = (RadioButton) findViewById(R.id.rb_cpu_info);
        mMeminfoRb = (RadioButton) findViewById(R.id.rb_meminfo);
        mDisplayRb = (RadioButton) findViewById(R.id.rb_display);
        mCpuTimeRb = (RadioButton) findViewById(R.id.rb_cpu_time);
        mCpuStatus = (RadioButton) findViewById(R.id.rb_cpu_status);
        mBuildInfoRb = (RadioButton) findViewById(R.id.rb_build_info);
        mCpuSceneFreq = (RadioButton) findViewById(R.id.rb_scene_freq);
        mCpuVoltageRb = (RadioButton) findViewById(R.id.rb_cpu_voltage);
        mTemperatureRb = (RadioButton) findViewById(R.id.rb_temperature);
        mCpuSettingsRb = (RadioButton) findViewById(R.id.rb_cpu_settings);
    }

    private void initValues() {
        currentFragmentTag = MainActivity.class.getSimpleName();
        mFragmentManager = getSupportFragmentManager();

        mMoreFragmen = new MoreFragment();
        mPropFragment = new PropFragment();
        mBuildFragment = new BuildFragment();
        mSystemFragment = new SystemFragment();
        mDisplayFragment = new DisplayFragment();
        mMemInfoFragment = new MemInfoFragment();
        mCpuInfoFragment = new CpuInfoFragment();
        mBaseInfoFragment = new CoreInfoFragment();
        mCpuSceneFragment = new CpuSceneFragment();
        mCpuStatusFragment = new CpuStatusFragment();
        mCpuRunTimeFragment = new CpuRunTimeFragment();
        mCpuVoltageFragment = new CpuVoltageFragment();
        mTemperatureFragment = new TemperatureFragment();
        mCpuSettingsFragment = new CpuSettingsFragment();

        changeFrament(mBaseInfoFragment, null, CoreInfoFragment.class.getSimpleName());
    }

    private void initLisener() {
        mSysRb.setOnClickListener(this);
        mPropRb.setOnClickListener(this);
        mBaseRb.setOnClickListener(this);
        mMoreRb.setOnClickListener(this);
        mCpuInfo.setOnClickListener(this);
        mCpuTimeRb.setOnClickListener(this);
        mDisplayRb.setOnClickListener(this);
        mMeminfoRb.setOnClickListener(this);
        mCpuStatus.setOnClickListener(this);
        mBuildInfoRb.setOnClickListener(this);
        mCpuSceneFreq.setOnClickListener(this);
        mCpuVoltageRb.setOnClickListener(this);
        mTemperatureRb.setOnClickListener(this);
        mCpuSettingsRb.setOnClickListener(this);
    }

    /**
     * 设置显示的页面
     * <p>
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
            case R.id.rb_cpu_settings:
                changeFrament(mCpuSettingsFragment, null, CpuSettingsFragment.class.getSimpleName());
                break;
            case R.id.rb_temperature:
                changeFrament(mTemperatureFragment, null, TemperatureFragment.class.getSimpleName());
                break;
            case R.id.rb_cpu_voltage:
                changeFrament(mCpuVoltageFragment, null, CpuVoltageFragment.class.getSimpleName());
                break;
            case R.id.rb_scene_freq:
                changeFrament(mCpuSceneFragment, null, CpuSceneFragment.class.getSimpleName());
                break;
            case R.id.rb_cpu_status:
                changeFrament(mCpuStatusFragment, null, CpuStatusFragment.class.getSimpleName());
                break;
            case R.id.rb_display:
                changeFrament(mDisplayFragment, null, DisplayFragment.class.getSimpleName());
                break;
            case R.id.rb_meminfo:
                changeFrament(mMemInfoFragment, null, MemInfoFragment.class.getSimpleName());
                break;
            case R.id.rb_prop:
                changeFrament(mPropFragment, null, PropFragment.class.getSimpleName());
                break;
            case R.id.rb_sys_info:
                changeFrament(mSystemFragment, null, SystemFragment.class.getSimpleName());
                break;
            case R.id.rb_base_info:
                changeFrament(mBaseInfoFragment, null, CoreInfoFragment.class.getSimpleName());
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

    public void setFloatWindowVisible(boolean visible) {
        this.isFloatWindowVisible = visible;
    }

    public boolean isFloatWindowVisible() {
        return isFloatWindowVisible;
    }

}
