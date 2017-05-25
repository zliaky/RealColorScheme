package com.realcolorscheme.colorscheme;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by mac on 17/4/16.
 */

public class Bluetooth {

    public static BluetoothAdapter mBluetoothAdapter;
    public static BluetoothSocket mmSocket;
    public static BluetoothDevice mmDevice;
    public static OutputStream mmOutputStream;
    public static InputStream mmInputStream;
    public static Thread workerThread;

    public static byte[] readBuffer;
    public static int readBufferPosition;
    public static volatile boolean stopWorker;

    public static boolean btState;

    public static Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            //关闭ProgressDialog
            //myDialog.dismiss();
            //btBtn.setBackgroundResource(R.drawable.bluetooth_connected);
            //更新UI
            //statusTextView.setText("Completed!");
        }};

    public static void connectBt() {
        if(btState == false) {
            //myDialog = ProgressDialog.show(MainActivity.this, "Connect to Pallete", "Please wait...", true, false);
            new Thread(){
                @Override
                public void run() {
                    try {
                        findBT();
                        openBT();
                        btState = true;
                    } catch (IOException ex) {
                    }
                    handler.sendEmptyMessage(0);
                }}.start();
        }
    }

    public static void findBT()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if(mBluetoothAdapter == null)
//        {
//            info.setText("No bluetooth adapter available");
//        }

        if(!mBluetoothAdapter.isEnabled())
        {
            Log.d("find bt","mBluetoothAdapter not Enabled...");
            //Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals("HC-06"))
                {
                    mmDevice = device;
                    break;
                }
            }
        }
        //info.setText("Bluetooth Device Found");
    }

    public static void openBT() throws IOException
    {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID

        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        Log.d("openBt","create");

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }

        //mmSocket.connect();
        Log.d("openBt","connect");
        mmOutputStream = mmSocket.getOutputStream();
        Log.d("openBt","out");
        mmInputStream = mmSocket.getInputStream();
        Log.d("openBt","in");

        beginListenForData();
        Log.d("openBt","listen");

        //info.setText("Bluetooth Opened");
    }

    public static void beginListenForData()
    {
        //final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    public static void closeBT() throws IOException
    {
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
        //info.setText("Bluetooth Closed");
    }


}
