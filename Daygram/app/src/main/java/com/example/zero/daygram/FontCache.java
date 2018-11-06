package com.example.zero.daygram;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

public class FontCache {
    private static HashMap<String,Typeface> fontcache=new HashMap<String,Typeface>();
    public static Typeface getFont(String fontname,Context context){
        Typeface font=fontcache.get(fontname);
        if(font==null){
            try{
                font=Typeface.createFromAsset(context.getAssets(),"font/OPTIAgency-Gothic.otf");
            }catch (Exception e){
                return null;
            }
            fontcache.put(fontname,font);
        }
        return font;
    }
}
