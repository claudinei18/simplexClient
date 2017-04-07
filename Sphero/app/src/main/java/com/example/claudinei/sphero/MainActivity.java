package com.example.claudinei.sphero;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.orbotix.ConvenienceRobot;
import com.orbotix.DualStackDiscoveryAgent;
import com.orbotix.Ollie;
import com.orbotix.Sphero;
import com.orbotix.classic.RobotClassic;
import com.orbotix.common.DiscoveryException;
import com.orbotix.common.Robot;
import com.orbotix.common.RobotChangedStateListener;
import com.orbotix.le.RobotLE;

public class MainActivity extends AppCompatActivity implements RobotChangedStateListener {

    private ConvenienceRobot mRobot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DualStackDiscoveryAgent.getInstance().addRobotStateListener( MainActivity.this );
                startDiscovery();
            }
        });


        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRobot != null) {
                    mRobot.sleep();
                }
            }
        });


    }

    public void startDiscovery(){
        try {
            DualStackDiscoveryAgent.getInstance().startDiscovery(this);
        } catch( DiscoveryException e ) {
            //handle exception
        }
    }


    @Override
    public void handleRobotChangedState(Robot robot, RobotChangedStateListener.RobotChangedStateNotificationType type) {
        switch (type) {
            case Online:
                // Bluetooth Classic (Sphero)
                if (robot instanceof RobotClassic) {
                    mRobot = new Sphero(robot);
                }
                // Bluetooth LE (Ollie)
                if (robot instanceof RobotLE) {
                    mRobot = new Ollie(robot);
                }
                break;
        }
    }

}
