package com.prolificinteractive.materialdialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * A subclass of Dialog that can display one, two or three buttons,
 * a title and content with a Material theme
 */
public class MaterialDialog extends Dialog {

  private final LinearLayout topPanel;
  private final LinearLayout contentPanel;
  private final FrameLayout customPanel;
  private final ViewGroup buttonPanel;

  private final ImageView icon;
  private final TextView title;
  private final TextView message;
  private final View noButtonSpacer;

  private final TextView buttonPositive;
  private final TextView buttonNegative;
  private final TextView buttonNeutral;

  private final ViewGroup customContainer;
  private final ListView listView;
  private final ScrollView scrollView;

  /**
   * Construct an MaterialDialog that uses the theme defined using
   * {@link com.prolificinteractive.materialdialog.R.attr#MaterialDialogTheme}
   * or defaulting to a light theme
   */
  public MaterialDialog(Context context) {
    this(context, 0);
  }

  /**
   * Construct an MaterialDialog that uses an explicit theme.
   * The Backport library defines both a light and dark themes.
   *
   * @see com.prolificinteractive.materialdialog.R.style#Theme_MaterialDialog_Light
   * @see com.prolificinteractive.materialdialog.R.style#Theme_MaterialDialog_Dark
   */
  public MaterialDialog(Context context, int theme) {
    super(context, getDialogTheme(context, theme));
    getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.mdb__dialog);

    topPanel = (LinearLayout) findViewById(R.id.mdb__topPanel);
    contentPanel = (LinearLayout) findViewById(R.id.mdb__contentPanel);
    customPanel = (FrameLayout) findViewById(R.id.mdb__customPanel);
    buttonPanel = (ViewGroup) findViewById(R.id.mdb__buttonPanel);

    icon = (ImageView) findViewById(android.R.id.icon);
    title = (TextView) findViewById(R.id.mdb__title);
    message = (TextView) findViewById(R.id.mdb__message);
    noButtonSpacer = findViewById(R.id.mdb__textSpacerNoButtons);

    customContainer = (ViewGroup) findViewById(R.id.mdb__custom);
    listView = (ListView) findViewById(R.id.mdb__list);
    scrollView = (ScrollView) findViewById(R.id.mdb__scroll);

