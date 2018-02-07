package com.fadisu.cpurun.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.fadisu.cpurun.R;
import com.fadisu.cpurun.util.CpuSettingsUtils;
import com.fadisu.cpurun.util.CpuUtils;
import com.mediatek.perfservice.IPerfServiceWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CpuSettingsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = CpuSettingsFragment.class.getSimpleName();

    private List<String> mVcoreList;
    private List<Integer> mFreqMinList;
    private List<Integer> mFreqMaxList;
    private List<Integer> mCoreMinList;
    private List<Integer> mCoreMaxList;
    private List<String> mScreenOffEnableList;

    private ArrayAdapter<Integer> mFreqMinAdapter;
    private ArrayAdapter<Integer> mFreqMaxAdapter;
    private ArrayAdapter<String> mVcoreAdapter;
    private ArrayAdapter<Integer> mCoreMinAdapter;
    private ArrayAdapter<Integer> mCoreMaxAdapter;
    private ArrayAdapter<String> mScreenOffEnableMaxAdapter;

    private Context mContext;
    private CpuSettingsUtils mCpuSettingsUtils;

    private View mView;

    private Button btn_no;
    private Button btn_yes;

    private Spinner sp_cpu_vcore;
    private Spinner sp_cpu_core_min;
    private Spinner sp_cpu_core_max;
    private Spinner sp_cpu_freq_min;
    private Spinner sp_cpu_freq_max;
    private Spinner sp_screenof_enable;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_settings, container, false);
        initViews();
        initValues();
        initListeners();
        return mView;
    }

    private void initViews() {
        btn_no = (Button) mView.findViewById(R.id.btn_no);
        btn_yes = (Button) mView.findViewById(R.id.btn_yes);

        sp_cpu_vcore = (Spinner) mView.findViewById(R.id.sp_cpu_vcore);
        sp_cpu_core_min = (Spinner) mView.findViewById(R.id.sp_cpu_core_min);
        sp_cpu_core_max = (Spinner) mView.findViewById(R.id.sp_cpu_core_max);
        sp_cpu_freq_min = (Spinner) mView.findViewById(R.id.sp_cpu_freq_min);
        sp_cpu_freq_max = (Spinner) mView.findViewById(R.id.sp_cpu_freq_max);
        sp_screenof_enable = (Spinner) mView.findViewById(R.id.sp_screenof_enable);
    }

    private void initValues() {
        mCpuSettingsUtils = CpuSettingsUtils.getInstance(mContext);

        mVcoreList = getVcoreList(mContext);
        mVcoreAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, mVcoreList);
        sp_cpu_vcore.setAdapter(mVcoreAdapter);

        mCoreMinList = getCoreMinList();
        mCoreMinAdapter = new ArrayAdapter<Integer>(mContext, android.R.layout.simple_spinner_dropdown_item, mCoreMinList);
        sp_cpu_core_min.setAdapter(mCoreMinAdapter);

        mCoreMaxList = getCoreMaxList();
        mCoreMaxAdapter = new ArrayAdapter<Integer>(mContext, android.R.layout.simple_spinner_dropdown_item, mCoreMaxList);
        sp_cpu_core_max.setAdapter(mCoreMaxAdapter);

        mFreqMinList = CpuUtils.getCpuFreqList();
        Collections.sort(mFreqMinList);
        mFreqMinAdapter = new ArrayAdapter<Integer>(mContext, android.R.layout.simple_spinner_dropdown_item, mFreqMinList);
        sp_cpu_freq_min.setAdapter(mFreqMinAdapter);

        mFreqMaxList = CpuUtils.getCpuFreqList();
        mFreqMaxAdapter = new ArrayAdapter<Integer>(mContext, android.R.layout.simple_spinner_dropdown_item, mFreqMaxList);
        sp_cpu_freq_max.setAdapter(mFreqMaxAdapter);

        mScreenOffEnableList = getScreenOffEnableList(mContext);
        mScreenOffEnableMaxAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, mScreenOffEnableList);
        sp_screenof_enable.setAdapter(mScreenOffEnableMaxAdapter);
    }

    private void initListeners() {
        btn_no.setOnClickListener(this);
        btn_yes.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_yes:
                startCpuSettings();
                break;
            case R.id.btn_no:
                stopCpuSettings();
                break;
        }
    }

    private void startCpuSettings() {
        String vcoreStr = (String) sp_cpu_vcore.getSelectedItem();
        int vcoreMode = 0;
        if (mContext.getString(R.string.settings_cpu_vcore_default).equals(vcoreStr)) {
            vcoreMode = CpuSettingsUtils.VCORE_DEFAULT;
        } else if (mContext.getString(R.string.settings_cpu_vcore_powersave).equals(vcoreStr)) {
            vcoreMode = CpuSettingsUtils.VCORE_POWERSAVE;
        } else if (mContext.getString(R.string.settings_cpu_vcore_perf).equals(vcoreStr)) {
            vcoreMode = CpuSettingsUtils.VCORE_PERF;
        }

        int coreMin = (int) sp_cpu_core_min.getSelectedItem();
        int coreMax = (int) sp_cpu_core_max.getSelectedItem();
        int freqMin = (int) sp_cpu_freq_min.getSelectedItem();
        int freqMax = (int) sp_cpu_freq_max.getSelectedItem();

        String screenOffStr = (String) sp_screenof_enable.getSelectedItem();
        int screenOffVaule = 0;
        if (mContext.getString(R.string.settings_screenoff_enable_yes).equals(screenOffStr)) {
            screenOffVaule = IPerfServiceWrapper.SCREEN_OFF_ENABLE;
        } else if (mContext.getString(R.string.settings_screenoff_enable_no).equals(screenOffStr)) {
            screenOffVaule = IPerfServiceWrapper.SCREEN_OFF_DISABLE;
        } else if (mContext.getString(R.string.settings_screenoff_enable_batter).equals(screenOffStr)) {
            screenOffVaule = IPerfServiceWrapper.SCREEN_OFF_WAIT_RESTORE;
        }

        mCpuSettingsUtils.setCpu(coreMax, freqMax, vcoreMode, freqMin, freqMax, coreMin, coreMax, screenOffVaule);
    }

    private void stopCpuSettings() {
        mCpuSettingsUtils.userUnreg();
    }

    private ArrayList<String> getScreenOffEnableList(Context mContext) {
        ArrayList<String> result = new ArrayList<>();
        result.add(mContext.getString(R.string.settings_screenoff_enable_batter));
        result.add(mContext.getString(R.string.settings_screenoff_enable_no));
        result.add(mContext.getString(R.string.settings_screenoff_enable_yes));
        return result;
    }

    private ArrayList<String> getVcoreList(Context mContext) {
        ArrayList<String> result = new ArrayList<>();
        result.add(mContext.getString(R.string.settings_cpu_vcore_default));
        result.add(mContext.getString(R.string.settings_cpu_vcore_powersave));
        result.add(mContext.getString(R.string.settings_cpu_vcore_perf));
        return result;
    }

    private ArrayList<Integer> getCoreMinList() {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 1; i <= mCpuSettingsUtils.CPU_NUMBER; i++) {
            result.add(i);
        }
        return result;
    }

    private ArrayList<Integer> getCoreMaxList() {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = mCpuSettingsUtils.CPU_NUMBER; i > 0; i--) {
            result.add(i);
        }
        return result;
    }
}
