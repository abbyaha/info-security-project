package haseley.abby.info_security_password_creator_and_manager;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Michael on 4/15/2016.
 */
public class ChangePassword extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SetMasterPassword()).commit();
    }

    public static class SetMasterPassword extends Fragment {
        SharedPreferences mSharedPreferences;
        public SetMasterPassword() {
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_change_login, container, false);
            Button ChangeButton = (Button) rootView.findViewById(R.id.change_button);
            ChangeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    change();
                    Log.d("Change", "Change password");
                }
            });
            return rootView;
        }
        public void change(){
            EditText pass = (EditText)getActivity().findViewById(R.id.password);
            String password = pass.getText().toString();
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            mSharedPreferences.edit().putString("stored_key", password).apply();
            Log.d("Password", "Password is: " + password);
            Toast.makeText(getActivity(),
                    "Password Updated Successfully",
                    Toast.LENGTH_LONG).show();
            getActivity().finish();

        }
    }
}
