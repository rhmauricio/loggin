package rhmauricio.loggin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

public class DrawerActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    protected int home_menu;
    protected DrawerLayout fullLayout;
    protected Toolbar toolbar;
    protected NavigationView navigationView;
    protected Bundle extras;

    //private GoogleSignHandle googleSignHandle;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        /**
         * This is going to be our actual root layout.
         */
        fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer, null);
        /**
         * {@link FrameLayout} to inflate the child's view. We could also use a {@link android.view.ViewStub}
         */
        FrameLayout activityContainer = (FrameLayout) fullLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);

        /**
         * Note that we don't pass the child's layoutId to the parent,
         * instead we pass our inflated layout.
         */
        super.setContentView(fullLayout);

        /** Create a toolbar and check whether child set Toolbar
         * option or not.
         */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (useToolbar()){
            //toolbar.setTitle(toolbarTitle());
            setSupportActionBar(toolbar);
        }else
            toolbar.setVisibility(View.GONE);

        /** Set menu depending on child **/
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setMenu();
        if (navigationView != null) {
            setupNavigationDrawerContent();
        }


        /** Set the Drawer toggle **/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, fullLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        fullLayout.addDrawerListener(toggle);

        toggle.syncState();

        /**
         * Instance email & username of
         * navigation drawer
         */
        TextView email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email_drawer);
        TextView username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_drawer);
        final ImageView profileImg = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.image_drawer);

        // Load preferences
        preferences = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        /**
         * Set username and email depending on
         * whether the user is a guest or logged in
         *  with Facebook or Google+ or as a Guest
         */
        if (preferences.getBoolean(getString(R.string.is_guest), false) ){
            email.setText("");
            username.setText(getString(R.string.guest));
        }else{
            /** Read data from shared preferences **/
            // Set name
            username.setText(preferences.getString(WelcomeScreenActivity.LOGIN_NAME, ""));
            // Set email
            email.setText(preferences.getString(WelcomeScreenActivity.LOGIN_EMAIL, ""));
            // Create image from fb URL
            FetchImage fetchImage = new FetchImage(this, new FetchImage.AsyncResponse() {
                @Override
                public void processFinish(Bitmap bitmap) {
                    if (bitmap != null) {
                        Resources res = getResources();
                        RoundedBitmapDrawable roundBitmap = RoundedBitmapDrawableFactory
                                .create(res, bitmap);
                        roundBitmap.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()) / 2.0f);
                        profileImg.setImageDrawable(roundBitmap);
                    }
                }
            });
            fetchImage.execute(preferences.getString(WelcomeScreenActivity.LOGIN_IMG, ""));

            googleSignHandle = new GoogleSignHandle(this, getApplicationContext());
        }

    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    protected void setMenu(){
        navigationView.inflateMenu(R.menu.find_routes_menu);
    }

    /**
     * Helper method that can be used by child classes to
     * specify that they don't want a {@link Toolbar}
     * @return true
     */
    protected boolean useToolbar()
    {
        return true;
    }

    @Override
    public void onBackPressed() {
        if (fullLayout.isDrawerOpen(GravityCompat.START)) {
            fullLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    protected void setupNavigationDrawerContent() {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {

                        // Handle navigation view item clicks here.
                        //Intent intent;
                        Handler handler = new Handler();
                        switch (item.getItemId()) {
                            case R.id.home:
                                fullLayout.closeDrawer(GravityCompat.START);
                                if (!item.isChecked()) {
                                    if (home_menu == 1)
                                        intent = new Intent(DrawerActivity.this,SeeRoutes_Main.class);
                                    else{
                                        intent = new Intent(DrawerActivity.this,FindRoute_MainActivity.class);
                                    }
                                    handler.postDelayed(delay, 150);
                                    item.setChecked(true);      // Start activity after some delay
                                }
                                break;
                            case R.id.find_route:
                                fullLayout.closeDrawer(GravityCompat.START);
                                if (!item.isChecked()) {
                                    intent = new Intent(DrawerActivity.this,FindRoute_MainActivity.class);
                                    handler.postDelayed(delay, 150);
                                    item.setChecked(true);      // Start activity after some delay
                                }
                                break;
                            case R.id.see_routes:
                                fullLayout.closeDrawer(GravityCompat.START);
                                if (!item.isChecked()) {
                                    intent = new Intent(DrawerActivity.this,SeeRoutes_Main.class);
                                    handler.postDelayed(delay, 150);
                                    item.setChecked(true);      // Start activity after some delay
                                }
                                break;
                            case R.id.zone1:
                                fullLayout.closeDrawer(GravityCompat.START);
                                intent = new Intent(DrawerActivity.this, SeeRoutes_Lists.class);
                                intent.putExtra("option", "zone1");
                                handler.postDelayed(delay, 150);    // Start activity after some delay
                                break;
                            case R.id.zone2:
                                fullLayout.closeDrawer(GravityCompat.START);
                                intent = new Intent(DrawerActivity.this, SeeRoutes_Lists.class);
                                intent.putExtra("option", "zone2");
                                handler.postDelayed(delay, 150);    // Start activity after some delay
                                break;
                            case R.id.zone3:
                                fullLayout.closeDrawer(GravityCompat.START);
                                intent = new Intent(DrawerActivity.this, SeeRoutes_Lists.class);
                                intent.putExtra("option", "zone3");
                                handler.postDelayed(delay, 150);    // Start activity after some delay
                                break;
                            case R.id.zone4:
                                fullLayout.closeDrawer(GravityCompat.START);
                                intent = new Intent(DrawerActivity.this, SeeRoutes_Lists.class);
                                intent.putExtra("option", "zone4");
                                handler.postDelayed(delay, 150);    // Start activity after some delay
                                break;
                            case R.id.logout:
                                // Update preferences, in this case
                                // changing logging status
                                preferences.edit().putBoolean(
                                        getString(R.string.is_logged), false).apply();

                                fullLayout.closeDrawer(GravityCompat.START);

                                // Logout from Google
                                if (preferences.getInt(WelcomeScreenActivity.LOGIN_OPTION, 0) == WelcomeScreenActivity.GOOGLE_LOGIN)
                                    googleSignHandle.signOutAndRevoke();
                                else {
                                    // Return to Welcome Activity
                                    intent = new Intent(DrawerActivity.this, WelcomeScreenActivity.class);
                                    intent.putExtra("activity", "home");
                                    handler.postDelayed(delay, 150);
                                }
                                break;
                        }
                        return true;
                    }
                });
    }
    protected Intent intent;

    protected Runnable delay = new Runnable() {
        @Override
        public void run() {
            startActivity(intent);
            finish();
        }
    };

    protected Intent putExtras(Class className){
        Intent intent = new Intent(this, className);
        intent.putExtra("username", extras.getString("username"));
        intent.putExtra("email", extras.getString("email"));
        return intent;
    }

    @Override
    public void signOutRevokeAccess() {
        intent = new Intent(DrawerActivity.this,WelcomeScreenActivity.class);
        intent.putExtra("activity", "home");
        startActivity(intent);
        finish();
    }
}
