package com.hms.grocy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    final Fragment homeFragment = new HomeFragment();
    final Fragment storeFragment = new StoreFragment();
    final Fragment cartFragment = new CartFragment();
    final Fragment profileFragment = new ProfileFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment activeFragment = homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, profileFragment, "2")
                .hide(profileFragment)
                .commit();

        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, cartFragment, "2")
                .hide(cartFragment)
                .commit();

        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, storeFragment, "2")
                .hide(storeFragment)
                .commit();

        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, homeFragment, "1")
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case R.id.menu_navigation_home:
                fragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(homeFragment)
                        .commit();
                activeFragment = homeFragment;
                return true;
            case R.id.menu_navigation_store:
                fragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(storeFragment)
                        .commit();
                activeFragment = storeFragment;
                return true;
            case R.id.menu_navigation_cart:
                fragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(cartFragment)
                        .commit();
                activeFragment = cartFragment;
                return true;
            case R.id.menu_navigation_profile:
                fragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(profileFragment)
                        .commit();
                activeFragment = profileFragment;
                return true;
        }

        return false;

    }

}