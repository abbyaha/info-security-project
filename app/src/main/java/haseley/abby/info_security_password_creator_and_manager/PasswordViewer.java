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
    List<TempPassword> passwords = new ArrayList<TempPassword>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_viewer);

        //Grab the list view
        listView = getListView();
        //ListView listView1 = (ListView) findViewById(R.id.listView1);

        getPasswords();

        ArrayAdapter<TempPassword> adapter = new ArrayAdapter<>(listView.getContext(),
                android.R.layout.simple_list_item_1, passwords);

        listView.setAdapter(adapter);

        //Set the listener for clicking an item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //Finding which item was clicked?
                String item = ((TextView) view).getText().toString();

                //Find out which password this was based on the text...
                TempPassword password = findItemByString(item);

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

    private  void getPasswords(){
        //TODO: Replace with accessing the database
        passwords.add(new TempPassword("OSU", "This is a sentence", "Pass1"));
        passwords.add(new TempPassword("EMAIL", "This is another sentence", "Pass2"));
        passwords.add(new TempPassword("YOUTUBE", "This is a third sentence", "Pass3"));
        passwords.add(new TempPassword("BANK", "This is also a sentence", "Pass4"));
        passwords.add(new TempPassword("LAPTOP", "This is yet another sentence", "Pass5"));
    }

    //Helper method
    private TempPassword findItemByString(String itemToString){
        TempPassword result = null;

        for(TempPassword p : passwords){
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
