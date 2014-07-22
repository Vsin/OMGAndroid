package com.example.omgandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class MainActivity extends Activity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    TextView mainTextView;
    Button mainButton;
    EditText mainEditText;
    ListView mainListView;
    ArrayAdapter mArrayAdapter;
    ArrayList mNameList = new ArrayList();
    ShareActionProvider mShareActionProvider;
    private static final String PREFS = "prefs";
    private static final String PREF_NAME = "name";
    SharedPreferences mSharedPreferences;

    private static final String QUERY_URL = "http://openlibrary.org/search.json?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 1. Access the TextView defined in layout XML
        // and set its text
        displayWelcome();
        mainTextView = (TextView) findViewById(R.id.main_textview);
        mainButton = (Button) findViewById(R.id.main_button);
        mainButton.setOnClickListener(this);
        mainEditText = (EditText) findViewById(R.id.main_edittext);
        mainListView = (ListView) findViewById(R.id.main_listview);
        mArrayAdapter = new ArrayAdapter(this,
                        android.R.layout.simple_list_item_1,
                        mNameList);
        mainListView.setAdapter(mArrayAdapter);
        mainListView.setOnItemClickListener(this);
    }

    private void displayWelcome() {
        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);

        String name = mSharedPreferences.getString(PREF_NAME, "");

        if (name.length() < 0) {
            Toast.makeText(this, "Welcome back, " + name + "!", Toast.LENGTH_LONG).show();
        } else {

            // otherwise show a dialog to ask their name
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Hello!");
            alert.setMessage("What is your name?");

            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {

                    String inputName = input.getText().toString();

                    SharedPreferences.Editor e = mSharedPreferences.edit();
                    e.putString(PREF_NAME, inputName);
                    e.commit();

                    Toast.makeText(getApplicationContext(), "Welcome, " + inputName + "!",
                            Toast.LENGTH_LONG).show();
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });

            alert.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        queryBooks(mainEditText.getText().toString());
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        Log.d("OMG Android", position + ": " + mNameList.get(position));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu
        // add items to the action bar if present
        getMenuInflater().inflate(R.menu.main, menu);

        // Access the Share Item defined in menu XML
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);

        // Access the object responsible for
        // putting together the sharing submenu
        if (shareItem != null) {
            mShareActionProvider = (ShareActionProvider) shareItem.getActionProvider();
        }

        // Create and Intent to share content
        setShareIntent();

        return true;
    }

    private void setShareIntent() {

        if (mShareActionProvider != null) {

            // Create an Intent with the contents of TextView
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Android Development");
            shareIntent.putExtra(Intent.EXTRA_TEXT, mainTextView.getText());

            // Make sure the provider knows that
            // it should work with the Intent.
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private void queryBooks(String searchString) {
        String urlString = "";
        try {
            urlString = URLEncoder.encode(searchString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(QUERY_URL + urlString,
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).
                                                                                            show();
                            Log.d("omg android", jsonObject.toString());
                        }

                        public void onFailure(int statusCode, Throwable throwable) {
                            Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " +
                                                        throwable.getMessage(), Toast.LENGTH_LONG);

                            Log.d("omg android", statusCode + " " + throwable.getMessage());
                        }
        });
    }
}
