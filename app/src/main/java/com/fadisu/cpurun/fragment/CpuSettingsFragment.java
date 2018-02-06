package com.fadisu.cpurun.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fadisu.cpurun.R;
import com.fadisu.cpurun.util.CpuSettingsUtils;

public class CpuSettingsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = CpuSettingsFragment.class.getSimpleName();

    private Context mContext;
    private CpuSettingsUtils mCpuSettingsUtils;

    private View mView;
    private TextView tv_cpu_governor_content;
    private LinearLayout ll_cpu_governor_content;

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
        tv_cpu_governor_content = (TextView) mView.findViewById(R.id.tv_cpu_governor_content);
        ll_cpu_governor_content = (LinearLayout) mView.findViewById(R.id.ll_cpu_governor_content);
    }

    private void initValues() {
        mCpuSettingsUtils = CpuSettingsUtils.getInstance(mContext);
        tv_cpu_governor_content.setText(mContext.getString(R.string.settings_cpu_vcore_default));
    }

    private void initListeners() {
        ll_cpu_governor_content.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_cpu_governor_content:
                showVcoreModeDialog();
                break;
        }
    }

    private void showVcoreModeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        final String[] GOVERNORS = new String[]{mContext.getString(R.string.settings_cpu_vcore_default)
                ,mContext.getString(R.string.settings_cpu_vcore_powersave)
                ,mContext.getString(R.string.settings_cpu_vcore_default)
                ,mContext.getString(R.string.settings_cpu_vcore_perf)};

        builder.setItems(GOVERNORS, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CpuSettingsUtils.VCORE_DEFAULT:
                    case CpuSettingsUtils.VCORE_DEFAULT_2:
                        mCpuSettingsUtils.setCpuVcoreMode(CpuSettingsUtils.CPU_NUMBER, CpuSettingsUtils.MAX_CPU_FREQ, CpuSettingsUtils.VCORE_DEFAULT);
                        break;
                    case CpuSettingsUtils.VCORE_POWERSAVE:
                        mCpuSettingsUtils.setCpuVcoreMode(CpuSettingsUtils.CPU_NUMBER, CpuSettingsUtils.MIN_CPU_FREQ, CpuSettingsUtils.VCORE_POWERSAVE);
                        break;
                    case CpuSettingsUtils.VCORE_PERF:
                        mCpuSettingsUtils.setCpuVcoreMode(CpuSettingsUtils.CPU_NUMBER, CpuSettingsUtils.MAX_CPU_FREQ, CpuSettingsUtils.VCORE_PERF);
                        break;
                }

                tv_cpu_governor_content.setText(GOVERNORS[which]);
            }
        });
        builder.show();
    }

}
