package me.himanshusoni.quantityview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Quantity view to add and remove quantities
 */
public class QuantityView extends LinearLayout implements View.OnClickListener {
    private Drawable drawableAddButonIcon;
    private Drawable drawableRemoveButonIcon;
    private Drawable quantityBackground, addButtonBackground, removeButtonBackground;
    private String addButtonText, removeButtonText;

    private int quantity;
    private boolean quantityDialog;
    private int maxQuantity = Integer.MAX_VALUE, minQuantity = Integer.MAX_VALUE;
    private int quantityPadding;
    private int quantityButtonIconSize;
    private float quantityTextSize;
    private int quantityTextColor, addButtonTextColor, removeButtonTextColor;

    private Button mButtonAdd, mButtonRemove;
    private TextView mTextViewQuantity;

    private String labelDialogTitle = "Change Quantity";
    private String labelPositiveButton = "Change";
    private String labelNegativeButton = "Cancel";

    public interface OnQuantityChangeListener {
        /**
         * when the new quantity is ready for the change, the return should be TRUE
         *
         * @param com_id           the component id
         * @param oldQuantity      the old value
         * @param newQuantity      the new value
         * @param programmatically is this controlled by api
         * @return TRUE when the new value is ready for the change
         */
        boolean onQuantityChanged(int com_id, int oldQuantity, int newQuantity, boolean programmatically);

        void onLimitReached(int bound_int);
    }

    private OnQuantityChangeListener onQuantityChangeListener;
    private OnClickListener mTextViewClickListener;

    public QuantityView(Context context) {
        super(context);
        init(null, 0);
    }

