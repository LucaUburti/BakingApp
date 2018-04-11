package uby.luca.bakingapp;


import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import uby.luca.bakingapp.data.Recipe;
import uby.luca.bakingapp.data.Step;

import static java.lang.Long.getLong;
import static uby.luca.bakingapp.adapters.RecipeAdapter.PARCELED_RECIPE;
import static uby.luca.bakingapp.adapters.StepsAdapter.STEP_POSITION;

public class StepDetailsFragment extends Fragment {
    @BindView(R.id.step_details_shortdesc_tv)
    TextView stepDetailsShortdescTv;
    @BindView(R.id.step_details_desc_tv)
    TextView stepDetailsDescTv;
    @BindView(R.id.step_detail_prev_tv)
    TextView prevTv;
    @BindView(R.id.step_detail_next_tv)
    TextView nextTv;
    @BindView(R.id.exoPlayer)
    PlayerView simpleExoPlayerView;
    @BindView(R.id.step_navbar_rl)
    RelativeLayout stepNavbarRl;

    SimpleExoPlayer simpleExoPlayer;
    String PLAYER_POSITION="playerPosition";

    OnStepNavbarClickListener onStepNavbarClickListener;

    public interface OnStepNavbarClickListener {
        void onStepNavbarClicked(int clickedStep);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onStepNavbarClickListener = (OnStepNavbarClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }

    public StepDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);

        ButterKnife.bind(this, rootView);

        final Recipe recipe = getArguments().getParcelable(PARCELED_RECIPE);
        final int clickedStepPosition = getArguments().getInt(STEP_POSITION);
        final Step step;
        if (recipe != null) {
            boolean isLandscape = this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
            boolean isTablet = getResources().getBoolean(R.bool.isTablet);
            String videoURL = recipe.getSteps().get(clickedStepPosition).getVideoURL();
            if (videoURL.isEmpty()) { //try the thumbnail URL
                videoURL = recipe.getSteps().get(clickedStepPosition).getThumbnailURL();
            }

            setupCorrectLayout(isLandscape, isTablet, !videoURL.isEmpty()); // make player fullscreen if in landscape, on a phone, and has a video


            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), new DefaultTrackSelector(), new DefaultLoadControl());
            simpleExoPlayerView.setPlayer(simpleExoPlayer);

            if (!videoURL.isEmpty()) {
                Uri videoUri = Uri.parse(videoURL);
                simpleExoPlayerView.setVisibility(View.VISIBLE);
                String userAgent = Util.getUserAgent(getActivity(), "BackingApp");
                MediaSource mediaSource = new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
                simpleExoPlayer.prepare(mediaSource);
                simpleExoPlayer.setPlayWhenReady(true);
                if (savedInstanceState!=null){
                    long playerPosition=savedInstanceState.getLong(PLAYER_POSITION, simpleExoPlayer.getCurrentPosition());
                    simpleExoPlayer.seekTo(playerPosition);
                }
            } else { //no video available, hide player
                simpleExoPlayerView.setVisibility(View.GONE);
            }


            step = recipe.getSteps().get(clickedStepPosition);
            stepDetailsShortdescTv.setText(step.getShortDescription());
            stepDetailsDescTv.setText(step.getDescription());


            if (clickedStepPosition == 0) {
                prevTv.setVisibility(View.GONE);
            } else {
                prevTv.setVisibility(View.VISIBLE);
                prevTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onStepNavbarClickListener.onStepNavbarClicked(clickedStepPosition - 1);
                    }
                });
            }

            if (clickedStepPosition == recipe.getSteps().size() - 1) {
                nextTv.setVisibility(View.GONE);
            } else {
                nextTv.setVisibility(View.VISIBLE);
                nextTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onStepNavbarClickListener.onStepNavbarClicked(clickedStepPosition + 1);
                    }
                });
            }

        }
        return rootView;
    }

    private void setupCorrectLayout(boolean isLandscape, boolean isTablet, boolean hasVideo) {
        if (isLandscape && !isTablet && hasVideo) { //landscape on a phone: display player in full screen
            stepDetailsShortdescTv.setVisibility(View.GONE);
            stepDetailsDescTv.setVisibility(View.GONE);
//            prevTv.setVisibility(View.GONE);
//            nextTv.setVisibility(View.GONE);
//            stepNavbarRl.setVisibility(View.GONE);
            ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
            if (ab != null) {
                ab.hide();
            }
        } else {    //in all other cases, display the other views normally
            stepDetailsShortdescTv.setVisibility(View.VISIBLE);
            stepDetailsDescTv.setVisibility(View.VISIBLE);
//            prevTv.setVisibility(View.VISIBLE);
//            nextTv.setVisibility(View.VISIBLE);
//            stepNavbarRl.setVisibility(View.VISIBLE);
            ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
            if (ab != null) {
                ab.show();
            }
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (simpleExoPlayer!=null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (simpleExoPlayer!=null){
            long playerPosition=simpleExoPlayer.getCurrentPosition();
            outState.putLong(PLAYER_POSITION, playerPosition);
        }

    }
}

