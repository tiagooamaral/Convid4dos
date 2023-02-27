package com.webplanet.convid4dos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webplanet.convid4dos.adapters.HostListAdapter;
import com.webplanet.convid4dos.model.HostList;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editNameHost;
    Button buttonAddHostList;
    ListView listViewHostList;
    private BottomNavigationView navigationView;

    List<HostList> hostList;

    DatabaseReference databaseHostList;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNameHost = (EditText) findViewById(R.id.editNameHost);
        buttonAddHostList = (Button) findViewById(R.id.buttonAddHostList);
        listViewHostList = (ListView) findViewById(R.id.listViewHostList);
        navigationView = (BottomNavigationView) findViewById(R.id.navigationView);

        databaseHostList = FirebaseDatabase.getInstance().getReference("HostList");

        hostList = new ArrayList<>();

        buttonAddHostList.setOnClickListener(view -> addHostList());

        listViewHostList.setOnItemClickListener((adapterView, view, i, l) -> {
            HostList host = hostList.get(i);
            Intent intent = new Intent(getApplicationContext(), AddGuestActivity.class);
            intent.putExtra("HOST_LIST_ID", host.getHostListId());
            intent.putExtra("HOST_LIST_NAME", host.getHostListName());
            startActivity(intent);
        });
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseHostList.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hostList.clear();

                for(DataSnapshot hostSnapshot : dataSnapshot.getChildren()) {
                    HostList host = hostSnapshot.getValue(HostList.class);
                    hostList.add(host);
                }
                HostListAdapter adapter = new HostListAdapter(MainActivity.this, hostList);
                listViewHostList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }
    private void addHostList() {
        String name = editNameHost.getText().toString().toUpperCase().trim();

        if( !TextUtils.isEmpty(name) ) {
            String id = databaseHostList.push().getKey();
            HostList hostList = new HostList(id, name);
            databaseHostList.child(name).setValue(hostList);

            Toast.makeText(this, "Lista adicionada", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Você deve digitar um nome", Toast.LENGTH_LONG).show();
        }
    }
}