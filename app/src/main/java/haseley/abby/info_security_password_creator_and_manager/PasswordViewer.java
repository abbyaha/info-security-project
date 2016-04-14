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

                Calendar creation = password.getCreationDate();
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

    private String findBestTimeString(Calendar old, Calendar current){
        String result;

        int yearsPassed = current.get(Calendar.YEAR) - old.get(Calendar.YEAR);
        int monthsPassed = current.get(Calendar.MONTH) - old.get(Calendar.MONTH);
        int daysPassed = current.get(Calendar.DAY_OF_MONTH) - old.get(Calendar.DAY_OF_MONTH);
        int hoursPassed = current.get(Calendar.HOUR_OF_DAY) - old.get(Calendar.HOUR_OF_DAY);
        int minutesPassed = current.get(Calendar.MINUTE) - old.get(Calendar.MINUTE);
        int secondsPassed = current.get(Calendar.SECOND) - old.get(Calendar.SECOND);

        if(yearsPassed > 0){
            result = "Age: " + yearsPassed + " YEARS old";
        }
        else if(monthsPassed > 0){
            result = "Age: " + monthsPassed + " YEARS old";
        }
        else if(daysPassed > 0){
            result = "Age: " + daysPassed + " YEARS old";
        }
        else if(hoursPassed > 0){
            result = "Age: " + hoursPassed + " YEARS old";
        }
        else if(minutesPassed > 0){
            result = "Age: " + minutesPassed + " YEARS old";
        }
        else {
            result = "Age: " + secondsPassed + " YEARS old";
        }
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
            passwords = PasswordFile.decryptStore(getApplicationContext(), "WhiteWizard", "MyDifficultPassw");
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
