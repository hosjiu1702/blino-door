package com.example.hosjiu.blinoapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.EventListener;
import java.util.Set;
import java.util.UUID;

import com.example.hosjiu.blinoapp.Connecting;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_BLUETOOTH_ENABLE = 2;
    private String HC05_MAC_ADDRESS = "98:D3:31:F4:12:EE";

    private Button btnConnect;
    private Button btnUp;
    private Button btnStop;
    private Button btnDown;
    public static ProgressBar progressBar;
    public static LinearLayout container;

    private BluetoothAdapter bluetoothAdapter;
    private Connecting btModule = new Connecting();
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
        /*
        if(connecting != null){
            connecting.execute();
        }
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnUp:
                sendData("0");
                break;
            case R.id.btnStop:
                sendData("1");
                break;
            case R.id.btnDown:
                sendData("2");
                break;
            case R.id.btnConnect:
                connect();
                break;
        }

        /*
        // Lay danh sach cac device da duoc paired
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        for(BluetoothDevice device : pairedDevices)
        {
            String deviceName = device.getName();
            String deviceAddress = device.getAddress();
            System.out.println();
            System.out.print(deviceName + " : ");
            System.out.println(deviceAddress);
        }
        */
    }

    private void connect(){
        btModule.execute();
    }

    private void sendData(String data) {
        if(btModule.bluetoothSocket != null){
            try{
                OutputStream outputStream = btModule.bluetoothSocket.getOutputStream();
                outputStream.write(data.toString().getBytes());
            } catch (IOException e){
                Toast.makeText(getApplicationContext(),"Sending Error", Toast.LENGTH_LONG).show();
            }
        }
    }
}