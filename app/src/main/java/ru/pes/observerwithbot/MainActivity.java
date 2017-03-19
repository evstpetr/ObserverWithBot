package ru.pes.observerwithbot;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import ru.pes.observerwithbot.bot.BotService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //TextViews
    TextView tvBotName;
    TextView tvBotStatus;
    //Buttons
    Button btnBotOnOff;
    Button btnTest;
    //Messenger
    Messenger mService = null;
    //String constants
    private static final String BOT_STATUS_ON = "запущен";
    private static final String BOT_STATUS_OFF = "остановлен";

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been unexpectedly disconnected - process crashed.
            mService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvBotName = (TextView) findViewById(R.id.tvBotName);
        tvBotStatus = (TextView) findViewById(R.id.tvBotStatus);
        tvBotStatus.setText(BOT_STATUS_OFF);
        btnBotOnOff = (Button) findViewById(R.id.btnBotOnOff);
        btnBotOnOff.setOnClickListener(this);
        btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, BotService.class));
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBotOnOff:
                if (!checkRunningServices(BotService.class)) {
                    tvBotStatus.setText(BOT_STATUS_ON);
                    startService(new Intent(this, BotService.class));
                    bindService(new Intent(this, BotService.class), mConnection, Context.BIND_AUTO_CREATE);
                } else {
                    tvBotStatus.setText(BOT_STATUS_OFF);
                    unbindService(mConnection);
                    stopService(new Intent(this, BotService.class));
                }
                break;
            case R.id.btnTest:
                try {
                    Bundle b = new Bundle();
                    b.putString("data", "test");
                    Message msg = Message.obtain(null, BotService.MSG_SEND_TEST);
                    msg.setData(b);
                    mService.send(msg);
                }
                catch (RemoteException e) {
                    // In this case the service has crashed before we could even do anything with it
                    System.out.println("Service not running");
                }
                break;


        }

    }

    private boolean checkRunningServices(Class<?> serviceClass) {
        ActivityManager am = (ActivityManager) this
                .getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> rs = am.getRunningServices(50); //50 - The maximum number of entries to return in the list.
        // The actual number returned may be smaller, depending on how many services are running.
        for (int i = 0; i < rs.size(); i++) {
            ActivityManager.RunningServiceInfo rsi = rs.get(i);
            System.out.println(rsi.service.getClassName() + " " + serviceClass.getName());
            if (rsi.service.getClassName().equals(serviceClass.getName())) {
                return true;
            }
        }
        return false;
    }
}
