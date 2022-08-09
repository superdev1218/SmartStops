package com.creativeinfoway.smartstops.app.ui.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;

public class WarningActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAgree;
    TextView tvPrivacyLink;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);
        type = getIntent().getStringExtra("type");
        initids();
    }

    private void initids() {

        btnAgree = findViewById(R.id.act_warning_btn_agree);
        tvPrivacyLink=findViewById(R.id.tv_privacy_link);

        tvPrivacyLink.setPaintFlags(tvPrivacyLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvPrivacyLink.setOnClickListener(this);
        btnAgree.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == btnAgree) {
//            Intent intent = new Intent(WarningActivity.this, LoginActivity.class);

            if(type.equals("TermsCondition")){
                Intent intent = new Intent(WarningActivity.this, HomeActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(WarningActivity.this, SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
        else if(v==tvPrivacyLink)
        {
            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.PRIVACY_POLICY)));
            Intent intent = new Intent(WarningActivity.this,PrivacyPolicyActivity.class);
            ///intent.putExtra("txtSName","WARNING");
            startActivity(intent);
        }
    }
}
