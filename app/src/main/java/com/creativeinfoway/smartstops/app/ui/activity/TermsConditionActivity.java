package com.creativeinfoway.smartstops.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TermsConditionActivity extends AppCompatActivity implements View.OnClickListener {

    private String str_agree_text_file;
    private TextView tvTermsCondition;
    private Button btnAgree;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);

        type = getIntent().getStringExtra("type");

        initids();
        setTextFile();

    }

    private void initids() {
        tvTermsCondition = findViewById(R.id.tv_txt_agree);
        btnAgree = findViewById(R.id.act_termscondition_btn_agree);
        btnAgree.setOnClickListener(this);
    }

    private String readFileFromRawDirectory(int resourceId) {
        InputStream iStream = this.getResources().openRawResource(resourceId);
        ByteArrayOutputStream byteStream = null;
        try {
            byte[] buffer = new byte[iStream.available()];
            iStream.read(buffer);
            byteStream = new ByteArrayOutputStream();
            byteStream.write(buffer);
            byteStream.close();
            iStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteStream.toString();
    }

    private void setTextFile() {
        str_agree_text_file = readFileFromRawDirectory(R.raw.termsconditions);
        tvTermsCondition.setText((str_agree_text_file));
    }

    @Override
    public void onClick(View v) {

        if (v == btnAgree) {
            Intent intent = new Intent(TermsConditionActivity.this, WarningActivity.class);
            intent.putExtra("type",type);

            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}

