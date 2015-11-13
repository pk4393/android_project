package com.example.android.navigationdrawerexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import helperclasses.BestProductDetail;

import static com.android.volley.Request.Method.GET;

public class BestProductSearchActivity extends Activity {

    ListView productListView;
    ProductListAdapter productAdapter;
    ArrayList<BestProductDetail> productArray=new ArrayList<>();
    String url="";
    String categoryId="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_product_search);
        productListView=(ListView)findViewById(R.id.listView);
        productAdapter=new ProductListAdapter(BestProductSearchActivity.this,R.layout.custombestproductlistview,productArray);
        productListView.setAdapter(productAdapter);

        Intent intent=getIntent();
        categoryId=intent.getStringExtra("categoryId");
        //url
        url="http://api.bestbuy.com/beta/products/trendingViewed(categoryId="+categoryId+")?apiKey=rw5mk6btukthdwu45xbwcssx";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            if(jsonArray.length()!=0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String sku = object.getString("sku");
                                    JSONObject title = object.getJSONObject("names");
                                    String name=title.getString("title");
                                    JSONObject image = object.getJSONObject("images");
                                    String productImageUrl=image.getString("standard");
                                    JSONObject prices = object.getJSONObject("prices");
                                    String salePrice = prices.getString("current");
                                    JSONObject descriptions = object.getJSONObject("descriptions");
                                    String productDetail = descriptions.getString("short");
                                    productArray.add(new BestProductDetail(productDetail, productImageUrl, name, salePrice, sku));
                                }
                            }
                            else
                            {
                                Toast.makeText(BestProductSearchActivity.this,"NO Trending Product Available for this category",Toast.LENGTH_LONG).show();
                            }
                            productAdapter.notifyDataSetChanged();

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
        MySingleton.getInstance(BestProductSearchActivity.this).addToRequestQueue(jsObjRequest);

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BestProductDetail product=productAdapter.getItem(i);
                String productName = product.getProductName();
                String productPrice = product.getPrice();
                String imageUrl = product.getImageUrl();
                String description=product.getDescription();
                String sku=product.getSku();
                Intent intent1 = new Intent(BestProductSearchActivity.this, ProductsFullDetailActivity.class);
                intent1.putExtra("productName", productName);
                intent1.putExtra("productPrice", productPrice);
                intent1.putExtra("imageUrl", imageUrl);
                intent1.putExtra("description",description);
                intent1.putExtra("sku",sku);
                intent1.putExtra("categoryId",categoryId);
                startActivity(intent1);
            }
        });
    }//end of onCreate


    class ProductListAdapter extends BaseAdapter {
        Context context;
        int layoutId;
        ArrayList<BestProductDetail> listProduct;

        public ProductListAdapter(Context context, int layoutId,
                               ArrayList<BestProductDetail> listProduct) {
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
        public BestProductDetail getItem(int position) {
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
            final BestProductDetail product = listProduct.get(position);
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView=inflater.inflate(layoutId,null);
            TextView textViewName = (TextView) itemView.findViewById(R.id.textView2);
            TextView textViewSalePrice = (TextView) itemView.findViewById(R.id.textView4);
            textViewName.setText(product.getProductName());
            textViewSalePrice.setText(product.getPrice());
            return itemView;
        }
    }
}
