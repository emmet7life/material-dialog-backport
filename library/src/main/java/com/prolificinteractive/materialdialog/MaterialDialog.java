package com.prolificinteractive.materialdialog;

import android.app.Dialog;
import android.content.Context;

/**
 * FIXME add some comments for people browsing
 */
public class MaterialDialog extends Dialog {

  public MaterialDialog(Context context) {
    super(context);
  }

  public MaterialDialog(Context context, int theme) {
    super(context, theme);
  }

  protected MaterialDialog(Context context, boolean cancelable,
      OnCancelListener cancelListener) {
    super(context, cancelable, cancelListener);
  }
}
