package org.auction.udp;

/**
 * Created by alamgir on 6/5/2017.
 */

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import org.bdlions.client.reqeust.threads.IServerCallback;
import org.bdlions.client.reqeust.threads.UDPCom;
import org.bdlions.client.reqeust.uploads.UploadService;
import org.bdlions.transport.packet.IPacketHeader;

import java.io.File;

/**
 * Created by alamgir on 5/21/2017.
 */
public class BackgroundUploader extends AsyncTask<Object, Integer, Void> {

    IPacketHeader packetHeader = null;
    String request = null;
    //IServerCallback callback = null;
    Handler handler = null;

   UDPCom udpCom = UDPCom.getInstance("185.5.54.210", 10000);

    @Override
    protected Void doInBackground(Object ... params) {
        try {
            Uri imageUri = (Uri) params[0];
            Handler handler = (Handler) params [ 1 ];
            File image = new File(imageUri.getPath());
//            String fileName = UploadService.uploadImage("http://185.5.54.210/", image);
            String fileName = image.getName();
            Message message = new Message();
            message.obj = fileName;
            handler.sendMessage(message);
            System.out.println(fileName);
        }
        catch (Exception ex){

        }
        return null;
    }


}

