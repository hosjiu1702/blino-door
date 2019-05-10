package com.example.hosjiu.blinoapp;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // private static final int REQUEST_ENABLE_BT = ;
    private final int REQUEST_BLUETOOTH_ENABLE = 2;
    private String HC05_MAC_ADDRESS = "98:D3:31:F4:12:EE";

    private final String UP = "0";
    private final String STOP = "1";
    private final String DOWN = "2";

    private Button btnConnect;
    private Button btnUp;
    private Button btnStop;
    private Button btnDown;
    public static ProgressBar progressBar;
    public static LinearLayout container;

    public static SoundManager mSoundManager = null;

    public static BluetoothAdapter bluetoothAdapter = null;

    // Dung bien toan cuc btModule de tham chieu
    // cho cac doi tuong AsyncTask khac nhau.
    private Connecting btModule = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConnect = (Button)findViewById(R.id.btnConnect);
        btnUp = (Button)findViewById(R.id.btnUp);
        btnStop = (Button)findViewById(R.id.btnStop);
        btnDown = (Button)findViewById(R.id.btnDown);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        container = (LinearLayout)findViewById(R.id.container);

        btnConnect.setOnClickListener(this);
        btnUp.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnDown.setOnClickListener(this);

        mSoundManager = new SoundManager(5, MainActivity.this);

        // Kiem tra Bluetooth module co available tren thiet bi hay khong
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if( bluetoothAdapter == null )
        {
            Toast.makeText(getApplicationContext(), "Your device doesn't support Bluetooth", Toast.LENGTH_LONG).show();
            finish();
        }

        // Bluetooth da duoc BAT hay chua
        if( !bluetoothAdapter.isEnabled() )
        {
            // Khoi tao bluetooth Intent
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            /* Goi startActivityForResult() de cho phep nguoi dung enable Bluetooth (System settings)
             * (Yeu cau BLUETOOTH permission)
             */
             startActivityForResult(enableBluetooth, REQUEST_BLUETOOTH_ENABLE);
        }

        new CountDownTimer(200, 200) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Do nothing
            }

            @Override
            public void onFinish() {
                mSoundManager.playInviteSound();
            }
        }.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!bluetoothAdapter.isEnabled()){
            // Khoi tao bluetooth Intent
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            /* Goi startActivityForResult() de cho phep nguoi dung enable Bluetooth (System settings)
             * (Yeu cau BLUETOOTH permission)
             */
            startActivityForResult(enableBluetooth, REQUEST_BLUETOOTH_ENABLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent Data)
    {
        if(requestCode == REQUEST_BLUETOOTH_ENABLE)
        {
            System.out.println("BLUETOOTH_ENABLE action was invoked");
            if(resultCode == RESULT_OK)
            {
                System.out.println("BLUETOOTH is enabled by you");
            }
            else if(resultCode == RESULT_CANCELED)
            {
                System.out.println("BLUETOOTH is disabled by you");

                // Neu user khong cho phep thi exit.
                finishAndRemoveTask();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnUp:
                sendData(btModule, UP);
                break;
            case R.id.btnStop:
                sendData(btModule, STOP);
                break;
            case R.id.btnDown:
                sendData(btModule, DOWN);
                break;
            case R.id.btnConnect:
                connect(new Connecting(MainActivity.this));
                break;
        }
    }

    private void connect(Connecting btModule_) {
        /*
        * Logic cua method connect() su dung nhieu instance
        * cua class AsyncTask cho moi lan ket noi.
        * */

        if (btModule == null) {
            btModule = btModule_;
            btModule.execute();
        }
        else {
            if (btModule.bluetoothSocket.isConnected()) {
                try {
                    btModule.bluetoothSocket.close();
                } catch (IOException e) {
                    Log.d("bluetoothSocket", "close() method is caught");
                }
            }
            btModule = btModule_;
            btModule.execute();
        }
    }

    private void sendData(Connecting btModule_, String data) {
        if (btModule_ == null) {
            Toast.makeText(getApplicationContext(), "Bạn chưa kết nối!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(btModule_.bluetoothSocket != null){
            try {
                OutputStream outputStream = btModule_.bluetoothSocket.getOutputStream();
                outputStream.write(data.toString().getBytes());
                switch (data) {
                    case UP:
                        mSoundManager.stopCloseDoorSound();
                        mSoundManager.playOpenDoorSound();
                        break;
                    case STOP:
                        break;
                    case DOWN:
                        mSoundManager.stopOpenDoorSound();
                        mSoundManager.playCloseDoorSound();
                        break;
                }
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(),"Xin kết nối lại.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}