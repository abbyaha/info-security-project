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
                //TempPassword password = findItemByString(item);
                PasswordEntry password = findItemByString(item);

                Bundle b = new Bundle();
                b.putString("Account", password.getAccount());
                b.putString("Sentence", password.getSentence());
                b.putString("Password", password.getPassword());
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

    private void fillList(){
        //Grab the list view
        listView = getListView();
        //ListView listView1 = (ListView) findViewById(R.id.listView1);

        getPasswordsFromFile();

        ArrayAdapter<PasswordEntry> adapter = new WWAdapater(listView.getContext(),
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
