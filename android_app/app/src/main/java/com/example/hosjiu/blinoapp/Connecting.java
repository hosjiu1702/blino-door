package com.example.hosjiu.blinoapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import com.example.hosjiu.blinoapp.MainActivity;

/**
 * Created by giahu on 3/29/2018.
 */

public class Connecting extends AsyncTask<Void, Void, Void>{
    private final String HC05_MAC_ADDRESS = "98:D3:31:F4:12:EE";
    private final String BluetoothUUID = "00001101-0000-1000-8000-00805F9B34FB";
    private UUID clientUUID = UUID.fromString(BluetoothUUID);

    private BluetoothAdapter bluetoothAdapter;
    public BluetoothDevice HC05;
    public BluetoothSocket bluetoothSocket = null;

    private boolean isConnect = true;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        MainActivity.container.setVisibility(View.GONE);
        MainActivity.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //Get paired device using MAC address
        HC05 = bluetoothAdapter.getRemoteDevice(HC05_MAC_ADDRESS);
        //Create RFCOMM channel using UUID
        try{
            bluetoothSocket = HC05.createRfcommSocketToServiceRecord(clientUUID);
        } catch (IOException e){
            System.out.println("Creating RFCOMM channel is unsuccessful");
        }
        //Connecting to the HC05 device
        try{
            if(bluetoothSocket != null)
            {
                bluetoothSocket.connect();

            }
            else
            {
                System.out.println("bluetoothSocket is a null variable");
            }
        } catch (IOException e){
            System.out.println("Connecting to the HC05 is unsuccessful");
            isConnect = false;
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
        if(isConnect){
            MainActivity.container.setVisibility(View.VISIBLE);
            MainActivity.progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
