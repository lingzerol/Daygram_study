package com.example.zero.daygram;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

public class RegularTextView extends android.support.v7.widget.AppCompatTextView {

    public RegularTextView(Context context) {
        super(context);
        applynewfont(context);
    }

    public RegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applynewfont(context);
    }

    public RegularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applynewfont(context);
    }
    private void applynewfont(Context context){
        Typeface typeface=FontCache.getFont("OPTIAgency-Gothic.otf",context);
        setTypeface(typeface);
    }
}
