package com.coolmusic.musician;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException; 
import java.lang.reflect.Array;

import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet; 
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class MusicView extends SurfaceView implements SurfaceHolder.Callback
{
	private SurfaceHolder holder;
	private DThread dThread; 
	private int Size_w = 0;
	private int Size_h = 0;
	
	public long m_Speed = 200;
	private long runtime;
	private Context context;
	
	public boolean m_pause = true;

	public MusicView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public MusicView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public MusicView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	public void setSpeed(long s)
	{
		this.m_Speed = s>150 ? s : 150;
	}
	
	public void clearBlocks()
	{
		dThread.clearBlocks();
	}
	
	public void initDefaultWav(Context context)
	{
		initWavs(context);
	}
	
	public void initCustomWav(Context context)
	{
		MobclickAgent.onEvent(context, "CustomNote");
		initWavsFsd();
	}
	
	private void init(Context context)
	{
		this.context = context;
		Size_w = context.getResources().getDisplayMetrics().widthPixels;
		Size_h = context.getResources().getDisplayMetrics().heightPixels;
		//Log.d("Matrix","Size_w: "+Size_w+"   Size_h: "+Size_h);
		
		
		holder = this.getHolder();  
        holder.addCallback(this); 
        
        initBlocks(context);
        initWavs(context);
        
        setOnTouchListener(new View.OnTouchListener()
        {
          public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
          {
        	  //Log.d("Matrix","action: "+paramAnonymousMotionEvent.getAction());
        	  if(!m_pause)
        		  MusicView.this.dThread.doTouch(paramAnonymousMotionEvent.getX(), paramAnonymousMotionEvent.getY());
              return false;
          }
        });
        setFocusable(true);
	}
	
	public void MoveAction(MotionEvent e)
	{
		dThread.doMove(e.getX(), e.getY());
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		//Log.d("Matrix","surfaceChanged");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		//Log.d("Matrix","surfaceCreated");
		dThread = new DThread(holder ,context);
		if(!dThread.isRun)
		{
			Log.d("Matrix","surfaceCreated");
			dThread.isRun = true;  
			dThread.start(); 
			runtime = System.currentTimeMillis();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.d("Matrix","surfaceDestroyed");
		dThread.isRun = false; 
		
		try {
			dThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dThread = null;
	}
	
	public void Close()
	{
		dThread.isRun = false;  
	}
	
	public File ScreenShot()
	{
		return dThread.ScreenShot();
	}
	
	private class Block
	{
	    public boolean m_c = false;
	    public int m_x;
	    public int m_y;

	    Block()
	    {
	    }
	}
	
    private Drawable m_blockoff = null;
    private Drawable m_blockon = null;
    private Drawable m_blocklight = null;
    private int m_block_w = 0;
    private int m_block_h = 0;
    private int m_blocklig_w = 0;
    private int m_blocklig_h = 0;
    private Block[][] m_block;
    private SoundPool m_snd = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    private int[] m_sp = new int[10];
    
    private int m_line = 0;
    
	private synchronized void doDraw(Canvas canvas)
	{
		for(int i=0;i<16;i++)
		{
			for(int j=0;j<10;j++)
			{
				Drawable localDrawable;
				if(m_block[i][j].m_c)
				{
					if(i == m_line)
					{
						localDrawable = m_blocklight;
						int offset_x = (m_blocklig_w - m_block_w)/2;
						int offset_y = (m_blocklig_h-m_block_h)/2;
						int x = m_block[i][j].m_x - offset_x;
						int y = m_block[i][j].m_y - offset_y;
						localDrawable.setBounds(x, y, m_blocklig_w+x, m_blocklig_h+y);
						localDrawable.draw(canvas);
					}
					else
					{
						localDrawable = m_blockon;
						localDrawable.setBounds(m_block[i][j].m_x, m_block[i][j].m_y, m_block_w+m_block[i][j].m_x, m_block_h+m_block[i][j].m_y);
						localDrawable.draw(canvas);
					}
				}
				else
				{
					localDrawable = m_blockoff;
					localDrawable.setBounds(m_block[i][j].m_x, m_block[i][j].m_y, m_block_w+m_block[i][j].m_x, m_block_h+m_block[i][j].m_y);
					localDrawable.draw(canvas);
				}
			}
		}
	}
	
    private void initBlocks(Context context)
    {
    	int[] arrayOfInt = new int[]{16,10};
    	m_block = (Block[][]) Array.newInstance(Block.class, arrayOfInt);
    	
    	m_blockoff = context.getResources().getDrawable(R.drawable.check);
    	m_block_w = m_blockoff.getIntrinsicWidth();
    	m_block_h = m_blockoff.getIntrinsicHeight();
    	
    	m_blockon = context.getResources().getDrawable(R.drawable.on32);
    	m_blocklight = context.getResources().getDrawable(R.drawable.lighton);
    	m_blocklig_w = m_blocklight.getIntrinsicWidth();
    	m_blocklig_h = m_blocklight.getIntrinsicHeight();
    	
    	//Log.d("Matrix","m_blockoff_w: "+m_blockoff_w+"   m_blockoff_h: "+m_blockoff_h);
    	float offset_x = (Size_w - m_block_w*16)/17.0f;
    	float offset_y = (Size_h - m_block_h*10)/11.0f;
    	//Log.d("Matrix","offset_x: "+offset_x+"   offset_y: "+offset_y);
    	
    	int i = 0;
    	int j = 0;
    	while(true)
    	{
    		if(j>=10)
    		{
    			++i;
    			j = 0;
    			if(i>=16)
    				break;
    		}
    		m_block[i][j] = new Block();
    		m_block[i][j].m_x = i*m_block_w + (int)((i+1)*offset_x+0.5f);
    		m_block[i][j].m_y = j*m_block_h + (int)((j+1)*offset_y+0.5f);
    		//Log.d("Matrix","i: "+i+"   j: "+j);
    		//Log.d("Matrix","i: "+m_block[i][j].m_x+"   j: "+m_block[i][j].m_y);
    		++j;
    	}
    	//Log.d("Matrix","i: "+i+"   j: "+j);
    }
	
	public void initWavsFsd()
	{
		m_snd.release();
		m_snd = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		String s = FileUtil.getMusicFloder("Music");
		m_sp[0] = m_snd.load(s+"01.wav", 0);
    	m_sp[1] = m_snd.load(s+"02.wav", 0);
    	m_sp[2] = m_snd.load(s+"03.wav", 0);
    	m_sp[3] = m_snd.load(s+"04.wav", 0);
    	m_sp[4] = m_snd.load(s+"05.wav", 0);
    	m_sp[5] = m_snd.load(s+"06.wav", 0);
    	m_sp[6] = m_snd.load(s+"07.wav", 0);
    	m_sp[7] = m_snd.load(s+"08.wav", 0);
    	m_sp[8] = m_snd.load(s+"09.wav", 0);
    	m_sp[9] = m_snd.load(s+"10.wav", 0);
	}
	
    public void initWavs(Context context)
    {
    	m_snd.release();
    	m_snd = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    	m_sp[0] = m_snd.load(context, R.raw.w01, 0);
    	m_sp[1] = m_snd.load(context, R.raw.w02, 0);
    	m_sp[2] = m_snd.load(context, R.raw.w03, 0);
    	m_sp[3] = m_snd.load(context, R.raw.w04, 0);
    	m_sp[4] = m_snd.load(context, R.raw.w05, 0);
    	m_sp[5] = m_snd.load(context, R.raw.w06, 0);
    	m_sp[6] = m_snd.load(context, R.raw.w07, 0);
    	m_sp[7] = m_snd.load(context, R.raw.w08, 0);
    	m_sp[8] = m_snd.load(context, R.raw.w09, 0);
    	m_sp[9] = m_snd.load(context, R.raw.w10, 0);
    }
    
	private class DThread extends Thread
	{
		private SurfaceHolder holder;  
        public boolean isRun ; 
        

        
        public  DThread(SurfaceHolder holder, Context context)  
        {  
            this.holder =holder;   
            isRun = false;
        }
        
    	public void clearBlocks()
    	{
    		for(int i=0;i<16;i++)
    		{
    			for(int j=0;j<10;j++)
    			{
    				m_block[i][j].m_c = false;
    			}
    		}
    	}
      
        public void doMove(float x, float y) {
			// TODO Auto-generated method stub
        //	Log.d("Matrix","mx: "+x+"   my: "+y);
		}

		public void doTouch(float x, float y) {
			// TODO Auto-generated method stub
		//	Log.d("Matrix","x: "+x+"   y: "+y);
			for(int i=0;i<16;i++)
			{
				for(int j=0;j<10;j++)
				{
					if(x>m_block[i][j].m_x && x<(m_block_w+m_block[i][j].m_x) && y>m_block[i][j].m_y && y< (m_block_h+m_block[i][j].m_y))
					{
						m_block[i][j].m_c = !m_block[i][j].m_c;
					}
				}
			}
		}
		

		
	    private void sndPlay(int index)
	    {
	      m_snd.play(m_sp[index], 1.0F, 1.0F, 0, 0, 1.0F);
	    }
		
	    private void playSound(int line)
	    {
	    	for(int j=0;j<10;j++)
	    	{
	    		if(m_block[line][j].m_c)
	    		{
	    			sndPlay(j);
	    		}
	    	}
	    }
	    
	    public File ScreenShot()
	    {
	    	String sdpath = FileUtil.getMusicFloder("Musician");
	    	if(null == sdpath)
	    		return null;
	    	String mPath = sdpath + "screenshot.png";
	    	Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
	    	Canvas canvas = new Canvas(bitmap);
	    	canvas.drawColor(Color.BLACK);
	    	doDraw(canvas);
	    	FileOutputStream fout = null;
			File imageFile = new File(mPath);
			if(imageFile.exists())
				imageFile.delete();
			try {
			    fout = new FileOutputStream(imageFile);
			    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
			   // fout.flush();
			    fout.close();

			} catch (FileNotFoundException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			    return null;
			} catch (IOException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			    return null;
			}
			return imageFile;
	    }
	    
		@Override  
        public void run()  
        { 
        	Canvas canvas = null; 
        	while(isRun) 
        	{
        		try  
                {  
                    synchronized (holder)  
                    {  
                    	canvas = holder.lockCanvas();
                    	canvas.drawColor(Color.BLACK);
                    	doDraw(canvas);
                    	if(m_pause)
                    		continue;
                    	long t = System.currentTimeMillis() - runtime;
                    	if(t>m_Speed)
                    	{
                    		runtime = System.currentTimeMillis();
                    		++m_line;
                    		if(m_line>16)
                    			m_line = 0;
                    		playSound(m_line);
                    	}
                    }  
                }  
                catch (Exception e) {  
                    // TODO: handle exception  
                    e.printStackTrace();  
                }  
                finally  
                {  
                    if(canvas!= null)  
                    {  
                        holder.unlockCanvasAndPost(canvas);
                    }  
                }  
        	}
        	Log.d("Matrix","Thread stop");
        }
	}

}
