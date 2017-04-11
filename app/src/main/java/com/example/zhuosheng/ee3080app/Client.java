package com.example.zhuosheng.ee3080app;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.TextView;

public class Client extends AsyncTask<Void, Void, String> {

    String dstAddress;
    int dstPort,timeout;
    String response = "";
    Bitmap bmp;

    private PostTaskListener<String> postTaskListener;

    Client(PostTaskListener<String> postTaskListener, String addr, int port, Bitmap bmp, int TO) {
        dstAddress = addr;
        dstPort = port;
        timeout = TO;
        this.bmp = bmp;
        this.postTaskListener = postTaskListener;
    }

    @Override
    protected String doInBackground(Void... arg0) {

        Socket socket = null;

        try {
            InetAddress serverAdd = InetAddress.getByName(dstAddress);
            SocketAddress sc_add = new InetSocketAddress(serverAdd,dstPort);
            socket = new Socket();
            socket.connect(sc_add,timeout);

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] bytes = baos.toByteArray();
            int size = bytes.length;
            out.writeInt(size);
            out.write(bytes);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            response = in.readLine();

            //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
            //1024);
            //byte[] buffer = new byte[1024];

            //int bytesRead;
            //InputStream inputStream = socket.getInputStream();

			/*
			 * notice: inputStream.read() will block if no data return
			 */
            /*while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                response += byteArrayOutputStream.toString("UTF-8");
            }*/

        } catch (SocketTimeoutException e){
            response = "TimeoutException:" + e.toString();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if(result != null && postTaskListener != null){
            postTaskListener.onPostTask(result);
        }
    }

}