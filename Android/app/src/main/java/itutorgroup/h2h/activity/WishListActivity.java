package itutorgroup.h2h.activity;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import itutorgroup.h2h.R;
import itutorgroup.h2h.bean.MapList;
import itutorgroup.h2h.bean.Singleton;

public class WishListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

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
        mRecyclerView.invalidate();
        CitiesAdapter adapter = new CitiesAdapter();
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
            String city = Singleton.getCurrentUser().getCities().get(position);
            holder.bindCityHolder(city);
        }

        @Override
        public int getItemCount() {
            return Singleton.getCurrentUser().getCities().size();
        }
    }

    private class CityHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;

        public CityHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.list_name);
            itemView.setOnClickListener(this);
        }

        public void bindCityHolder(String city){
            name.setText(city);
        }

        @Override
        public void onClick(View v) {
            Log.d("WishListActivity","onClick");
            v.getContext().startActivity(MapActivity.newIntent(v.getContext(),name.getText().toString()));

        }
    }
}



