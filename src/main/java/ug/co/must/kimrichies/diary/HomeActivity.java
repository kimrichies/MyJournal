package ug.co.must.kimrichies.diary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    // Sing out button.
    Button SignOutButton;

    Button addNote,viewDelete;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SignOutButton= (Button) findViewById(R.id.sign_out);
        addNote =(Button)findViewById(R.id.add);
        viewDelete=(Button)findViewById(R.id.view);

        //adding listeners to the buttons
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(getApplicationContext(),AddActivity.class);
                startActivity(intent);
            }
        });

        viewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(getApplicationContext(),ViewActivity.class);
                startActivity(intent);
            }
        });

        // Showing Log out button.
        SignOutButton.setVisibility(View.VISIBLE);

        // Adding Click Listener to User Sign Out button.
        SignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity main = new MainActivity();
                main.UserSignOutFunction();

            }
        });

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
