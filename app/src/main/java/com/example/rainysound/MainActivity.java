package com.example.rainysound;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rainysound.Adapter.AddMusicSoundAdapter;
import com.example.rainysound.Adapter.MenuTopAdapter;
import com.example.rainysound.MyInterface.IAddMusicSoundClickListener;
import com.example.rainysound.MyInterface.IMenuTopClickListener;
import com.example.rainysound.MySharedPreference.MySharedPreference;
import com.example.rainysound.Service.RainySoundService;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, RainySoundService.CallBack
        , IAddMusicSoundClickListener, IMenuTopClickListener {
    private TextView txtTitle;
    private ImageView imvBackground;
    private int[] imvBg = new int[]{R.drawable.bg_perfect_storm, R.drawable.bg_rain_on_window, R.drawable.bg_leaves
            , R.drawable.bg_beginning_of_the_rain, R.drawable.bg_lake, R.drawable.bg_rain_on_roof, R.drawable.bg_rain_on_sidewalk
            , R.drawable.bg_calm_beach, R.drawable.bg_peaceful_water, R.drawable.bg_rain_on_tent, R.drawable.bg_ocean_rain
            , R.drawable.bg_fireplace, R.drawable.bg_thunderstorm};

    private int[] musicSound = new int[]{R.raw.piano_awaiting_return, R.raw.piano_main, R.raw.piano_smoother_move, R.raw.guitar};

    private ImageButton ibtnPlayPause, ibtnAddSound, ibtnClock;


    private ServiceConnection connection;
    private RainySoundService rainySoundService;

    private RecyclerView rcvTopMenu;

    private List<Integer> listItemTopMenu;
    private List<Integer> listImage;

    private Dialog dialog, dialogTopMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_more_media_player);

        dialogTopMenu = new Dialog(this);
        dialogTopMenu.setContentView(R.layout.dialog_item_top_menu);
        listItemTopMenu = new ArrayList<>();
        addImageToListImage();
        setData();
        setDataToRcvTopMenu();
        ibtnPlayPause.setOnClickListener(this);
        ibtnAddSound.setOnClickListener(this);
        ibtnClock.setOnClickListener(this);
    }

    private void init() {
        txtTitle = findViewById(R.id.txtTitle);
        imvBackground = findViewById(R.id.imvBackground);
        ibtnPlayPause = findViewById(R.id.ibtnPlayPause);
        ibtnAddSound = findViewById(R.id.ibtnAddSound);
        rcvTopMenu = findViewById(R.id.rcvTopMenu);
        ibtnClock = findViewById(R.id.ibtnClock);
    }

    private void setData() {
//        if (getIntent() != null) {
//            if(getIntent().getExtras() != null)
//            {
//                int position = getIntent().getExtras().getInt(HomeActivity.INTENT_HOME_ITEM_POSITION);
//                txtTitle.setText(getIntent().getExtras().getString(HomeActivity.INTENT_HOME_ITEM_TITLE));
//                MyUtil.loadImageFromUrl(this, imvBackground, imvBg[position]);
//                connectService(position);
//            }
//        }
        MySharedPreference.putData(this, MySharedPreference.STATE_DESTROY, -1);
        if (MySharedPreference.getDataInt(this, MySharedPreference.ITEM_POSITION) != -1) {
            int position = MySharedPreference.getDataInt(this, MySharedPreference.ITEM_POSITION);
            txtTitle.setText(MySharedPreference.getDataString(this, MySharedPreference.ITEM_TITLE));
            MyUtil.loadImageFromUrl(this, imvBackground, imvBg[position]);
            connectService(position);
        }

        if (MySharedPreference.getDataBolean(this, MySharedPreference.STATE_MEDIA)) {
            ibtnPlayPause.setImageResource(R.drawable.btn_pause_normal);
        } else {
            ibtnPlayPause.setImageResource(R.drawable.btn_play_normal);
        }
    }

    private void setDataToRcvTopMenu() {
        GridLayoutManager layoutManager;

        if (listItemTopMenu.size() != 0) {
            layoutManager = new GridLayoutManager(this, listItemTopMenu.size(), LinearLayoutManager.VERTICAL, false);
        } else {
            layoutManager = new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false);

        }
        rcvTopMenu.setLayoutManager(layoutManager);

        MenuTopAdapter menuTopAdapter = new MenuTopAdapter(this, listItemTopMenu);

        rcvTopMenu.setAdapter(menuTopAdapter);
    }

    private void connectService(final int position) {
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                RainySoundService.ServiceBinder binder = (RainySoundService.ServiceBinder) service;
                rainySoundService = binder.getService();
                rainySoundService.setCallBack(MainActivity.this);
                rainySoundService.playMedia(position);

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        Intent intent = new Intent(this, RainySoundService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    private void addImageToListImage() {
        listImage = new ArrayList<>();
        listImage.add(R.drawable.ic_piano_gray_1);
        listImage.add(R.drawable.ic_piano_gray_2);
        listImage.add(R.drawable.ic_piano_gray_3);
        listImage.add(R.drawable.ic_guitar_gray);
    }

    private void addMoreSound() {
        RecyclerView rcvListMusic = dialog.findViewById(R.id.rcvListMusic);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false);

        rcvListMusic.setLayoutManager(layoutManager);

        AddMusicSoundAdapter addMusicSoundAdapter = new AddMusicSoundAdapter(this, listImage, listItemTopMenu);

        rcvListMusic.setAdapter(addMusicSoundAdapter);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        if (connection != null) {
            unbindService(connection);
        }
        MySharedPreference.putData(this, MySharedPreference.STATE_DESTROY, 0);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtnPlayPause:
                rainySoundService.changePlayPause();
                break;

            case R.id.ibtnAddSound:
                addMoreSound();
                break;

            case R.id.ibtnClock:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_time_picker);

                final EditText edtHour, edtMinute;
                TextView txtConfirm, txtCancel;

                edtHour = dialog.findViewById(R.id.edtHour);
                edtMinute = dialog.findViewById(R.id.edtMinute);
                txtConfirm = dialog.findViewById(R.id.txtConfirm);
                txtCancel = dialog.findViewById(R.id.txtCancel);

                txtConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int hour = 0, minute = 0;
                        if (!edtHour.getText().toString().isEmpty()) {
                            hour = Integer.parseInt(edtHour.getText().toString());
                        }
                        if (!edtMinute.getText().toString().isEmpty()) {
                            minute = Integer.parseInt(edtMinute.getText().toString());
                        }

                        long time = MyUtil.parseTime(hour, minute);

                        if (hour != 0 && minute != 0) {
                            setTime(time);
                            Toast.makeText(MainActivity.this, getString(R.string.turnOffAfter)
                                    + hour + getString(R.string.hour) + minute + getString(R.string.minute), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });

                txtCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
        }
    }


    private void setTime(long time) {
        if (rainySoundService != null) {
            rainySoundService.countDown(time);
        }
    }

    @Override
    public void changeIconPlayPause(boolean b) {
        if (b) {
            ibtnPlayPause.setImageResource(R.drawable.btn_pause_normal);
        } else {
            ibtnPlayPause.setImageResource(R.drawable.btn_play_normal);
        }
    }

    @Override
    public void finishMainActivity() {
        if (MySharedPreference.getDataInt(this, MySharedPreference.STATE_DESTROY) == -1) {
            showHomeActivity();
        } else {
            finish();
        }

    }

    @Override
    public void addMusicSoundOnClick(int position) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, musicSound[position]);
        mediaPlayer.setLooping(true);
        rainySoundService.addMediaPlayer(mediaPlayer);
        listItemTopMenu.add(listImage.get(position));
        reloadTopMenu();
        if (dialog != null) {
            dialog.dismiss();
        }
        checkHideIbtnAddSound();
    }

    @Override
    public void menuTopOnClick(int position) {
        showTopMenuDialog(position);
    }

    private void checkHideIbtnAddSound() {
        if (listItemTopMenu.size() == 3) {
            ibtnAddSound.setVisibility(View.GONE);
        } else {
            ibtnAddSound.setVisibility(View.VISIBLE);
        }
    }

    private void reloadTopMenu() {
        setDataToRcvTopMenu();
    }

    private void showTopMenuDialog(final int position) {
        TextView txtDelete = dialogTopMenu.findViewById(R.id.txtDelete);

        txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeSound(position);
            }
        });
        dialogTopMenu.show();
    }

    private void removeSound(int position) {
        listItemTopMenu.remove(position);
        rainySoundService.removeMediaPlayer(position);
        reloadTopMenu();
        if (dialogTopMenu != null) {
            dialogTopMenu.dismiss();
        }
        checkHideIbtnAddSound();
    }

    private void showHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (rainySoundService != null) {
            rainySoundService.stopMedia();
        }
        showHomeActivity();
        super.onBackPressed();

    }
}