    buttonPositive = (TextView) findViewById(android.R.id.button1);
    buttonNegative = (TextView) findViewById(android.R.id.button2);
    buttonNeutral = (TextView) findViewById(android.R.id.button3);
  }

  /**
   * Get the list view used in the dialog
   *
   * @return the ListView
   */
  public ListView getListView() {
    return listView;
  }

  /**
   * Set the title text for this dialog
   * @param titleId The resource id for the title
   */
  @Override public void setTitle(int titleId) {
    this.setTitle(getContext().getResources().getText(titleId));
  }

  /**
   * Set the title text for this dialog
   * @param title The new text for the title
   */
  @Override public void setTitle(CharSequence title) {
    this.title.setText(title);
    setTopPanelVisibility();
  }

  /**
   * Correctly set top panel visibilities
   */
  private void setTopPanelVisibility() {
    icon.setVisibility(icon.getDrawable() != null ? View.VISIBLE : View.GONE);
    title.setVisibility(TextUtils.isEmpty(title.getText()) ? View.GONE : View.VISIBLE);
    topPanel.setVisibility(
        icon.getVisibility() == View.VISIBLE || title.getVisibility() == View.VISIBLE ?
            View.VISIBLE : View.GONE
    );
  }

  /**
   * Display a message as the dialog contents
   * @param messageId the resource id for the message
   */
  public void setMessage(int messageId) {
    setMessage(getContext().getText(messageId));
  }

  /**
   * Display a message as the dialog contents
   * @param message the new text for the message
   */
  public void setMessage(CharSequence message) {
    this.message.setText(message);
    setContentPanelsVisibility();
  }

  /**
   * Set resId to 0 if you don't want an icon
   * @param iconId the resourceId of the drawable to use as the icon or 0 if you don't want an icon
   */
  public void setIcon(int iconId) {
    if (iconId == 0) {
      setIcon(null);
    } else {
      setIcon(getContext().getResources().getDrawable(iconId));
    }
  }

  /**
   * @param icon the icon to display, null if none
   */
  public void setIcon(Drawable icon) {
    this.icon.setImageDrawable(icon);
    setTopPanelVisibility();
  }

  /**
   * Set the view to display in that dialog
   * @param view the View
   */
  public void setView(View view) {
    customContainer.removeAllViews();
    if (view != null) {
      customContainer.addView(view);
    }
    setContentPanelsVisibility();
  }

  /**
   * Correctly set visibility of content
   */
  private void setContentPanelsVisibility() {
    boolean hasCustomView = customContainer.getChildCount() > 0;
    customPanel.setVisibility(hasCustomView ? View.VISIBLE : View.GONE);
    contentPanel.setVisibility(hasCustomView ? View.GONE : View.VISIBLE);
    if (!hasCustomView) {
      this.message.setVisibility(TextUtils.isEmpty(message.getText()) ? View.GONE : View.VISIBLE);
      listView.setVisibility(listView.getAdapter() != null ? View.VISIBLE : View.GONE);
    }
  }

  /**
   * Set the text of a button which will dismiss the dialog when clicked
   *
   * @param id Which button to set the text for, can be one of
   * {@link DialogInterface#BUTTON_POSITIVE},
   * {@link DialogInterface#BUTTON_NEGATIVE}, or
   * {@link DialogInterface#BUTTON_NEUTRAL}
   * @param buttonText The text to display for the button.
   */
  public void setButton(final int id, CharSequence buttonText) {
    setButton(id, buttonText, new View.OnClickListener() {
      @Override public void onClick(View v) {
        dismiss();
      }
    });
  }

  /**
   * Set a listener to be invoked when the indicated button of the dialog is pressed.
   * Dialog will be dismissed after listener is called
   *
   * @param id Which button to set the text for, can be one of
   *            {@link DialogInterface#BUTTON_POSITIVE},
   *            {@link DialogInterface#BUTTON_NEGATIVE}, or
   *            {@link DialogInterface#BUTTON_NEUTRAL}
   * @param buttonText The text to display for the button.
   * @param listener Click listener, can be null
   */
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

  /**
   * Set a listener to be invoked when the indicated button of the dialog is pressed.
   * Dialog will dismiss if delegate is null or it returns false.
   *
   * @param id Which button to set the text for, can be one of
   *            {@link DialogInterface#BUTTON_POSITIVE},
   *            {@link DialogInterface#BUTTON_NEGATIVE}, or
   *            {@link DialogInterface#BUTTON_NEUTRAL}
   * @param buttonText The text to display for the button.
   * @param delegate Click delegate, can be null
   */
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

  /**
   * Set a listener to be invoked when the indicated button of the dialog is pressed.
   * Dialog will dismiss if delegate is null or it returns false.
   *
   * @param id Which button to set the text for, can be one of
   *            {@link DialogInterface#BUTTON_POSITIVE},
   *            {@link DialogInterface#BUTTON_NEGATIVE}, or
   *            {@link DialogInterface#BUTTON_NEUTRAL}
   * @param buttonText The text to display for the button.
   * @param listener View.OnClickListener for the button
   */
  private void setButton(final int id, CharSequence buttonText,
      final View.OnClickListener listener) {
    TextView button;
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

    buttonPanel.setVisibility(View.VISIBLE);
    noButtonSpacer.setVisibility(View.GONE);
    button.setVisibility(View.VISIBLE);
    button.setText(buttonText);
    button.setOnClickListener(listener);
  }

  /**
   * Get a usable theme reference
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

      return R.style.Theme_MaterialDialog;
    }

    return theme;
  }

  /**
   * Interface used to allow the creator of a dialog to run some code when an item on
   * the dialog is clicked, and determine if the dialog is to be dismissed
   */
  public static interface OnClickDelegate {
    /**
     * @param dialog the dialog with the button clicked
     * @param which Which button was clicked
     * @return if true, dialog will not automatically be dismissed
     */
    public boolean onClick(MaterialDialog dialog, int which);
  }

  /**
   * Builder for {@link com.prolificinteractive.materialdialog.MaterialDialog}
   */
  public static final class Builder {

    private final Context mContext;
    private int mTheme = 0;
    private boolean cancelable = true;
    private Drawable icon;
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

    private OnCancelListener onCancelListener;
    private OnDismissListener onDismissListener;
    private OnKeyListener onKeyListener;

    private ListType listType = ListType.NONE;
    private OnClickListener listListener;
    private OnClickDelegate listDelegate;
    private ListAdapter listAdapter;
    private OnMultiChoiceClickListener listMultListener;

    private boolean[] checkedItems = null;
    private int checkedItem = -1;

    /**
     * Constructor using a context for this builder and the
     * {@link com.prolificinteractive.materialdialog.MaterialDialog} it creates
     */
    public Builder(Context context) {
      this(context, 0);
    }

    /**
     * Constructor using a context and theme for this builder and the
     * {@link com.prolificinteractive.materialdialog.MaterialDialog} it creates
     *
     * @param context Context
     * @param theme Style resource to use for the dialog theme
     * @see com.prolificinteractive.materialdialog.R.style#Theme_MaterialDialog
     * @see com.prolificinteractive.materialdialog.R.style#Theme_MaterialDialog_Dark
     * @see com.prolificinteractive.materialdialog.R.style#Theme_MaterialDialog_Light
     */
    public Builder(Context context, int theme) {
      this.mTheme = getDialogTheme(context, theme);
      this.mContext = context;
    }

    /**
     * Returns a Context with the appropriate theme for dialogs created by this Builder.
     * Applications should use this Context for obtaining LayoutInflaters for inflating views
     * that will be used in the resulting dialogs, as it will cause views to be inflated with the correct theme
     * @return A Context for built Dialogs
     */
    public Context getContext() {
      return new ContextThemeWrapper(mContext, mTheme);
    }

    /**
     * Creates a {@link com.prolificinteractive.materialdialog.MaterialDialog}
     * with the arguments supplied to this builder.
     * It does not {@linkplain #show()} the dialog.
     * This allows the user to do any extra processing before displaying the dialog.
     * Use {@linkplain #show()} if you don't have any other processing
     * to do and want this to be created and displayed.
     */
    public MaterialDialog create() {
      final MaterialDialog dialog = new MaterialDialog(mContext, mTheme);
      if (icon != null) {
        dialog.setIcon(icon);
      }
      if (title != null) {
        dialog.setTitle(title);
      }
      if (message != null) {
        dialog.setMessage(message);
      }
      if (view != null) {
        dialog.setView(view);
      }

      if (listAdapter != null) {
        final ListView listView = dialog.getListView();
        listView.setAdapter(listAdapter);
        if (listType == ListType.SINGLE) {
          listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
          listView.setItemChecked(checkedItem, true);
        } else if (listType == ListType.MULTI) {
          listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
          if (checkedItems != null) {
            for (int i = 0; i < checkedItems.length; i++) {
              listView.setItemChecked(i, checkedItems[i]);
            }
          }
        } else {
          listView.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
        }
        if (listType == ListType.MULTI) {
          listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
              if (listMultListener != null) {
                listMultListener.onClick(dialog, position, listView.isItemChecked(position));
              }
            }
          });
        } else {
          listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
              boolean handled = listType == ListType.ITEMS;
              if (listListener != null) {
                listListener.onClick(dialog, position);
                dialog.dismiss();
              } else if (listDelegate != null) {
                handled = listDelegate.onClick(dialog, position);
              }
              if (!handled) {
                dialog.dismiss();
              }
            }
          });
        }
      }
      dialog.setContentPanelsVisibility();

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

      dialog.setOnCancelListener(onCancelListener);
      dialog.setOnDismissListener(onDismissListener);
      dialog.setOnKeyListener(onKeyListener);
      dialog.setCancelable(cancelable);

      return dialog;
    }

    /**
     * Creates a {@link com.prolificinteractive.materialdialog.MaterialDialog} with the
     * arguments supplied to this builder and {@linkplain #show()}'s the dialog
     */
    public MaterialDialog show() {
      MaterialDialog dialog = create();
      dialog.show();
      return dialog;
    }

    /**
     * Set a title for the dialog
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setTitle(int titleId) {
      return setTitle(mContext.getText(titleId));
    }

    /**
     * Set a title for the dialog
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setTitle(CharSequence title) {
      this.title = title;
      return this;
    }

    /**
     * Set a message to show as the dialog's content
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setMessage(int messageId) {
      return setMessage(mContext.getText(messageId));
    }

    /**
     * Set a message to show as the dialog's content
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setMessage(CharSequence message) {
      //Message and custom view cannot co-exist
      this.view = null;
      this.message = message;
      return this;
    }

    /**
     * Set an icon to show next to the dialog's title
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setIcon(int iconId) {
      return setIcon(mContext.getResources().getDrawable(iconId));
    }

    /**
     * Set an icon to show next to the dialog's title
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setIcon(Drawable icon) {
      this.icon = icon;
      return this;
    }

    /**
     * Set the custom view for the dialog.
     * This will replace any message or list.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setView(View view) {
      //Message and custom view cannot co-exist
      this.message = null;
      this.view = view;
      return this;
    }

    /**
     * Set a listener to be invoked when the positive button of the dialog is pressed
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setPositiveButton(int textId) {
      return setPositiveButton(mContext.getText(textId));
    }

    /**
     * Set a listener to be invoked when the positive button of the dialog is pressed
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setPositiveButton(CharSequence text) {
      this.positiveText = text;
      return this;
    }

    /**
     * Set a listener to be invoked when the positive button of the dialog is pressed
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setPositiveButton(CharSequence text, OnClickListener listener) {
      this.positiveText = text;
      this.positiveListener = listener;
      return this;
    }

    /**
     * Set a listener to be invoked when the positive button of the dialog is pressed
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setPositiveButton(int textId, OnClickListener listener) {
      return setPositiveButton(mContext.getText(textId), listener);
    }

    /**
     * Set a delegate to be invoked when the positive button of the dialog is pressed
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setPositiveButton(CharSequence text, OnClickDelegate delegate) {
      this.positiveText = text;
      this.positiveDelegate = delegate;
      return this;
    }

    /**
     * Set a delegate to be invoked when the positive button of the dialog is pressed
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setPositiveButton(int textId, OnClickDelegate delegate) {
      return setPositiveButton(mContext.getText(textId), delegate);
    }

    /**
     * Set a listener to be invoked when the negative button of the dialog is pressed
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setNegativeButton(int textId) {
      return setNegativeButton(mContext.getText(textId));
    }

    /**
     * Set a listener to be invoked when the negative button of the dialog is pressed
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setNegativeButton(CharSequence text) {
      this.negativeText = text;
      return this;
    }

    /**
     * Set a listener to be invoked when the negative button of the dialog is pressed
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setNegativeButton(CharSequence text, OnClickListener listener) {
      this.negativeText = text;
      this.negativeListener = listener;
      return this;
    }

    /**
     * Set a listener to be invoked when the negative button of the dialog is pressed
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setNegativeButton(int textId, OnClickListener listener) {
      return setNegativeButton(mContext.getText(textId), listener);
    }

    /**
     * Set a delegate to be invoked when the negative button of the dialog is pressed
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setNegativeButton(CharSequence text, OnClickDelegate delegate) {
      this.negativeText = text;
      this.negativeDelegate = delegate;
      return this;
    }

    /**
     * Set a delegate to be invoked when the negative button of the dialog is pressed
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setNegativeButton(int textId, OnClickDelegate delegate) {
      return setNegativeButton(mContext.getText(textId), delegate);
    }

    /**
     * Set a listener to be invoked when the neutral button of the dialog is pressed
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setNeutralButton(int textId) {
      return setNeutralButton(mContext.getText(textId));
    }

    /**
     * Set a listener to be invoked when the neutral button of the dialog is pressed
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setNeutralButton(CharSequence text) {
      this.neutralText = text;
      return this;
    }

    /**
     * Set a listener to be invoked when the neutral button of the dialog is pressed
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setNeutralButton(CharSequence text, OnClickListener listener) {
      this.neutralText = text;
      this.neutralListener = listener;
      return this;
    }

    /**
     * Set a listener to be invoked when the neutral button of the dialog is pressed
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setNeutralButton(int textId, OnClickListener listener) {
      return setNeutralButton(mContext.getText(textId), listener);
    }

    /**
     * Set a delegate to be invoked when the neutral button of the dialog is pressed
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setNeutralButton(CharSequence text, OnClickDelegate delegate) {
      this.neutralText = text;
      this.neutralDelegate = delegate;
      return this;
    }

    /**
     * Set a delegate to be invoked when the neutral button of the dialog is pressed
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setNeutralButton(int textId, OnClickDelegate delegate) {
      return setNeutralButton(mContext.getText(textId), delegate);
    }

    /**
     * Set the dialog's {@link android.content.DialogInterface.OnCancelListener}
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setOnCancelListener(OnCancelListener onCancelListener) {
      this.onCancelListener = onCancelListener;
      return this;
    }

    /**
     * Set the dialog's {@link android.content.DialogInterface.OnDismissListener}
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setOnDismissListener(OnDismissListener onDismissListener) {
      this.onDismissListener = onDismissListener;
      return this;
    }

    /**
     * Set the dialog's {@link android.content.DialogInterface.OnKeyListener}
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setOnKeyListener(OnKeyListener onKeyListener) {
      this.onKeyListener = onKeyListener;
      return this;
    }

    /**
     * Set the dialog as cancelable, or not. Default is cancelable
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setCancelable(boolean cancelable) {
      this.cancelable = cancelable;
      return this;
    }

    /**
     * Set a list of items to be displayed in the dialog as the content,
     * you will be notified of the selected item via the supplied listener.
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setItems(CharSequence[] items, OnClickListener listener) {
      return setAdapter(
          new ArrayAdapter<CharSequence>(mContext, android.R.layout.simple_list_item_1, items),
          listener
      );
    }

    /**
     * Set a list of items to be displayed in the dialog as the content,
     * you will be notified of the selected item via the supplied listener.
     * This should be an array type i.e. R.array.foo
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setItems(int itemsId, OnClickListener listener) {
      return setItems(mContext.getResources().getTextArray(itemsId), listener);
    }

    /**
     * Set a list of items, which are supplied by the given {@link android.widget.ListAdapter},
     * to be displayed in the dialog as the content,
     * you will be notified of the selected item via the supplied listener.
     *
     * @param adapter the {@link android.widget.ListAdapter} to supply items
     * @param listener The listener that will be called when an item is clicked
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setAdapter(ListAdapter adapter, OnClickListener listener) {
      this.listType = ListType.ITEMS;
      this.listAdapter = adapter;
      this.listListener = listener;
      return this;
    }

    /**
     * Set a list of items to be displayed in the dialog as the content,
     * you will be notified of the selected item via the supplied delegate.
     * This should be an array type i.e. R.array.foo
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setItems(CharSequence[] items, OnClickDelegate delegate) {
      return setAdapter(
          new ArrayAdapter<CharSequence>(mContext, android.R.layout.simple_list_item_1, items),
          delegate
      );
    }

    /**
     * Set a list of items to be displayed in the dialog as the content,
     * you will be notified of the selected item via the supplied delegate
     *
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setItems(int itemsId, OnClickDelegate delegate) {
      return setItems(mContext.getResources().getTextArray(itemsId), delegate);
    }


    /**
     * Set a list of items, which are supplied by the given {@link android.widget.ListAdapter},
     * to be displayed in the dialog as the content,
     * you will be notified of the selected item via the supplied delegate.
     *
     * @param adapter the {@link android.widget.ListAdapter} to supply items
     * @param delegate The delegate that will be called when an item is clicked
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setAdapter(ListAdapter adapter, OnClickDelegate delegate) {
      this.listType = ListType.ITEMS;
      this.listAdapter = adapter;
      this.listDelegate = delegate;
      return this;
    }

    /**
     * Set a list of items to be displayed in the dialog as the content,
     * you will be notified of the selected item via the supplied listener.
     * The list will have a check mark displayed to the right of the text for the checked item.
     * Clicking on an item in the list will not dismiss the dialog.
     *
     * @param items items to display
     * @param checkedItem specifies the initially checked item, -1 for none
     * @param delegate notified when an item on the list is clicked
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setSingleChoiceItems(CharSequence[] items, int checkedItem,
        OnClickDelegate delegate) {
      return setSingleChoiceItems(
          new ArrayAdapter<CharSequence>(mContext, android.R.layout.simple_list_item_single_choice,
              items),
          checkedItem,
          delegate
      );
    }

    /**
     * Set a list of items to be displayed in the dialog as the content,
     * you will be notified of the selected item via the supplied listener.
     * The list will have a check mark displayed to the right of the text for the checked item.
     * Clicking on an item in the list will not dismiss the dialog.
     *
     * @param itemsId items to display
     * @param checkedItem specifies the initially checked item, -1 for none
     * @param delegate notified when an item on the list is clicked
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setSingleChoiceItems(int itemsId, int checkedItem, OnClickDelegate delegate) {
      return setSingleChoiceItems(mContext.getResources().getTextArray(itemsId), checkedItem,
          delegate);
    }

    /**
     * Set a list of items to be displayed in the dialog as the content,
     * you will be notified of the selected item via the supplied listener.
     * The list will have a check mark displayed to the right of the text for the checked item.
     * Clicking on an item in the list will not dismiss the dialog.
     *
     * @param adapter items to display
     * @param checkedItem specifies the initially checked item, -1 for none
     * @param delegate notified when an item on the list is clicked
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setSingleChoiceItems(ListAdapter adapter, int checkedItem,
        OnClickDelegate delegate) {
      this.listType = ListType.SINGLE;
      this.listAdapter = adapter;
      this.listDelegate = delegate;
      this.checkedItem = checkedItem;
      return this;
    }

    /**
     * Set a list of items to be displayed in the dialog as the content,
     * you will be notified of the selected item via the supplied listener.
     * The list will have a check mark displayed to the right of the text for the checked item.
     * Clicking on an item in the list will not dismiss the dialog.
     *
     * @param items items to display
     * @param checkedItem specifies the initially checked item, -1 for none
     * @param listener notified when an item on the list is clicked
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setSingleChoiceItems(CharSequence[] items, int checkedItem,
        OnClickListener listener) {
      return setSingleChoiceItems(
          new ArrayAdapter<CharSequence>(mContext, android.R.layout.simple_list_item_single_choice,
              items),
          checkedItem,
          listener
      );
    }

    /**
     * Set a list of items to be displayed in the dialog as the content,
     * you will be notified of the selected item via the supplied listener.
     * The list will have a check mark displayed to the right of the text for the checked item.
     * Clicking on an item in the list will not dismiss the dialog.
     *
     * @param itemsId items to display
     * @param checkedItem specifies the initially checked item, -1 for none
     * @param listener notified when an item on the list is clicked
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setSingleChoiceItems(int itemsId, int checkedItem, OnClickListener listener) {
      return setSingleChoiceItems(mContext.getResources().getTextArray(itemsId), checkedItem,
          listener);
    }

    /**
     * Set a list of items to be displayed in the dialog as the content,
     * you will be notified of the selected item via the supplied listener.
     * The list will have a check mark displayed to the right of the text for the checked item.
     * Clicking on an item in the list will not dismiss the dialog.
     *
     * @param adapter items to display
     * @param checkedItem specifies the initially checked item, -1 for none
     * @param listener notified when an item on the list is clicked
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setSingleChoiceItems(ListAdapter adapter, int checkedItem,
        OnClickListener listener) {
      this.listType = ListType.SINGLE;
      this.listAdapter = adapter;
      this.listListener = listener;
      this.checkedItem = checkedItem;
      return this;
    }

    /**
     * Set a list of items to be displayed in the dialog as the content,
     * you will be notified of the selected item via the supplied listener.
     * The list will have a check mark displayed to the right of the text for each checked item.
     * Clicking on an item in the list will not dismiss the dialog.
     *
     * @param items items to display
     * @param checkedItems specifies which items are checked.
     * It should be null in which case no items are checked.
     * If non null it must be exactly the same length as the array of items
     * @param listener notified when an item on the list is clicked.
     * The dialog will not be dismissed when an item is clicked.
     * It will only be dismissed if clicked on a button,
     * if no buttons are supplied it's up to the user to dismiss the dialog
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems,
        OnMultiChoiceClickListener listener) {
      return setMultiChoiceItems(
          new ArrayAdapter<CharSequence>(mContext,
              android.R.layout.simple_list_item_multiple_choice, items),
          checkedItems,
          listener
      );
    }


    /**
     * Set a list of items to be displayed in the dialog as the content,
     * you will be notified of the selected item via the supplied listener.
     * The list will have a check mark displayed to the right of the text for each checked item.
     * Clicking on an item in the list will not dismiss the dialog.
     *
     * @param itemsId items to display, e.g. R.array.foo
     * @param checkedItems specifies which items are checked.
     * It should be null in which case no items are checked.
     * If non null it must be exactly the same length as the array of items
     * @param listener notified when an item on the list is clicked.
     * The dialog will not be dismissed when an item is clicked.
     * It will only be dismissed if clicked on a button,
     * if no buttons are supplied it's up to the user to dismiss the dialog
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setMultiChoiceItems(int itemsId, boolean[] checkedItems,
        OnMultiChoiceClickListener listener) {
      return setMultiChoiceItems(mContext.getResources().getTextArray(itemsId), checkedItems,
          listener);
    }


    /**
     * Set a list of items to be displayed in the dialog as the content,
     * you will be notified of the selected item via the supplied listener.
     * The list will have a check mark displayed to the right of the text for each checked item.
     * Clicking on an item in the list will not dismiss the dialog.
     *
     * @param adapter adapter to pull items from
     * @param checkedItems specifies which items are checked.
     * It should be null in which case no items are checked.
     * If non null it must be exactly the same length as the array of items
     * @param listener notified when an item on the list is clicked.
     * The dialog will not be dismissed when an item is clicked.
     * It will only be dismissed if clicked on a button,
     * if no buttons are supplied it's up to the user to dismiss the dialog
     * @return This Builder object to allow for chaining of calls to set methods
     */
    public Builder setMultiChoiceItems(ListAdapter adapter, boolean[] checkedItems,
        OnMultiChoiceClickListener listener) {
      this.listType = ListType.MULTI;
      this.listAdapter = adapter;
      this.listMultListener = listener;
      this.checkedItems = checkedItems;
      return this;
    }

    /**
     * Type of list selection method to implement
     */
    private static enum ListType {
      NONE, ITEMS, SINGLE, MULTI
    }
  }
}
