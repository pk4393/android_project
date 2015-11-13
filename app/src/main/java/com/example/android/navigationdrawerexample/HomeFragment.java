package com.example.android.navigationdrawerexample;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import helperclasses.Category;

import static com.android.volley.Request.Method.GET;

/**
 * Created by prashantkumar on 04/11/15.
 */
public class HomeFragment extends Fragment {
    TextView mTextView;
    Button buttonSearch;
    ImageView imageSwitcher;
    ArrayList<String> arrayCategory=new ArrayList<String>();
    ArrayAdapter<String> adapterCity;
    AutoCompleteTextView autoText;

    ListView listViewCategory;
    ArrayList<Category> listCategory = new ArrayList<Category>();
    String url = "";
    UserListAdapter listAdapter;
    int[] imageArray = {R.drawable.laptop,R.drawable.watch,R.drawable.switshirt,R.drawable.iphone,R.drawable.shoe};

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragView = inflater.inflate(R.layout.homefragment, container, false);
        imageSwitcher = (ImageView) fragView.findViewById(R.id.imageView);
        listViewCategory = (ListView) fragView.findViewById(R.id.listView);
        mTextView = (TextView) fragView.findViewById(R.id.textView);
        buttonSearch = (Button) fragView.findViewById(R.id.button);
        autoText=(AutoCompleteTextView)fragView.findViewById(R.id.autoCompleteTextView);
        listAdapter = new UserListAdapter(getActivity(), R.layout.customlistview, listCategory);
        listViewCategory.setAdapter(listAdapter);

        arrayCategory.add("Gift Ideas");
        arrayCategory.add("Learning Toys");
        arrayCategory.add("DVD Games");
        arrayCategory.add("Unique Gifts");
        arrayCategory.add("TVs");
        arrayCategory.add("All Flat-Panel TVs");
        arrayCategory.add("Blu-ray Players");
        adapterCity=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,arrayCategory);
        autoText.setAdapter(adapterCity);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i = 0;

            public void run() {
                imageSwitcher.setImageResource(imageArray[i]);
                i++;
                if (i > imageArray.length - 1) {
                    i = 0;
                }
                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(runnable, 2000);

            //Volley Api Json Code
            url = "http://api.bestbuy.com/v1/categories?format=json&apiKey=rw5mk6btukthdwu45xbwcssx";

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("categories");
                                if (jsonArray.length()!=0)
                                {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String name = object.getString("name");
                                        String id = object.getString("id");

                                        listCategory.add(new Category(name, id));
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getActivity(),"No subcategory available",Toast.LENGTH_LONG).show();
                                }
                                listAdapter.notifyDataSetChanged();
                                Log.e("result", listCategory.toString());
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
            MySingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);

            buttonSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String searchResult = autoText.getText().toString();
                    String searchUrl = "http://api.bestbuy.com/v1/categories(name=" + searchResult + ")?format=json&apiKey=rw5mk6btukthdwu45xbwcssx";
                    String urlNew = searchUrl.replaceAll(" ", "%20");

                    //              String url1=urlNew.replaceAll("&","%26");

                    JsonObjectRequest jsObjRequest1 = new JsonObjectRequest
                            (GET, urlNew, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        listCategory.clear();
                                        // listAdapter.notifyDataSetChanged();
                                        JSONArray jsonArray = response.getJSONArray("categories");
                                        JSONObject object = jsonArray.getJSONObject(0);
                                        String name = object.getString("name");
                                        String id = object.getString("id");
                                        listCategory.add(new Category(name, id));
                                        listAdapter.notifyDataSetChanged();
                                        Log.e("result", listCategory.toString());
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
                    MySingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest1);
                }
            });

            listViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Category cat = listAdapter.getItem(i);
                    String catId = cat.getCategoryId();
                    Intent intent = new Intent(getActivity(), SubcategoryActivity.class);
                    intent.putExtra("categoryId", catId);
                    startActivity(intent);
                }
            });

        autoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoText.showDropDown();
            }
        });
            return fragView;
    }
        class UserListAdapter extends BaseAdapter {
            Context context;
            int layoutId;
            ArrayList<Category> listCategory;

            public UserListAdapter(Context context, int layoutId,
                                   ArrayList<Category> listCategory) {
                super();
                this.context = context;
                this.layoutId = layoutId;
                this.listCategory = listCategory;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return listCategory.size();
            }

            @Override
            public Category getItem(int position) {
                // TODO Auto-generated method stub
                return listCategory.get(position);
            }


            @Override
            public long getItemId(int position) {
                // TODO Auto-generated method stub
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                final Category user = listCategory.get(position);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View itemView = inflater.inflate(layoutId, null);
                TextView textViewName = (TextView) itemView.findViewById(R.id.textView);
                textViewName.setText(user.getName());
                return itemView;
            }
        }
    }