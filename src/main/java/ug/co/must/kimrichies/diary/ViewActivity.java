package ug.co.must.kimrichies.diary;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        TextView textView = (TextView)findViewById(R.id.EditView);

        Button del = (Button)findViewById(R.id.delete);
        del.setOnClickListener(this);

        // Show all the notes sorted by date
        String URL = "content://com.must.provider/diary";
        Uri notes = Uri.parse(URL);

        Cursor c = getContentResolver().query(notes, null, null, null, "date");
        String result = "";

        if (!c.moveToFirst()) {
            Toast.makeText(this, result + " no content found!", Toast.LENGTH_LONG).show();
        } else {
            do {
                result = result + "\n" + c.getString(c.getColumnIndex(JournalProvider.DATE)) +
                        "\t\t\t\t\t\t\t\t\t" + c.getString(c.getColumnIndex(JournalProvider.TITLE)) +
                        "\t\t\t\t\t\t\t\t\t" + c.getString(c.getColumnIndex(JournalProvider.GRATEFUL)) +
                        "\t\t\t\t\t\t\t\t\t" + c.getString(c.getColumnIndex(JournalProvider.DONE)) +
                        "\t\t\t\t\t\t\t\t\t" + c.getString(c.getColumnIndex(JournalProvider.FEEL

                ));

                textView.setText(result);

            } while (c.moveToNext());

        }


    }

    //inflating or displaying the menu
    @Override
    public boolean onCreateOptionsMenu(Menu myMenuObject) {
        MenuInflater inflateMenu = getMenuInflater();
        inflateMenu.inflate(R.menu.journal_menu, myMenuObject);
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

    //Deleting all records from the database using a content provider
    @Override
    public void onClick(View v) {
        ContentResolver cr = getContentResolver();
        // Remove a specific row.
        //cr.delete(myRowUri, null, null);
        // Remove the first row.
        String URL = "content://com.must.provider/diary";
        Uri notes = Uri.parse(URL);
        String where = null;
        cr.delete(notes, where , null);

        //Show a text to confirm deletion
        //Toast.makeText(getApplicationContext(), "All Data Deleted", Toast.LENGTH_LONG).show();

        //adding the dialog to show delete confirmation
        final Context context = this;
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.added_layout);
        dialog.setTitle("Deleted");

        TextView text = (TextView) dialog.findViewById(R.id.viewText);
        text.setText(" All notes have been deleted");

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

}
