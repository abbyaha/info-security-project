package haseley.abby.info_security_password_creator_and_manager;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PasswordViewer extends ListActivity {
    List<PasswordEntry> passwords = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_viewer);

        fillList();

        //Set the listener for clicking an item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //Finding which item was clicked?
                String item = ((TextView) view).getText().toString();

                //Find out which password this was based on the text...
                PasswordEntry password = findItemByString(item);

                Bundle b = new Bundle();
                b.putString("Account", password.getAccount());
                b.putString("Sentence", password.getSentence());
                b.putString("Password", password.getPassword());
                Log.d("---->PasswordView", "Gathered everything but date");
                String creation = password.getCreationDate();
                Calendar now = Calendar.getInstance();

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

    private int[] extractDatePieces(String dateString){
        int[] values = new int[6];
        String[] parse = dateString.split("\\.");
        Log.d("---->PasswordCreate", "Parsing String");
        for(int i = 0; i < 6; i++){
            values[i] = Integer.parseInt(parse[i]);
        }
        return values;
    }

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

    private String findBestTimeString(String old, Calendar current){
        String result;
        Log.d("---->PasswordCreate", "Making age string");
        int[] dateValues = extractDatePieces(old);
        int[] elapsed = getElapsedTime(dateValues, current);

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
        Log.d("---->PasswordCreate", "Selected string: " + result);
        return result;
    }

    private void fillList(){
        //Grab the list view
        listView = getListView();
        //ListView listView1 = (ListView) findViewById(R.id.listView1);

        getPasswordsFromFile();

        ArrayAdapter<PasswordEntry> adapter = new WWAdapter(listView.getContext(),
                android.R.layout.simple_list_item_1, makeArray(passwords));

        listView.setAdapter(adapter);
    }

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
            passwords = PasswordFile.decryptStore(getApplicationContext(), "WhiteWizard2", "MyDifficultPassw");
        } catch(Exception e){
            //TODO: Uh?
        }
    }

    //Helper method
    private PasswordEntry findItemByString(String itemToString){
        PasswordEntry result = null;

        for(PasswordEntry p : passwords){
            if(p.toString().equalsIgnoreCase(itemToString)){
                result = p;
                break;
            }
        }

        return result;
    }

    private void goToPasswordDetails(Bundle b){
        Log.d("Viewer", "About to make an intent for password details");
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
        super.onPause();
        finish();
    }
}
