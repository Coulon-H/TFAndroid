package com.example.mainactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {

    TextView adresse;
    Button p, r, s;
    EditText rN, rF;
    Intent i;
    ArrayList<Machine> t = new ArrayList<>();
    String filepath;
    File f = null;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileretrieve();
        adresse = findViewById(R.id.adress);
        p = findViewById(R.id.browse);
        r = findViewById(R.id.receive);
        s = findViewById(R.id.send);
        rN = findViewById(R.id.retrievermachine);
        rF = findViewById(R.id.retrieverfile);

        adresse.setText("Adresse Machine : "+ Addresse());
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = rN.getText().toString();
                init(text);
                checkPermissions();
                i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("*/*");
                startActivityForResult(i, 10);
            }
        });

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String host = retrieveuser();
                Client c = new Client(host,82, f);
                c.execute(host);
            }
        });

        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Server s;
                try {
                    s = new Server();
                    s.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

   protected void onStop() {
        super.onStop();
        fileregistration();
    }

    protected void checkPermissions() {
        final int writeExternalPermissionCheck = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);



        if (writeExternalPermissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults != null) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Pass GRANTED", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please enable write permission from Apps", Toast.LENGTH_SHORT).show();

                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                filepath = data.getData().getPath();
                assert filepath != null;
                String filename = filepath.substring(filepath.lastIndexOf("/") + 1);
                rF.setText(filename);
                String[] t = filepath.split(":");
                f = new File(Environment.getExternalStorageDirectory(), t[1]);
            }
        }
    }

    private static String Addresse(){ // Taking the address of the machine

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.i("Activity", "***** IP="+ ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("Activity", ex.toString());
        }
        return null;
    }


        private String[] text = null;
        private Machine pc = null;
        private String name;
        private ObjectOutputStream oos = null;
        private FileOutputStream fou = null;

        public void init(String machine){
            if(machine.contains("/")) { // Verification of User input
                this.text = machine.split("/");
                this.pc = new Machine(text[0], text[1]);
                t.add(pc);
            }else{
                this.name = machine;
            }
        }


        private void fileregistration(){ // Writing the new list to the file
            try{
                fou = openFileOutput("Save.txt", MODE_PRIVATE);
                oos = new ObjectOutputStream(fou);
                oos.writeObject(t);
                oos.flush();
                oos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String retrieveuser(){// Retrieve tha address of the machine asked
                for(Machine m : t){
                    String o = m.getNom();
                    String l = m.getAddresse();
                    System.out.println(o+" "+l);
                    System.out.println(t.toString() );
                    if(o.equals(name) || l.equals(name)){
                        this.pc = m;
                        break;
                    }
                }

            return this.pc.getAddresse();
        }


        private void fileretrieve(){ // Take the list of machine registered
            try{


                FileInputStream fin = openFileInput("Save.txt");
                ObjectInputStream oin = new ObjectInputStream(fin);

                t = (ArrayList<Machine>) oin.readObject();
                oin.close();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

}