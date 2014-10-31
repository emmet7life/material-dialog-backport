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

  public static class Builder {

    private final Context mContext;
    private int mTheme = 0;
    private boolean mScrollable = false;
    private CharSequence title;
    private CharSequence message;
    private View view;

    private CharSequence positiveText;
    private OnClickListener positiveListener;
    private OnClickDelegate positiveDelegate;

    private CharSequence negativeText;
    private OnClickListener negativeListener;
    private OnClickDelegate negativeDelegate;

    private CharSequence neutralText;
    private OnClickListener neutralListener;
    private OnClickDelegate neutralDelegate;

    public Builder(Context context) {
      mContext = context;
    }

    public Builder(Context context, int theme) {
      this.mContext = context;
      this.mTheme = theme;
    }

    /**
     * Set the dialog to use scrollable variant
     *
     * @param scrollable use scrollable layout variant
     */
    public Builder setScrollable(boolean scrollable) {
      this.mScrollable = scrollable;
      return this;
    }

    /**
     * Set the theme of the dialog. Default is Light
     *
     * @param theme Style resource to use for the dialog theme
     * @see com.prolificinteractive.materialdialog.R.style#MaterialDialog
     * @see com.prolificinteractive.materialdialog.R.style#MaterialDialog_Dark
     * @see com.prolificinteractive.materialdialog.R.style#MaterialDialog_Light
     */
    public Builder setTheme(int theme) {
      this.mTheme = theme;
      return this;
    }

    public MaterialDialog create() {
      MaterialDialog dialog = new MaterialDialog(mContext, mTheme, mScrollable);
      if (title != null) {
        dialog.setTitle(title);
      }
      if (message != null) {
        dialog.setMessage(message);
      }
      if (view != null) {
        dialog.setView(view);
      }
      if (positiveText != null) {
        if (positiveDelegate != null) {
          dialog.setButton(DialogInterface.BUTTON_POSITIVE, positiveText, positiveDelegate);
        } else if (positiveListener != null) {
          dialog.setButton(DialogInterface.BUTTON_POSITIVE, positiveText, positiveListener);
        } else {
          dialog.setButton(DialogInterface.BUTTON_POSITIVE, positiveText);
        }
      }
      if (negativeText != null) {
        if (negativeDelegate != null) {
          dialog.setButton(DialogInterface.BUTTON_NEGATIVE, negativeText, negativeDelegate);
        } else if (negativeListener != null) {
          dialog.setButton(DialogInterface.BUTTON_NEGATIVE, negativeText, negativeListener);
        } else {
          dialog.setButton(DialogInterface.BUTTON_NEGATIVE, negativeText);
        }
      }
      if (neutralText != null) {
        if (neutralDelegate != null) {
          dialog.setButton(DialogInterface.BUTTON_NEUTRAL, neutralText, neutralDelegate);
        } else if (neutralListener != null) {
          dialog.setButton(DialogInterface.BUTTON_NEUTRAL, neutralText, neutralListener);
        } else {
          dialog.setButton(DialogInterface.BUTTON_NEUTRAL, neutralText);
        }
      }
      return dialog;
    }

    public MaterialDialog show() {
      MaterialDialog dialog = create();
      dialog.show();
      return dialog;
    }

    public Builder setTitle(int titleId) {
      return setTitle(mContext.getText(titleId));
    }

    public Builder setTitle(CharSequence title) {
      this.title = title;
      return this;
    }

    public Builder setMessage(int messageId) {
      return setMessage(mContext.getText(messageId));
    }

    public Builder setMessage(CharSequence message) {
      this.message = message;
      return this;
    }

    public Builder setView(View view) {
      this.view = view;
      return this;
    }

    public Builder setPositiveButton(int textId) {
      return setPositiveButton(mContext.getText(textId));
    }

    public Builder setPositiveButton(CharSequence text) {
      this.positiveText = text;
      return this;
    }

    public Builder setPositiveButton(CharSequence text, OnClickListener listener) {
      this.positiveText = text;
      this.positiveListener = listener;
      return this;
    }

    public Builder setPositiveButton(int textId, OnClickListener listener) {
      return setPositiveButton(mContext.getText(textId), listener);
    }

    public Builder setPositiveButton(CharSequence text, OnClickDelegate delegate) {
      this.positiveText = text;
      this.positiveDelegate = delegate;
      return this;
    }

    public Builder setPositiveButton(int textId, OnClickDelegate delegate) {
      return setPositiveButton(mContext.getText(textId), delegate);
    }

    public Builder setNegativeButton(int textId) {
      return setNegativeButton(mContext.getText(textId));
    }

    public Builder setNegativeButton(CharSequence text) {
      this.negativeText = text;
      return this;
    }

    public Builder setNegativeButton(CharSequence text, OnClickListener listener) {
      this.negativeText = text;
      this.negativeListener = listener;
      return this;
    }

    public Builder setNegativeButton(int textId, OnClickListener listener) {
      return setNegativeButton(mContext.getText(textId), listener);
    }

    public Builder setNegativeButton(CharSequence text, OnClickDelegate delegate) {
      this.negativeText = text;
      this.negativeDelegate = delegate;
      return this;
    }

    public Builder setNegativeButton(int textId, OnClickDelegate delegate) {
      return setNegativeButton(mContext.getText(textId), delegate);
    }

    public Builder setNeutralButton(int textId) {
      return setNeutralButton(mContext.getText(textId));
    }

    public Builder setNeutralButton(CharSequence text) {
      this.neutralText = text;
      return this;
    }

    public Builder setNeutralButton(CharSequence text, OnClickListener listener) {
      this.neutralText = text;
      this.neutralListener = listener;
      return this;
    }

    public Builder setNeutralButton(int textId, OnClickListener listener) {
      return setNeutralButton(mContext.getText(textId), listener);
    }

    public Builder setNeutralButton(CharSequence text, OnClickDelegate delegate) {
      this.neutralText = text;
      this.neutralDelegate = delegate;
      return this;
    }

    public Builder setNeutralButton(int textId, OnClickDelegate delegate) {
      return setNeutralButton(mContext.getText(textId), delegate);
    }
  }
}
