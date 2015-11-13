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
import android.widget.Button;
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

public class ProductsActivity extends Activity {

    ListView listViewProduct;
    Button tradingButton;
    ArrayList<Products> productArray = new ArrayList<>();
    String url = "";
    UserListAdapter listAdapter;
    String categoryId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        listViewProduct=(ListView)findViewById(R.id.listView);
        tradingButton=(Button)findViewById(R.id.button1);
        listAdapter=new UserListAdapter(ProductsActivity.this,R.layout.customproductlistview,productArray);
        listViewProduct.setAdapter(listAdapter);

        Intent intent=getIntent();
        final String categoryId=intent.getStringExtra("categoryId");
        //Volley Api Json Code
        url = "http://api.bestbuy.com/v1/products(categoryPath.id="+categoryId+")?format=json&apiKey=rw5mk6btukthdwu45xbwcssx";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("products");
                            if(jsonArray.length()!=0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    if (i == 8)
                                        continue;
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String sku = object.getString("sku");
                                    String productId = object.getString("productId");
                                    String name = object.getString("name");
                                    String regularPrice = object.getString("regularPrice");
                                    String salePrice = object.getString("salePrice");
                                    String productImageUrl = object.getString("image");
                                    String description = object.getString("shortDescription");
                                    productArray.add(new Products(name, sku, productId, regularPrice, salePrice, productImageUrl, description));
                                }
                            }
                            else
                            {
                                Toast.makeText(ProductsActivity.this,"No Product available for this category",Toast.LENGTH_LONG).show();
                            }
                            listAdapter.notifyDataSetChanged();

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
        MySingleton.getInstance(ProductsActivity.this).addToRequestQueue(jsObjRequest);

        listViewProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Products p = listAdapter.getItem(i);
                String productName = p.getProductName();
                String productPrice = p.getProductSalePrice();
                String imageUrl = p.getImageUrl();
                String description=p.getDescription();
                String sku=p.getProductSku();
                Intent intent1 = new Intent(ProductsActivity.this, ProductsFullDetailActivity.class);
                intent1.putExtra("productName", productName);
                intent1.putExtra("productPrice", productPrice);
                intent1.putExtra("imageUrl", imageUrl);
                intent1.putExtra("description",description);
                intent1.putExtra("categoryId",categoryId);
                intent1.putExtra("sku",sku);
                startActivity(intent1);
            }
        });

        tradingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProductsActivity.this,BestProductSearchActivity.class);
                intent.putExtra("categoryId",categoryId);
                startActivity(intent);

            }
        });
    }//end of OnCreate
    
    class UserListAdapter extends BaseAdapter {
        Context context;
        int layoutId;
        ArrayList<Products> listProduct;

        public UserListAdapter(Context context, int layoutId,
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(layoutId, null);
            TextView textViewName = (TextView) itemView.findViewById(R.id.textView2);
            TextView textViewSalePrice = (TextView) itemView.findViewById(R.id.textView4);
            textViewName.setText(product.getProductName());
            textViewSalePrice.setText(product.getProductSalePrice());
            return itemView;
        }
    }
}
