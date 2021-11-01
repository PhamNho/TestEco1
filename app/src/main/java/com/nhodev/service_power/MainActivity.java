package com.nhodev.service_power;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nhodev.service_power.adapter.MusicAdapter;
import com.nhodev.service_power.models.MusicModel;
import com.nhodev.service_power.service.PlayMusicService;
import com.nhodev.service_power.utils.NotificationConst;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<MusicModel> items = new ArrayList();
    Button btnPre, btnPause, btnNext;
    ConstraintLayout clControl;
    MusicAdapter mAdapter;
    private Boolean isPlay = false;
    private MusicModel modelSelected;
    private static int MY_RQ_PERMISSION = 100;
    RecyclerView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initAdapter();
        initAction();
        checkPermission();
    }

    private void initAdapter() {
        mAdapter = new MusicAdapter(this, items);
        listView.setAdapter(mAdapter);
        listView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initAction() {
        mAdapter.setOnItemClickListener((musicModel, position) -> {
            Log.d("myLog", " " + musicModel.getName());
            Log.d("myLog", " " + musicModel.getPath());
            startService(musicModel);
            modelSelected = musicModel;
            isPlay = true;
            btnPause.setText("Tạm dụng");
            clControl.setVisibility(View.VISIBLE);
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlay) {
                    isPlay = false;
                    btnPause.setText("Tiếp tục");
                    pauseMusic();
                } else {
                    isPlay = true;
                    btnPause.setText("Tạm dùng");
                    rePlayMusic();
                }
            }
        });

        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modelSelected.getNumber() <= 0) {
                    startService(items.get(items.size() - 1));
                    modelSelected = items.get(items.size() - 1);
                } else {
                    startService(items.get(modelSelected.getNumber() - 1));
                    modelSelected = items.get(modelSelected.getNumber() - 1);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modelSelected.getNumber() >= (items.size() - 1)) {
                    startService(items.get(0));
                    modelSelected = items.get(0);
                } else {
                    startService(items.get(modelSelected.getNumber() + 1));
                    modelSelected = items.get(modelSelected.getNumber() + 1);
                }
            }
        });
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                }
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_RQ_PERMISSION);
                return;
            } else {
                loadFile();
            }
        }
    }

    private void initView() {
        listView = findViewById(R.id.lvMusic);
        btnPre = findViewById(R.id.btnPre);
        btnPause = findViewById(R.id.btnPause);
        btnNext = findViewById(R.id.btnNext);
        clControl = findViewById(R.id.clControl);
    }

    private void loadFile() {
        ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        items.clear();
        for (int i = 0; i < mySongs.size(); i++) {
            items.add(new MusicModel(i, mySongs.get(i).getName(), mySongs.get(i).getPath()));
        }
        mAdapter.notifyDataSetChanged();
    }

    public ArrayList<File> findSong(File root) {
        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();    // All file and folder automatic collect
        if (files != null) {
            for (File singleFile : files) {
                if (singleFile.isDirectory() && !singleFile.isHidden()) {
                    al.addAll(findSong(singleFile)); //Recursively call
                } else {
                    if (singleFile.getName().endsWith(".mp3")) {
                        al.add(singleFile);
                    }
                }
            }
        }
        return al;
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_RQ_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Đã câp quyền", Toast.LENGTH_SHORT).show();
                loadFile();
            } else {
                checkPermission();
            }
        }
    }

    public void startService(MusicModel model) {
        Intent intent = new Intent(this, PlayMusicService.class);
        String songUrl = model.getPath();
        intent.putExtra("pathMusic", songUrl);
        startService(intent);
    }

    public void pauseMusic() {
        Intent intent = new Intent(this, PlayMusicService.class);
        intent.putExtra("action", NotificationConst.ACTION_PAUSE);
        startService(intent);
    }

    public void rePlayMusic() {
        Intent intent = new Intent(this, PlayMusicService.class);
        intent.putExtra("action", NotificationConst.ACTION_RE_PLAY);
        startService(intent);
    }
}