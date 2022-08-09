package com.creativeinfoway.smartstops.app.ui.activity;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.creativeinfoway.smartstops.app.android.navigation.testapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mapbox.navigation.utils.SmartStopsPrefLanguage;

public class LanguageUtils {

    Activity activity;

    public LanguageUtils(Activity activity) {
        this.activity = activity;
    }

    public void openBottomSheet() {
        SmartStopsPrefLanguage smartStopsPrefLanguage = new SmartStopsPrefLanguage(activity);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        bottomSheetDialog.setContentView(R.layout.custom_language_bottom_shit_dailog);

        ImageView close = bottomSheetDialog.findViewById(R.id.iv_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();

            }
        });

        RadioGroup radioGroup = bottomSheetDialog.findViewById(R.id.radio_group);
        RadioButton btnGerman = (RadioButton) radioGroup.findViewById(R.id.rb_german);
        RadioButton btnFrench = (RadioButton) radioGroup.findViewById(R.id.rb_french);
        RadioButton btnMandarin = (RadioButton) radioGroup.findViewById(R.id.rb_Mandarin);
        RadioButton btnContonoese = (RadioButton) radioGroup.findViewById(R.id.rb_contonese);
        RadioButton btnEnglish = (RadioButton) radioGroup.findViewById(R.id.rb_English);
        Button btnDone = bottomSheetDialog.findViewById(R.id.btn_done);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == R.id.rb_german) {
                    smartStopsPrefLanguage.putString("lang", "german");
                } else if (selectedId == R.id.rb_french) {
                    smartStopsPrefLanguage.putString("lang", "french");
                } else if (selectedId == R.id.rb_Mandarin || selectedId == 4) {
                    smartStopsPrefLanguage.putString("lang", "mandarin");
                } else if (selectedId == R.id.rb_contonese) {
                    smartStopsPrefLanguage.putString("lang", "cantonoese");
                } else if (selectedId == R.id.rb_English) {
                    smartStopsPrefLanguage.putString("lang", "english");
                }
                bottomSheetDialog.dismiss();
            }
        });
        switch (smartStopsPrefLanguage.getString("lang")) {
            case "german":
                btnGerman.setChecked(true);
                break;
            case "french":
                btnFrench.setChecked(true);
                break;
            case "mandarin":
                btnMandarin.setChecked(true);
                break;
            case "cantonoese":
                btnContonoese.setChecked(true);
                break;
            case "english":
                btnEnglish.setChecked(true);
                break;
            default:
                btnEnglish.setChecked(true);
                break;
        }

        bottomSheetDialog.show();

    }

}
