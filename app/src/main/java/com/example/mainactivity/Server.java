package com.example.mainactivity;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
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
            FileWriter fw = new FileWriter();


            String clientAddress = client.getInetAddress().getHostAddress();
            System.out.println("\r\nNew connection from " + clientAddress);
            //
            // Read binary data from client socket and write to file
            //
            ins = client.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(ins);
            int fileSize = fw.writeFile(f, bis);
            bis.close();

            //
            // Close socket connection
            //
            client.close();
            System.out.println("\r\nWrote " + fileSize + " bytes to file " + s[0]);
        }catch (IOException e){
            e.printStackTrace();
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
        }
    }
}
