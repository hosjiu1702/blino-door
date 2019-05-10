package com.example.hosjiu.blinoapp;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by hosjiu on 3/29/2018.
 * Updated 1: by hosjiu on 8/05/2019.
 */

public class Connecting extends AsyncTask<Void, Void, Void> {

    private final String HC05_MAC_ADDRESS = "98:D3:31:F4:12:EE";
    private final String BluetoothUUID = "00001101-0000-1000-8000-00805F9B34FB";
    private UUID clientUUID = UUID.fromString(BluetoothUUID);

    // private BluetoothAdapter bluetoothAdapter;
    public BluetoothDevice HC05;
    public BluetoothSocket bluetoothSocket = null;

    private boolean isConnected = false;

    private Context mContext;

    //public SoundManager mSoundManager;
    private boolean isPlaying = false;

    // Constructor
    public Connecting(Context context) {
        mContext = context;
        // mSoundManager = new SoundManager(5, mContext);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        MainActivity.container.setVisibility(View.GONE);
        MainActivity.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        /*
        MainActivity.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        */

        //Get paired device using MAC address
        HC05 = MainActivity.bluetoothAdapter.getRemoteDevice(HC05_MAC_ADDRESS);

        // Create RFCOMM channel using UUID
        //  And return a bluetooth socket for outgoing connection
        try {
            bluetoothSocket = HC05.createRfcommSocketToServiceRecord(clientUUID);
        } catch (IOException e) {
            System.out.println("Creating RFCOMM channel is unsuccessful");
        }

         MainActivity.mSoundManager.playConnectSound();

        // Connecting to the HC05 device.
        // Reconnecting until the process is successful.
        while (!isConnected) {
            if (bluetoothSocket != null) {
                try {
                    bluetoothSocket.connect();
                    isConnected = true;
                    isPlaying = false;
                    MainActivity.mSoundManager.pauseConnectingSound();
                } catch (IOException e) {
                    Log.d("bluetoothSocket connect()", "Connecting to the HC05 failed.");
                    Log.d("bluetoothSocket connect()", "It is being reconnected.");
                }
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        //Toast.makeText(MainActivity.this,"a", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if(isConnected){
            MainActivity.container.setVisibility(View.VISIBLE);
            MainActivity.progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(mContext, "Đã kết nối thành công.", Toast.LENGTH_SHORT).show();
            MainActivity.mSoundManager.playFinishSound();
        }
    }
}
