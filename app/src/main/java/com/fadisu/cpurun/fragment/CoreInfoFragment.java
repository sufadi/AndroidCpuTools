package com.fadisu.cpurun.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.fadisu.cpurun.R;
import com.fadisu.cpurun.adapter.CustomAdapter;
import com.fadisu.cpurun.util.BuildHelper;
import com.fadisu.cpurun.util.CpuUtils;
import com.fadisu.cpurun.util.ProcCpuInfo;

import java.util.ArrayList;
import java.util.List;


public class CoreInfoFragment extends Fragment implements CustomAdapter.LayoutView {

    public static final int UPDATE_UI = 0;

    private Context mContext;
    private List<String> result;
    private CustomAdapter<String> mCustomAdapter;

    private View mView;
    private TextView mInfoTv;
    private ListView mListView;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_UI:
                    mCustomAdapter.updateData((ArrayList<String>) result);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_core, container, false);
        initViews();
        initValues();
        initListeners();
        return mView;
    }

    private void initViews() {
        mInfoTv = (TextView) mView.findViewById(R.id.tv_info);
        mListView = (ListView) mView.findViewById(R.id.listview);
    }

    private void initValues() {
        mInfoTv.setText(ProcCpuInfo.getArchitecture());

        result = new ArrayList<String>();
        result.add(getString(R.string.cpu_core_number) + CpuUtils.getNumCpuCores());
        result.add(getString(R.string.cpu_bits) + (CpuUtils.isCpu64() ? getString(R.string.cpu_bits_64) : getString(R.string.cpu_bits_32)));
        result.add(getString(R.string.cpu_abi) + BuildHelper.getCpuAbi());
        result.add(getString(R.string.cpu_plateform) + BuildHelper.getHardWare());
        result.add(getString(R.string.cpu_processor) + ProcCpuInfo.getProcessor());
        result.add(getString(R.string.cpu_cur_governor) + CpuUtils.getCpuGovernor());
        result.add(getString(R.string.cpu_min_freq) + CpuUtils.getCpuMinFreq() + getString(R.string.cpu_hz));
        result.add(getString(R.string.cpu_max_freq) + CpuUtils.getCpuMaxFreq() + getString(R.string.cpu_hz));
        result.add(getString(R.string.cpu_available_governors) + CpuUtils.getCpuAvailableGovernorsSimple());
        result.add(getString(R.string.cpu_available_frequencies) + CpuUtils.getCpuAvailableFrequenciesSimple());

        mCustomAdapter = new CustomAdapter<String>(result);
        mListView.setAdapter(mCustomAdapter);
        mHandler.sendEmptyMessage(UPDATE_UI);
    }

    private void initListeners() {
        mCustomAdapter.setLayoutView(this);
    }

    @Override
    public <T> View setView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_base, null);
            holder = new ViewHolder();
            convertView.setTag(holder);

            holder.tv_info = (TextView) convertView.findViewById(R.id.tv_info);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_info.setText(result.get(position));

        return convertView;
    }

    class ViewHolder {
        TextView tv_info;
    }
}
