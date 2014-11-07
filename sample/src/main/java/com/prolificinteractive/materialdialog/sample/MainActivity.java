package com.prolificinteractive.materialdialog.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.prolificinteractive.materialdialog.MaterialDialog;

public class MainActivity extends Activity {

  @InjectView(R.id.edit_title) EditText editTitle;
  @InjectView(R.id.edit_message) EditText editMessage;
  @InjectView(R.id.icon_type) Spinner iconTypes;
  @InjectView(R.id.list_type) Spinner listTypes;
  @InjectView(R.id.check_button_positive) CheckBox checkPositive;
  @InjectView(R.id.check_button_negative) CheckBox checkNegative;
  @InjectView(R.id.check_button_neutral) CheckBox checkNeutral;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);
  }

  @OnClick(R.id.button_system) void onSystemClicked() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    if (!editTitle.getText().toString().trim().isEmpty()) {
      builder.setTitle(editTitle.getText().toString());
    }
    if (!editMessage.getText().toString().trim().isEmpty()) {
      builder.setMessage(editMessage.getText().toString());
    }
    switch (iconTypes.getSelectedItemPosition()) {
      case 1:
        builder.setIcon(android.R.drawable.ic_dialog_info);
        break;
      case 2:
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        break;
      default:
        break;
    }
    switch (listTypes.getSelectedItemPosition()) {
      case 1:
        builder.setItems(R.array.sample_items, new TestClickListener(this));
        break;
      case 2:
        builder.setSingleChoiceItems(R.array.sample_items, 0, new TestClickListener(this));
        break;
      case 3:
        builder.setMultiChoiceItems(R.array.sample_items, null,
            new DialogInterface.OnMultiChoiceClickListener() {
              @Override public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                Toast.makeText(MainActivity.this,
                    "Item Clicked: " + which + " - Selected ? " + isChecked, Toast.LENGTH_SHORT)
                    .show();
              }
            });
        break;
      default:
        break;
    }
    if (checkPositive.isChecked()) {
      builder.setPositiveButton("Yes", new TestClickListener(this));
    }
    if (checkNegative.isChecked()) {
      builder.setNegativeButton("No", new TestClickListener(this));
    }
    if (checkNeutral.isChecked()) {
      builder.setNeutralButton("Cancel", new TestClickListener(this));
    }
    builder.show();
  }

  @OnClick(R.id.button_material) void onBackportClicked() {
    MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
    if (!editTitle.getText().toString().trim().isEmpty()) {
      builder.setTitle(editTitle.getText().toString());
    }
    if (!editMessage.getText().toString().trim().isEmpty()) {
      builder.setMessage(editMessage.getText().toString());
    }
    switch (iconTypes.getSelectedItemPosition()) {
      case 1:
        builder.setIcon(android.R.drawable.ic_dialog_info);
        break;
      case 2:
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        break;
      default:
        break;
    }
    switch (listTypes.getSelectedItemPosition()) {
      case 1:
        builder.setItems(R.array.sample_items, new TestClickListener(this));
        break;
      case 2:
        builder.setSingleChoiceItems(R.array.sample_items, 0, new TestClickListener(this));
        break;
      case 3:
        builder.setMultiChoiceItems(R.array.sample_items, null,
            new DialogInterface.OnMultiChoiceClickListener() {
              @Override public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                Toast.makeText(MainActivity.this,
                    "Item Clicked: " + which + " - Selected ? " + isChecked, Toast.LENGTH_SHORT)
                    .show();
              }
            });
        break;
      default:
        break;
    }
    if (checkPositive.isChecked()) {
      builder.setPositiveButton("Yes", new TestClickListener(this));
    }
    if (checkNegative.isChecked()) {
      builder.setNegativeButton("No", new TestClickListener(this));
    }
    if (checkNeutral.isChecked()) {
      builder.setNeutralButton("Cancel", new TestClickListener(this));
    }
    builder.show();
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

  private static class TestClickListener implements DialogInterface.OnClickListener {

    private final Context context;

    public TestClickListener(Context context) {
      this.context = context;
    }

    @Override public void onClick(DialogInterface dialog, int which) {
      Toast.makeText(context, "Item Clicked: " + which, Toast.LENGTH_SHORT).show();
    }
  }
}
