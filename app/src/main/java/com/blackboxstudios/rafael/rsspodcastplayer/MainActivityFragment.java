package com.blackboxstudios.rafael.rsspodcastplayer;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackboxstudios.rafael.rsspodcastplayer.network.RetrieveRssFeed;
import com.blackboxstudios.rafael.rsspodcastplayer.objects.PodcastData;
import com.blackboxstudios.rafael.rsspodcastplayer.utils.FeedResponse;
import com.blackboxstudios.rafael.rsspodcastplayer.utils.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements FeedResponse{

    @Bind(R.id.searchField)SearchView searchField;
    @Bind(R.id.podcastImage)ImageView podcastImage;
    @Bind(R.id.podcastTitle)TextView podcastTitle;

    private Boolean done = false;
    private String currentTag = null;
    private PodcastData podcastData = null;

    private ArrayList<PodcastData> content;
    private int pos = 0;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, rootView);

        searchField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.contentEquals("")){
                    if (Utility.isNetworkAvailable(getActivity())){
                        try{
                            new RetrieveRssFeed(MainActivityFragment.this, getContext()).execute(query);
                        }catch (Exception e){
                            Log.e("Exception", e.toString());
                        }
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        return rootView;
    }

    @OnClick(R.id.podcastImage)
    public void showPodcasts(){
        if (content.size()>0){
            Bundle args = new Bundle();
            args.putParcelableArrayList("podcasts", content);

            Intent i = new Intent(getActivity(), PodcastListActivity.class);
            i.putParcelableArrayListExtra("podcasts", content);
            startActivity(i);



        }
    }


    @Override
    public void processFinish(String output) throws XmlPullParserException, IOException {
        //this you will received result fired from async class of onPostExecute(result) method.




        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        try{
            xpp.setInput( new StringReader( output ) );
        }catch (NullPointerException e){

        }


        Boolean firstCategory = true;

        try{
            content = new ArrayList<>();

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        currentTag = xpp.getName();
                        if (currentTag.equalsIgnoreCase("url")){
                            Glide.with(getContext()).load(xpp.nextText())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(podcastImage);
                        }else if(currentTag.equalsIgnoreCase("item")){
                            podcastData = new PodcastData();
                            podcastData.setId(String.valueOf(pos));
                        }else if (podcastData != null){
                            if(xpp.getName().equalsIgnoreCase("title")){
                                //System.out.println("title: "+xpp.nextText());
                                podcastData.setTitle(xpp.nextText());
                            }else if(xpp.getName().contains("image")){
                                //System.out.println("title: "+xpp.nextText());
                                podcastData.setImage(xpp.getAttributeValue(null, "href"));
                            }else if(xpp.getName().equalsIgnoreCase("description")){
                                int token = xpp.nextToken();
                                while(token!=XmlPullParser.CDSECT){
                                    token = xpp.nextToken();
                                }
                                String cdata = xpp.getText();
                                podcastData.setDescription(cdata);
                                Log.i("Info", cdata);
                            }else if(xpp.getName().contains("enclosure")){
                                //System.out.println("encoded: "+xpp.nextText());
                                podcastData.setUrl(xpp.getAttributeValue(null, "url"));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        currentTag = xpp.getName();
                        if (currentTag.equalsIgnoreCase("item") && podcastData != null) {

                            content.add(podcastData);
                            pos ++;
                        } else if (currentTag.equalsIgnoreCase("channel")) {
                            done = true;
                        }
                        break;

                }

                eventType = xpp.next();
            }
            System.out.println("End document");
            Log.d("ContentSize", String.valueOf(content.size()));

        }catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
