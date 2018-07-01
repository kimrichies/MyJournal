package ug.co.must.kimrichies.diary;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddActivity extends AppCompatActivity implements View.OnClickListener{

    Button save;
    EditText title, grateful,done,feel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        save = (Button)findViewById(R.id.Save);
        save.setOnClickListener(this);


        title = (EditText)findViewById(R.id.title);
        grateful = (EditText)findViewById(R.id.grateful);
        done = (EditText)findViewById(R.id.done);
        feel = (EditText)findViewById(R.id.feel);

    }
    @Override
    public void onClick(View v) {
        // Add a new title record
        ContentValues values = new ContentValues();
        values.put(JournalProvider.TITLE, ((EditText)findViewById(R.id.title)).getText().toString());

        //get current date
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        values.put(JournalProvider.DATE, (currentDate));

        // Add a new grateful record
        values.put(JournalProvider.GRATEFUL, ((EditText)findViewById(R.id.grateful)).getText().toString());

        // Add a new Done record
        values.put(JournalProvider.DONE, ((EditText)findViewById(R.id.done)).getText().toString());

        // Add a new Feel record
        values.put(JournalProvider.FEEL, ((EditText)findViewById(R.id.feel)).getText().toString());

        Uri uri = getContentResolver().insert(JournalProvider.CONTENT_URI, values);

        //Toast.makeText(getBaseContext(), "New Note has Been Successfully Inserted!", Toast.LENGTH_LONG).show();

        //adding the dialog to show added information
        final Context context = this;
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.added_layout);
        dialog.setTitle("You have Added a Note");

        TextView text = (TextView) dialog.findViewById(R.id.viewText);
        text.setText(" Your New Note has Been Added");

        Button dialogButton = (Button) dialog.findViewById(R.id.OK);
        // if the ok button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);

            }
        });

        dialog.show();

    }




    //inflating or displaying the menu
    @Override
    public boolean onCreateOptionsMenu(Menu myMenu) {
        MenuInflater inflateMenu = getMenuInflater();
        inflateMenu.inflate(R.menu.journal_menu, myMenu);
        return true;
    }

    //determining the selected menu and opening the right activity
    public boolean onOptionsItemSelected(MenuItem itemSelect){

        //using the switch statement to determine the option chosen by user
        switch (itemSelect.getItemId()){
            case R.id.add:
                //create the activity
                Intent menu1 = new Intent (this, AddActivity.class);
                //start the created activity
                startActivity(menu1);
                return true;

            case R.id.view:
                //Create and start the activity in one go
                startActivity(new Intent(this, ViewActivity.class));
                return true;
            case R.id.notification:
                startActivity(new Intent(this, NotificationsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(itemSelect);
        }
    }

}










