package haseley.abby.info_security_password_creator_and_manager;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordViewer extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_viewer);

        //Grab the list view
        ListView listView = getListView();
        //ListView listView1 = (ListView) findViewById(R.id.listView1);

        //Temporary: Testing list view with an object type
        TempPassword[] temps = {
                new TempPassword("OSU", "This is a sentence", "Pass1"),
                new TempPassword("EMAIL", "This is another sentence", "Pass2"),
                new TempPassword("YOUTUBE","This is a third sentence", "Pass3"),
                new TempPassword("BANK", "This is also a sentence", "Pass4"),
                new TempPassword("LAPTOP", "This is yet another sentence", "Pass5"),
        };

        ArrayAdapter<TempPassword> adapter = new ArrayAdapter<>(listView.getContext(),
                android.R.layout.simple_list_item_1, temps);


        //String[] accounts = {"OSU", "EMAIL"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(getListView().getContext(), android.R.layout.simple_list_item_1, accounts);
        getListView().setAdapter(adapter);

        //Tset the listener for clicking an item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //Finding which item was clicked?
                String item = ((TextView) view).getText().toString();

                //TODO: Find out which password this was based on the text...

                Bundle b = new Bundle();
                b.putString("Account", "This is the Account");
                b.putString("Sentence", "This is the Sentence.");
                b.putString("Password", "This is the Password");
                goToPasswordDetails(b);
            }
        });
        //Replace/remove and make a New Password button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void goToPasswordDetails(Bundle b){
        Log.d("Viewer", "About to make an intent for password details");
        Intent intent = new Intent(this, PasswordDetails.class);
        intent.putExtras(b);
        startActivity(intent);
    }
}
