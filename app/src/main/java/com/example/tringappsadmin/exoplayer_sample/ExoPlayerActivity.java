package com.example.tringappsadmin.exoplayer_sample;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextRenderer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import java.util.List;

public class ExoPlayerActivity extends AppCompatActivity implements VideoRendererEventListener, View.OnClickListener,
        SimpleExoPlayer.VideoListener, TextRenderer.Output {


    private static final String TAG = "MainActivity";
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private Button enableCc, disableCc, fullScreen, smallScreen;
    private MappingTrackSelector mappingTrackSelector;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exoplayer);

        enableCc = (Button) findViewById(R.id.enable_cc);
        disableCc = (Button) findViewById(R.id.disable_cc);
        fullScreen=(Button) findViewById(R.id.full_screen);
        smallScreen=(Button) findViewById(R.id.small_screen);
        toolbar= (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        enableCc.setOnClickListener(this);
        disableCc.setOnClickListener(this);
        fullScreen.setOnClickListener(this);
        smallScreen.setOnClickListener(this);

setSupportActionBar(toolbar);

        mappingTrackSelector = new DefaultTrackSelector();

        LoadControl loadControl = new DefaultLoadControl();

        player = ExoPlayerFactory.newSimpleInstance(this, mappingTrackSelector, loadControl);
        simpleExoPlayerView = new SimpleExoPlayerView(this);
        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exoplayerView);

        simpleExoPlayerView.setUseController(true);
        simpleExoPlayerView.requestFocus();

        simpleExoPlayerView.setPlayer(player);


        Uri mp4VideoUri = Uri.parse("https://nbclim-f.akamaihd.net/i/Prod/NBCU_LM_VMS_-_KNTV/924/799/Celebration_of_Life_Service_Honors_Late_SF_Mayor_Ed_Lee__797116.mp4,,.csmil/master.m3u8"); //Radnom 540p indian channel

        DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoplayer2example"), bandwidthMeterA);

        MediaSource videoSource = new HlsMediaSource(mp4VideoUri, dataSourceFactory, 1, null, null);
        final LoopingMediaSource loopingSource = new LoopingMediaSource(videoSource);

        player.prepare(loopingSource);

        player.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {
                Log.v(TAG, "Listener-onTimelineChanged...");
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.v(TAG, "Listener-onTracksChanged...");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Log.v(TAG, "Listener-onLoadingChanged...isLoading:" + isLoading);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.v(TAG, "Listener-onPlayerStateChanged..." + playbackState);
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.v(TAG, "Listener-onPlayerError...");
                player.stop();
                player.prepare(loopingSource);
                player.setPlayWhenReady(true);
            }

            @Override
            public void onPositionDiscontinuity() {
                Log.v(TAG, "Listener-onPositionDiscontinuity...");
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                Log.v(TAG, "Listener-onPlaybackParametersChanged...");
            }
        });

        player.setPlayWhenReady(true);
        player.setVideoDebugListener(this);
    }


    @Override
    public void onVideoEnabled(DecoderCounters counters) {

    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

    }

    @Override
    public void onVideoInputFormatChanged(Format format) {

    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

        Log.v(TAG, "onVideoSizeChanged [" + " width: " + width + " height: " + height + "]");

    }

    @Override
    public void onRenderedFirstFrame() {
    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {

    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop()...");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart()...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume()...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause()...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy()...");
        player.release();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.enable_cc:
                mappingTrackSelector.setRendererDisabled(2, false);
                break;

            case R.id.disable_cc:
                mappingTrackSelector.setRendererDisabled(2, true);
                mappingTrackSelector.clearSelectionOverrides();
                break;

            case R.id.full_screen:
                toolbar.setVisibility(View.GONE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                simpleExoPlayerView.setSystemUiVisibility(
                                 View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                              |   View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                         |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_FULLSCREEN);

                break;

            case R.id.small_screen:
                toolbar.setVisibility(View.VISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                simpleExoPlayerView.setSystemUiVisibility(
                       View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                               |View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

                break;
        }
    }

    @Override
    public void onCues(List<Cue> cues) {
    }

    @Override
    public void onBackPressed() {

        toolbar.setVisibility(View.VISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        simpleExoPlayerView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        |View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);


    }
}




