package com.example.project_livrable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.regex.*;


import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    boolean existant = false;
    public static final String TAG = "hamid <3";

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    DatabaseReference reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.role, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void registerdInfo(View view) {
        existant = false;
        Intent registeredIntent = new Intent(getApplicationContext(), MainActivity.class);

        EditText firstNameView = (EditText) findViewById(R.id.FirstName);
        String firstName = firstNameView.getText().toString();
        EditText lastNameView = (EditText) findViewById(R.id.LastName);
        String lastName = lastNameView.getText().toString();
        EditText userNameView = (EditText) findViewById(R.id.Username2);
        String username = userNameView.getText().toString();
        EditText emailView = (EditText) findViewById(R.id.Email);
        String email = emailView.getText().toString();
        EditText passwordView = (EditText) findViewById(R.id.Password2);
        String password = passwordView.getText().toString();
        Spinner roleView = (Spinner) findViewById(R.id.spinner2);
        String role = roleView.getSelectedItem().toString();
        boolean fromRegistered;
        if (!firstName.equals("") && !lastName.equals("") && !username.equals("") && !email.equals("") && !password.equals("") && !role.equals("")) {
            fromRegistered = true;
            rootNode = FirebaseDatabase.getInstance();

            reference = rootNode.getReference("Client");
            reference2 = rootNode.getReference("Employé(e)");
            HelperClass help = new HelperClass(firstName,lastName,username,email,password,role);
            MainActivity hamid = new MainActivity();
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!hamid.checkIfUsername(username,snapshot) && isValidEmailAddress(email) && isUsernameValid(username) && isNameValid(firstName)&& isNameValid(lastName) && password.length()>=6) {
                        registeredIntent.putExtra("firstName", firstName);
                        registeredIntent.putExtra("lastName", lastName);
                        registeredIntent.putExtra("username", username);
                        registeredIntent.putExtra("email", email);
                        registeredIntent.putExtra("password", password);
                        registeredIntent.putExtra("fromRegistered", fromRegistered);
                        if (role.equals("Employé(e)")) {
                            reference2.child(username).setValue(help);

                            HashMap hamid = new HashMap();
                            hamid.put("Prenom","empty");
                            hamid.put("Nom","empty");
                            hamid.put("Date de naissance","empty");
                            hamid.put("Adresse","empty");

                            HashMap hamid2 = new HashMap();
                            hamid2.put("Preuve de domicile","empty");

                            Hashtable<String,HelperService> hamid4 = new Hashtable<String,HelperService>();
                            HelperService help2 = new HelperService("Permis",username,hamid,hamid2);
                            HelperService help3 = new HelperService("CarteSante",username,hamid,hamid2);
                            HelperService help4 = new HelperService("Identite",username,hamid,hamid2);
                            hamid4.put("Permis", help2);
                            hamid4.put("CarteSante", help3);
                            hamid4.put("Identite", help4);

                            rootNode.getReference("Services").child(username+"_services").setValue(hamid4);
                        }
                        if (role.equals("Client")) {
                            reference.child(username).setValue(help);
                        }

                        startActivity(registeredIntent);

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String spinText = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public boolean isValidEmailAddress(String emai) {
        Boolean val = true;
        String email = emai.replaceAll("\\s+","");
        if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            val = true;
        }
        else if (email.isEmpty()){
            AlertDialog.Builder bbb = new AlertDialog.Builder(RegisterActivity.this);
            bbb.setMessage("Champ ne peut pas être vide.");
            AlertDialog alert = bbb.create();
            alert.show();
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            AlertDialog.Builder bbb = new AlertDialog.Builder(RegisterActivity.this);
            bbb.setMessage("Champ invalide, entrez un email correcte.");
            AlertDialog alert = bbb.create();
            alert.show();
            val = false;
        }
        return val;
    }
    public boolean isUsernameValid(String usernam){
        Pattern pattern = Pattern.compile("[A-Za-z0-9_]+");
        Boolean val = true;

        if (usernam == null) {
            AlertDialog.Builder bbb = new AlertDialog.Builder(RegisterActivity.this);
            bbb.setMessage("Champ ne peut pas être vide.");
            AlertDialog alert = bbb.create();
            alert.show();
            return false;
        }
        String username = usernam.replaceAll("\\s+","");
        Matcher m = pattern.matcher(username);
        if (m.matches() && username.length() >=3) {
            return true;
        }
        else if (!m.matches()){
            AlertDialog.Builder bbb = new AlertDialog.Builder(RegisterActivity.this);
            bbb.setMessage("Champ doit seulement contenir des lettres et chiffres.");
            AlertDialog alert = bbb.create();
            alert.show();
            val = false;
        }
        else if (username.length() <3){
            AlertDialog.Builder bbb = new AlertDialog.Builder(RegisterActivity.this);
            bbb.setMessage("Champ doit avoir 3 caractères ou plus.");
            AlertDialog alert = bbb.create();
            alert.show();
            val = false;
        }
        return val;
    }
    public boolean isNameValid(String usernam){
        Pattern pattern = Pattern.compile("[A-Za-z]+");

        if (usernam == null) {
            AlertDialog.Builder bbb = new AlertDialog.Builder(RegisterActivity.this);
            bbb.setMessage("Champ ne peut pas être vide.");
            AlertDialog alert = bbb.create();
            alert.show();
            return false;
        }
        String username = usernam.replaceAll("\\s+","");
        if (!pattern.matcher(username).matches()) {
            AlertDialog.Builder bbb = new AlertDialog.Builder(RegisterActivity.this);
            bbb.setMessage("Champ doit seulement contenir des lettres.");
            AlertDialog alert = bbb.create();
            alert.show();
        }
        Matcher m = pattern.matcher(username);
        return m.matches();
    }
}