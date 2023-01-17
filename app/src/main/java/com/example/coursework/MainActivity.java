package com.example.coursework;

import android.os.Bundle;

import com.example.coursework.R;
import com.example.coursework.databinding.ActivityMainBinding;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.coursework.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.navigationHostFragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    public void switchFragment(Fragment fragment, int id) {
//        Fragment previousFragment = getSupportFragmentManager().findFragmentById(R.id.savedFragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id); //Store object id into bundle.
        fragment.setArguments(bundle);
        transaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right);
        transaction.replace(R.id.relativeLayoutFilter, fragment).addToBackStack("");
        transaction.addToBackStack(null);//Able to go back by popping transaction
        transaction.commit();
    }
}