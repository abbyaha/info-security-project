package haseley.abby.info_security_password_creator_and_manager;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WWAdapter extends ArrayAdapter<PasswordEntry> {

    public WWAdapter(Context context, int textViewResourceId, PasswordEntry[] objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        TextView tv = (TextView) view.findViewById(android.R.id.text1);
        tv.setTextColor(Color.argb(255,255, 255,255));

        return view;
    }
}