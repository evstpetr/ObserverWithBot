package ru.pes.observerwithbot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //TextViews
    TextView tvBotName;
    TextView tvBotStatus;
    //Buttons
    Button btnBotOnOff;
    //String constants
    private static final String BOT_STATUS_ON = "запущен";
    private static final String BOT_STATUS_OFF = "остановлен";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvBotName = (TextView) findViewById(R.id.tvBotName);
        tvBotStatus = (TextView) findViewById(R.id.tvBotStatus);
        tvBotStatus.setText(BOT_STATUS_OFF);
        btnBotOnOff = (Button) findViewById(R.id.btnBotOnOff);
        btnBotOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvBotStatus.getText() == BOT_STATUS_OFF) {
                    tvBotStatus.setText(BOT_STATUS_ON);
                } else {
                    tvBotStatus.setText(BOT_STATUS_OFF);
                }
            }
        });
    }
}
