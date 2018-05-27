package com.example.troyphattrinh.fitness_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.widget.ShareDialog;

import java.io.InputStream;


public class FbMainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private String email = "";
    private View headerView;
    private TextView fbNameText;
    private ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        FacebookSdk.sdkInitialize(this);

//        Intent i = getIntent();
//        this.email = i.getStringExtra("email");

        shareDialog = new ShareDialog(this);

        Bundle bundle = getIntent().getExtras();
        String fbName = bundle.get("name").toString();
        String fbAvatar = bundle.get("avatar").toString();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.menu_icon);
        actionbar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        //handle clicked item of the navigation menu
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.inflateHeaderView(R.layout.nav_header);

        fbNameText = headerView.findViewById(R.id.nav_header_title);
        fbNameText.setText(fbName);
//        emailText = headerView.findViewById(R.id.nav_header_title);
//        emailText.setText(email);

        Fragment fragment = new FbHomeActivity();

//        bundle.putString("welcomeText", name);
        fragment.setArguments(bundle);


        FragmentManager fragManager = getSupportFragmentManager();
        FragmentTransaction fragTransaction= fragManager.beginTransaction();

        fragTransaction.replace(R.id.screen, fragment);

        fragTransaction.commit();


        new FbMainMenuActivity.DownloadImage((ImageView) headerView.findViewById(R.id.nav_header_image)).execute(fbAvatar);

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
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        Fragment fragment = null;

        // Add code here to update the UI based on the item selected

        int id = menuItem.getItemId();

        if(id == R.id.nav_home){
            drawerLayout.closeDrawers();
            fragment = new FbHomeActivity();
        }
        else if(id == R.id.nav_cal_bmi){
            drawerLayout.closeDrawers();
            fragment = new BmiActivity();
        }
        else if(id == R.id.nav_foot_step){
            drawerLayout.closeDrawers();
            fragment = new FootstepActivity();
        }
        else if(id == R.id.nav_heart_rate){
            drawerLayout.closeDrawers();
            fragment = new HeartRateActivity();
        }
        else if(id == R.id.nav_view_record){
            drawerLayout.closeDrawers();
            fragment = new UserInfoActivity();
        }
        else if(id == R.id.nav_view_health){
            drawerLayout.closeDrawers();
            fragment = new HealthInfo();
        }
        else if(id == R.id.nav_logout){
            drawerLayout.closeDrawers();
            logout();
        }

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

        return false;
    }


    public class DownloadImage extends AsyncTask<String, Void, Bitmap>
    {
        ImageView image;

        public DownloadImage(ImageView image)
        {
            this.image = image;
        }

        protected Bitmap doInBackground(String... urls)
        {
            String url = urls[0];
            Bitmap icon = null;
            try
            {
                InputStream in = new java.net.URL(url).openStream();
                icon = BitmapFactory.decodeStream(in);
            }
            catch (Exception e)
            {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return icon;
        }

        protected void onPostExecute(Bitmap bitmap)
        {
            image.setImageBitmap(bitmap);
        }
    }


    private void logout(){
        LoginManager.getInstance().logOut();
        Intent login = new Intent(FbMainMenuActivity.this, MainActivity.class);
        startActivity(login);
        finish();
    }

}