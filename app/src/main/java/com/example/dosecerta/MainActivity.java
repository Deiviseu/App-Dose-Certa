package com.example.dosecerta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener{

    Button btEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btEntrar = findViewById(R.id.btEntrar);
        btEntrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent tela_home = new Intent(this, TelaHomeActivity.class);
        startActivity(tela_home);
    }
}