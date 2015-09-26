package com.blackboxstudios.rafael.rsspodcastplayer;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackboxstudios.rafael.rsspodcastplayer.objects.PodcastData;
import com.blackboxstudios.rafael.rsspodcastplayer.utils.RecyclerItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class PodcastListActivityFragment extends Fragment {

    @Bind(R.id.list)RecyclerView podcastList;

    private ArrayList<PodcastData> content;
    private int pos = 0;
    private FancyAdapter fancyAdapter;

    public PodcastListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_podcast_list, container, false);

        ButterKnife.bind(this, rootView);

        Bundle arguments = getArguments();
        podcastList.setHasFixedSize(true);

        Intent intent = getActivity().getIntent();
        content = intent.getParcelableArrayListExtra("podcasts");
        Log.d("RetrievedList", String.valueOf(content.size()));

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        podcastList.setLayoutManager(llm);

        fancyAdapter = new FancyAdapter(content);
        podcastList.setAdapter(fancyAdapter);
        fancyAdapter.notifyDataSetChanged();

        podcastList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("podcastListener", String.valueOf(position));

                        navigateToPlayer(position);

                    }
                })
        );

        return rootView;
    }

    public void navigateToPlayer(int position){
        Bundle args = new Bundle();
        args.putParcelableArrayList("podcasts", content);
        args.putInt("position", position);


        MediaPlayerFragment mediaPlayerFragment = new MediaPlayerFragment();
        mediaPlayerFragment.setArguments(args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragmentContainer, mediaPlayerFragment, PodcastListActivity.TAG_MEDIAPLAYER_FRAGMENT);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(PodcastListActivity.TAG_MEDIAPLAYER_FRAGMENT);
        transaction.commit();




    }

    public class FancyAdapter extends RecyclerView.Adapter<TracksViewHolder> {

        private ArrayList<PodcastData> podcastList;
        public FancyAdapter(ArrayList<PodcastData> podcastList){
            this.podcastList = podcastList;
        }

        @Override
        public int getItemCount() {
            return (podcastList == null)?0:podcastList.size();
        }

        @Override
        public void onBindViewHolder(TracksViewHolder tracksViewHolder, int i) {
            tracksViewHolder.populateFrom(podcastList.get(i));

        }

        @Override
        public TracksViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View trackView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.podcast_item, viewGroup, false);

            return  new TracksViewHolder(trackView);
        }


    }

    public class TracksViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.title) TextView title;
        @Bind(R.id.description) TextView description;
        @Bind(R.id.podcastImage) ImageView thumbnail;
        @Bind(R.id.back) ImageView back;


        public TracksViewHolder(View row){
            super(row);
            ButterKnife.bind(this, row);

        }

        void populateFrom(PodcastData podcastData){
            title.setText(podcastData.getTitle());
            //description.setText(podcastData.getDescription());

            Glide.with(getContext()).load(podcastData.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(thumbnail);

            Glide.with(getContext()).load(podcastData.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(back);

        }
    }
}
