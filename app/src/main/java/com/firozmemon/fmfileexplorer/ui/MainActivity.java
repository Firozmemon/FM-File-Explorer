package com.firozmemon.fmfileexplorer.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.firozmemon.fmfileexplorer.FMApplication;
import com.firozmemon.fmfileexplorer.R;
import com.firozmemon.fmfileexplorer.helper.AlertDialogHelper;
import com.firozmemon.fmfileexplorer.ui.apps.AppFragment;
import com.firozmemon.fmfileexplorer.ui.base.AlertDialogCallback;
import com.firozmemon.fmfileexplorer.ui.base.BaseFragment;
import com.firozmemon.fmfileexplorer.ui.storage.fragments.DefaultFragment;
import com.firozmemon.fmfileexplorer.ui.storage.fragments.StorageFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final int REQUEST_EXTERNAL_STORAGE = 101;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.frame)
    FrameLayout frameLayout;

    @OnClick(R.id.fab)
    void onFabClick() {
        BaseFragment fragment = (BaseFragment) getFragmentManager().findFragmentByTag(CURRENT_TAG);
        if (fragment == null) {
            fragment = (BaseFragment) getFragmentManager().findFragmentByTag(TAG_DEFAULT);
        }

        fragment.fabClicked();
    }

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_DEFAULT = "default";
    private static final String TAG_SD0 = "sd0";
    private static final String TAG_SD1 = "sd1";
    private static final String TAG_SD2 = "sd2";
    private static final String TAG_SD3 = "sd3";
    private static final String TAG_SD4 = "sd4";
    private static final String TAG_ROOT = "root";
    private static final String TAG_APP = "apps";
    public static String CURRENT_TAG = TAG_DEFAULT;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        // Adding Runtime Permission handling code
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
            } else {
                initialSetup(savedInstanceState);
            }
        } else {
            initialSetup(savedInstanceState);
        }
    }

    private void initialSetup(Bundle savedInstanceState) {
        //Navigation Drawer setup
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        setupNavigationView();

        mHandler = new Handler();

        if (savedInstanceState == null) {
            navItemIndex = -1;
            CURRENT_TAG = TAG_DEFAULT;
            loadHomeFragment();
        }
    }

    private void setupNavigationView() {
        List<String> storageList = ((FMApplication) getApplication()).getStorageDirectories();
        // Assuming a device can have max 5 storage paths
        // FIXME: have dynamic storage options w.r.t device storage availability
        if (storageList.size() >= 5) {
            // do nothing, display all 5 storage options
        } else {
            int storageCount = storageList.size();

            Menu navMenu = navigationView.getMenu();

            switch (storageCount - 1) {
                case 0:
                    navMenu.findItem(R.id.nav_sd0).setVisible(true);
                    navMenu.findItem(R.id.nav_sd1).setVisible(false);
                    navMenu.findItem(R.id.nav_sd2).setVisible(false);
                    navMenu.findItem(R.id.nav_sd3).setVisible(false);
                    navMenu.findItem(R.id.nav_sd4).setVisible(false);
                    break;
                case 1:
                    navMenu.findItem(R.id.nav_sd0).setVisible(true);
                    navMenu.findItem(R.id.nav_sd1).setVisible(true);
                    navMenu.findItem(R.id.nav_sd2).setVisible(false);
                    navMenu.findItem(R.id.nav_sd3).setVisible(false);
                    navMenu.findItem(R.id.nav_sd4).setVisible(false);
                    break;
                case 2:
                    navMenu.findItem(R.id.nav_sd0).setVisible(true);
                    navMenu.findItem(R.id.nav_sd1).setVisible(true);
                    navMenu.findItem(R.id.nav_sd2).setVisible(true);
                    navMenu.findItem(R.id.nav_sd3).setVisible(false);
                    navMenu.findItem(R.id.nav_sd4).setVisible(false);
                    break;
                case 3:
                    navMenu.findItem(R.id.nav_sd0).setVisible(true);
                    navMenu.findItem(R.id.nav_sd1).setVisible(true);
                    navMenu.findItem(R.id.nav_sd2).setVisible(true);
                    navMenu.findItem(R.id.nav_sd3).setVisible(true);
                    navMenu.findItem(R.id.nav_sd4).setVisible(false);
                    break;
                case 4: // this case will never be called :D
                default:
                    navMenu.findItem(R.id.nav_sd0).setVisible(true);
                    navMenu.findItem(R.id.nav_sd1).setVisible(true);
                    navMenu.findItem(R.id.nav_sd2).setVisible(true);
                    navMenu.findItem(R.id.nav_sd3).setVisible(true);
                    navMenu.findItem(R.id.nav_sd4).setVisible(true);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            BaseFragment fragment = (BaseFragment) getFragmentManager().findFragmentByTag(CURRENT_TAG);
            if (fragment == null) {
                fragment = (BaseFragment) getFragmentManager().findFragmentByTag(TAG_DEFAULT);
            }

            fragment.performBackPressedOperation();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        CURRENT_TAG = TAG_DEFAULT;
        if (id == R.id.nav_sd0) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_SD0;
        } else if (id == R.id.nav_sd1) {
            navItemIndex = 1;
            CURRENT_TAG = TAG_SD1;
        } else if (id == R.id.nav_sd2) {
            navItemIndex = 2;
            CURRENT_TAG = TAG_SD2;
        } else if (id == R.id.nav_sd3) {
            navItemIndex = 3;
            CURRENT_TAG = TAG_SD3;
        } else if (id == R.id.nav_sd4) {
            navItemIndex = 4;
            CURRENT_TAG = TAG_SD4;
        } else if (id == R.id.nav_root) {
            navItemIndex = 5;
            CURRENT_TAG = TAG_ROOT;
        } else if (id == R.id.nav_apps) {
            navItemIndex = 6;
            CURRENT_TAG = TAG_APP;
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);

        loadHomeFragment();
        return true;
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {

        // set toolbar title
        //setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.animator.fade_in,
                        android.R.animator.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        List<String> storageList = ((FMApplication) getApplication()).getStorageDirectories();
        switch (navItemIndex) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                // storage path
                return StorageFragment.getInstance(storageList.get(navItemIndex));
            case 5:
                // root path
                return StorageFragment.getInstance("/");

            case 6:
                // apps
                return AppFragment.getInstance();

            default:
                return DefaultFragment.getInstance();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Load data
                initialSetup(null);

            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialogHelper alertDialogHelper = new AlertDialogHelper(MainActivity.this);
                    alertDialogHelper.displayAlertDialog("This permission is important to Display Storage content",
                            "Important permission required", "OK", "Deny",
                            new AlertDialogCallback() {
                                @Override
                                public void alertDialogPositiveButtonClicked(Object obj) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
                                }

                                @Override
                                public void alertDialogNegativeButtonClicked(String message) {
                                    MainActivity.this.finish();
                                }
                            });
                    //ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
                }
            }
        }
    }
}
