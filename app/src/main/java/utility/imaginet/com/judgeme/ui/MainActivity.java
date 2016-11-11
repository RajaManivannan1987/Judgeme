package utility.imaginet.com.judgeme.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.millennialmedia.MMSDK;
import com.millennialmedia.UserData;

import org.json.JSONException;
import org.json.JSONObject;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.helper.AppController;
import utility.imaginet.com.judgeme.helper.PrefManager;
import utility.imaginet.com.judgeme.helper.VolleyCommonClass;
import utility.imaginet.com.judgeme.helper.judgeMeUrls;
import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView clickText, titleTextView, logoutTextView;
    private PrefManager prefManager;
    private ImageView imageView;
    private ImageLoader imageLoader;
    private Bitmap imageBitmap;
    int fragemetValue = 0;
    private String UserName, uid, current_clipID, userId, likeuserID;
    private DrawerLayout drawer;
    boolean flag = false;
//    MainFragment mainFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MMSDK.initialize(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        UserData userData = new UserData().setAge(25).setGender(UserData.Gender.MALE);
        MMSDK.setUserData(userData);
        if (!flag) {
            FragmentManager fragment = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragment.beginTransaction();
            MainFragment mainFragment = new MainFragment();
           // Log.d("MainActivity", "Fragment Call");
            fragmentTransaction.add(R.id.Main_frame_container, mainFragment, "MainFragment").addToBackStack(null);
            fragmentTransaction.commit();
            LoadData();
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            flag=true;
        }
    }

    public void setActionBarTitle(String title, String Uid) {
        titleTextView.setText(title);
        uid = Uid;
    }

    private void LoadData() {
        FindViewId();
        prefManager = new PrefManager(getApplicationContext());
        imageLoader = AppController.getInstance().getImageLoader();
        FeatchArtist(judgeMeUrls.FETCH_ARTIST + "&token=" + prefManager.getObjectId(), 1);
        onClickListener();
    }

    private void onClickListener() {
        clickText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(Gravity.LEFT);
            }
        });
        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragemetValue == 0) {
                    if (!titleTextView.getText().toString().matches("")) {
                        Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
                        i.putExtra("uid", uid);
                        startActivity(i);
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_leave);
                    }
                }
            }
        });
        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragemetValue == 1) {
                    LogoutMethod(judgeMeUrls.LOGOUT + "&token=" + prefManager.getObjectId(), 4);
                } else {
                    Intent i = new Intent(getApplicationContext(), HelperActivity.class);
                    startActivity(i);
                }

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragement = null;
                FeatchArtist(judgeMeUrls.NEXTZONE + "&token=" + prefManager.getObjectId(), 2);
                if (fragemetValue == 2) {
                    fragement = new NextUpFragment();
                } else if (fragemetValue == 3) {
                    fragement = new TrendingClipsFragment();
                } else if (fragemetValue == 4) {
                    fragement = new TrendingArtistsFragment();
                } else if (fragemetValue == 5) {
                    fragement = new MyProfileFragment();
                } else {
                    fragement = new MainFragment();
                }
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_container, fragement).addToBackStack(null).commit();
            }
        });
    }

    private void FindViewId() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        clickText = (TextView) findViewById(R.id.textclick);
        imageView = (ImageView) findViewById(R.id.imageView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        logoutTextView = (TextView) findViewById(R.id.logoutTextView);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        MainActivity.this.overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            prefManager.logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeZone(Bitmap bitmap) {
        imageBitmap = bitmap;
        if (fragemetValue != 1) {
            imageView.setImageBitmap(bitmap);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        String title = null;
        int id = item.getItemId();
        if (id == R.id.nav_Preferences) {
            flag = true;
            if (getSupportFragmentManager().findFragmentById(R.id.Main_frame_container) != null) {
                Log.d("MainActivity", "Fragment Call");
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.Main_frame_container)).commit();
            }
            fragemetValue = 1;
            logoutTextView.setVisibility(View.VISIBLE);
            logoutTextView.setText("Logout");
            titleTextView.setText(R.string.pref);
            logoutTextView.setBackground(getResources().getDrawable(android.R.color.transparent));
            imageView.setVisibility(View.GONE);
            imageView.setImageBitmap(null);
            fragment = new PreferencesFragment();
            title = "PreferencesFragment";
        } else if (id == R.id.nav_Next_Up) {
            flag = true;
            if (getSupportFragmentManager().findFragmentById(R.id.Main_frame_container) != null) {
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.Main_frame_container)).commit();
            }

            fragemetValue = 2;
            logoutTextView.setVisibility(View.VISIBLE);
            logoutTextView.setText("");
            logoutTextView.setBackground(getResources().getDrawable(android.R.drawable.ic_menu_help));
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(imageBitmap);
            title = "NextUpFragment";
            titleTextView.setText("");
            fragment = new NextUpFragment();
        } else if (id == R.id.nav_Trending_Clips) {
            flag = true;
            if (getSupportFragmentManager().findFragmentById(R.id.Main_frame_container) != null) {
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.Main_frame_container)).commit();
            }
            fragemetValue = 3;
            titleTextView.setText(R.string.trendingclips);
            logoutTextView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            //imageView.setImageBitmap(imageBitmap);
            fragment = new TrendingClipsFragment();
            title = "TrendingClipsFragment";
        } else if (id == R.id.nav_Trending_Artists) {
            flag = true;
            if (getSupportFragmentManager().findFragmentById(R.id.Main_frame_container) != null) {
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.Main_frame_container)).commit();
            }
            fragemetValue = 4;
            titleTextView.setText(R.string.trendingartists);
            logoutTextView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            //imageView.setImageBitmap(imageBitmap);
            fragment = new TrendingArtistsFragment();
            title = "TrendingArtistsFragment";
        } else if (id == R.id.nav_My_Profile) {
            flag = true;
            if (getSupportFragmentManager().findFragmentById(R.id.Main_frame_container) != null) {
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.Main_frame_container)).commit();
            }
            fragemetValue = 5;
            titleTextView.setText(UserName);
            logoutTextView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            // imageView.setImageBitmap(imageBitmap);
            fragment = new MyProfileFragment();
            title = "MyProfileFragment";
        } else if (id == R.id.nav_Likes) {
            flag = true;
            if (getSupportFragmentManager().findFragmentById(R.id.Main_frame_container) != null) {
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.Main_frame_container)).commit();
            }
            fragemetValue = 6;
            titleTextView.setText(R.string.likes);
            imageView.setImageBitmap(null);
            logoutTextView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            fragment = new LikesFragment();
            title = "LikesFragment";
        } else if (id == R.id.nav_Discussions) {
            flag = true;
            if (getSupportFragmentManager().findFragmentById(R.id.Main_frame_container) != null) {
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.Main_frame_container)).commit();
            }
            fragemetValue = 7;
            imageView.setImageBitmap(null);
            logoutTextView.setVisibility(View.GONE);
            titleTextView.setText(R.string.discussion);
            imageView.setVisibility(View.VISIBLE);
            fragment = new DiscussionFragment();
            title = "DiscussionFragment";
        } else if (id == R.id.nav_Help) {
            flag = true;
            if (getSupportFragmentManager().findFragmentById(R.id.Main_frame_container) != null) {
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.Main_frame_container)).commit();
            }
            fragemetValue = 8;
            imageView.setImageBitmap(null);
            logoutTextView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            //logoutTextView.setText("Help..?");
            titleTextView.setText("Help");
            fragment = new HelpFragment();
            title = "Help";
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, title).addToBackStack(null).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void LogoutMethod(String url, int type) {
        FeatchArtist(url, type);
    }

    private void FeatchArtist(String url, final int type) {
        VolleyCommonClass.getDataFromServer(url, new interfaceSelector() {

            public void volleyError(String error) {
            }

            @Override
            public void volleyResponse(JSONObject response) {
                if (type == 1) {
                    Type1method(response, type);
                } else if (type == 2) {
                    Type1method(response, type);
                } else if (type == 4) {
                    Type4method(response);
                }
            }
        });
    }

    private void Type4method(JSONObject response) {
        try {
            JSONObject object = response.getJSONObject("data");
            String resultcode = object.optString("resultcode");
            if (resultcode.startsWith("200")) {
                LoginManager.getInstance().logOut();
                String resultmessage = object.optString("resultmessage");
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                MainActivity.this.overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Type1method(JSONObject response, int type) {
        try {
            JSONObject object = response.getJSONObject("data");
            JSONObject UserObj = object.getJSONObject("user");
            userId = UserObj.getString("uid");
            String firstname = UserObj.optString("firstname");
            String Lastname = UserObj.optString("lastname");
            UserName = firstname + " " + Lastname;
            String zonesetting = UserObj.optString("zonesetting");

            if (zonesetting.startsWith("World")) {
                if (type == 1) {
                    imageLoader(UserObj.optString("worldiconURLlowres"), 1);
                } else {
                    imageLoader(UserObj.optString("worldiconURLlowres"), 2);
                }
            } else if (zonesetting.startsWith("Country")) {
                if (type == 1) {
                    imageLoader(UserObj.optString("countryiconURLlowres"), 1);
                } else {
                    imageLoader(UserObj.optString("countryiconURLlowres"), 2);
                }
            } else if (zonesetting.startsWith("State")) {
                if (type == 1) {
                    imageLoader(UserObj.optString("stateiconURLlowres"), 1);
                } else {
                    imageLoader(UserObj.optString("stateiconURLlowres"), 2);
                }
            } else if (zonesetting.startsWith("City")) {
                if (type == 1) {
                    imageLoader(UserObj.optString("cityiconURLlowres"), 1);
                } else {
                    imageLoader(UserObj.optString("cityiconURLlowres"), 2);
                }
            }

        } catch (Exception e) {

        }
    }

    private void imageLoader(String photoURL, final int type) {
        imageLoader.get(photoURL, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (type == 2) {
                    imageView.setImageBitmap(imageContainer.getBitmap());
                    //imageBitmap = imageContainer.getBitmap();
                } else if (type == 1) {
                    imageBitmap = imageContainer.getBitmap();
                    imageView.setImageBitmap(imageContainer.getBitmap());

                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
