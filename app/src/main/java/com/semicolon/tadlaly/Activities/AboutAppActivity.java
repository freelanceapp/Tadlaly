package com.semicolon.tadlaly.Activities;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.tadlaly.Models.AboutAppModel;
import com.semicolon.tadlaly.R;
import com.semicolon.tadlaly.Services.Api;
import com.semicolon.tadlaly.Services.Services;
import com.semicolon.tadlaly.Services.Tags;
import com.semicolon.tadlaly.language.LocalManager;

import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AboutAppActivity extends AppCompatActivity {
    private TextView txt_aboutApp;
    private ProgressBar progBar;
    private ImageView back;
    private String current_language;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LocalManager.updateResources(newBase,LocalManager.getLanguage(newBase)));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        initView();
        getData();

    }

    private void getData() {
        try {
            Retrofit retrofit = Api.getRetrofit(Tags.Base_Url);
            Call<AboutAppModel> call = retrofit.create(Services.class).AboutApp();
            call.enqueue(new Callback<AboutAppModel>() {
                @Override
                public void onResponse(Call<AboutAppModel> call, Response<AboutAppModel> response) {
                    if (response.isSuccessful())
                    {
                        txt_aboutApp.setText(response.body().getAbout_app());
                        progBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<AboutAppModel> call, Throwable t) {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(AboutAppActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                    Log.e("Error",t.getMessage());
                }
            });

        }catch (NullPointerException e){}
        catch (Exception e){}

    }

    private void initView() {
        current_language = Paper.book().read("language", Locale.getDefault().getLanguage());
        back = findViewById(R.id.back);

        if (current_language.equals("ar"))
        {
            back.setRotation(180f);
        }

        back.setOnClickListener(view -> finish());
        txt_aboutApp = findViewById(R.id.txt_aboutApp);
        progBar = findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
    }


}
