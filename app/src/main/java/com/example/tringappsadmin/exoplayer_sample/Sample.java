package com.example.tringappsadmin.exoplayer_sample;

/**
 * Created by Tringapps Admin on 12/19/2017.
 */

public class Sample {


    /* SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    String videoURL = "http://blueappsoftware.in/layout_design_android_blog.mp4";

    //    String videoURL = "https://nbclim-f.akamaihd.net/i/Prod/NBCU_LM_VMS_-_KNTV/924/799/Celebration_of_Life_Service_Honors_Late_SF_Mayor_Ed_Lee__797116.mp4,,.csmil/master.m3u8";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exoplayer);

        exoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exo_player_view);
        try {


            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

            Uri videoURI = Uri.parse(videoURL);

            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);

            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        } catch (Exception e) {
            Log.e("MainAcvtivity", " exoplayer error " + e.toString());
        }
    }
}*/



   /**
    *
    * Sample 2
    *
     **/


   /* Context mContext;
    SimpleExoPlayerView playerView;
    EditText editText;
    private ComponentListener componentListener;
    private DataSource.Factory mediaDataSourceFactory;
    private Handler mainHandler;
    private DefaultTrackSelector trackSelector;
    SimpleExoPlayer player;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exoplayer);
        mContext = this;
        editText = (EditText) findViewById(R.id.mediaPath);
        Button btnPlay = (Button) findViewById(R.id.play);
        playerView = (SimpleExoPlayerView) findViewById(R.id.video_view);
        mediaDataSourceFactory = buildDataSourceFactory(true);
        mainHandler = new Handler();
        componentListener = new ComponentListener();
        trackSelector = new DefaultTrackSelector();
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (editText.getText() != null && editText.getText().length() > 0) {
//                    initializePlayer(editText.getText().toString());
                    initializePlayer("http://blueappsoftware.in/layout_design_android_blog.mp4");
//                    initializePlayer(" https://nbclim-f.akamaihd.net/i/Prod/NBCU_LM_VMS_-_KNTV/924/799/Celebration_of_Life_Service_Honors_Late_SF_Mayor_Ed_Lee__797116.mp4,,.csmil/master.m3u8");
//                }
            }
        });
    }

    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
       *//* return ((AnalyticsApplication) getApplication())
                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);*//*
       return null;
    }

    private void initializePlayer(String path) {
        player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
        player.addListener(componentListener);
//        String path = file:///storage/emulated/0/SugarBox/master.m3u8";
        Uri uri = Uri.parse(path);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);
        playerView.setPlayer(player);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, null);
    }

    private void releasePlayer() {
        if (player != null) {
            player.removeListener(componentListener);
            player.release();
            player = null;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if (editText.getText() != null && editText.getText().length() > 0) {
                initializePlayer(editText.getText().toString());
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            if (editText.getText() != null && editText.getText().length() > 0) {
                initializePlayer(editText.getText().toString());
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }


    class ComponentListener implements com.google.android.exoplayer2.ExoPlayer.EventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady,
                                         int playbackState) {
            String stateString;
            switch (playbackState) {
                case com.google.android.exoplayer2.ExoPlayer.STATE_IDLE:
                    stateString = "ExoPlayer.STATE_IDLE      -";
                    break;
                case com.google.android.exoplayer2.ExoPlayer.STATE_BUFFERING:
                    stateString = "ExoPlayer.STATE_BUFFERING -";
                    break;
                case com.google.android.exoplayer2.ExoPlayer.STATE_READY:
                    stateString = "ExoPlayer.STATE_READY     -";
                    break;
                case com.google.android.exoplayer2.ExoPlayer.STATE_ENDED:
                    stateString = "ExoPlayer.STATE_ENDED     -";
                    break;
                default:
                    stateString = "UNKNOWN_STATE             -";
                    break;
            }
            Log.d("ExopLayer", "changed state to " + stateString
                    + " playWhenReady: " + playWhenReady);
        }

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups,
                                    TrackSelectionArray trackSelections) {
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
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


        private DrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManager(UUID uuid, String licenseUrl, String[] keyRequestPropertiesArray) throws UnsupportedDrmException {
            if (Util.SDK_INT < 18) {
                return null;
            }
            HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl,
                    buildHttpDataSourceFactory(false));
            if (keyRequestPropertiesArray != null) {
                for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
                    drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i],
                            keyRequestPropertiesArray[i + 1]);
                }
            }
            return new DefaultDrmSessionManager<>(uuid,
                    FrameworkMediaDrm.newInstance(uuid), drmCallback, null, mainHandler, null);
        }


        private HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter) {
            *//*return ((AnalyticsApplication) getApplication())
                    .buildHttpDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
*//*

            return null;
        }


    }
}*/

}
