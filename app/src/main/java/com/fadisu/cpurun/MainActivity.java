package com.fadisu.cpurun;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fadisu.cpurun.util.CpuUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static int UPDATE_UI = 0;

    private TextView tvInfo;
    private Button btnFreq;

    private List<Integer> result;

    private Handler mHandle = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_UI:
                    updateUI();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initValues();
        initLisener();
    }

    private void initViews() {
        tvInfo = (TextView) findViewById(R.id.tv_info);
        btnFreq = (Button) findViewById(R.id.btn_freq);

        tvInfo.setText(CpuUtils.getCpuName());
    }

    private void initValues() {
        result = new ArrayList<>();
    }

    private void initLisener() {
        btnFreq.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_freq:
                updateCpuFreq();

                mHandle.sendEmptyMessage(UPDATE_UI);
                break;
        }
    }

    private void updateCpuFreq() {
        result = CpuUtils.getCpuCurFreq();
    }

    private void updateUI() {
        String info = "";
        for (Integer value : result) {
            info = info + value + "\n";
        }

        tvInfo.setText(info);
    }
}
