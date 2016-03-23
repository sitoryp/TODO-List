package sitosmobiledevelopment.todolist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Darran on 3/16/2016.
 */
class CustomAdapter extends ArrayAdapter<ListItemModel> {

    public ArrayList<ListItemModel> mListItems;

    public CustomAdapter(Context context, ArrayList<ListItemModel> todoTasks) {
        super(context, R.layout.custom_row, todoTasks);
        mListItems = todoTasks;
    }

    class ViewHolder {
        TextView todoText;
        CheckBox complete;

        ViewHolder(View view) {
            todoText = (TextView) view.findViewById(R.id.ToDoText);
            complete = (CheckBox) view.findViewById(R.id.CompleteCheckBox);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View customView = convertView;
        ViewHolder holder = null;
        if (customView == null) {

            LayoutInflater todoInflater = LayoutInflater.from(getContext());
            customView = todoInflater.inflate(R.layout.custom_row, parent, false);
            holder = new ViewHolder(customView);
            customView.setTag(holder);
            Log.d("VIVZ", "Created a View Row!");
        } else {
            holder = (ViewHolder) customView.getTag();
        }

        ListItemModel todoTask = getItem(position);

        holder.todoText.setText(todoTask.text);
        holder.complete.setChecked(todoTask.completed);

        return customView;

    }
}
