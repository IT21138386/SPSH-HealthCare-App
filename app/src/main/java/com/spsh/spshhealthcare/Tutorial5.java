package com.spsh.spshhealthcare;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.spsh.spshhealthcare.database.DBHelper;

import java.util.List;

public class Tutorial5 extends AppCompatActivity {

    EditText et_username, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial5);

        et_username = findViewById(R.id.et_username_sathira); //initializing
        et_password = findViewById(R.id.et_password_sathira);
    }

    public void saveUser(View view){
        String name = et_username.getText().toString(); //values in textviews are retrieved assigned to string variables
        String password = et_password.getText().toString();
        DBHelper dbHelper = new DBHelper(this);

        if(name.isEmpty()||password.isEmpty()){ //condition to check if fields are empty
            Toast.makeText(this, getResources().getString(R.string.emptyFieldToast), Toast.LENGTH_SHORT).show();
        }else { //not empty
            long inserted = dbHelper.addInfo(name, password); //inserting data, inserted variable hold the number of rows inserted

            if(inserted>0){
                Toast.makeText(this, getResources().getString(R.string.successToast), Toast.LENGTH_SHORT).show(); et_username.setText(""); et_password.setText("");
            }else{
                Toast.makeText(this, getResources().getString(R.string.unsuccessToast), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void viewAll(View view){ //button click event
        DBHelper dbHelper = new DBHelper(this); //we just have to call this and it will return this.info (line 82 of DBHelper class)

        //getting the data
        List info = dbHelper.readAll();

        String[] infoArray = (String[]) info.toArray(new String[0]);//string array to display all the details one by one in the dialogue box

        //building alert dialogue box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Users Details");

        builder.setItems(infoArray, new DialogInterface.OnClickListener() {  //sets dialogue interface, creates onclick event when we click one element
            @Override                                            //int i is the index of the element we are going to click
            public void onClick(DialogInterface dialogInterface, int i) { //onclick event, it is needed for update, delete and other crud operations
                String userName = infoArray[i].split(":")[0]; //this splits the row of data into 2, 0 means left hand side element, in this case it is the username
                //Toast.makeText(Tutorial5.this, userName, Toast.LENGTH_SHORT).show(); //this will toast the username

                //since we have the username, we can now set it to the edit text component
                et_username.setText(userName);
                et_password.setText("*********");
            }
        });

        //setting button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //no need to implement anything
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show(); //similar to toast messages
    }

    public void deleteUser(View view){
        DBHelper dbHelper = new DBHelper(this);

        //deleting user
        String userName = et_username.getText().toString();
        
        if(userName.isEmpty()){ //nothing is selected inorder to delete
            Toast.makeText(this, getResources().getString(R.string.unSelectedDeleteToast), Toast.LENGTH_SHORT).show();
        }else{
            dbHelper.deleteInfo(view, userName);
            Toast.makeText(this, getResources().getString(R.string.DeleteSuccessToast, userName), Toast.LENGTH_SHORT).show();

            //clears the components
            et_username.setText("");
            et_password.setText("");
        }
    }

    public void updateUser(View view){
        DBHelper dbHelper = new DBHelper(this);
        String userName = et_username.getText().toString();
        String password = et_password.getText().toString();

        if(userName.isEmpty()||password.isEmpty()){
            Toast.makeText(this, R.string.unSelectedUpdateToast, Toast.LENGTH_SHORT).show();
        }else {
            dbHelper.updateInfo(view,userName,password);

            //clears the components
            et_username.setText("");
            et_password.setText("");
        }
    }
}