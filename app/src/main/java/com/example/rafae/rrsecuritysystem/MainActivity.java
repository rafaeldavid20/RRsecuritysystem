package com.example.rafae.rrsecuritysystem;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText mInputView;
    private Button openDoorButton;
    private Button closeDoorButton;
    private Button onButton;
    private Button offButton;
    private TextView mcondition;


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("condition");
    private BluetoothServer mBluetoothServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInputView = (EditText) findViewById(R.id.input);
        openDoorButton = (Button) findViewById(R.id.button);
        closeDoorButton = (Button) findViewById(R.id.button2);
        onButton = (Button) findViewById(R.id.button3);
        offButton = (Button) findViewById(R.id.button4);
        mcondition = (TextView) findViewById(R.id.ctextView);

        mBluetoothServer = new BluetoothServer();
        mBluetoothServer.setListener(mBluetoothServerListener);

        try {
            mBluetoothServer.start();
        } catch (BluetoothServer.BluetoothServerException e) {
            e.printStackTrace();
            writeError(e.getMessage());
        }


    }

    @Override
    protected void onStart(){
        super.onStart();

        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                mcondition.setText(text);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        mBluetoothServer.stop();
        mBluetoothServer = null;
    }

    /**
     * Bluetooth server events listener.
     */
    private BluetoothServer.IBluetoothServerListener mBluetoothServerListener =
            new BluetoothServer.IBluetoothServerListener() {
                @Override
                public void onStarted() {
                    writeMessage("*** Server has started, waiting for client connection ***");
                    openDoorButton.setEnabled(false);
                    closeDoorButton.setEnabled(false);
                    onButton.setEnabled(false);
                    offButton.setEnabled(false);
                }

                @Override
                public void onConnected() {
                    writeMessage("*** Client has connected ***");
                    openDoorButton.setEnabled(true);
                    closeDoorButton.setEnabled(true);
                    onButton.setEnabled(true);
                    offButton.setEnabled(true);
                }

                @Override
                public void onData(byte[] data) {
                    writeMessage(new String(data));
                }

                @Override
                public void onError(String message) {
                    writeError(message);
                }

                @Override
                public void onStopped() {
                    writeMessage("*** Server has stopped ***");
                    openDoorButton.setEnabled(false);
                    closeDoorButton.setEnabled(false);
                    onButton.setEnabled(false);
                    offButton.setEnabled(false);
                }
            };




    public void onClick(View view){

        String message = "on";
        mConditionRef.setValue("on");
        try {
            mBluetoothServer.send(message.toString().getBytes());

        } catch (BluetoothServer.BluetoothServerException e) {
            e.printStackTrace();
            writeError(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            writeError(e.getMessage());
        }
    }

    public void offClick(View view){

        String message = "off";
        mConditionRef.setValue("off");
        try {
            mBluetoothServer.send(message.toString().getBytes());

        } catch (BluetoothServer.BluetoothServerException e) {
            e.printStackTrace();
            writeError(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            writeError(e.getMessage());
        }
    }

    public void openClick(View view){

        String message = "open";
        mConditionRef.setValue("open");
        try {
            mBluetoothServer.send(message.toString().getBytes());

        } catch (BluetoothServer.BluetoothServerException e) {
            e.printStackTrace();
            writeError(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            writeError(e.getMessage());
        }
    }

    public void closeClick(View view){

        String message = "close";
        mConditionRef.setValue("close");
        try {
            mBluetoothServer.send(message.toString().getBytes());

        } catch (BluetoothServer.BluetoothServerException e) {
            e.printStackTrace();
            writeError(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            writeError(e.getMessage());
        }
    }



    private void writeMessage(String message){
        mInputView.setText(message + "\r\n" + mInputView.getText().toString());
    }

    private void writeError(String message){
        writeMessage("ERROR: " + message);
    }
}
