/*
package com.example.tringappsadmin.exoplayer_sample;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParser;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.MediaCodecVideoRenderer;
import com.google.android.exoplayer2.decoder;
import java.io.IOException;
import java.util.UUID;
import com.google.android.exoplayer2.ExoPlayer;

public class MainActivity extends AppCompatActivity implements ManifestFetcher.ManifestCallback<HlsPlaylist>,
 HlsSampleSource.EventListnener, AudioManager.OnAudioFocusChangeListener, View.OnClickListener,
ExoPlayer, ExoPlayer.EventListener, AdaptiveMediaSourceEventListener{

private SurfaceView surfaceView;
private Button btn_play, btn_pause;
private ExoPlayer player;
private MediaController.MediaPlayerControl playerControl;
private String video_url;
private Handler mainHandler;
private AudioManager am;
private String useragent;
private ManifestFetcher<HlsPlaylist> playlistFetcher;
private static final int BUFFER_SEGMENT_SIZE=64*1024;
private static final int MAIN_BUFFER_SEGMENTS=254;
private static final int TYPE_VIDEO=0;
private TextView txt_playState;
private TrackRenderer videoRenderer;
private MediaCodecAudioRenderer audioRenderer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);

surfaceView = (SurfaceView) findViewById(R.id.surface_view);
txt_playState=(TextView) findViewById(R.id.textView);
btn_play=(Button) findViewById(R.id.play);
btn_pause=(Button) findViewById(R.id.pause);

btn_play.setOnClickListener(this);
btn_pause.setOnClickListener(this);

player=ExoPlayer.Factory.newInstance(2);
playerControl = new PlayerControl(player);
video_url = "https://nbclim-f.akamaihd.net/i/Prod/NBCU_LM_VMS_-_KNTV/924/799/Celebration_of_Life_Service_Honors_Late_SF_Mayor_Ed_Lee__797116.mp4,,.csmil/master.m3u8";

am=(AudioManager) this.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
mainHandler=new Handler();
useragent=Util.getUserAgent(this,"MainActivity");
        HlsPlaylistParser parser =new HlsPlaylistParser();
        playlistFetcher=new ManifestFetcher<>(video_url, new DefaultUriDataSource(this,useragent),parser);
        playlistFetcher.singleLoad(mainHandler.getLooper(),this);


    }

    public void onSingleManifest(HlsPlaylist manifest){
        LoadControl loadControl =new DefaultLoadControl(new DefaultAllocator(BUFFER_SEGMENT_SIZE));
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        PtsTimeStampAdjusterProvider  timeStampAdjusterProvider = new PtsTimeStampAdjusterProvider();
        boolean haveSubTitles= false;
        boolean haveAudios=false;

        if (manifest instanceof HlsMasterPlaylist){
HlsMasterPlaylist masterPlaylist = (HlsMasterPlaylist) manifest;
haveSubTitles=!masterPlaylist.subtitles.isEmpty();
        }

        DataSource dataSource =new DefaultUriDataSource(this, bandwidthMeter, useragent);
        HlsChunkSource chunkSource = new HlsChunkSource(true, dataSource, manifest,
                DefaultHlsTrackSelector.nevDefaultInstance(this), bandwidthMeter,
                timeStampAdjusterProvider, HlsChunkSource.HlsChunkHolder.ADAPTIVEMODE_SPLICE);
        HlsSampleSource sampleSource = new HlsSampleSource(chunkSource, loadControl, MAIN_BUFFER_SEGMENTS*BUFFER_SEGMENT_SIZE,
                mainHandler, this, TYPE_VIDEO);
        MediaCodecVideoRenderer videoRenderer = new MediaCodecVideoRenderer(this,
                sampleSource, MediaCodecSelector.DEFAULT,
                MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        MediaCodecAudioRenderer audioRenderer = new MediaCodecAudioRenderer(sampleSource, MediaCodecSelector.DEFAULT);
        this.videoRenderer= videoRenderer;
        this.audioRenderer= audioRenderer;
        pushSurface(false);
        player.prepare(videoRenderer,audioRenderer);
        player.addListener(this);

        if (requestFocus()){
            player.setPlayWhenReady(true);
        }



    }

    public Boolean requestFocus(){
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == am.requestAudioFocus(MainActivity.this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
    }

    private void pushSurface(boolean blockForSurfacePush){
        if(videoRenderer==null){
            return;
        }
        if (blockForSurfacePush){
            player.blockingSendMessages(videoRenderer, MediaCodecVideoRenderer.MSG_SET_SURFACE, surfaceView.getHolder().getSurface());
        }else{
            player.sendMessages(videoRenderer, MediaCodecVideoRenderer.MSG_SET_SURFACE, surfaceView.getHolder().getSurface());
        }
    }

   public void onSingleManifestError(IOException e){

   }

    @Override
    public void onAudioFocusChange(int i) {

    }

    @Override
    public void onClick(View view) {
switch (view.getId()){
    case R.id.play:
        playerControl.start();
        break;
    case R.id.pause:
        playerControl.pause();
        break;
}
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState){
       String text ="";
       switch (playbackState){
           case ExoPlayer.STATE_BUFFERING:
               text+="buffering";
               break;

           case ExoPlayer.STATE_ENDED:
               text+="ended";
               break;

           case ExoPlayer.STATE_IDLE:
               text+="idle";
               break;

           case ExoPlayer.STATE_PREPARING:
               text+="preparing";
               break;

           case ExoPlayer.STATE_READY:
               text+="ready";
               break;

           default:
               text+="unknown";
               break;


       }
       txt_playState.setText(text);
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onPlayWhenReadyCommitted(){

    }


    @Override
    public void onLoadStarted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs) {

    }

    @Override
    public void onLoadCompleted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {

    }

    @Override
    public void onLoadCanceled(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {

    }

    @Override
    public void onLoadError(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded, IOException error, boolean wasCanceled) {

    }

    @Override
    public void onUpstreamDiscarded(int trackType, long mediaStartTimeMs, long mediaEndTimeMs) {

    }

    @Override
    public void onDownstreamFormatChanged(int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaTimeMs) {

    }

    @Override
    public void addListener(EventListener listener) {

    }

    @Override
    public void removeListener(EventListener listener) {

    }

    @Override
    public int getPlaybackState() {
        return 0;
    }

    @Override
    public void prepare(MediaSource mediaSource) {

    }

    @Override
    public void prepare(MediaSource mediaSource, boolean resetPosition, boolean resetState) {

    }

    @Override
    public void setPlayWhenReady(boolean playWhenReady) {

    }

    @Override
    public boolean getPlayWhenReady() {
        return false;
    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public void seekToDefaultPosition() {

    }

    @Override
    public void seekToDefaultPosition(int windowIndex) {

    }

    @Override
    public void seekTo(long positionMs) {

    }

    @Override
    public void seekTo(int windowIndex, long positionMs) {

    }

    @Override
    public void setPlaybackParameters(@Nullable PlaybackParameters playbackParameters) {

    }

    @Override
    public PlaybackParameters getPlaybackParameters() {
        return null;
    }

    @Override
    public void stop() {

    }

    @Override
    public void release() {

    }

    @Override
    public void sendMessages(ExoPlayerMessage... messages) {

    }

    @Override
    public void blockingSendMessages(ExoPlayerMessage... messages) {

    }

    @Override
    public int getRendererCount() {
        return 0;
    }

    @Override
    public int getRendererType(int index) {
        return 0;
    }

    @Override
    public TrackGroupArray getCurrentTrackGroups() {
        return null;
    }

    @Override
    public TrackSelectionArray getCurrentTrackSelections() {
        return null;
    }

    @Override
    public Object getCurrentManifest() {
        return null;
    }

    @Override
    public Timeline getCurrentTimeline() {
        return null;
    }

    @Override
    public int getCurrentPeriodIndex() {
        return 0;
    }

    @Override
    public int getCurrentWindowIndex() {
        return 0;
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public long getCurrentPosition() {
        return 0;
    }

    @Override
    public long getBufferedPosition() {
        return 0;
    }

    @Override
    public int getBufferedPercentage() {
        return 0;
    }

    @Override
    public boolean isCurrentWindowDynamic() {
        return false;
    }

    @Override
    public boolean isCurrentWindowSeekable() {
        return false;
    }
}

*/
