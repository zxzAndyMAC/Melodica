package com.coolmusic.musician;
  
 
import android.app.Dialog;
import android.view.View; 

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

public class SettingDialog extends Dialog{
	
	private SettingSeekBar mSeekBar = null;
	private SettingListener l = null;
	private ToggleButton tb = null;
	private Button b = null;
	private Button b1 = null;
	
	public SettingDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	protected SettingDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public SettingDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public void setL(SettingListener ll)
	{
		this.l = ll;
	}
	
	public void setToggleBoff()
	{
		tb.setChecked(false);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    this.setContentView(R.layout.dialog);
	    mSeekBar = (SettingSeekBar)findViewById(R.id.seek1);
	    
	    b = (Button)findViewById(R.id.Button1);
	    b.setOnClickListener(new View.OnClickListener()
	    {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(null != l)
				{
					l.SettingListenerState(SettingListener.CLEAR_BLOCKS, 0);
				}
			}
	    	
	    });
	    
	    b1 = (Button)findViewById(R.id.Button2);
	    b1.setOnClickListener(new View.OnClickListener()
	    {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(null != l)
				{
					l.SettingListenerState(SettingListener.SHARE_FRIEND, 0);
				}
			}
	    	
	    });
	    
	    tb = (ToggleButton)findViewById(R.id.toggleButton1);
	    tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if(arg1)
                {
    				if(null != l)
    				{
    					l.SettingListenerState(SettingListener.NOTE_CUSTOME, 0);
    				}
                }
                else
                {
    				if(null != l)
    				{
    					l.SettingListenerState(SettingListener.NOTE_DEFAULT, 0);
    				}
                }
                 
            }
        });
	    
	    mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
	    {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				mSeekBar.setSeekBarText("\t\n"+progress+"ms");
				if(null != l)
				{
					l.SettingListenerState(SettingListener.SPEED_CHANGE, progress);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
	    	
	    });
	}
}
