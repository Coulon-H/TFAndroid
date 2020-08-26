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
            fichename(fc);
            byte[] backup = new byte[(int) this.fc.length()];

            fin = new FileInputStream(fc);
            din = new BufferedInputStream(fin);
            this.out = client.getOutputStream();


            din.read(backup, 0, backup.length);
            out.write(backup, 0, backup.length);
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (this.din != null) {
                try {
                    this.fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    this.din.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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

    public void fichename(File f){ // Fonction envoyant le nom et la taille du fichier
        try{
            String file = f.getName() + "/"+ f.length();
            out = client.getOutputStream();
            dout = new DataOutputStream(out);

            dout.writeUTF(file);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(out != null) {
                out = null;
                dout = null;
            }
        }
    }

}
