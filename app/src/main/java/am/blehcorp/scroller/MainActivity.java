package am.blehcorp.scroller;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.Key;

public class MainActivity extends AppCompatActivity {

    private final double psi = 1;
    private double down_y = 0;
    private double up_y = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.i ("TOUCHEVENT", event.toString());

        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            down_y = event.getY();
        }
        else if (action == MotionEvent.ACTION_MOVE) {
            double new_y = event.getY();

            up_y = new_y;
        }
        else if (action == MotionEvent.ACTION_UP) {

            int param = (int) Math.floor((up_y - down_y) * psi );
            send_udp_scroll_package(param);

        }

        return super.onTouchEvent(event);
    }



    private void send_udp_scroll_package (int param) {

        Log.i ("UDP", "Sending udp package: " + Integer.toString(param) );

        DatagramSocket ds = null;
        String message = "SCROLL " + Integer.toString(param);
        Log.i("UDP", "Message: [" + message + "]");

        sendMessage (message);

    }

    /* taken from https://stackoverflow.com/questions/19540715/send-and-receive-data-on-udp-socket-java-android */
    private void sendMessage(final String message) {

        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {

            String stringData;

            @Override
            public void run() {

                DatagramSocket ds = null;
                try {
                    ds = new DatagramSocket();
                    // IP Address below is the IP address of that Device where server socket is opened.
                    InetAddress serverAddr = InetAddress.getByName("255.255.255.255");
                    DatagramPacket dp;
                    dp = new DatagramPacket(message.getBytes(), message.length(), serverAddr, 6666);
                    ds.send(dp);


                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (ds != null) {
                        ds.close();
                    }
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Log.i("UDP_SEND","Sent done!");

                    }
                });
            }
        });

        thread.start();
    }
}