    public QuantityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public QuantityView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.QuantityView, defStyle, 0);

        addButtonText = getResources().getString(R.string.qv_add);
        if (a.hasValue(R.styleable.QuantityView_qv_addButtonText)) {
            addButtonText = a.getString(R.styleable.QuantityView_qv_addButtonText);
        }
        addButtonBackground = ContextCompat.getDrawable(getContext(), R.drawable.qv_btn_selector);
        if (a.hasValue(R.styleable.QuantityView_qv_addButtonBackground)) {
            addButtonBackground = a.getDrawable(R.styleable.QuantityView_qv_addButtonBackground);
        }
        addButtonTextColor = a.getColor(R.styleable.QuantityView_qv_addButtonTextColor, Color.BLACK);
        removeButtonText = getResources().getString(R.string.qv_remove);
        if (a.hasValue(R.styleable.QuantityView_qv_removeButtonText)) {
            removeButtonText = a.getString(R.styleable.QuantityView_qv_removeButtonText);
        }
        if (a.hasValue(R.styleable.QuantityView_qv_addButtonIcon)) {
            drawableAddButonIcon = a.getDrawable(R.styleable.QuantityView_qv_addButtonIcon);
        }

        removeButtonBackground = ContextCompat.getDrawable(getContext(), R.drawable.qv_btn_selector);
        if (a.hasValue(R.styleable.QuantityView_qv_removeButtonBackground)) {
            removeButtonBackground = a.getDrawable(R.styleable.QuantityView_qv_removeButtonBackground);
        }
        removeButtonTextColor = a.getColor(R.styleable.QuantityView_qv_removeButtonTextColor, Color.BLACK);
        if (a.hasValue(R.styleable.QuantityView_qv_removeButtonIcon)) {
            drawableRemoveButonIcon = a.getDrawable(R.styleable.QuantityView_qv_removeButtonIcon);
        }
        quantity = a.getInt(R.styleable.QuantityView_qv_quantity, 0);
        maxQuantity = a.getInt(R.styleable.QuantityView_qv_maxQuantity, Integer.MAX_VALUE);
        minQuantity = a.getInt(R.styleable.QuantityView_qv_minQuantity, 0);

        quantityPadding = (int) a.getDimension(R.styleable.QuantityView_qv_quantityPadding, pxFromDp(16));
        quantityButtonIconSize = (int) a.getDimension(R.styleable.QuantityView_qv_controlButtonIconSize, pxFromDp(16));
        quantityTextColor = a.getColor(R.styleable.QuantityView_qv_quantityTextColor, Color.BLACK);
        quantityBackground = ContextCompat.getDrawable(getContext(), R.drawable.qv_bg_selector);
        if (a.hasValue(R.styleable.QuantityView_qv_quantityBackground)) {
            quantityBackground = a.getDrawable(R.styleable.QuantityView_qv_quantityBackground);
        }
        quantityDialog = a.getBoolean(R.styleable.QuantityView_qv_quantityDialog, true);
        if (a.hasValue(R.styleable.QuantityView_qv_quantityTextSize)) {
            quantityTextSize = a.getDimensionPixelOffset(R.styleable.QuantityView_qv_quantityTextSize, 16);
        }
        int quantityButtonsPadding = (int) a.getDimension(R.styleable.QuantityView_qv_quantityButtonsPadding, pxFromDp(16));
        a.recycle();
        mButtonAdd = new Button(getContext());
        mButtonAdd.setGravity(Gravity.CENTER);
        mButtonAdd.setPadding(quantityButtonsPadding, quantityButtonsPadding, quantityButtonsPadding, quantityButtonsPadding);
        mButtonAdd.setMinimumHeight(0);
        mButtonAdd.setMinimumWidth(0);
        mButtonAdd.setMinHeight(0);
        mButtonAdd.setMinWidth(0);
        mButtonAdd.setTypeface(Typeface.DEFAULT_BOLD);
        if (drawableAddButonIcon != null) {
            setDrawableAddButonIcon(drawableAddButonIcon);
        } else {
            setAddButtonText(addButtonText);
        }
        setAddButtonBackground(addButtonBackground);
        setAddButtonTextColor(addButtonTextColor);

        mButtonRemove = new Button(getContext());
        mButtonRemove.setGravity(Gravity.CENTER);
        mButtonRemove.setPadding(quantityButtonsPadding, quantityButtonsPadding, quantityButtonsPadding, quantityButtonsPadding);
        mButtonRemove.setMinimumHeight(0);
        mButtonRemove.setMinimumWidth(0);
        mButtonRemove.setMinHeight(0);
        mButtonRemove.setMinWidth(0);
        mButtonRemove.setTextSize(16);
        mButtonRemove.setTypeface(Typeface.DEFAULT_BOLD);
        if (drawableRemoveButonIcon != null) {
            setDrawableRemoveButonIcon(drawableRemoveButonIcon);
        } else {
            setRemoveButtonText(removeButtonText);
        }
        setRemoveButtonBackground(removeButtonBackground);
        setRemoveButtonTextColor(removeButtonTextColor);

        mTextViewQuantity = new TextView(getContext());
        mTextViewQuantity.setGravity(Gravity.CENTER);
        setQuantityTextSize(quantityTextSize);
        setQuantityTextColor(quantityTextColor);
        setQuantity(quantity);
        setQuantityBackground(quantityBackground);
        setQuantityPadding(quantityPadding);

        setOrientation(HORIZONTAL);

        addView(mButtonRemove, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addView(mTextViewQuantity, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addView(mButtonAdd, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        setGravity(Gravity.CENTER_VERTICAL);
        mButtonAdd.setOnClickListener(this);
        mButtonRemove.setOnClickListener(this);
        mTextViewQuantity.setOnClickListener(this);

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.height = LayoutParams.WRAP_CONTENT;
        lp.width = LayoutParams.WRAP_CONTENT;
        setLayoutParams(lp);
    }

    public void setQuantityTextSize(float size) {
        quantityTextSize = size;
        mTextViewQuantity.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        // mTextViewQuantity.setTextSize(size);
    }

    public void setDrawableAddButonIcon(Drawable drawableAddButonIcon) {
        //drawableAddButonIcon.setBounds(0, 0, quantityButtonIconSize, quantityButtonIconSize);
        ScaleDrawable scaleDrawable = new ScaleDrawable(drawableAddButonIcon, 0, quantityButtonIconSize, quantityButtonIconSize);
        scaleDrawable.setBounds(0, 0, quantityButtonIconSize, quantityButtonIconSize);
        mButtonAdd.setCompoundDrawablesWithIntrinsicBounds(drawableAddButonIcon, null, null, null);
    }

    public void setDrawableRemoveButonIcon(Drawable drawableRemoveButonIcon) {
        //drawableRemoveButonIcon.setBounds(0, 0, quantityButtonIconSize, quantityButtonIconSize);
        ScaleDrawable scaleDrawable = new ScaleDrawable(drawableRemoveButonIcon, 0, quantityButtonIconSize, quantityButtonIconSize);
        scaleDrawable.setBounds(0, 0, quantityButtonIconSize, quantityButtonIconSize);
        mButtonRemove.setCompoundDrawablesWithIntrinsicBounds(drawableRemoveButonIcon, null, null, null);
    }

    public void setQuantityClickListener(OnClickListener ocl) {
        mTextViewClickListener = ocl;
    }


    private void quantity_level(int oldQty, int preNewQty, boolean reach_bound, int bound_qty) {
        if (onQuantityChangeListener != null && reach_bound) {
            onQuantityChangeListener.onLimitReached(bound_qty);
        }

        if (onQuantityChangeListener != null) {
            if (onQuantityChangeListener.onQuantityChanged(getId(), oldQty, preNewQty, false)) {
                quantity = preNewQty;
                mTextViewQuantity.setText(String.valueOf(preNewQty));
            }
        } else {
            quantity = preNewQty;
            mTextViewQuantity.setText(String.valueOf(preNewQty));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mButtonAdd) {
            //  int oldQty = quantity;
            int preNewQty = quantity + 1;
            quantity_level(quantity, quantity + 1, preNewQty > maxQuantity, maxQuantity);

        } else if (v == mButtonRemove) {
            // int oldQty = quantity;
            int preNewQty = quantity - 1;
            quantity_level(quantity, quantity - 1, preNewQty < minQuantity, minQuantity);
        } else if (v == mTextViewQuantity) {
            if (!quantityDialog) return;

            if (mTextViewClickListener != null) {
                mTextViewClickListener.onClick(v);
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(labelDialogTitle);

            final View inflate = LayoutInflater.from(getContext()).inflate(R.layout.qv_dialog_changequantity, null, false);
            final EditText et = (EditText) inflate.findViewById(R.id.qv_et_change_qty);
            et.setText(String.valueOf(quantity));
            builder.setView(inflate);
            builder.setPositiveButton(labelPositiveButton, null);
            final AlertDialog dialog = builder.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newQuantity = et.getText().toString();
                    if (isValidNumber(newQuantity)) {
                        int intNewQuantity = Integer.parseInt(newQuantity);
                        Log.d(VIEW_LOG_TAG, "newQuantity " + intNewQuantity + " max " + maxQuantity);
                        if (intNewQuantity > maxQuantity) {
                            Toast.makeText(getContext(), "Maximum quantity allowed is " + maxQuantity, Toast.LENGTH_LONG).show();
                        } else if (intNewQuantity < minQuantity) {
                            Toast.makeText(getContext(), "Minimum quantity allowed is " + minQuantity, Toast.LENGTH_LONG).show();
                        } else {
                            setQuantity(intNewQuantity);
                            hideKeyboard(et);
                            dialog.dismiss();
                        }

                    } else {
                        Toast.makeText(getContext(), "Enter valid number", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void hideKeyboard(View focus) {
        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (focus != null) {
            inputManager.hideSoftInputFromWindow(focus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public OnQuantityChangeListener getOnQuantityChangeListener() {
        return onQuantityChangeListener;
    }

    public void setOnQuantityChangeListener(OnQuantityChangeListener onQuantityChangeListener) {
        this.onQuantityChangeListener = onQuantityChangeListener;
    }

    public Drawable getQuantityBackground() {
        return quantityBackground;
    }

    public void setQuantityBackground(Drawable quantityBackground) {
        this.quantityBackground = quantityBackground;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mTextViewQuantity.setBackground(quantityBackground);
        } else {
            mTextViewQuantity.setBackgroundDrawable(quantityBackground);
        }
    }

    public Drawable getAddButtonBackground() {
        return addButtonBackground;
    }

    public void setAddButtonBackground(Drawable addButtonBackground) {
        this.addButtonBackground = addButtonBackground;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mButtonAdd.setBackground(addButtonBackground);
        } else {
            mButtonAdd.setBackgroundDrawable(addButtonBackground);
        }
    }

    public Drawable getRemoveButtonBackground() {
        return removeButtonBackground;
    }

    public void setRemoveButtonBackground(Drawable removeButtonBackground) {
        this.removeButtonBackground = removeButtonBackground;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mButtonRemove.setBackground(removeButtonBackground);
        } else {
            mButtonRemove.setBackgroundDrawable(removeButtonBackground);
        }
    }

    public String getAddButtonText() {
        return addButtonText;
    }

    public void setButtonImageSize(int s) {
        quantityButtonIconSize = s;
    }

    public void setAddButtonText(String addButtonText) {
        this.addButtonText = addButtonText;
        mButtonAdd.setText(addButtonText);
    }

    public String getRemoveButtonText() {
        return removeButtonText;
    }

    public void setRemoveButtonText(String removeButtonText) {
        this.removeButtonText = removeButtonText;
        mButtonRemove.setText(removeButtonText);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int newQuantity) {
        boolean limitReached = false;

        if (newQuantity > maxQuantity) {
            newQuantity = maxQuantity;
            limitReached = true;
        }
        if (newQuantity < minQuantity) {
            newQuantity = minQuantity;
            limitReached = true;
        }
        if (!limitReached) {

            this.quantity = newQuantity;
            mTextViewQuantity.setText(String.valueOf(this.quantity));
            if (onQuantityChangeListener != null) {
                onQuantityChangeListener.onQuantityChanged(getId(), quantity, newQuantity, true);
            }
        } else {
            if (onQuantityChangeListener != null)
                onQuantityChangeListener.onLimitReached(newQuantity);
        }
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public int getQuantityPadding() {
        return quantityPadding;
    }

    public void setQuantityPadding(int quantityPadding) {
        this.quantityPadding = quantityPadding;
        mTextViewQuantity.setPadding(quantityPadding, quantityPadding, quantityPadding, quantityPadding);
    }

    public int getQuantityTextColor() {
        return quantityTextColor;
    }

    public void setQuantityTextColor(int quantityTextColor) {
        this.quantityTextColor = quantityTextColor;
        mTextViewQuantity.setTextColor(quantityTextColor);
    }

    public void setQuantityTextColorRes(int quantityTextColorRes) {
        this.quantityTextColor = ContextCompat.getColor(getContext(), quantityTextColorRes);
        mTextViewQuantity.setTextColor(quantityTextColor);
    }

    public int getAddButtonTextColor() {
        return addButtonTextColor;
    }

    public void setAddButtonTextColor(int addButtonTextColor) {
        this.addButtonTextColor = addButtonTextColor;
        mButtonAdd.setTextColor(addButtonTextColor);
    }

    public void setAddButtonTextColorRes(int addButtonTextColorRes) {
        this.addButtonTextColor = ContextCompat.getColor(getContext(), addButtonTextColorRes);
        mButtonAdd.setTextColor(addButtonTextColor);
    }

    public int getRemoveButtonTextColor() {
        return removeButtonTextColor;
    }

    public void setRemoveButtonTextColor(int removeButtonTextColor) {
        this.removeButtonTextColor = removeButtonTextColor;
        mButtonRemove.setTextColor(removeButtonTextColor);
    }

    public void setRemoveButtonTextColorRes(int removeButtonTextColorRes) {
        this.removeButtonTextColor = ContextCompat.getColor(getContext(), removeButtonTextColorRes);
        mButtonRemove.setTextColor(removeButtonTextColor);
    }

    public String getLabelDialogTitle() {
        return labelDialogTitle;
    }

    public void setLabelDialogTitle(String labelDialogTitle) {
        this.labelDialogTitle = labelDialogTitle;
    }

    public String getLabelPositiveButton() {
        return labelPositiveButton;
    }

    public void setLabelPositiveButton(String labelPositiveButton) {
        this.labelPositiveButton = labelPositiveButton;
    }

    public String getLabelNegativeButton() {
        return labelNegativeButton;
    }

    public void setLabelNegativeButton(String labelNegativeButton) {
        this.labelNegativeButton = labelNegativeButton;
    }

    public void setQuantityDialog(boolean quantityDialog) {
        this.quantityDialog = quantityDialog;
    }

    public boolean isQuantityDialog() {
        return quantityDialog;
    }

    private int dpFromPx(final float px) {
        return (int) (px / getResources().getDisplayMetrics().density);
    }

    private int pxFromDp(final float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }


    public static boolean isValidNumber(String string) {
        try {
            return Integer.parseInt(string) <= Integer.MAX_VALUE;
        } catch (Exception e) {
            return false;
        }
    }

    public void setEnabled(boolean b) {
        mButtonAdd.setVisibility(b ? VISIBLE : INVISIBLE);
        mButtonRemove.setVisibility(b ? VISIBLE : INVISIBLE);
        mTextViewQuantity.setOnClickListener(b ? this : null);
    }

}
