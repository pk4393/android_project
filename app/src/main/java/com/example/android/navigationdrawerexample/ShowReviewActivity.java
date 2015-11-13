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

import helperclasses.ProductReviewDetail;

import static com.android.volley.Request.Method.GET;

public class ShowReviewActivity extends Activity {

    ListView reviewListView;
    ProductReviewListAdapter reviewListAdapter;
    ArrayList<ProductReviewDetail> arrayReview=new ArrayList<>();
    String urlReview="";
    String sku="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviewlayout);
        reviewListView=(ListView)findViewById(R.id.listView);
        reviewListAdapter=new ProductReviewListAdapter(ShowReviewActivity.this,R.layout.reviewscustomlistview,arrayReview);
        reviewListView.setAdapter(reviewListAdapter);

        Intent intent=getIntent();
        sku=intent.getStringExtra("sku");

        urlReview="http://api.bestbuy.com/v1/reviews(sku="+sku+")?format=json&apiKey=rw5mk6btukthdwu45xbwcssx";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (GET, urlReview, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("reviews");
                            if(jsonArray.length()!=0) {
                                arrayReview.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    JSONArray reviewer = object.getJSONArray("reviewer");
                                    JSONObject reviwerObject=reviewer.getJSONObject(0);
                                    String name=reviwerObject.getString("name");
                                    String rating = object.getString("rating");
                                    String comment=object.getString("comment");
                                    arrayReview.add(new ProductReviewDetail(name,rating,comment));
                                }
                            }
                            else
                            {
                                Toast.makeText(ShowReviewActivity.this, "No Product review available", Toast.LENGTH_LONG).show();
                            }
                            reviewListAdapter.notifyDataSetChanged();

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
        MySingleton.getInstance(ShowReviewActivity.this).addToRequestQueue(jsObjRequest);


    }//end of OnCreate

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }

    class ProductReviewListAdapter extends BaseAdapter {
        Context context;
        int layoutId;
        ArrayList<ProductReviewDetail> listProduct;

        public ProductReviewListAdapter(Context context, int layoutId,
                                        ArrayList<ProductReviewDetail> listProduct) {
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
        public ProductReviewDetail getItem(int position) {
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
            final ProductReviewDetail product = listProduct.get(position);
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView=inflater.inflate(layoutId,null);
            TextView textViewName = (TextView) itemView.findViewById(R.id.textView2);
            TextView textViewRating = (TextView) itemView.findViewById(R.id.textView4);
            TextView textViewComment=(TextView)itemView.findViewById(R.id.textView6);
            textViewName.setText(product.getReviewerName());
            textViewRating.setText(product.getProductRating());
            textViewComment.setText(product.getReviewComment());
            return itemView;
        }
    }

}
