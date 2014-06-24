package com.ideabag.playtunes.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ideabag.playtunes.R;
import com.ideabag.playtunes.activity.MainActivity;
import com.ideabag.playtunes.adapter.AlbumsOneAdapter;
import com.ideabag.playtunes.util.TrackerSingleton;

public class AlbumsOneFragment extends ListFragment {
	
	public static final String TAG = "One Album Fragment";
	
	private final static String STATE_KEY_ALBUM_ID = "album_id";
	
	AlbumsOneAdapter adapter;
	private MainActivity mActivity;
	
	private String ALBUM_ID;
	
	private View albumArtHeader;
	
	public void setAlbumId( String album_id ) {
		
		ALBUM_ID = album_id;
		
	}
	
	
	@Override public void onAttach( Activity activity ) {
		super.onAttach( activity );
		
		mActivity = ( MainActivity ) activity;
		
	}
    
	@Override public void onActivityCreated( Bundle savedInstanceState ) {
		super.onActivityCreated( savedInstanceState );
    	
		getView().setBackgroundColor( getResources().getColor( android.R.color.white ) );
		
		
		if ( null != savedInstanceState && savedInstanceState.containsKey( STATE_KEY_ALBUM_ID ) ) {
			
			ALBUM_ID = savedInstanceState.getString( STATE_KEY_ALBUM_ID );
			
		}
		
		adapter = new AlbumsOneAdapter( getActivity(), ALBUM_ID );
		
		//View header = getLayoutInflater().inflate( R.layout.header, null );
		
		//ListView lv = ( ListView ) getView().findViewById( R.id.AlbumListView );
		
		//lv.addHeaderView( );
		//Resources r = getResources();
		int headerHeightPx = ( int ) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 240, getResources().getDisplayMetrics() );
		albumArtHeader = getActivity().getLayoutInflater().inflate( R.layout.view_album_header, null, false );
		albumArtHeader.setLayoutParams( new AbsListView.LayoutParams( AbsListView.LayoutParams.MATCH_PARENT, headerHeightPx ) );
		
		getListView().addHeaderView( albumArtHeader, null, false );
		//getListView().addHeaderView(  );
		//lv.setAdapter( adapter );
		
		//lv.setOnItemClickListener( this );
		
    	setListAdapter( adapter );
    	
    	ActionBar bar =	( ( ActionBarActivity ) getActivity() ).getSupportActionBar();
		
		ImageView iv = ( ImageView ) albumArtHeader.findViewById( R.id.AlbumArtFull );
		
		if ( null != adapter.albumArtUri ) {
			
			iv.setImageURI( adapter.albumArtUri );
			
		}
		
		if ( null != adapter.albumTitle ) {
			
			bar.setTitle( adapter.albumTitle );
			mActivity.actionbarTitle = bar.getTitle();
			bar.setSubtitle( adapter.getCount() + " tracks" );
			mActivity.actionbarSubtitle = bar.getSubtitle();
			
		}
		
	}
	
	
	@Override public void onListItemClick( ListView l, View v, int position, long id ) {
		
		mActivity.BoundService.setPlaylistCursor( adapter.getCursor() );
		
		mActivity.BoundService.setPosition( position - l.getHeaderViewsCount() );
		
		mActivity.BoundService.play();
		
		// Set the title of the playlist
		
		// 
		
	}
	
	@Override public void onResume() {
		super.onResume();
		
		Tracker t = TrackerSingleton.getDefaultTracker( mActivity );
		
	        // Set screen name.
	        // Where path is a String representing the screen name.
		t.setScreenName( TAG );
		t.set( "_count", ""+adapter.getCount() );
		
	        // Send a screen view.
		t.send( new HitBuilders.AppViewBuilder().build() );
		
	}
	
	@Override public void onPause() {
		super.onPause();
		
	}
	
	@Override public void onSaveInstanceState( Bundle outState ) {
        super.onSaveInstanceState( outState );
        
        outState.putString( STATE_KEY_ALBUM_ID, ALBUM_ID );
        
	}
	
}