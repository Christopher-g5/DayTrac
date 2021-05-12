package edu.sjsu.android.daytrac;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class BaseClass extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.your_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.clearHistoryButton:

                return true;
            case R.id.uninstallButton:
                Intent delete = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + getPackageName()));
                startActivity(delete);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}