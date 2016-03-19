package sitosmobiledevelopment.todolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;


import com.firebase.client.Firebase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button addButton;
    private CheckBox complete;
    private EditText todoText;
    private ListView todoList;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //integrating Firebase
        Firebase.setAndroidContext(this);
        final Firebase reference = new Firebase("https://flickering-heat-8688.firebaseio.com/").push();

        addButton = (Button)  findViewById(R.id.addButton);
        complete = (CheckBox) findViewById(R.id.CompleteCheckBox);
        todoText = (EditText) findViewById(R.id.addTaskText);
        todoList = (ListView) findViewById(R.id.todoListView);

        final ArrayList<ListItemModel> testing = new ArrayList<>();
        final ListAdapter adapter = new CustomAdapter(this, testing);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItemModel listItem = new ListItemModel(todoText.getText().toString(), complete.isChecked());
                testing.add(listItem);
                todoText.setText("");
                todoList.setAdapter(adapter);
            }
        });

        save = (Button) findViewById(R.id.saveButton);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.setValue(testing);
            }
        });


//        todoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                complete.setChecked(true);
//            }
//        });

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}

