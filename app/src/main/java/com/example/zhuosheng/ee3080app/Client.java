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
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class Client extends AsyncTask<Void, Void, String> {
    private Context context;
    ProgressDialog progDailog;

    String dstAddress;
//    AppCompatActivity ac;
//    TakenPicture takenPicture;
    int dstPort,timeout;
    String response = "";
    Bitmap bmp;
    byte successful = (byte) Integer.parseInt("10",2);;
    byte start_code = (byte)  Integer.parseInt("01",2);;
    private static final String TAG = "Client";
    private PostTaskListener<String> postTaskListener;

    Client(PostTaskListener<String> postTaskListener, String addr, int port, Bitmap bmp, int TO, Context ctx) {
        dstAddress = addr;
        dstPort = port;
        timeout = TO;
        this.bmp = bmp;
//        this.ac = ac;
        this.postTaskListener = postTaskListener;
//        this.takenPicture = takenPicture;
        context = ctx;
        progDailog = new ProgressDialog(context);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progDailog.setMessage("Connecting ...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();
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
            //out.write(successful);

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
            response = "Exception: Timeout";// + e.toString();
            Log.e(TAG, "Exception: Timeout");
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "Exception: Unknown Host";// + e.toString();
            Log.e(TAG, "Exception: Unknown Host");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "Exception: IO";// + e.toString();
            Log.e(TAG, "Exception: IO");

            ;
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
//        if(result!=null)
//        {
//            String[] res = result.split(",");
//            takenPicture.setMainName(res[0]);
//            String[] suggestion = new String[]{res[1],res[2],res[3],res[4]};
//            takenPicture.setSuggestion(suggestion);
//            List<TakenPicture> history = MyPreferences.loadSharedPreferencesLogList(ac);
//            history.add(takenPicture);
//            MyPreferences.saveSharedPreferencesLogList(ac, history);
//
//        }
        if(result != null && postTaskListener != null){
            postTaskListener.onPostTask(result);
        }
        progDailog.dismiss();
    }

}