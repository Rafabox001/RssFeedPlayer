package com.blackboxstudios.rafael.rsspodcastplayer.utils;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Rafael on 25/09/2015.
 */
public interface FeedResponse {
    void processFinish(String output) throws XmlPullParserException, IOException;
}
