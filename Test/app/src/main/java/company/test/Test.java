package company.test;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.net.URL;

import company.test.db.ItemContract;
import company.test.db.TestDaoHelper;
import company.test.dto.Greeting;

public class Test extends AppCompatActivity {
    public final static String strikeIronUserName = "stikeironusername@yourdomain.com";
    public final static String strikeIronPassword = "strikeironpassword";
    public final static String apiURL = "http://rest-service.guides.spring.io/greeting";


    private TestDaoHelper helper = new TestDaoHelper(this);
    private Test activity = this;
    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        listView = (ListView)this.findViewById(R.id.taskListView);
        this.updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.add_item:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add a task");
                builder.setMessage("What do you want to do?");
                final EditText inputField = new EditText(this);
                builder.setView(inputField);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String task = inputField.getText().toString();
                        Log.d("MainActivity", task);

                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        values.clear();
                        values.put(ItemContract.Columns.ITEM, task);

                        db.insertWithOnConflict(ItemContract.TABLE, null, values,
                                SQLiteDatabase.CONFLICT_IGNORE);

                        activity.updateUI();
                    }
                });

                builder.setNegativeButton("Cancel", null);

                builder.create().show();
                return true;
            case R.id.action_settings:
                Log.d("MainActivity", "Settings");
                return true;
            default:
                return false;
        }
    }

    public void updateUI() {
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(ItemContract.TABLE,
                new String[]{ItemContract.Columns._ID, ItemContract.Columns.ITEM},
                null, null, null, null, null);
        ListAdapter listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.item_view,
                cursor,
                new String[] { ItemContract.Columns.ITEM},
                new int[] { R.id.taskTextView},
                0
        );
        listView.setAdapter(listAdapter);
    }

    public void onDoneButtonClick(View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
        String task = taskTextView.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                ItemContract.TABLE,
                ItemContract.Columns.ITEM,
                task);

        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateUI();
        retrieveGl();
    }

    public void retrieveGl() {

        String email = "blah@hotmail.com";
        if( email != null && !email.isEmpty()) {

            String urlString = apiURL + "LicenseInfo.RegisteredUser.UserID=" + strikeIronUserName + "&LicenseInfo.RegisteredUser.Password=" + strikeIronPassword + "&VerifyEmail.Email=" + email + "&VerifyEmail.Timeout=30";

            new CallAPI().execute(urlString);

        }
    }

    private class CallAPI extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            RestTemplate restTemplate = new RestTemplate();

            String urlString=params[0]; // URL to call

            String resultToDisplay = "";

            InputStream in = null;

            // HTTP Get
            try {

                URL url = new URL(urlString);

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Greeting greeting = restTemplate.getForObject(apiURL, Greeting.class);

                Log.d("Test", greeting.toString());

            } catch (Exception e ) {

                System.out.println(e.getMessage());

                return e.getMessage();

            }

            return resultToDisplay;
        }

        protected void onPostExecute(String result) {

        }

    } // end CallAPI


    private class emailVerificationResult {
        public String statusNbr;
        public String hygieneResult;
    }


}
