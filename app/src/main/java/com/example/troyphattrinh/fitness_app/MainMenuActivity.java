package com.example.troyphattrinh.fitness_app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private FirebaseAuth firebaseAuth;
    private String email;
    private View headerView;
    private TextView emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        NavigationView navigationView = findViewById(R.id.nav_view);

        Intent i = getIntent();
        this.email = i.getStringExtra("normalEmail");

        //setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setup action bar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.menu_icon); //includes menu_icon in the action bar
        actionbar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //handle clicked item of the navigation menu
        navigationView.setNavigationItemSelectedListener(this);


        headerView = navigationView.inflateHeaderView(R.layout.nav_header);
        emailText = headerView.findViewById(R.id.nav_header_title);
        emailText.setText(email);

        Fragment fragment = new HomeActivity();

        FragmentManager fragManager = getSupportFragmentManager();
        FragmentTransaction fragTransaction= fragManager.beginTransaction();

        fragTransaction.replace(R.id.screen, fragment);

        fragTransaction.commit();

    }


    //handle clicked menu icon on the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                //GravityCompat.START ensures the navigation bar opens correctly
                //either from right to left or vice versa
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.

        Fragment fragment = null;

        int id = menuItem.getItemId();

        // Add code here to update the UI based on the item selected
        if(id == R.id.nav_home){
            fragment = new HomeActivity();
        }
        else if(id == R.id.nav_cal_bmi){
            fragment = new BmiActivity();
        }
        else if(id == R.id.nav_foot_step){
            fragment = new FootstepActivity();
        }
        else if(id == R.id.nav_heart_rate){
            fragment = new HeartRateActivity();
        }
        else if(id == R.id.nav_view_record){
            fragment = new UserInfoActivity();
        }
        else if(id == R.id.nav_view_health){
            fragment = new HealthInfo();
        }
        else if(id == R.id.nav_logout){
            logout();
        }

        drawerLayout.closeDrawers();

        if(fragment != null){

            Bundle bundle = new Bundle();
            bundle.putString("EMAIL_KEY", email);
            fragment.setArguments(bundle);

            FragmentManager fragManager = getSupportFragmentManager();
            FragmentTransaction fragTransaction= fragManager.beginTransaction();

            fragTransaction.replace(R.id.screen, fragment);

            fragTransaction.commit();
        }

        // set item as selected to persist highlight
        menuItem.setChecked(true);

        return true;
    }

    private void logout(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(MainMenuActivity.this, MainActivity.class));
    }

}