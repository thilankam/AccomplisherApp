package com.thilanka1.accomplisherapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get ListView object from xml
        final ListView listView = (ListView) findViewById(R.id.listView); // ListView findViewById.
        // listView Object.
        // this listView object has used below.
        // This "listView" object can work with other components.

        // Create a new Adapter
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1); // This is the simple list.

        // Assign adapter to ListView
        // setting the Adapter here...
        //
        listView.setAdapter(adapter); // This is listView. Setting the Adapter for ListView.

        // Use Firebase to populate the list.
        Firebase.setAndroidContext(this); // This is for the Android Context.

        new Firebase("https://amber-torch-5438.firebaseio.com/todoItems") // "TodoItems" get updated on cloud here.
                .addChildEventListener(new ChildEventListener() {
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        adapter.add((String)dataSnapshot.child("text").getValue());
                    }
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        adapter.remove((String)dataSnapshot.child("text").getValue());
                    }
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
                    public void onCancelled(FirebaseError firebaseError) { }
                });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // You need to create a Firebase account at https://www.firebase.com/
        // After you create your account on firebase, use the account information below.
        // Add items via the Button and EditText at the bottom of the window.
        final EditText text = (EditText) findViewById(R.id.todoText);
        final Button button = (Button) findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Firebase("https://amber-torch-5438.firebaseio.com/todoItems") // your Firebase account information here.
                        .push()
                        .child("text") // The Text to be added.
                        .setValue(text.getText().toString());
            }
        });


        // Delete items when clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                new Firebase("https://amber-torch-5438.firebaseio.com/todoItems") // Your Firebase Dashboard account info here.
                        .orderByChild("text")
                        .equalTo((String) listView.getItemAtPosition(position))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                                    firstChild.getRef().removeValue();
                                }
                            }
                            public void onCancelled(FirebaseError firebaseError) { }
                        });
            }
        });

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

        // I have disabled the MenuItem for this app, just for the simplicity of the app.
    }*/
}
