package com.coolmusic.musician;

public interface SettingListener {
	public final int SPEED_CHANGE = 0x1;
	public final int NOTE_CUSTOME = 0x2;
	public final int CLEAR_BLOCKS = 0x3;
	public final int NOTE_DEFAULT = 0x4;
	public final int SHARE_FRIEND = 0x5;
	
	public abstract void SettingListenerState(int state ,long time);
}
