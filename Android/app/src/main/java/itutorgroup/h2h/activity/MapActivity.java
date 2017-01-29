package itutorgroup.h2h.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import itutorgroup.h2h.MyApplication;
import itutorgroup.h2h.R;
import itutorgroup.h2h.bean.MapList;

public class MapActivity extends AppCompatActivity {

    private static final String TAG = "MAPSFRAGMENT";
    private GoogleMap mMap;
    private RecyclerView mRecyclerView;
    private ArrayList<MapList> mCoordinateList = new ArrayList<>();
    private SupportMapFragment mMapFragment;
    private static String EXTRA_CITY = "CITY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        FragmentManager manager = getSupportFragmentManager();
        mMapFragment = (SupportMapFragment) manager.findFragmentById(R.id.mapList);

        mRecyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecorator(20));

        updateUI();

    }

    public static Intent newIntent(Context packageContext, String city){
        Intent intent = new Intent(packageContext, MapActivity.class);
        intent.putExtra(EXTRA_CITY, city);
        return intent;
    }


    public void updateUI(){

        //get the city from Intent!!
        String uri = "http://sfsuswe.com/~rarora/H2H/mini/public/events/getEventByCity/?city="
                + Uri.encode(getIntent().getStringExtra(EXTRA_CITY));

        Log.v("Maps Query",uri);

        JsonArrayRequest req = new JsonArrayRequest(uri,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {

                            if(mCoordinateList.size()!=0)
                                mCoordinateList.clear();
                            else
                                mCoordinateList = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {

                                MapList mapList = new MapList();
                                JSONObject obj= (JSONObject) response.get(i);

                                if(obj.has("username"))
                                    mapList.setName(obj.getString("username"));
                                if(obj.has("latitude"))
                                    mapList.setLatitude(obj.getDouble("latitude"));
                                if(obj.has("longitude"))
                                    mapList.setLongitude(obj.getDouble("longitude"));
                                if(obj.has("meetingid"))
                                    mapList.setMeetingID(obj.getString("meetingid"));

                                mCoordinateList.add(mapList);
                            }

                            updateMap();
                            updateRecyclerView();



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

    private void updateMap() {
        mMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                for(MapList mapList : mCoordinateList){
                    LatLng eventLocation = new LatLng(mapList.getLatitude(),mapList.getLongitude());
                    MarkerOptions options = new MarkerOptions();
                    options.position(eventLocation);
                    options.title(mapList.getName());
                    mMap.addMarker(options);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(eventLocation));
                }
                mMap.setMinZoomPreference(10f);
            }
        });
    }

    private void updateRecyclerView(){
        mRecyclerView.invalidate();
        MapsAdapter mapsAdapter = new MapsAdapter();
        mapsAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mapsAdapter);
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }

    private class MapsAdapter extends RecyclerView.Adapter<MapListHolder> {

        @Override
        public MapListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.maps_list_view,parent,false);
            return new MapListHolder(view);
        }

        @Override
        public void onBindViewHolder(MapListHolder holder, int position) {
            MapList mapList = mCoordinateList.get(position);
            holder.bindMapHolder(mapList);
        }

        @Override
        public int getItemCount() {
            return mCoordinateList.size();
        }
    }

    private class MapListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;
        private MapList mapList;

        public MapListHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.list_name);
            itemView.setOnClickListener(this);
        }

        public void bindMapHolder(MapList mapList){
            this.mapList = mapList;
            name.setText(mapList.getName());
        }

        @Override
        public void onClick(View v) {
            Log.d("MapActivity","onClick");
            v.getContext().startActivity(JoinMeetingActivity.newIntent(v.getContext(), mapList.getMeetingID()));

        }
    }


}


class VerticalSpaceItemDecorator extends RecyclerView.ItemDecoration {
    private final int spacer;
//    private final int horizontalSpacer;

    public VerticalSpaceItemDecorator(int spacer) {
        this.spacer = spacer;
//        this.horizontalSpacer
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = spacer;
        outRect.right = spacer;
        outRect.left = spacer;
    }
}