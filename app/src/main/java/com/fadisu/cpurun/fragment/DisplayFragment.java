package com.fadisu.cpurun.fragment;

import android.content.Context;
import android.opengl.GLSurfaceView;
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
import com.fadisu.cpurun.bean.ScreenInfo;
import com.fadisu.cpurun.util.BuildHelper;
import com.fadisu.cpurun.util.RendererUtil;
import com.fadisu.cpurun.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;


public class DisplayFragment extends Fragment implements CustomAdapter.LayoutView {

    public static final int UPDATE_UI = 0;

    private Context mContext;
    private List<String> result;
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
        mView = inflater.inflate(R.layout.fragment_display, container, false);
        initViews();
        initValues();
        initListeners();
        return mView;
    }

    private void initViews() {
        mListView = (ListView) mView.findViewById(R.id.listview);
    }

    private void initValues() {

        result = new ArrayList<String>();

        final RendererUtil mRendererUtil = new RendererUtil();
        GLSurfaceView mGLSurfaceView = (GLSurfaceView) mView.findViewById(R.id.surfaceView);
        mGLSurfaceView.setEGLContextClientVersion(1);
        mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        mGLSurfaceView.setRenderer(mRendererUtil);
        mGLSurfaceView.post(new Runnable() {
            @Override
            public void run() {
                result.add(getString(R.string.gl_vendor) + mRendererUtil.gl_vendor);
                result.add(getString(R.string.gl_renderer) + mRendererUtil.gl_renderer);
                result.add(getString(R.string.gl_version) + mRendererUtil.gl_version);
                result.add(getString(R.string.gl_extensions) + mRendererUtil.gl_extensions);
                mHandler.sendEmptyMessage(UPDATE_UI);
            }
        });

        ScreenInfo mScreenInfo = ScreenUtil.getScreenInfo(mContext);
        result.add(getString(R.string.sys_screen_real_metrics) + mScreenInfo.screenRealMetrics);
        result.add(getString(R.string.screen_density) + mScreenInfo.densityDpiStr);
        result.add(getString(R.string.screen_size) + mScreenInfo.sizeStr);

        result.add(getString(R.string.gpu_freq_volt));
        result.addAll(ScreenUtil.getGpuFreqVolt());

        result.add(getString(R.string.sys_dislpay_id) + BuildHelper.getDisplay());

        mCustomAdapter = new CustomAdapter<String>(result);
        mListView.setAdapter(mCustomAdapter);
        mHandler.sendEmptyMessage(UPDATE_UI);
    }

    private void initListeners() {
        mCustomAdapter.setLayoutView(this);
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
