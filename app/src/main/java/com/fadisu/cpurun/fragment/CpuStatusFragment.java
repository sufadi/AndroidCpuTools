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
import com.fadisu.cpurun.util.CpuUtils;
import com.fadisu.cpurun.util.ProcCpuStatUtil;

import java.util.ArrayList;
import java.util.List;


public class CpuStatusFragment extends Fragment implements CustomAdapter.LayoutView {

    public static final int UPDATE_UI = 0;

    private boolean isRun;
    private Context mContext;
    private List<String> result;
    private Thread mThread;
    private CustomAdapter<String> mCustomAdapter;

    private View mView;
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
        mView = inflater.inflate(R.layout.fragment_base, container, false);
        initViews();
        initValues();
        initListeners();
        return mView;
    }

    private void initViews() {
        mListView = (ListView) mView.findViewById(R.id.listview);
    }

    private void initValues() {
        result = CpuUtils.getCpuCurFreq(mContext);
        mCustomAdapter = new CustomAdapter<String>(result);
        mListView.setAdapter(mCustomAdapter);
        mHandler.sendEmptyMessage(UPDATE_UI);
    }

    private void initListeners() {
        mCustomAdapter.setLayoutView(this);
    }

    private void startTask() {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRun) {
                    List<String> temp = new ArrayList<>();
                    temp.add(mContext.getString(R.string.cpu_usage) + ProcCpuStatUtil.getCpuUsage());
                    temp.addAll(CpuUtils.getCpuCurFreq(mContext));

                    if (null != temp) {
                        result.clear();
                        result.addAll(temp);
                        mHandler.sendEmptyMessage(UPDATE_UI);
                    }

                }
            }
        });

        mThread.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        isRun = true;

        startTask();
    }

    @Override
    public void onStop() {
        isRun = false;

        if (mThread != null) {
            mThread.interrupt();
        }
        super.onStop();
    }

    class ViewHolder {
        TextView tv_info;
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
}
