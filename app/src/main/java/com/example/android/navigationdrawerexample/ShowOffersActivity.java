package com.example.android.navigationdrawerexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import helperclasses.Products;

import static com.android.volley.Request.Method.GET;

public class ShowOffersActivity extends Activity {

    ListView listViewOffers;
    ProductOffersListAdapter offersAdapter;
    ArrayList<Products> arrayOffers=new ArrayList<>();
    String url="";
    String sku="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_offers);
        listViewOffers=(ListView)findViewById(R.id.listView);
        offersAdapter=new ProductOffersListAdapter(ShowOffersActivity.this,R.layout.showoffers_customlistview,arrayOffers);
        listViewOffers.setAdapter(offersAdapter);

        Intent intent=getIntent();
        sku=intent.getStringExtra("sku");

        url="http://api.bestbuy.com/beta/products/"+sku+"/openBox?apiKey=rw5mk6btukthdwu45xbwcssx";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            if(jsonArray.length()!=0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jObject=jsonArray.getJSONObject(i);
                                    JSONArray jsonArrayOffers=jObject.getJSONArray("offers");
                                    for(int j=0;j<jsonArrayOffers.length();j++)
                                    {
                                        JSONObject object=jsonArrayOffers.getJSONObject(j);
                                        String condition=object.getString("condition");
                                        JSONObject priceObject=object.getJSONObject("prices");
                                        String currentPrice=priceObject.getString("current");
                                        String regularPrice= priceObject.getString("regular");
                                        arrayOffers.add(new Products(condition,currentPrice,regularPrice));
                                    }
                                }
                            }
                            else
                            {
                                Toast.makeText(ShowOffersActivity.this, "No offers available", Toast.LENGTH_LONG).show();
                            }
                            offersAdapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(ShowOffersActivity.this).addToRequestQueue(jsObjRequest);

    }//end of OnCreate

    class ProductOffersListAdapter extends BaseAdapter {
        Context context;
        int layoutId;
        ArrayList<Products> listProduct;

        public ProductOffersListAdapter(Context context, int layoutId,
                                        ArrayList<Products> listProduct) {
            super();
            this.context = context;
            this.layoutId = layoutId;
            this.listProduct = listProduct;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listProduct.size();
        }

        @Override
        public Products getItem(int position) {
            // TODO Auto-generated method stub
            return listProduct.get(position);
        }


        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final Products product = listProduct.get(position);
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView=inflater.inflate(layoutId,null);
            TextView textViewCondition = (TextView) itemView.findViewById(R.id.textView2);
            TextView textViewCurrentPrice = (TextView) itemView.findViewById(R.id.textView4);
            TextView textViewRegularPrice=(TextView)itemView.findViewById(R.id.textView6);
            textViewCondition.setText(product.getCondition());
            textViewCurrentPrice.setText(product.getProductSalePrice());
            textViewRegularPrice.setText(product.getProductRegularPrice());
            return itemView;
        }
    }

}
