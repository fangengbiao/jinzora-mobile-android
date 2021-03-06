package org.jinzora.playback.players;



import java.util.List;

import org.jinzora.playback.PlaybackInterface;

import android.os.RemoteException;
import android.util.Log;

public abstract class PlaybackDevice implements PlaybackInterface {
	protected List<String> playlist = null;
	
	protected static final int ADD_REPLACE 	= 0;
	protected static final int ADD_END 		= 1;
	protected static final int ADD_CURRENT 	= 2;
	
	private boolean autopaused = false;
	
	protected int nextQueuedPos = -1;
	
	/**
	 * Don't require our implementations to
	 * implement this.
	 */
	public boolean isPlaying() {
		return false;
	}
	
	@Override
	public void onCallBegin() {
		//Log.d("jinzora","receiving call");
		
		if (this.isPlaying()) {
			autopaused=true;
			try {
				this.pause();
			} catch (RemoteException e) {
				Log.e("jinzora","error autopausing",e);
			}
		}
	}
	
	@Override
	public void onCallEnd() {
		//Log.d("jinzora","call ended");
		if (!this.isPlaying() && autopaused) {
			try {
				this.pause();
			} catch (RemoteException e) {
				Log.e("jinzora","error autoresuming",e);
			}
		}
		autopaused=false;
	}
	
	@Override
	public void registerRemoteControl() {
		
	}
	
	@Override
	public void unregisterRemoteControl() {
		
	}
	
	@Override
	public void queueNext(int pos) throws RemoteException {
		nextQueuedPos = pos;
	}
	
	@Override
	public void setBaseURL(String url) throws RemoteException {
		// Devices don't need this method.
	}
	
	@Override
	public void setPlaybackDevice(String playerClass, String arg) throws RemoteException {
		// Devices don't need this method.
	}
	
	public String getAddTypeString(int addType) {
		switch (addType) {
		case 0:
			return "REPLACE";
		case 1:
			return "END";
		case 2:
			return "CURRENT";
		}
		return null;
	}
}
