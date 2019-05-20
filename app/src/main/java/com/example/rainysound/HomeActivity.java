package com.example.rainysound;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.rainysound.Adapter.HomeAdapter;
import com.example.rainysound.MyInterface.IHomeClickListener;
import com.example.rainysound.MySharedPreference.MySharedPreference;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements IHomeClickListener {
    private RecyclerView rcvListItem;
    private List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        setDataToRcvListItem();
    }

    private void setDataToList()
    {
        list = new ArrayList<>();
        list.add(getString(R.string.perfectStorm));
        list.add(getString(R.string.rainyOnWindow));
        list.add(getString(R.string.rainyOnLeaves));
        list.add(getString(R.string.slightlyRain));
        list.add(getString(R.string.afternoonLake));
        list.add(getString(R.string.rainyOnTheRoof));
        list.add(getString(R.string.rainyOnTheStreet));
        list.add(getString(R.string.peacefulBeach));
        list.add(getString(R.string.sufaceLakeIsCalm));
        list.add(getString(R.string.rainyOnTheTent));
        list.add(getString(R.string.oceanRainy));
        list.add(getString(R.string.rainyAfternoon));
        list.add(getString(R.string.thunderstorms));
    }

    private void init() {
        rcvListItem = findViewById(R.id.rcvListItem);
    }

    private void setDataToRcvListItem()
    {
        setDataToList();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rcvListItem.setLayoutManager(layoutManager);

        HomeAdapter adapter = new HomeAdapter(this,list);
        rcvListItem.setAdapter(adapter);

        rcvListItem.setNestedScrollingEnabled(false);
    }


    @Override
    public void HomeItemOnClick(int position) {
        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
//        intent.putExtra(INTENT_HOME_ITEM_POSITION,position);
//        intent.putExtra(INTENT_HOME_ITEM_TITLE,list.get(position));
        MySharedPreference.putData(this,MySharedPreference.ITEM_POSITION,position);
        MySharedPreference.putData(this,MySharedPreference.ITEM_TITLE,list.get(position));
        MySharedPreference.putData(this,MySharedPreference.STATE_MEDIA,true);
        startActivity(intent);
        finish();
        Toast.makeText(this, ""+position, Toast.LENGTH_SHORT).show();
    }
}
