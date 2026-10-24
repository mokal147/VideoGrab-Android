package com.mokal147.videograb;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText urlInput;
    private EditText startInput;
    private EditText endInput;
    private RadioGroup typeGroup;
    private RadioGroup rangeGroup;

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    private TextView createLabel(String text) {
        TextView label = new TextView(this);
        label.setText(text);
        label.setTextSize(16);
        label.setTextColor(Color.rgb(35, 45, 55));
        label.setPadding(0, dp(12), 0, dp(6));
        return label;
    }

    private EditText createInput(String hint) {
        EditText input = new EditText(this);
        input.setHint(hint);
        input.setTextSize(16);
        input.setSingleLine(true);
        return input;
    }

    private RadioButton createRadio(String text) {
        RadioButton radio = new RadioButton(this);
        radio.setText(text);
        radio.setTextSize(15);
        return radio;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scrollView = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(20), dp(24), dp(20), dp(24));
        layout.setLayoutDirection(ViewGroup.LAYOUT_DIRECTION_RTL);
        layout.setBackgroundColor(Color.rgb(245, 247, 250));

        TextView title = new TextView(this);
        title.setText("VideoGrab");
        title.setTextSize(28);
        title.setTextColor(Color.rgb(20, 120, 85));
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, dp(12));
        layout.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("تنزيل الفيديوهات والموسيقى");
        subtitle.setTextSize(16);
        subtitle.setTextColor(Color.DKGRAY);
        subtitle.setGravity(Gravity.CENTER);
        layout.addView(subtitle);

        layout.addView(createLabel("رابط الفيديو أو الموسيقى"));

        urlInput = createInput("ألصق الرابط هنا");
        layout.addView(urlInput);

        layout.addView(createLabel("نوع التنزيل"));

        typeGroup = new RadioGroup(this);
        typeGroup.setOrientation(RadioGroup.HORIZONTAL);

        RadioButton videoRadio = createRadio("فيديو");
        RadioButton audioRadio = createRadio("صوت");

        videoRadio.setId(1);
        audioRadio.setId(2);

        typeGroup.addView(videoRadio);
        typeGroup.addView(audioRadio);
        videoRadio.setChecked(true);

        layout.addView(typeGroup);

        layout.addView(createLabel("نطاق التنزيل"));

        rangeGroup = new RadioGroup(this);
        rangeGroup.setOrientation(RadioGroup.HORIZONTAL);

        RadioButton fullRadio = createRadio("كامل الفيديو");
        RadioButton partRadio = createRadio("جزء محدد");

        fullRadio.setId(3);
        partRadio.setId(4);

        rangeGroup.addView(fullRadio);
        rangeGroup.addView(partRadio);
        fullRadio.setChecked(true);

        layout.addView(rangeGroup);

        layout.addView(createLabel("وقت البداية (ساعة:دقيقة:ثانية)"));
        startInput = createInput("00:00:00");
        layout.addView(startInput);

        layout.addView(createLabel("وقت النهاية (ساعة:دقيقة:ثانية)"));
        endInput = createInput("00:00:30");
        layout.addView(endInput);

        Button analyzeButton = new Button(this);
        analyzeButton.setText("تحليل الرابط");
        layout.addView(analyzeButton);

        TextView status = new TextView(this);
        status.setText("أدخل رابطًا للبدء");
        status.setTextSize(14);
        status.setGravity(Gravity.CENTER);
        status.setPadding(0, dp(16), 0, dp(8));
        layout.addView(status);

        analyzeButton.setOnClickListener(v -> {
            String url = urlInput.getText().toString().trim();

            if (url.isEmpty()) {
                Toast.makeText(
                    this,
                    "يرجى إدخال رابط أولًا",
                    Toast.LENGTH_SHORT
                ).show();
                return;
            }

            status.setText(
                "تم استلام الرابط. سنضيف تحليل الروابط في المرحلة التالية."
            );
        });

        scrollView.addView(layout);
        setContentView(scrollView);
    }
}
