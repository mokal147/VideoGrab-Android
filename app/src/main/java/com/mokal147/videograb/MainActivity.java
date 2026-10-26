package com.mokal147.videograb;

import android.app.Activity;
import android.content.Intent;
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

    private void startTermuxDownload(String url, String mode, String start, String end) {
        Intent intent = new Intent();
        intent.setClassName("com.termux", "com.termux.app.RunCommandService");
        intent.setAction("com.termux.RUN_COMMAND");
        intent.putExtra("com.termux.RUN_COMMAND_PATH", "/data/data/com.termux/files/home/.termux/tasker/videograb-download");
        intent.putExtra("com.termux.RUN_COMMAND_ARGUMENTS", new String[]{url, mode, start, end});
        intent.putExtra("com.termux.RUN_COMMAND_WORKDIR", "/data/data/com.termux/files/home");
        intent.putExtra("com.termux.RUN_COMMAND_BACKGROUND", true);
        intent.putExtra("com.termux.RUN_COMMAND_SESSION_ACTION", "0");
        try {
            startService(intent);
            Toast.makeText(this, "تم إرسال طلب التنزيل إلى Termux", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "تعذر تشغيل Termux: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
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

            String mode = typeGroup.getCheckedRadioButtonId() == 2
                ? "audio"
                : "video";

            String start = "";
            String end = "";

            if (rangeGroup.getCheckedRadioButtonId() == 4) {
                start = startInput.getText().toString().trim();
                end = endInput.getText().toString().trim();

                if (!start.matches("\\d{2}:\\d{2}:\\d{2}")
                    || !end.matches("\\d{2}:\\d{2}:\\d{2}")) {
                    Toast.makeText(this, "اكتب الوقت بصيغة 00:00:30", Toast.LENGTH_LONG).show();
                    return;
                }

                String[] a = start.split(":");
                String[] b = end.split(":");

                int startSeconds = Integer.parseInt(a[0]) * 3600
                    + Integer.parseInt(a[1]) * 60
                    + Integer.parseInt(a[2]);

                int endSeconds = Integer.parseInt(b[0]) * 3600
                    + Integer.parseInt(b[1]) * 60
                    + Integer.parseInt(b[2]);

                if (Integer.parseInt(a[1]) > 59 || Integer.parseInt(a[2]) > 59
                    || Integer.parseInt(b[1]) > 59 || Integer.parseInt(b[2]) > 59
                    || endSeconds <= startSeconds) {
                    Toast.makeText(this, "تأكد أن الوقت صحيح وأن النهاية بعد البداية.", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            status.setText("جارٍ إرسال طلب التنزيل إلى Termux...");
            startTermuxDownload(url, mode, start, end);
        });

        scrollView.addView(layout);
        setContentView(scrollView);
    }
}
