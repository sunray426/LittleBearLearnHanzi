package com.littlebear.learnhanzi;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.iflytek.cloud.SpeechUtility;
import com.littlebear.learnhanzi.R;


import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
        if(SpeechUtility.getUtility() == null) {
            System.out.println("speechUtility is null!");
        } else {
            System.out.println("speechUtility is created success!");
        }


        //开启
        SQLiteStudioService.instance().start(this);
        //关闭
        //SQLiteStudioService.instance().stop(this);

    }


}
