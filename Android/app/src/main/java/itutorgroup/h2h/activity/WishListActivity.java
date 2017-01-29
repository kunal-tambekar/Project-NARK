package itutorgroup.h2h.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import itutorgroup.h2h.MyApplication;
import itutorgroup.h2h.R;
import itutorgroup.h2h.bean.CityClass;
import itutorgroup.h2h.bean.Singleton;


public class WishListActivity extends AppCompatActivity {

    private static final String TAG ="WISHLISTACTIVITY" ;
    private RecyclerView mRecyclerView;
    private ArrayList<CityClass> mCities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.wish_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecorator(20));

        updateUI();
    }

    public void updateUI(){

        String uri = "http://sfsuswe.com/~rarora/H2H/mini/public/events/getAllEvents/?userid="
                + Singleton.getCurrentUser().getUid();

        Log.v("WishListActivity Query",uri);
        JsonArrayRequest req = new JsonArrayRequest(uri,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {

                            if(mCities.size()!=0)
                                mCities.clear();
                            else
                                mCities = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {

                                CityClass city = new CityClass();
                                JSONObject obj= (JSONObject) response.get(i);

                                if(obj.has("city"))
                                    city.setName(obj.getString("city"));
                                if(obj.has("noOfUsers"))
                                    city.setNoOfUsers(obj.getString("noOfUsers"));

                                mCities.add(city);
                            }

                            if(mCities.size() == 0)
                                ShowToast();
                            else{
                                mRecyclerView.invalidate();
                                CitiesAdapter adapter = new CitiesAdapter();
                                mRecyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        MyApplication.getInstance().addToRequestQueue(req,TAG);

    }

    public void ShowToast(){
        Toast.makeText(this, "Your wish list is empty, please go back and retry.",Toast.LENGTH_SHORT);
    }


    private class CitiesAdapter extends RecyclerView.Adapter<WishListActivity.CityHolder> {

        @Override
        public WishListActivity.CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.city_list_view,parent,false);
            return new WishListActivity.CityHolder(view);
        }

        @Override
        public void onBindViewHolder(WishListActivity.CityHolder holder, int position) {
            CityClass city = mCities.get(position);
            holder.bindCityHolder(city);
        }

        @Override
        public int getItemCount() {
            return mCities.size();
        }
    }

    private class CityHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mName;
        private TextView mNoOfUsers;

        public CityHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.list_name);
            mNoOfUsers = (TextView) itemView.findViewById(R.id.noOfUsers);
            itemView.setOnClickListener(this);
        }

        public void bindCityHolder(CityClass city){
            mName.setText(city.getName());
            mNoOfUsers.setText(city.getNoOfUsers() + " broadcaster(s)");

        }

        @Override
        public void onClick(View v) {
            Log.d("WishListActivity","onClick");
            v.getContext().startActivity(MapActivity.newIntent(v.getContext(),mName.getText().toString()));

        }
    }
}



