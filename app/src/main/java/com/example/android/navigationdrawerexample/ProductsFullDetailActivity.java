package com.example.android.navigationdrawerexample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

public class ProductsFullDetailActivity extends Activity {

    ImageView productImageView;
    TextView textViewName,textViewPrice,textViewDescription;
    Button buttonReview,buttonOffers;

    String url="";
    String productName;
    String productPrice;
    String description;
    ImageRequest request;
    String categoryId;
    String sku="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_full_detail);
        productImageView=(ImageView)findViewById(R.id.imageView);
        textViewName=(TextView)findViewById(R.id.textView2);
        textViewPrice=(TextView)findViewById(R.id.textView4);
        textViewDescription=(TextView)findViewById(R.id.textView6);
        buttonReview=(Button)findViewById(R.id.button);
        buttonOffers=(Button)findViewById(R.id.button2);

        Intent intent=getIntent();
        productName=intent.getStringExtra("productName");
        productPrice=intent.getStringExtra("productPrice");
        url=intent.getStringExtra("imageUrl");
        description=intent.getStringExtra("description");
        categoryId=intent.getStringExtra("categoryId");
        sku=intent.getStringExtra("sku");

        // Retrieves an image specified by the URL, displays it in the UI.
        if(!url.equalsIgnoreCase("")) {
            request = new ImageRequest(url,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            productImageView.setImageBitmap(bitmap);
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            productImageView.setImageResource(R.drawable.shirt);
                        }
                    });
        }
// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(request);

        textViewName.setText(productName);
        textViewPrice.setText(productPrice);
        textViewDescription.setText(description);
        buttonReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductsFullDetailActivity.this, ShowReviewActivity.class);
                intent.putExtra("sku", sku);
                startActivity(intent);
            }
        });

        buttonOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductsFullDetailActivity.this, ShowOffersActivity.class);
                intent.putExtra("sku", sku);
                startActivity(intent);
            }
        });
    }
}
