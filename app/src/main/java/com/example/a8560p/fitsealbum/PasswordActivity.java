package com.example.a8560p.fitsealbum;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {
    String password, inputPassword;
    Button btnConfirm;
    TextInputLayout txtPassword;
    MyPrefs myPrefs;
    Integer passMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myPrefs = new MyPrefs(this);
        password = myPrefs.getPassword();
        passMode = myPrefs.getPassMode();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        txtPassword = (TextInputLayout)findViewById(R.id.txt_Password);

        btnConfirm = (Button)findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPassword()){
                    if(0 == passMode){
                        startActivity(new Intent(PasswordActivity.this,MainActivity.class));
                        finish();
                    }
                    else if(1 == passMode){
                        startActivity(new Intent(PasswordActivity.this,SetPasswordActivity.class));
                        finish();
                    }
                    else if(2 == passMode){
                        AlertDialog builder = new AlertDialog.Builder(PasswordActivity.this).create(); //Use context
                        builder.setTitle("Confirm");
                        builder.setMessage("Do you want to delete your current password ?");
                        builder.setButton(Dialog.BUTTON_POSITIVE,"Confirm", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        myPrefs.setPassword("");
                                        Toast.makeText(PasswordActivity.this, "Password has been delete", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(PasswordActivity.this,MainActivity.class));
                                        startActivity(new Intent(PasswordActivity.this,SettingsActivity.class));
                                        finish();
                                    }
                                });
                        builder.setButton(Dialog.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        startActivity(new Intent(PasswordActivity.this,MainActivity.class));
                                        startActivity(new Intent(PasswordActivity.this,SettingsActivity.class));
                                        finish();
                                    }
                                });
                        builder.show();
                    }
                }
            }
        });
    }

    boolean checkPassword(){
        inputPassword=txtPassword.getEditText().getText().toString();

        if(!inputPassword.matches(password)){
            txtPassword.setError("Password is not correct");
            return false;
        }
        else{
            txtPassword.setError(null);
            txtPassword.setErrorEnabled(false);
            return true;
        }
    }
}
