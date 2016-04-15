package haseley.abby.info_security_password_creator_and_manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class PasswordViewer extends AppCompatActivity{
    //List of passwords that will be displayed
    List<PasswordEntry> passwords = new ArrayList<>();
    //List view to load passwords into
    ListView listView;
    private static final String CRYPT_KEY_NAME = "my_crypt_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_viewer);

        fillList();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set the listener for clicking an item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //Find the string displayed with the clicked item
                String item = ((TextView) view).getText().toString();

                //Find out which password this was based on the text
                PasswordEntry password = findItemByString(item);

                //Bundle the password info to send to the details view
                Bundle b = new Bundle();
                b.putString("Account", password.getAccount());
                b.putString("Sentence", password.getSentence());
                b.putString("Password", password.getPassword());
                //Get creation date and current date
                String creation = password.getCreationDate();
                Calendar now = Calendar.getInstance();
                //Calculate an appropriate string for the password's age
                String passAge = findBestTimeString(creation, now);
                b.putString("Age", passAge);

                goToPasswordDetails(b);
            }
        });
        //New Password button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPasswordCreator();
            }
        });
    }
    public byte[] getKey(){
        KeyStore mKeyStore;
        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
            mKeyStore.load(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get an instance of KeyStore", e);
        }

        Object secretKey = null;
        try {
            KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) mKeyStore.getEntry(CRYPT_KEY_NAME, null);
            secretKey = secretKeyEntry.getSecretKey();
            Log.d("Key in get key: ", Base64.encodeToString(convertToBytes(secretKey),Base64.DEFAULT));

            return convertToBytes(secretKey);
        }catch (Exception e){
            Log.e("getKey: ", Log.getStackTraceString(e));
            return null;
        }
    }

    private byte[] convertToBytes(Object object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        }
    }

    //Turns the date string into a 6-element array of numbers
    private int[] extractDatePieces(String dateString){
        int[] values = new int[6];
        //Values are separated by .
        String[] parse = dateString.split("\\.");

        for(int i = 0; i < 6; i++){
            values[i] = Integer.parseInt(parse[i]);
        }
        return values;
    }

    //Calculate the difference in time between these dates
    private int[] getElapsedTime(int[] oldDate, Calendar current){
        int[] actualElapsed = new int[6];

        //Get the current change in each value
        int yearsPassed = current.get(Calendar.YEAR) - oldDate[0];
        int monthsPassed = current.get(Calendar.MONTH) - oldDate[1];
        int daysPassed = current.get(Calendar.DAY_OF_MONTH) - oldDate[2];
        int hoursPassed = current.get(Calendar.HOUR_OF_DAY) - oldDate[3];
        int minutesPassed = current.get(Calendar.MINUTE) - oldDate[4];
        int secondsPassed = current.get(Calendar.SECOND) - oldDate[5];

        //Correct for negative values in each field
        //By "borrowing" from the next largest
        if(secondsPassed < 0){
            secondsPassed += 60;
            minutesPassed--;
        }
        if(minutesPassed < 0){
            minutesPassed += 60;
            hoursPassed--;
        }
        if(hoursPassed < 0){
            hoursPassed += 24;
            daysPassed--;
        }
        if(daysPassed < 0){
            monthsPassed--;
            //Find out how many days were in the first month
            if(oldDate[1] == Calendar.JANUARY
                    || oldDate[1] == Calendar.MARCH
                    || oldDate[1] == Calendar.MAY
                    || oldDate[1] == Calendar.JULY
                    || oldDate[1] == Calendar.AUGUST
                    || oldDate[1] == Calendar.OCTOBER
                    || oldDate[1] == Calendar.DECEMBER)
            {
                //31 days
                daysPassed += 31;
            }
            else if(oldDate[1] == Calendar.APRIL
                    || oldDate[1] == Calendar.JUNE
                    || oldDate[1] == Calendar.SEPTEMBER
                    || oldDate[1] == Calendar.NOVEMBER){
                //30 days
                daysPassed += 30;
            }
            else{ //February
                daysPassed +=28;
            }
        }
        if(monthsPassed < 0){
            monthsPassed += 12;
            yearsPassed--;
        }
        //Note: Years should never be negative

        actualElapsed[0] = yearsPassed;
        actualElapsed[1] = monthsPassed;
        actualElapsed[2] = daysPassed;
        actualElapsed[3] = hoursPassed;
        actualElapsed[4] = minutesPassed;
        actualElapsed[5] = secondsPassed;

        return  actualElapsed;
    }

    //Get a user-friendly message about the age of the password
    private String findBestTimeString(String old, Calendar current){
        String result;

        //Get the int array of date values
        int[] dateValues = extractDatePieces(old);
        //Get the elapsed time between password creation and now
        int[] elapsed = getElapsedTime(dateValues, current);

        //Tell the user about the largest unit with a difference
        if(elapsed[0] > 0){
            result = "Age: " + elapsed[0] + " YEARS old";
        }
        else if(elapsed[1] > 0){
            result = "Age: " + elapsed[1] + " months old";
        }
        else if(elapsed[2] > 0){
            result = "Age: " + elapsed[2] + " days old";
        }
        else if(elapsed[3] > 0){
            result = "Age: " + elapsed[3] + " hours old";
        }
        else if(elapsed[4] > 0){
            result = "Age: " + elapsed[4] + " minutes old";
        }
        else {
            result = "Age: " + elapsed[5] + " seconds old";
        }

        return result;
    }

    private void fillList(){
        //Grab the list view
        listView = (ListView) findViewById(R.id.ourlistview);
        //listView = getListView();

        getPasswordsFromFile();

        //Note: Using a custom adapter class just to change the font color...
        ArrayAdapter<PasswordEntry> adapter = new WWAdapter(listView.getContext(),
                android.R.layout.simple_list_item_1, makeArray(passwords));

        listView.setAdapter(adapter);
    }

    //Helper method because the adapter needs an array of PasswordEntry, not array list
    private PasswordEntry[] makeArray(List<PasswordEntry> list){
        PasswordEntry[] array = new PasswordEntry[list.size()];

        for(int i =0; i < list.size(); i++){
            array[i] = list.get(i);
        }

        return array;
    }

    @Override
    public void onResume(){
        super.onResume();
        //Refresh the list view
        fillList();
    }

    private void getPasswordsFromFile(){
        try {
            //Decrypt password list
            passwords = PasswordFile.decryptStore(getApplicationContext(), "WhiteWizard2",  Arrays.copyOfRange(getKey(),0,16));
        } catch(Exception e){
            //Shouldn't get here
            Log.e("Reading Passwords", "Could not read passwords from file");
        }
    }

    //Helper method to use displayed string to find the object
    private PasswordEntry findItemByString(String itemToString){
        PasswordEntry result = null;

        for(PasswordEntry p : passwords){
            //Note: password's toString is what was displayed in the list
            if(p.toString().equalsIgnoreCase(itemToString)){
                result = p;
                break;
            }
        }

        return result;
    }

    //Bundle should have the 4 values for the detail view
    private void goToPasswordDetails(Bundle b){
        Intent intent = new Intent(this, PasswordDetails.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void goToPasswordCreator(){
        Intent intent = new Intent(this, CreatePassword.class);
        startActivity(intent);
    }
    @Override
    public void onPause(){
        //End activity if the user leaves the app
        super.onPause();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
        Log.d("Menu", "Trying to create menu button");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_change_password){
            Intent intent = new Intent(this, ChangePassword.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

}
