package org.auction.udp;

/**
 * Created by alamgir on 6/5/2017.
 */

import android.os.AsyncTask;

        import android.os.AsyncTask;
        import android.util.Log;

        import com.auction.util.ACTION;
        import com.google.gson.Gson;
        import com.google.gson.GsonBuilder;

        import org.bdlions.client.reqeust.threads.IServerCallback;
        import org.bdlions.client.reqeust.threads.UDPCom;
        import org.bdlions.transport.packet.IPacketHeader;
        import org.bdlions.transport.packet.PacketHeaderImpl;

/**
 * Created by alamgir on 5/21/2017.
 */
public class BackgroundWork extends AsyncTask<Object, Integer, Void> {

   UDPCom udpCom = UDPCom.getInstance("185.5.54.210", 10000);

    @Override
    protected Void doInBackground(Object ... params) {
        if(params.length < 3){
            return null;
        }
        IPacketHeader packetHeader = null;
        String request = null;
        IServerCallback callback = null;

        if(params [ 0 ] instanceof IPacketHeader){
            packetHeader = (IPacketHeader) params [ 0 ];
        }
        if(params [ 1 ] instanceof String){
            request = (String) params [ 1 ];
        }
        if(params [ 2 ] instanceof IServerCallback){
            callback = (IServerCallback)params [ 2 ];
        }
        if(packetHeader == null || callback == null){
            return null;
        }
        udpCom.send(packetHeader, request, callback);
        return null;
    }


}

