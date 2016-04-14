package haseley.abby.info_security_password_creator_and_manager;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

//This class exists so that we can change the text color of the list view from the default
public class WWAdapter extends ArrayAdapter<PasswordEntry> {

    public WWAdapter(Context context, int textViewResourceId, PasswordEntry[] objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        TextView tv = (TextView) view.findViewById(android.R.id.text1);
        //Changing the text color
        tv.setTextColor(Color.argb(255,255, 255,255));

        return view;
    }
}