package uby.luca.bakingapp;


import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import uby.luca.bakingapp.data.Recipe;
import uby.luca.bakingapp.data.Step;

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
    @BindView(R.id.exo_player)
    PlayerView simpleExoPlayerView;
    @BindView(R.id.step_navbar_rl)
    RelativeLayout stepNavbarRl;

    SimpleExoPlayer simpleExoPlayer;
    final String PLAYER_POSITION = "playerPosition";

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);

        ButterKnife.bind(this, rootView);

        Recipe recipe = null;
        if (getArguments() != null) {
            recipe = getArguments().getParcelable(PARCELED_RECIPE);
        }
        final int clickedStepPosition = getArguments().getInt(STEP_POSITION);

        final Step step;
        if (recipe != null && clickedStepPosition >= 0) {
            boolean isLandscape = this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
            boolean isTablet = getResources().getBoolean(R.bool.isTablet);
            String videoURL = recipe.getSteps().get(clickedStepPosition).getVideoURL();
            if (videoURL.isEmpty()) { //try the thumbnail URL
                videoURL = recipe.getSteps().get(clickedStepPosition).getThumbnailURL();
            }

            setupCorrectLayout(isLandscape, isTablet, !videoURL.isEmpty()); // make player fullscreen if in landscape, on a phone, and has a video

            if (!videoURL.isEmpty()) { //we can init the player
                DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getActivity());
                simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, new DefaultTrackSelector(), new DefaultLoadControl());
                simpleExoPlayerView.setPlayer(simpleExoPlayer);
                Uri videoUri = Uri.parse(videoURL);
                String userAgent = Util.getUserAgent(getActivity(), "BackingApp");
                //MediaSource mediaSource = new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
                MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(getActivity(), userAgent)).createMediaSource(videoUri);
                simpleExoPlayer.prepare(mediaSource);
                simpleExoPlayer.setPlayWhenReady(true);
                if (savedInstanceState != null) {
                    long playerPosition = savedInstanceState.getLong(PLAYER_POSITION, simpleExoPlayer.getCurrentPosition());
                    simpleExoPlayer.seekTo(playerPosition);
                }
            }


            step = recipe.getSteps().get(clickedStepPosition);
            stepDetailsShortdescTv.setText(step.getShortDescription());
            stepDetailsDescTv.setText(step.getDescription());

            //setup bottom Prev and Next navigation buttons
            if (clickedStepPosition == 0) { //first step
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
            if (clickedStepPosition == (recipe.getSteps().size() - 1)) { //last step
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
        if (!hasVideo) {
            simpleExoPlayerView.setVisibility(View.GONE);//no video available, hide player

        } else {
            simpleExoPlayerView.setVisibility(View.VISIBLE);

            if (isLandscape && !isTablet) { //landscape on a phone
                //hide unnecessary view
                stepDetailsShortdescTv.setVisibility(View.GONE);
                stepDetailsDescTv.setVisibility(View.GONE);
                ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
                if (ab != null) {
                    ab.hide();
                }

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
                params.height = ViewGroup.LayoutParams.MATCH_PARENT; //give the player as much room as possible
                simpleExoPlayerView.setLayoutParams(params);


            } else {    //in all other cases, display the other views normally
                stepDetailsShortdescTv.setVisibility(View.VISIBLE);
                stepDetailsDescTv.setVisibility(View.VISIBLE);
                ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
                if (ab != null) {
                    ab.show();
                }

                int correctHeightDp; //limit the player size so it's not too bulky
                if (isLandscape) {
                    correctHeightDp = 300; //a little more room if in landscape
                } else {
                    correctHeightDp = 250; //a little smaller for portrait
                }
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
                params.height = (int) (correctHeightDp * Resources.getSystem().getDisplayMetrics().density); //https://stackoverflow.com/questions/8295986/how-to-calculate-dp-from-pixels-in-android-programmatically
                simpleExoPlayerView.setLayoutParams(params);

            }
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (simpleExoPlayer != null) {
            long playerPosition = simpleExoPlayer.getCurrentPosition();
            outState.putLong(PLAYER_POSITION, playerPosition);
        }

    }
}

