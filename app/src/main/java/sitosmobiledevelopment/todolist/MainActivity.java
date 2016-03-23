package sitosmobiledevelopment.todolist;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS = "TodoFile";

    private Button addButton;
    private CheckBox complete;
    private EditText todoText;
    private ListView todoList;
    private Button save;
    private final ArrayList<ListItemModel> testing = new ArrayList<>();
    private Firebase firebaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListAdapter adapter = new CustomAdapter(this, testing);

        Firebase.setAndroidContext(this);

        SharedPreferences preferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        String id = preferences.getString("id", null);


        if (id == null) {
            Firebase reference = new Firebase("https://flickering-heat-8688.firebaseio.com/").push();
            firebaseReference = reference;
            id = reference.getKey();
            SharedPreferences.Editor editor = getSharedPreferences(PREFS, MODE_PRIVATE).edit();
            editor.putString("id", id);
            editor.apply();
        }else {
            Firebase reference = new Firebase("https://flickering-heat-8688.firebaseio.com/"+id);
            firebaseReference = reference;
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        ListItemModel task = postSnapShot.getValue(ListItemModel.class);
                        testing.add(task);
                        todoList.setAdapter(adapter);
                    }
                    Toast.makeText(getBaseContext(), "Your ToDo List has successfully loaded!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Toast.makeText(getBaseContext(), "We were unable to load your ToDo List successfully!", Toast.LENGTH_LONG).show();
                }

            });
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            })
        }

        addButton = (Button)  findViewById(R.id.addButton);
        complete = (CheckBox) findViewById(R.id.CompleteCheckBox);
        todoText = (EditText) findViewById(R.id.addTaskText);
        todoList = (ListView) findViewById(R.id.todoListView);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItemModel listItem = new ListItemModel(todoText.getText().toString(), false);
                testing.add(listItem);
                todoText.setText("");
                addItemToFirebase();
            }
        });

        save = (Button) findViewById(R.id.saveButton);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseReference.setValue(testing);
                Toast.makeText(getBaseContext(), "Your ToDo List has been saved!", Toast.LENGTH_LONG).show();
            }
        });

//         TODO finish setting on item click listener up...
        todoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ListItemModel update = new ListItemModel(todoText.getText().toString(), true);
                testing.set(position, update);
                addItemToFirebase();
            }
        });

    }

    public void addItemToFirebase(){
        firebaseReference.setValue(testing);
        Toast.makeText(getBaseContext(), "Your new task has been added!", Toast.LENGTH_LONG).show();
        testing.clear();
    }
}

