package com.east.east_utils.widgets.dialog.loading_dialog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.east.east_utils.R;
import com.east.east_utils.utils.ScreenUtils;
import com.east.east_utils.utils.StringUtils;
/**
 * |---------------------------------------------------------------------------------------------------------------|
 *  @description:  Activity作为加载Dialog框
 *  @author: East
 *  @date: 2019-10-28 17:23
 * |---------------------------------------------------------------------------------------------------------------|
 */
public class DialogLoadingActivity extends AppCompatActivity {

    public static String Msg = "msg";
    public static String FinishOnTouchOutside = "cancelOutSide";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ScreenUtils.isTablet())
            setContentView(R.layout.dialog_loading_pad);
        else
            setContentView(R.layout.dialog_loading_phone);

        String msg = getIntent().getStringExtra(Msg);
        Boolean cancelOutside = getIntent().getBooleanExtra(FinishOnTouchOutside,false);
        setFinishOnTouchOutside(cancelOutside);

        TextView tipTextView = findViewById(R.id.tipTextView);
        if (tipTextView != null) {
            tipTextView.setVisibility(View.VISIBLE);
            if (StringUtils.isEmpty(msg)) tipTextView.setText("");
            else
                tipTextView.setText(msg);
        }


    }
}
