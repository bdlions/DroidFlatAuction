package auction.org.droidflatauction;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by bdlions on 07/06/2017.
 */

public class MyAdvertPropertyAdapter extends BaseAdapter {
    private static Context context;
    private ArrayList<Integer> listId, productIdList;
    private ArrayList<String> property_title_list, property_bedroom_list,property_bathroom_list,property_price_list, imgList;
    public String sessionId;

    public MyAdvertPropertyAdapter(Context context, String sessionId, ArrayList<Integer> productIdList, ArrayList<Integer> listId, ArrayList<String> imgList, ArrayList<String> property_title_list,ArrayList<String> property_bedroom_list,ArrayList<String> property_bathroom_list,ArrayList<String> property_price_list) {
        this.context = context;
        this.sessionId = sessionId;
        this.productIdList = productIdList;
        this.imgList = imgList;
        this.listId = listId;
        this.property_title_list = property_title_list;
        this.property_bedroom_list = property_bedroom_list;
        this.property_bathroom_list = property_bathroom_list;
        this.property_price_list = property_price_list;
    }

    @Override
    public int getCount() {
        return property_title_list.size();
    }

    @Override
    public Object getItem(int position) {
        return property_title_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = View.inflate(context,R.layout.my_advert_property_row, null);
            /*convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int productId = productIdList.get(position);

                    Product product = new Product();
                    product.setId(productId);
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    String productString = gson.toJson(product);

                    //String sessionId = session.getSessionId();
                    org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
                    packetHeader.setAction(ACTION.FETCH_PRODUCT_INFO);
                    packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
                    packetHeader.setSessionId(sessionId);
                    new BackgroundWork().execute(packetHeader, productString, new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            try
                            {
                                String resultString = (String)msg.obj;
                                Gson gson = new Gson();
                                Product responseProduct = gson.fromJson(resultString, Product.class);
                                System.out.println(responseProduct.getTitle());

                                GsonBuilder gsonBuilder = new GsonBuilder();
                                Gson gson2 = gsonBuilder.create();
                                String productString = gson2.toJson(responseProduct);

                                Intent my_advert_property_intent = new Intent(context, MyAdvertStep2.class);
                                my_advert_property_intent.putExtra("productString", productString);
                                context.startActivity(my_advert_property_intent);
                            }
                            catch(Exception ex)
                            {
                                System.out.println(ex.toString());
                            }
                        }
                    });



                }
            });*/
        }
        ImageView property_images = (ImageView) convertView.findViewById(R.id.my_advert_property_image);

        TextView property_title = (TextView) convertView.findViewById(R.id.my_advert_property_title);
        TextView property_bedroom = (TextView) convertView.findViewById(R.id.my_advert_property_bedroom);
        TextView property_bathroom = (TextView) convertView.findViewById(R.id.my_advert_property_bathroom);
        TextView property_price = (TextView) convertView.findViewById(R.id.my_advert_property_price);

        //property_images.setImageResource(listId.get(position));
        String imageName = imgList.get(position);
        Picasso.with(convertView.getContext()).load(Constants.baseUrl+Constants.productImagePath_328_212+imageName).into(property_images);

        property_title.setText(property_title_list.get(position));
        property_bedroom.setText(property_bedroom_list.get(position));
        property_bathroom.setText(property_bathroom_list.get(position));
        property_price.setText(property_price_list.get(position));

        return convertView;
    }

}
