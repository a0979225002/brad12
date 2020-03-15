package tw.org.iii.brad.brad12;

import androidx.appcompat.app.AppCompatActivity;

import android.net.InetAddresses;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {
        private EditText input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = findViewById(R.id.input);
        getMyIpV2();

        new Thread(){
            @Override
            public void run() {
                receiveUDP();
            }
        }.start();
    }
    private void getMyIP(){
     //寫網路相關需加入執行序才不會錯
        new Thread(){
            @Override
            public void run() {
                try {
                    String myip =  InetAddress.getLocalHost().getHostAddress();
                    Log.v("brad","myip"+myip);
                } catch (UnknownHostException e) {
                    Log.v("Brad",e.toString());
                }
            }
        }.start();
    }
    private void getMyIpV2(){
        try {
            Enumeration<NetworkInterface> en =NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()){
                NetworkInterface networkInterface =  en.nextElement();
                Enumeration<InetAddress> ips = networkInterface.getInetAddresses();
                while (ips.hasMoreElements()){
                    InetAddress ip = ips.nextElement();
                    Log.v("brad",ip+"");
                }
            }
        } catch (Exception e) {
           Log.v("brad",e.toString());
        }
    }

    public void sendUDP(View view) {
        new  Thread(){
            @Override
            public void run() {
                byte[] data = input.getText().toString().getBytes();
                try {
                    DatagramSocket socket = new DatagramSocket();
                    DatagramPacket packet = new DatagramPacket(data, data.length,InetAddress.getByName("10.0.103.65"),8888);
                    socket.send(packet);
                    socket.close();

                    Log.v("brad","send ok");
                }catch (Exception e){
                    Log.v("brad",e.toString());
                }
            }
        }.start();

    }
    private void receiveUDP(){
        while (true) {
            byte[] buf = new byte[4096];
            try {
                DatagramSocket socket = new DatagramSocket(8888);
                DatagramPacket packet = new DatagramPacket(buf,buf.length);
                socket.receive(packet);
                socket.close();
                System.out.println("i got it");
                String who=packet.getAddress().getHostAddress();
                byte[] data = packet.getData();
                int len = packet.getLength();
                String mesg = new String(data,0,len);
                Log.v("brad","send");
                if (mesg.equals("quit")){
                    break;
                }
            } catch (Exception e) {
                Log.v("brad",e.toString());
            }
        }

    }}
