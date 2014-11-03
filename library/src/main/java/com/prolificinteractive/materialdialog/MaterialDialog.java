package com.prolificinteractive.materialdialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
 * FIXME add some comments for people browsing
 */
public class MaterialDialog extends Dialog {

  private final LinearLayout topPanel;
  private final LinearLayout contentPanel;
  private final FrameLayout customPanel;
  private final LinearLayout buttonPanel;

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

  public MaterialDialog(Context context) {
    this(context, 0);
  }

  public MaterialDialog(Context context, int theme) {
    super(context, getDialogTheme(context, theme));
    getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.mdb__dialog);

    topPanel = (LinearLayout) findViewById(R.id.mdb__topPanel);
    contentPanel = (LinearLayout) findViewById(R.id.mdb__contentPanel);
    customPanel = (FrameLayout) findViewById(R.id.mdb__customPanel);
    buttonPanel = (LinearLayout) findViewById(R.id.mdb__buttonPanel);

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

  public ListView getListView() {
    return listView;
  }

  @Override public void setTitle(int titleId) {
    this.setTitle(getContext().getResources().getText(titleId));
  }

  @Override public void setTitle(CharSequence title) {
    this.title.setText(title);
    setTopPanelVisibility();
  }

  private void setTopPanelVisibility() {
    topPanel.setVisibility(TextUtils.isEmpty(title.getText()) ? View.GONE : View.VISIBLE);
  }

  public void setMessage(int messageId) {
    setMessage(getContext().getText(messageId));
  }

  public void setMessage(CharSequence message) {
    this.message.setText(message);
    setContentPanelsVisibility();
  }

  public void setView(View view) {
    customContainer.removeAllViews();
    if (view != null) {
      customContainer.addView(view);
    }
    setContentPanelsVisibility();
  }

  private void setContentPanelsVisibility() {
    boolean hasCustomView = customContainer.getChildCount() > 0;
    customPanel.setVisibility(hasCustomView ? View.VISIBLE : View.GONE);
    contentPanel.setVisibility(hasCustomView ? View.GONE : View.VISIBLE);
    if (!hasCustomView) {
      this.message.setVisibility(TextUtils.isEmpty(message.getText()) ? View.GONE : View.VISIBLE);
      listView.setVisibility(listView.getAdapter() != null ? View.VISIBLE : View.GONE);
    }
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

  public static final class Builder {

    private final Context mContext;
    private int mTheme = 0;
    private boolean cancelable = true;
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

    public Builder(Context context) {
      this(context, 0);
    }

    /**
     * @param context Context
     * @param theme Style resource to use for the dialog theme
     * @see com.prolificinteractive.materialdialog.R.style#MaterialDialog
     * @see com.prolificinteractive.materialdialog.R.style#MaterialDialog_Dark
     * @see com.prolificinteractive.materialdialog.R.style#MaterialDialog_Light
     */
    public Builder(Context context, int theme) {
      this.mTheme = getDialogTheme(context, theme);
      this.mContext = context;
    }

    public Context getContext() {
      return new ContextThemeWrapper(mContext, mTheme);
    }

    public MaterialDialog create() {
      final MaterialDialog dialog = new MaterialDialog(mContext, mTheme);
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
        } else if (listType == ListType.MULTI) {
          listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
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
      //Message and custom view cannot co-exist
      this.view = null;
      this.message = message;
      return this;
    }

    public Builder setView(View view) {
      //Message and custom view cannot co-exist
      this.message = null;
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

    public Builder setOnCancelListener(OnCancelListener onCancelListener) {
      this.onCancelListener = onCancelListener;
      return this;
    }

    public Builder setOnDismissListener(OnDismissListener onDismissListener) {
      this.onDismissListener = onDismissListener;
      return this;
    }

    public Builder setOnKeyListener(OnKeyListener onKeyListener) {
      this.onKeyListener = onKeyListener;
      return this;
    }

    public Builder setCancelable(boolean cancelable) {
      this.cancelable = cancelable;
      return this;
    }

    public Builder setItems(CharSequence[] items, OnClickListener listener) {
      return setAdapter(
          new ArrayAdapter<CharSequence>(mContext, android.R.layout.simple_list_item_1, items),
          listener
      );
    }

    public Builder setItems(int itemsId, OnClickListener listener) {
      return setItems(mContext.getResources().getTextArray(itemsId), listener);
    }

    public Builder setAdapter(ListAdapter adapter, OnClickListener listener) {
      this.listType = ListType.ITEMS;
      this.listAdapter = adapter;
      this.listListener = listener;
      return this;
    }

    public Builder setItems(CharSequence[] items, OnClickDelegate delegate) {
      return setAdapter(
          new ArrayAdapter<CharSequence>(mContext, android.R.layout.simple_list_item_1, items),
          delegate
      );
    }

    public Builder setItems(int itemsId, OnClickDelegate delegate) {
      return setItems(mContext.getResources().getTextArray(itemsId), delegate);
    }

    public Builder setAdapter(ListAdapter adapter, OnClickDelegate delegate) {
      this.listType = ListType.ITEMS;
      this.listAdapter = adapter;
      this.listDelegate = delegate;
      return this;
    }

    public Builder setSingleChoiceItems(CharSequence[] items, OnClickDelegate delegate) {
      return setSingleChoiceItems(
          new ArrayAdapter<CharSequence>(mContext, android.R.layout.simple_list_item_single_choice,
              items),
          delegate
      );
    }

    public Builder setSingleChoiceItems(int itemsId, OnClickDelegate delegate) {
      return setSingleChoiceItems(mContext.getResources().getTextArray(itemsId), delegate);
    }

    public Builder setSingleChoiceItems(ListAdapter adapter, OnClickDelegate delegate) {
      this.listType = ListType.SINGLE;
      this.listAdapter = adapter;
      this.listDelegate = delegate;
      return this;
    }

    public Builder setSingleChoiceItems(CharSequence[] items, OnClickListener delegate) {
      return setSingleChoiceItems(
          new ArrayAdapter<CharSequence>(mContext, android.R.layout.simple_list_item_single_choice,
              items),
          delegate
      );
    }

    public Builder setSingleChoiceItems(int itemsId, OnClickListener delegate) {
      return setSingleChoiceItems(mContext.getResources().getTextArray(itemsId), delegate);
    }

    public Builder setSingleChoiceItems(ListAdapter adapter, OnClickListener delegate) {
      this.listType = ListType.SINGLE;
      this.listAdapter = adapter;
      this.listListener = delegate;
      return this;
    }

    public Builder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems,
        OnMultiChoiceClickListener listener) {
      return setMultiChoiceItems(
          new ArrayAdapter<CharSequence>(mContext,
              android.R.layout.simple_list_item_multiple_choice, items),
          checkedItems,
          listener
      );
    }

    public Builder setMultiChoiceItems(int itemsId, boolean[] checkedItems,
        OnMultiChoiceClickListener listener) {
      return setMultiChoiceItems(mContext.getResources().getTextArray(itemsId), checkedItems,
          listener);
    }

    public Builder setMultiChoiceItems(ListAdapter adapter, boolean[] checkedItems,
        OnMultiChoiceClickListener listener) {
      this.listType = ListType.MULTI;
      this.listAdapter = adapter;
      this.listMultListener = listener;
      return this;
    }

    private static enum ListType {
      NONE, ITEMS, SINGLE, MULTI
    }
  }
}
