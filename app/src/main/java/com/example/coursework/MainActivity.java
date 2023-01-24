package com.example.coursework;

import android.os.Bundle;

import com.example.coursework.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

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

    public void switchFragment(Fragment fragment, Bundle bundle) {
//        Fragment previousFragment = getSupportFragmentManager().findFragmentById(R.id.savedFragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        Bundle bundle = new Bundle();
//        bundle.putInt("id", id); //Store object id into bundle.
        fragment.setArguments(bundle);
        transaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right);

        //Find fragment using view elements
        if(fragment instanceof InformationFragment){
            transaction.replace(R.id.relativeLayoutFilter, fragment).addToBackStack("saved");
        }
        else if(fragment instanceof SuggestFragment){
            transaction.replace(R.id.WeatherFragmentDisplay, fragment).addToBackStack("weather");
        }
        else if(fragment instanceof SavedFragment){
            transaction.setCustomAnimations(R.anim.enter_left, R.anim.enter_right);
            transaction.replace(R.id.informationLayout, fragment);
        }
        else{

        }
        transaction.commit();
    }
}