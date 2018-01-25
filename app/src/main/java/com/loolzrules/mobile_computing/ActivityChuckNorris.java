package com.loolzrules.mobile_computing;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class ActivityChuckNorris extends AppCompatActivity {

    private RestTemplate restTemplate;
    private final String url = "http://api.icndb.com/jokes/random";
    private TextView mJoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuck_norris);
        mJoke = findViewById(R.id.tv_joke);

        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        new GetAJokeTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.chucknorris, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.refresh_joke:
                new GetAJokeTask().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class GetAJokeTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return restTemplate.getForObject(url, String.class);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                String text = new JSONObject(s).getJSONObject("value").getString("joke");
                mJoke.setText(text);
            } catch (JSONException e) {
                mJoke.setText("An error occurred");
                e.printStackTrace();
            }
        }
    }

}
