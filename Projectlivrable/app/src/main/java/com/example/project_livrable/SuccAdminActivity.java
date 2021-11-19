package com.example.project_livrable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ThemedSpinnerAdapter;

import android.media.MediaCodec;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SuccAdminActivity extends AppCompatActivity {

    LinearLayout myContainer;
    DatabaseReference myref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succ_admin);
        ListView myList = (ListView)findViewById(R.id.SuccListView);
        ArrayList<HelperClass> helisss = new ArrayList<HelperClass>();
        ArrayList<String> lissss = new ArrayList<String>();
        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, lissss);
        myList.setAdapter(myArrayAdapter);

        myref = FirebaseDatabase.getInstance().getReference().child("Employé(e)");
        myref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HelperClass value = snapshot.getValue(HelperClass.class);
                lissss.add(value.getUsername());
                helisss.add(value);
                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                HelperClass employee = helisss.get(position);
                showDialog();
                return false;
            }
        });
    }

    //I slept at 7am so dont wake me up, I sent the vid i used on discord
    //Hope your understand shdrt



    public void showDialog() {
        AlertDialog.Builder dialogue = new AlertDialog.Builder(getApplicationContext());
        LayoutInflater layoutInflate= getLayoutInflater();
        View dialogueView = layoutInflate.inflate(R.layout.activity_succ_list, null);
        dialogue.setView(dialogueView);
        dialogue.setTitle("Supprimer une succursale");
        AlertDialog alertDialog = dialogue.create();
        alertDialog.show();
    }


}

