package com.example.mainactivity;

import android.os.AsyncTask;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends AsyncTask<Void, Void, Void> {

    private static int port = 82;
    Socket client = null;
    String[] s = null;
    FileOutputStream fout;
    BufferedOutputStream dout = null;
    InputStream ins = null;
    DataInputStream din;
    File f;

    public Server() throws IOException {
        try (ServerSocket server = new ServerSocket(port)) {
            this.client = server.accept();
        }
    }


    @Override
    protected Void doInBackground(Void... voids) {

        try {
            receive();
            f = new File(s[0]);
            fout = new FileOutputStream(f);
            dout = new BufferedOutputStream(fout);
            ins = this.client.getInputStream();

            long taille = Long.parseLong(s[1]);
            byte[] receiver = new byte[(int) taille];
            int bytesRead = 0;

            while ((bytesRead = ins.read(receiver)) != -1)
                dout.write(receiver, 0, bytesRead);

            dout.flush();
            client.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public void receive() throws IOException {
        try{
            String r;
            ins = this.client.getInputStream();
            din = new DataInputStream(ins);

            r = din.readUTF();
            System.out.println(r);
            s = r.split("/");

        } catch (IOException ex) {
            ex.printStackTrace();
        }finally {
            if(ins != null){
                ins = null;
                din = null;
            }
        }
    }
}
