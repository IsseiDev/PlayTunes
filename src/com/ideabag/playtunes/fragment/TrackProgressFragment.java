package com.ideabag.playtunes.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.ideabag.playtunes.R;
import com.ideabag.playtunes.activity.NowPlayingActivity;

public class TrackProgressFragment extends Fragment {
	
	private static final int ONE_SECOND_IN_MILLI = 1000;
	
	private NowPlayingActivity mActivity;
	
	private SeekBar Bar;
	
	private Handler handle;
	
	private boolean isPlaying = false;
	
	@Override public void onAttach( Activity activity ) {
		
		super.onAttach( activity );
		
		mActivity = ( NowPlayingActivity ) activity;
		
		handle = new Handler();
		
	}
	
	@Override public void onActivityCreated( Bundle savedInstanceState ) {
		super.onActivityCreated( savedInstanceState );
		
		//getView().setOnClickListener( controlsClickListener );
		
		//getView().findViewById( R.id.FooterControls ).setOnClickListener( controlsClickListener );
    	
		//getView().findViewById( R.id.FooterControlsPlayPauseButton ).setOnClickListener( controlsClickListener );
		
		//mActivity.BoundService.setOnSongInfoChangedListener( this );
		
		this.Bar = ( SeekBar ) getView().findViewById( R.id.TrackProgressBar );
		
		//this.Bar.setProgressDrawable( mActivity.getResources().getDrawable( R.drawable.progress_indicator ) );
		
		this.Bar.setOnSeekBarChangeListener( mSeekBarChangedListener );
		
	}
	
	@Override public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		
		return inflater.inflate( R.layout.fragment_track_progress, null, false );
		
	}
	
	@Override public void onResume() {
		super.onResume();
		startProgress();
		
	}
		
	@Override public void onPause() {
		super.onPause();
		
		stopProgress();
		
	}
	
	private OnSeekBarChangeListener mSeekBarChangedListener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged( SeekBar seekBar, int progress, boolean fromUser) {
			
			if ( fromUser ) {
				
				stopProgress();
				
				setProgress( progress );
				
				mActivity.mBoundService.setSeekPosition( progress );
				
			}
			
			
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			
			stopProgress();
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			
			if ( isPlaying ) {
				
				startProgress();
				
			}
			
		}
		
	};
	
	private final Runnable mUpdateTimer = new Runnable() {

		@Override public void run() {
			
			//android.util.Log.i("Bar Progress",  "" + Bar.getProgress() );
			setProgress( Bar.getProgress() + ONE_SECOND_IN_MILLI );
			
			handle.postDelayed( mUpdateTimer, ONE_SECOND_IN_MILLI );
			
		}
		
		
		
	};
	
	//private 
	public void startProgress() {
		
		stopProgress();
		mUpdateTimer.run(); 
		//handle.postDelayed( mUpdateTimer, ONE_SECOND_IN_MILLI );
		
		
	}
	
	public void stopProgress() {
		
		handle.removeCallbacks( mUpdateTimer );
		
	}
	
	public void setProgress( int progress ) {
		
		this.Bar.setProgress( progress );
		
		int seconds = progress / ONE_SECOND_IN_MILLI;
		int minutes = seconds / 60;
		seconds = seconds % 60;
		
		( ( TextView ) getView().findViewById( R.id.TrackProgressPlayPosition ) ).setText( minutes + ":" + ( seconds < 10 ? "0" + seconds : seconds ) );
		
		
		
	}
	
	public void setDuration( int duration ) {
		
		this.Bar.setMax( duration );
		
		int seconds = duration / ONE_SECOND_IN_MILLI;
		int minutes = seconds / 60;
		seconds = seconds % 60;
		
		( ( TextView ) getView().findViewById( R.id.TrackProgressPlayLength ) ).setText( minutes + ":" + ( seconds < 10 ? "0" + seconds : seconds ) );
		
	}
	
}