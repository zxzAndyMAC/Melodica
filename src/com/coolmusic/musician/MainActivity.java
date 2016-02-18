package com.coolmusic.musician;
  

import java.io.File;

import com.umeng.analytics.MobclickAgent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
//import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class MainActivity extends Activity implements SettingListener{
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
//	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
//	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;
	
	private boolean helloScreen = true;
	private boolean begin = true;
	private GestureDetector gd;
	private MusicView v;
	private View suView;
	private SettingDialog seDialog;
	private AlertDialog al;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		v = (MusicView) findViewById(R.id.suview);
		suView = findViewById(R.id.fullscreen_content_2);
		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
//		findViewById(R.id.dummy_button).setOnTouchListener(
//				mDelayHideTouchListener);
		seDialog =  new SettingDialog(this, R.style.selectorDialog); 
		seDialog.setL(this);
		gd=new GestureDetector(this,new GestureListener());
		
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage(getString(R.string.txt));

		builder.setTitle(getString(R.string.Warn));

		builder.setPositiveButton(getString(R.string.sure), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			    dialog.dismiss();
			    seDialog.setToggleBoff();
			   }
		});
		al = builder.create();
		al.setOnDismissListener(new OnDismissListener(){

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				seDialog.setToggleBoff();
			}
			
		});
		MobclickAgent.updateOnlineConfig(this);//启用发送策略配置
        MobclickAgent.setSessionContinueMillis(30000);
        MobclickAgent.onEventBegin(this, "GameTime");
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(2000);
	}


