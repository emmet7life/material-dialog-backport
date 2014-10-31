package com.prolificinteractive.materialdialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
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
    this(context, 0, false);
  }

  public MaterialDialog(Context context, int theme) {
    this(context, theme, false);
  }

  public MaterialDialog(Context context, boolean scrollable) {
    this(context, 0, scrollable);
  }

  public MaterialDialog(Context context, int theme, boolean scrollable) {
    super(context, getDialogTheme(context, theme));
    getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    setContentView(scrollable ? R.layout.mdb__dialog_scrollabe : R.layout.mdb__dialog);

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
    this.title.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);
  }

  public void setMessage(int messageId) {
    setMessage(getContext().getText(messageId));
  }

  public void setMessage(CharSequence message) {
    this.message.setText(message);
    this.message.setVisibility(TextUtils.isEmpty(message) ? View.GONE : View.VISIBLE);
  }

  public void setView(View view) {
    customContainer.removeAllViews();
    if (view != null) {
      customContainer.addView(view);
    }
    customContainer.setVisibility(view == null ? View.GONE : View.VISIBLE);
  }

  public void setButton(final int id, CharSequence buttonText) {
    setButton(id, buttonText, new View.OnClickListener() {
      @Override public void onClick(View v) {
        dismiss();
      }
    });
  }

  public void setButton(final int id, CharSequence buttonText, final OnClickListener listener) {
    setButton(id, buttonText, new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (listener != null) {
          listener.onClick(MaterialDialog.this, id);
        }
        dismiss();
      }
    });
  }

  public void setButton(final int id, CharSequence buttonText, final OnClickDelegate delegate) {
    setButton(id, buttonText, new View.OnClickListener() {
      @Override public void onClick(View v) {
        boolean wasHandled = false;
        if (delegate != null) {
          wasHandled = delegate.onClick(MaterialDialog.this, id);
        }
        if (!wasHandled) {
          dismiss();
        }
      }
    });
  }

  private void setButton(final int id, CharSequence buttonText,
      final View.OnClickListener listener) {
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

    button.setVisibility(View.VISIBLE);
    button.setText(buttonText);
    button.setOnClickListener(listener);
  }

  /**
   * Get a useable theme reference
   *
   * @param context Context that has the theme
   * @param theme Theme user specifies
   * @return A (hopefully) usable theme for our dialog
   */
  private static int getDialogTheme(Context context, int theme) {
    if (theme == 0) {

      TypedValue out = new TypedValue();
      context.getTheme().resolveAttribute(R.attr.MaterialDialogTheme, out, false);
      if (out.type == TypedValue.TYPE_REFERENCE && out.data > 0) {
        return out.data;
      }

      return R.style.MaterialDialog;
    }

    return theme;
  }

  /**
   * FIXME high level documentation needed
   */
  public static interface OnClickDelegate {
    /**
     * @param dialog the dialog with the button clicked
     * @param which Which button was clicked
     * @return if true, dialog will not automatically be dismissed
     */
    public boolean onClick(MaterialDialog dialog, int which);
  }
}
