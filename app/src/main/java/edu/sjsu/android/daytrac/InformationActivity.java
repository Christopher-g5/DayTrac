package edu.sjsu.android.daytrac;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InformationActivity extends BaseClass{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Button phoneButton = findViewById(R.id.phoneButton);
        Intent intent = getIntent();
        phoneButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Uri data = Uri.parse(phoneButton.getText().toString());
                Intent intent = new Intent(Intent.ACTION_DIAL,data);
                startActivity(intent);
            }
        });
    }
}

