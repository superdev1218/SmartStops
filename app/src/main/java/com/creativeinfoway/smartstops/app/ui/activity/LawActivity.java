package com.creativeinfoway.smartstops.app.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.creativeinfoway.smartstops.app.ui.utils.Constant;

public class LawActivity extends AppCompatActivity {

    private String txtSName;
    private TextView txtSourceName;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_law);

        iv_back = findViewById(R.id.iv_back);
        txtSourceName = findViewById(R.id.txtSourceName);
        WebView webView = (WebView) findViewById(R.id.webview);

        txtSName  = getIntent().getStringExtra("txtSName");

        if(txtSName.equals("WARNING")){
            txtSourceName.setText("PRIVACY POLICY");
        }else{
            txtSourceName.setText(txtSName+"");
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        if(txtSName.equals("FAQ")){
            webView.loadUrl(Constant.FAQ);
        }
        else if(txtSName.equals("LAW")){
            webView.loadUrl(Constant.LAW);
        }else if(txtSName.equals("WARNING")){
            webView.loadUrl(Constant.PRIVACY_POLICY);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
