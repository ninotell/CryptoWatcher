package com.example.primeraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{
    private FirebaseAuth mAuth;
    public static final String USER_EMAIL = "com.example.primeraapp.USER_EMAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // Set custom toolbar
//        Toolbar myToolbar = findViewById(R.id.myToolbar);
//        setSupportActionBar(myToolbar);

        // initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();


    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//
////        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//
//    }

    public void signIn() {
        EditText emailText = findViewById(R.id.editTextTextEmailAddress);
        String email = emailText.getText().toString();
        EditText passwordText = (EditText) findViewById(R.id.editTextTextPassword);
        String password = passwordText.getText().toString();

        email = email.replaceAll("\\s","");

        if (validateInputs(email, password)) {
            Toast.makeText(this, "Some fields are empty", Toast.LENGTH_SHORT).show();
            return;
        };

        String TAG = "signIn";

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // if task is not successful show error
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                // show error toast ot user ,user already exist
                                Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthInvalidUserException e) {
                                //show error tost network exception
                                Toast.makeText(MainActivity.this, "User doesn't exists", Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, "Unknown error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                });;;
    }

    private boolean validateInputs(String email, String password) {
        return (email == null || email.isEmpty() || password == null || password.isEmpty());
    }

    public void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            String userEmail = user.getEmail();
            intent.putExtra(USER_EMAIL, userEmail);
            startActivity(intent);
        } else {
            Log.d("updateUI", "Null user");
        }
    }


    public void createAccount(View view) {
        EditText emailText = findViewById(R.id.editTextTextEmailAddress);
        String email = emailText.getText().toString();
        EditText passwordText = findViewById(R.id.editTextTextPassword);
        String password = passwordText.getText().toString();

        email = email.replaceAll("\\s","");
        if (validateInputs(email, password)) {
            Toast.makeText(this, "Some fields are empty", Toast.LENGTH_SHORT).show();
            return;
        };

        String TAG = "createAccountTag";
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // if task is not successful show error
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                // show error toast ot user ,user already exist
                                Toast.makeText(MainActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                            } catch (FirebaseNetworkException e) {
                                //show error tost network exception
                                Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, "Unknown error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                });;
    }


    /**
     * Se llama cuando el usuario toca LOGIN
     */
    public void login(View view) {
        this.signIn();
//        Intent i = new Intent(this, HomeActivity.class);
//        startActivity(i);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        updateUI(user);
    }
}