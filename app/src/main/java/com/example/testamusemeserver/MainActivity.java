package com.example.testamusemeserver;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.testamusemeserver.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AmusementAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        AmusementAPI amusementAPI = retrofit.create(AmusementAPI.class);
        disposable = amusementAPI.getRandAmusement("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError, this::onComplete, this::onSubscribe);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    private void onSubscribe(Disposable d) {
        Toast.makeText(this, "New query", Toast.LENGTH_SHORT).show();
    }

    private void onComplete() { }

    private void onSuccess(AmusementItem item) {
        binding.title.setText(item.title);
        binding.desc.setText(item.desc);

        try {
            Picasso.get().load(item.imgUrl).into(binding.img);
        } catch (Exception e) {
            Log.wtf("My_Error", e.toString());
        }
    }

    private void onError(Throwable t) {
        binding.title.setText(t.toString());
        Log.wtf("My_Error", t.toString());
    }
}