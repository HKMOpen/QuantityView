package me.himanshusoni.quantityview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by hesk on 27/8/2017.
 */
public class QuantityLabelView extends RelativeLayout {

    private TextView mTextViewQuantityLabel;
    private QuantityView mQuantityView;

    public QuantityLabelView(Context context) {
        super(context);
        bind(LayoutInflater.from(context).inflate(reslayout(), this, true));
        init(null, 0);
    }

    public QuantityLabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bind(LayoutInflater.from(context).inflate(reslayout(), this, true));
        init(attrs, 0);
    }

    public QuantityLabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bind(LayoutInflater.from(context).inflate(reslayout(), this, true));
        init(attrs, defStyleAttr);
    }

    public QuantityLabelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        bind(LayoutInflater.from(context).inflate(reslayout(), this, true));
        init(attrs, defStyleAttr);
    }

    @LayoutRes
    protected int reslayout() {
        return R.layout.qr_bar;
    }

    protected void bind(View hh) {
        //  ButterKnife.bind(this, hh);
        mTextViewQuantityLabel = (TextView) hh.findViewById(R.id.com_quantity_label);
        mQuantityView = (QuantityView) hh.findViewById(R.id.com_quantity);
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

    private int textPadding;

    public int getPadding() {
        return textPadding;
    }

    public void setPadding(int quantityPadding) {
        this.textPadding = quantityPadding;
        mTextViewQuantityLabel.setPadding(quantityPadding, quantityPadding, quantityPadding, quantityPadding);
    }

    public int getQuantity() {
        return mQuantityView.getQuantity();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void init(AttributeSet attrs, int defStyle) {

        final TypedArray az = getContext().obtainStyledAttributes(attrs, R.styleable.QuantityViewLabel, defStyle, 0);

        if (az.hasValue(R.styleable.QuantityViewLabel_qvl_label)) {
            mTextViewQuantityLabel.setText(az.getString(R.styleable.QuantityViewLabel_qvl_label));
        }

        mTextViewQuantityLabel.setTextColor(az.getColor(R.styleable.QuantityViewLabel_qvl_textColor, Color.BLACK));


        setPadding((int) az.getDimension(R.styleable.QuantityViewLabel_qvl_textPadding, pxFromDp(16)));


        if (az.hasValue(R.styleable.QuantityViewLabel_qvl_textSize)) {
            mTextViewQuantityLabel.setTextSize(az.getDimensionPixelOffset(R.styleable.QuantityViewLabel_qvl_textSize, 16));
        }
        az.recycle();


        final TypedArray am = getContext().obtainStyledAttributes(attrs, R.styleable.QuantityView, defStyle, 0);

        mQuantityView.setButtonImageSize((int) am.getDimension(R.styleable.QuantityView_qv_controlButtonIconSize, pxFromDp(16)));

        if (am.hasValue(R.styleable.QuantityView_qv_addButtonText)) {
            mQuantityView.setAddButtonText(am.getString(R.styleable.QuantityView_qv_addButtonText));
        }
        if (am.hasValue(R.styleable.QuantityView_qv_addButtonBackground)) {
            mQuantityView.setAddButtonBackground(am.getDrawable(R.styleable.QuantityView_qv_addButtonBackground));
        }
        if (am.hasValue(R.styleable.QuantityView_qv_addButtonIcon)) {
            mQuantityView.setDrawableAddButonIcon(am.getDrawable(R.styleable.QuantityView_qv_addButtonIcon));
            mQuantityView.setAddButtonText("");
        }

        if (am.hasValue(R.styleable.QuantityView_qv_removeButtonText)) {
            mQuantityView.setRemoveButtonText(am.getString(R.styleable.QuantityView_qv_removeButtonText));
        }
        if (am.hasValue(R.styleable.QuantityView_qv_removeButtonBackground)) {
            mQuantityView.setRemoveButtonBackground(am.getDrawable(R.styleable.QuantityView_qv_removeButtonBackground));
        }
        if (am.hasValue(R.styleable.QuantityView_qv_removeButtonIcon)) {
            mQuantityView.setDrawableRemoveButonIcon(am.getDrawable(R.styleable.QuantityView_qv_removeButtonIcon));
            mQuantityView.setRemoveButtonText("");
        }

        mQuantityView.setQuantity(am.getInt(R.styleable.QuantityView_qv_quantity, 0));
        mQuantityView.setMaxQuantity(am.getInt(R.styleable.QuantityView_qv_maxQuantity, Integer.MAX_VALUE));
        mQuantityView.setMinQuantity(am.getInt(R.styleable.QuantityView_qv_minQuantity, 0));
        mQuantityView.setQuantityPadding((int) am.getDimension(R.styleable.QuantityView_qv_quantityPadding, pxFromDp(16)));
        mQuantityView.setQuantityTextColor(am.getColor(R.styleable.QuantityView_qv_quantityTextColor, Color.BLACK));

        if (am.hasValue(R.styleable.QuantityView_qv_quantityBackground)) {
            mQuantityView.setQuantityBackground(am.getDrawable(R.styleable.QuantityView_qv_quantityBackground));
        }

        mQuantityView.setQuantityDialog(am.getBoolean(R.styleable.QuantityView_qv_quantityDialog, true));
        if (am.hasValue(R.styleable.QuantityView_qv_quantityTextSize)) {
            mQuantityView.setQuantityTextSize(am.getDimensionPixelOffset(R.styleable.QuantityView_qv_quantityTextSize, 16));
        }

        am.recycle();
    }
}
