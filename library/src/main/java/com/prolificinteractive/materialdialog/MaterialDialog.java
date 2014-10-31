package com.prolificinteractive.materialdialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * FIXME add some comments for people browsing
 */
public class MaterialDialog extends Dialog {

  private final TextView title;
  private final TextView message;

  private final Button buttonPositive;
  private final Button buttonNegative;
  private final Button buttonNeutral;

  private final FrameLayout customContainer;

  public MaterialDialog(Context context) {
    this(context, 0);
  }

  public MaterialDialog(Context context, int theme) {
    super(context, getDialogTheme(context, theme));
    getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.dialog);

    title = (TextView) findViewById(R.id.mdb__title);
    message = (TextView) findViewById(R.id.mdb__message);

    customContainer = (FrameLayout) findViewById(R.id.mdb__custom_content);

    buttonPositive = (Button) findViewById(R.id.mdb__positive);
    buttonNegative = (Button) findViewById(R.id.mdb__negative);
    buttonNeutral = (Button) findViewById(R.id.mdb__neutral);
  }

  @Override public void setTitle(int titleId) {
    this.setTitle(getContext().getResources().getText(titleId));
  }

  @Override public void setTitle(CharSequence title) {
    this.title.setText(title);
  }

  public void setIcon(int iconId) {
    setIcon(getContext().getResources().getDrawable(iconId));
  }

  public void setIcon(Drawable icon) {
    //TODO set title icon?
  }

  public void setMessage(int messageId) {
    setMessage(getContext().getText(messageId));
  }

  public void setMessage(CharSequence message) {
    this.message.setText(message);
  }

  public void setView(View view) {
    customContainer.removeAllViews();
    customContainer.addView(view);
  }

  public void setButton(final int id, CharSequence buttonText, final OnClickListener listener) {
    Button button;
    switch (id) {
      case DialogInterface.BUTTON_POSITIVE:
        button = buttonPositive;
        break;
      case DialogInterface.BUTTON_NEGATIVE:
        button = buttonNegative;
        break;
      case DialogInterface.BUTTON_NEUTRAL:
        button = buttonNeutral;
        break;
      default:
        throw new IllegalArgumentException("ID needs to be DialogInterface.BUTTON_*");
    }

    button.setText(buttonText);
    button.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (listener != null) {
          listener.onClick(MaterialDialog.this, id);
        }
        dismiss();
      }
    });
  }

  private static int getDialogTheme(Context context, int theme) {
    if (theme == R.style.MaterialDialog_Light || theme == R.style.MaterialDialog_Dark) {
      return theme;
    }

    TypedValue out = new TypedValue();
    context.getTheme().resolveAttribute(R.attr.MaterialDialogTheme, out, false);
    if (out.type == TypedValue.TYPE_REFERENCE && out.data > 0) {
      return out.data;
    }

    return R.style.MaterialDialog_Light;
  }
}
