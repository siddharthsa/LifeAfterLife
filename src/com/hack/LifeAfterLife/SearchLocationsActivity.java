package com.hack.LifeAfterLife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by siddharth.agarwal on 20/09/14.
 */
public class SearchLocationsActivity extends Activity {

    Button btnSubmitUser;
    private static final String GUID =
            "GUID";

    private static final String IOS_GUID =
            "b9407f30-f5f8-466e-aff9-25556b57fe6d-100-12";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        String guid = intent.getStringExtra(GUID);


        btnSubmitUser = (Button) findViewById(R.id.btnSubmitUser);
        btnSubmitUser.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Guid found. Fetching info", Toast.LENGTH_LONG).show();


                    Intent intent = new Intent(SearchLocationsActivity.this, UserTimelineActivity.class);
                    intent.putExtra(GUID, IOS_GUID);
                    startActivity(intent);

            }
        }
        );

    }
}