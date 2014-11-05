package com.prolificinteractive.materialdialog.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.prolificinteractive.materialdialog.MaterialDialog;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);
  }

  @OnClick(R.id.button_system_basic) void systemBasic() {
    new AlertDialog.Builder(this)
        .setTitle("Test Title")
        .setMessage("This is a test message")
        .setPositiveButton(android.R.string.ok, null)
        .setNegativeButton(android.R.string.cancel, null)
        .show();
  }

  @OnClick(R.id.button_material_basic) void backportBasic() {
    new MaterialDialog.Builder(MainActivity.this)
        .setTitle("Test Title")
        .setMessage("This is a test message")
        .setPositiveButton(android.R.string.ok)
        .setNegativeButton(android.R.string.cancel)
        .show();
  }

  @OnClick(R.id.button_system_content) void systemContent() {
    new AlertDialog.Builder(MainActivity.this)
        .setTitle("Send Mailer")
        .setMessage("Please enter your email address")
        .setView(getLayoutInflater().inflate(R.layout.dialog_contents, null))
        .setPositiveButton("Save", null)
        .setNegativeButton("Cancel", null)
        .setNeutralButton("Save Draft", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(MainActivity.this, "Draft Saved", Toast.LENGTH_SHORT).show();
          }
        })
        .show();
  }

  @OnClick(R.id.button_material_content) void backportContent() {
    new MaterialDialog.Builder(MainActivity.this)
        .setTitle("Send Mailer")
        .setMessage("Please enter your email address")
        .setView(getLayoutInflater().inflate(R.layout.dialog_contents, null))
        .setPositiveButton("Save")
        .setNegativeButton("Cancel")
        .setNeutralButton("Save Draft", new MaterialDialog.OnClickDelegate() {
          @Override public boolean onClick(MaterialDialog dialog, int which) {
            Toast.makeText(dialog.getContext(), "Draft Saved", Toast.LENGTH_SHORT).show();
            return true;
          }
        })
        .show();
  }

  @OnClick(R.id.button_system_themed) void systemThemed() {
    new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
        .setIcon(android.R.drawable.ic_dialog_info)
        .setTitle("Test Title")
        .setMessage("Test Message")
        .setPositiveButton(android.R.string.ok, null)
        .setNegativeButton(android.R.string.cancel, null)
        .show();
  }

  @OnClick(R.id.button_material_themed) void backportThemed() {
    new MaterialDialog.Builder(MainActivity.this, R.style.Theme_MaterialDialog_Dark)
        .setIcon(android.R.drawable.ic_dialog_info)
        .setTitle("Test Title")
        .setMessage("Test Message")
        .setPositiveButton(android.R.string.ok)
        .setNegativeButton(android.R.string.cancel)
        .show();
  }

  @OnClick(R.id.button_system_blank) void systemBlank() {
    new AlertDialog.Builder(MainActivity.this).show();
  }

  @OnClick(R.id.button_material_blank) void backportBlank() {
    new MaterialDialog.Builder(MainActivity.this).show();
  }

  @OnClick(R.id.button_system_list) void systemList() {
    new AlertDialog.Builder(MainActivity.this)
        .setTitle("List Items")
        .setItems(R.array.items, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(MainActivity.this, "Item " + which, Toast.LENGTH_SHORT).show();
          }
        })
        .setPositiveButton(android.R.string.ok, null)
        .show();
  }

  @OnClick(R.id.button_material_list) void backportList() {
    new MaterialDialog.Builder(MainActivity.this)
        .setTitle("List Items")
        .setItems(R.array.items, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(MainActivity.this, "Item " + which, Toast.LENGTH_SHORT).show();
          }
        })
        .setPositiveButton(android.R.string.ok)
        .show();
  }

  @OnClick(R.id.button_system_list_multi) void systemListMulti() {
    new AlertDialog.Builder(MainActivity.this)
        .setTitle("List Items")
        .setMultiChoiceItems(R.array.items, null, new DialogInterface.OnMultiChoiceClickListener() {
          @Override public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            Toast.makeText(MainActivity.this, "Item " + which + " " + isChecked, Toast.LENGTH_SHORT)
                .show();
          }
        })
        .setPositiveButton(android.R.string.ok, null)
        .show();
  }

  @OnClick(R.id.button_material_list_multi) void backportListMulti() {
    new MaterialDialog.Builder(MainActivity.this)
        .setTitle("List Items")
        .setMultiChoiceItems(R.array.items, null, new DialogInterface.OnMultiChoiceClickListener() {
          @Override public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            Toast.makeText(MainActivity.this, "Item " + which + " " + isChecked, Toast.LENGTH_SHORT)
                .show();
          }
        })
        .setPositiveButton(android.R.string.ok)
        .show();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
