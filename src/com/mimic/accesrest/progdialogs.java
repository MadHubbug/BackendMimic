package com.mimic.accesrest;

import java.io.IOException;

import com.hipmob.gifanimationdrawable.GifAnimationDrawable;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class progdialogs extends Dialog {
	private AnimationDrawable animation;
	private GifAnimationDrawable draw;
	private ImageView iv;
	
	public progdialogs(Context context) {
		super(context, R.style.TransparentProgressDialog);
		WindowManager.LayoutParams wlmp = getWindow().getAttributes();
    	wlmp.gravity = Gravity.CENTER_HORIZONTAL;
    	getWindow().setAttributes(wlmp);
		setTitle(null);
		setCancelable(false);
		setOnCancelListener(null);
		
		try {
			animation = new GifAnimationDrawable(context.getResources().openRawResource(R.raw.anim));
		} catch (NotFoundException e) {
			Log.d("woah", "not working");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		iv = new ImageView(context);
		iv.setImageDrawable(animation);
		layout.addView(iv, params);
		addContentView(layout, params);

	}
		
		
		
	
	
	@Override
	public void show() {
	  super.show();
	  iv.setVisibility(View.VISIBLE);
	}

	@Override
	public void dismiss() {
	  super.dismiss();
	  iv.setVisibility(View.GONE);
	}

	
}
