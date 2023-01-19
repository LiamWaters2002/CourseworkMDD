package com.example.coursework;

import static com.example.coursework.BuildConfig.MAPS_API_KEY;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coursework.databinding.FragmentWeatherBinding;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class WeatherFragment extends Fragment {

    private FragmentWeatherBinding binding;
    private FetchUrlData fetchUrlData;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WeatherAdapter weatherAdapter = new WeatherAdapter();
        weatherAdapter.execute("London", "feb7abc8a7msh50b4159db41f088p16428bjsn5ad7bf336048");
        try {
            Toast.makeText(getContext(), weatherAdapter.get().toString(), Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}



