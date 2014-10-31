package com.prolificinteractive.materialdialog.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.prolificinteractive.materialdialog.MaterialDialog;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.button_system).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        new AlertDialog.Builder(MainActivity.this)
            .setTitle("Test Title")
            .setMessage("This is a test message")
            .setPositiveButton("OK", null)
            .setNegativeButton("Cancel", null)
            .show();
      }
    });

    findViewById(R.id.button_material_basic).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        MaterialDialog dialog = new MaterialDialog(MainActivity.this);
        dialog.setTitle("Test Title");
        dialog.setMessage("This is a test message");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK");
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel");
        dialog.show();
      }
    });

    findViewById(R.id.button_material_custom_content).setOnClickListener(
        new View.OnClickListener() {
          @Override public void onClick(View v) {
            MaterialDialog dialog = new MaterialDialog(MainActivity.this, true);
            dialog.setTitle("Send Mailer");
            dialog.setMessage("Please enter your email address");
            dialog.setView(dialog.getLayoutInflater().inflate(R.layout.dialog_contents, null));
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Save");
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel");
            dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Save Draft",
                new MaterialDialog.OnClickDelegate() {
                  @Override public boolean onClick(MaterialDialog dialog, int which) {
                    Toast.makeText(dialog.getContext(), "Draft Saved", Toast.LENGTH_SHORT).show();
                    return true;
                  }
                });
            dialog.show();
          }
        });

    findViewById(R.id.button_material_custom_theme).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        MaterialDialog dialog = new MaterialDialog(MainActivity.this, R.style.MaterialDialog_Dark);
        dialog.setTitle("Test Title");
        dialog.setMessage("This is a test message");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK");
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel");
        dialog.show();
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
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
  }
}
