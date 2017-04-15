package com.realcolorscheme.colorscheme;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;

import java.io.InputStream;
import java.io.OutputStream;

public class Global {
    public static String filename;
    public static Bitmap bitmap;

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

}