//	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
//		@Override
//		public boolean onTouch(View view, MotionEvent motionEvent) {
//			delayedHide(100);
//			return false;
//		}
//	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			if(helloScreen)
				hideLOGO();
			else
				onVisibilityChange(false);
		}
	};
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void hideLOGO()
	{
		View controlsView = findViewById(R.id.fullscreen_content);
		//findViewById(R.id.fullscreen_content_2).setVisibility(View.VISIBLE);
		int mControlsWeight = 0;
		int mShortAnimTime = 0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			// If the ViewPropertyAnimator API is available
			// (Honeycomb MR2 and later), use it to animate the
			// in-layout UI controls at the bottom of the
			// screen.
			if (mControlsWeight == 0) {
				mControlsWeight = controlsView.getWidth();
			}
			if (mShortAnimTime == 0) {
				mShortAnimTime = getResources().getInteger(
						android.R.integer.config_shortAnimTime);
			}
			controlsView
					.animate()
					.translationX(mControlsWeight)
					.setDuration(mShortAnimTime);
		} else {
			// If the ViewPropertyAnimator APIs aren't
			// available, simply show or hide the in-layout UI
			// controls.
			controlsView.setVisibility(View.GONE);
		}
		
		helloScreen = false;
		delayedHide(1000);
	}
	
	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void onVisibilityChange(boolean visible)
	{
//		int mControlsHeight = 0;
//		int mShortAnimTime = 0;
		
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//			// If the ViewPropertyAnimator API is available
//			// (Honeycomb MR2 and later), use it to animate the
//			// in-layout UI controls at the bottom of the
//			// screen.
//			if (mControlsHeight == 0) {
//				mControlsHeight = controlsView.getHeight();
//			}
//			if (mShortAnimTime == 0) {
//				mShortAnimTime = getResources().getInteger(
//						android.R.integer.config_shortAnimTime);
//			}
//			controlsView
//					.animate()
//					.translationY(visible ? 0 : mControlsHeight)
//					.setDuration(mShortAnimTime);
//		} else {
//			// If the ViewPropertyAnimator APIs aren't
//			// available, simply show or hide the in-layout UI
//			// controls.
//			controlsView.setVisibility(visible ? View.VISIBLE
//					: View.GONE);
//		}
//
//		if (visible && AUTO_HIDE) {
//			// Schedule a hide().
//			delayedHide(AUTO_HIDE_DELAY_MILLIS);
//		}
		
		if(suView.getVisibility() == View.GONE)
		{
			suView.setVisibility(View.VISIBLE);
			seDialog.show();
			WindowManager.LayoutParams lp=seDialog.getWindow().getAttributes();			
			lp.alpha=0.8f;//透明度，黑暗度为lp.dimAmount=1.0f;
			lp.dimAmount=0.2f;
			seDialog.getWindow().setAttributes(lp);
			seDialog.setOnDismissListener(new OnDismissListener(){

				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					v.m_pause = false;
				}
				
			});
		}
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		View controlsView = findViewById(R.id.fullscreen_content);
		if(controlsView.getVisibility() == View.VISIBLE)
		{
			controlsView.setVisibility(View.GONE);
			onVisibilityChange(true);
			mHideHandler.removeCallbacks(mHideRunnable);
		}
		MobclickAgent.onPause(this);
	}
	
	@Override
	public void onResume() 
	{
	    super.onResume();
	    MobclickAgent.onResume(this);
	}
	
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK) 
        { 
        	Log.d("Matrix","close");
        	v.Close();
        	MobclickAgent.onEventEnd(this, "GameTime");
        	android.os.Process.killProcess(android.os.Process.myPid());
            return true; 
        } 
        return false; 
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		if(helloScreen)
			return false;
		else if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
		//	v.MoveAction(event);
		}
		return gd.onTouchEvent(event);
	}
	//触摸事件处理
	private class GestureListener extends GestureDetector.SimpleOnGestureListener
	{
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
		{
			//Log.d("Matrix","onScroll"); 
			return false;			
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY)
		{
			if((e1.getY()-e2.getY())>200 && (suView.getVisibility() == View.VISIBLE))
			{
				//v.ScreenShot();
				seDialog.show();
				v.m_pause = true;
			}
			return true;		
		}

		
		@Override
		public boolean onSingleTapUp(MotionEvent e)
		{
			//Log.d("Matrix","onSingleTapUp"); 
			if (TOGGLE_ON_CLICK) {
			//mSystemUiHider.toggle();
			} else {
				//mSystemUiHider.show();
			}
			return false;
		}
		
		@Override
		public boolean onDown(MotionEvent ev)
		{
			//Log.d("Matrix","onDown"); 
			if(begin)
			{
				begin = false;
				onVisibilityChange(false);
			}
			return false;
		}
		
		@Override
		public boolean onDoubleTap(MotionEvent e)
		{
			onVisibilityChange(true);
			delayedHide(2000);
			return true;
		}
	}
	@Override
	public void SettingListenerState(int state, long time) {
		// TODO Auto-generated method stub
		switch(state)
		{
		case SettingListener.SPEED_CHANGE:
			v.setSpeed(time);
			break;
		case SettingListener.NOTE_CUSTOME:
			CheckSDfile();
			break;
		case SettingListener.NOTE_DEFAULT:
			v.initDefaultWav(this);
			break;
		case SettingListener.CLEAR_BLOCKS:
			v.clearBlocks();
			break;
		case SettingListener.SHARE_FRIEND:
			share();
			break;
		}
	}
	
	private void share()
	{
		MobclickAgent.onEvent(this, "share");
		File f = v.ScreenShot();
		Intent intent=new Intent(Intent.ACTION_SEND);
		if(null == f)
			intent.setType("text/plain");
		else
		{
			intent.setType("image/png");
			Uri u = Uri.fromFile(f);
			intent.putExtra(Intent.EXTRA_STREAM, u);
		}
		intent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.share));    
		intent.putExtra(Intent.EXTRA_TEXT, this.getString(R.string.txt2));    
		startActivity(Intent.createChooser(intent, this.getTitle())); 
	}
	
	private void CheckSDfile()
	{
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			String s = FileUtil.getMusicFloder("Music");
			File f = new File(s + "01.wav");
			if(!f.exists())
				al.show();
			else
				v.initCustomWav(this);
		}
		else
			al.show();
	}
}
