package sitosmobiledevelopment.todolist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS = "TodoFile";

    private Button addButton;
    private EditText todoText;
    private ListView todoList;
    private Button save;
    private Button delete;

    private ArrayList<ListItemModel> mListItems = new ArrayList<>();
    private CustomAdapter mAdapter;

    private Firebase mFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpUi();

        // Set list stuff
        mAdapter = new CustomAdapter(this, mListItems);
        todoList.setAdapter(mAdapter);

        /* FIREBASE STUFF HERE */

        setUpFirebase();
    }

    public void setUpFirebase() {
        Firebase.setAndroidContext(this);

        SharedPreferences preferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        String id = preferences.getString("id", null);

        if (id == null) {
            // Create a new firebase ref
            mFirebase = new Firebase("https://flickering-heat-8688.firebaseio.com/").push();

            id = mFirebase.getKey();
            SharedPreferences.Editor editor = getSharedPreferences(PREFS, MODE_PRIVATE).edit();
            editor.putString("id", id).apply();
        } else {
            // Get your firebase ref from sharedprefs ref
            mFirebase = new Firebase("https://flickering-heat-8688.firebaseio.com/" + id);

            mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ListItemModel task = snapshot.getValue(ListItemModel.class);
                        mListItems.add(task);
                    }

                    refreshAdapter();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }
    public void deleteTask(int position){
        mListItems.remove(position);
        refreshAdapter();
        pushToFirebase();
        Toast.makeText(getBaseContext(),"Your task item has been removed!", Toast.LENGTH_LONG).show();
    }

    public void addTaskToList(ListItemModel task) {
        mListItems.add(task);
    }

    public void pushToFirebase(){
        mFirebase.setValue(mListItems);
        Toast.makeText(getBaseContext(), "Your new task has been added!", Toast.LENGTH_LONG).show();
    }

    public void refreshAdapter() {
        mAdapter.notifyDataSetChanged();
    }

    public void setUpUi() {
        addButton = (Button)  findViewById(R.id.addButton);
        save = (Button) findViewById(R.id.saveButton);
        todoText = (EditText) findViewById(R.id.addTaskText);
        todoList = (ListView) findViewById(R.id.todoListView);
//        delete = (Button) findViewById(R.id.deleteButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListItemModel listItem = new ListItemModel(todoText.getText().toString());
                addTaskToList(listItem);
                refreshAdapter();
                pushToFirebase();

                todoText.setText("");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebase.setValue(mListItems);
                Toast.makeText(getBaseContext(), "Your ToDo List has been saved!", Toast.LENGTH_LONG).show();
            }
        });

       todoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               ListItemModel listItem = mListItems.get(position);
               listItem.completed = !listItem.completed;
               refreshAdapter();
           }
       });
    }
}

