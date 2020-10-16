package com.example.mainactivity;

import android.os.AsyncTask;

import java.io.*;
import java.net.Socket;

public class Client extends AsyncTask<String, Void, Void> {

    private int port;
    private Socket client = null;
    private FileInputStream fin;   //Composants pour l'envoi
    private BufferedInputStream din = null;
    private OutputStream out = null;
    DataOutputStream dout;
    File fc ;
    String host;

    Client(String h, int p, File f){
        this.port = p;
        this.fc = f;
        this.host = h;
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {

            client = new Socket(host, port);
            fichename();
            //
            // Read file from disk
            //
            FileReader fileReader = new FileReader();
            byte[] data = fileReader.readFile(fc.getAbsolutePath());
            //
            // Send binary data over the TCP/IP socket connection
            //
            for (byte i : data) {
                out.write(i);
            }

            System.out.println("\r\nSent " + data.length + " bytes to server.");
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public void fichename(){ // Fonction envoyant le nom et la taille du fichier
        try{
            String file = fc.getName() + "/"+ fc.length();
            out = client.getOutputStream();
            dout = new DataOutputStream(out);

            dout.writeUTF(file);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
