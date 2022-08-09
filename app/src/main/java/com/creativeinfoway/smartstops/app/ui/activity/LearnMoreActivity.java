package com.creativeinfoway.smartstops.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;

public class LearnMoreActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnLearnMore,btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_more);

        initids();
    }

    private void initids() {

        mBtnLearnMore = findViewById(R.id.btn_learn_more);
        btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(this);
        mBtnLearnMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == mBtnLearnMore) {

            Intent intent = new Intent(LearnMoreActivity.this, TermsConditionActivity.class);
            intent.putExtra("type","TermsCondition");
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else if(v == btn_signup){
            Intent intent = new Intent(LearnMoreActivity.this, TermsConditionActivity.class);
            intent.putExtra("type","SignUp");
            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
