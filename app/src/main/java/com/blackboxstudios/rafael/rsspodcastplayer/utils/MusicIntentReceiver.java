package com.blackboxstudios.rafael.rsspodcastplayer.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import com.blackboxstudios.rafael.rsspodcastplayer.services.MusicService;


public class MusicIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("Broadcast action", intent.getAction());

        if (intent.getAction().equals(
                android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {

            Intent intent1 = new Intent(MusicService.ACTION_PAUSE);
            intent1.setClass(context,
                    com.blackboxstudios.rafael.rsspodcastplayer.services.MusicService.class);
            // send an intent to our MusicService to telling it to pause the
            // audio
            context.startService(intent1);

        } else if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {

            KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(
                    Intent.EXTRA_KEY_EVENT);
            if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                return;
            Log.d("keyCode action", String.valueOf(keyEvent.getKeyCode()));
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    Intent intentToggle = new Intent(MusicService.ACTION_TOGGLE_PLAYBACK);
                    intentToggle.setClass(context,
                            com.blackboxstudios.rafael.rsspodcastplayer.services.MusicService.class);
                    context.startService(intentToggle);
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    Intent intentPlay = new Intent(MusicService.ACTION_PLAY);
                    intentPlay.setClass(context,
                            com.blackboxstudios.rafael.rsspodcastplayer.services.MusicService.class);
                    context.startService(intentPlay);

                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    Intent intentPause = new Intent(MusicService.ACTION_PAUSE);
                    intentPause.setClass(context,
                            com.blackboxstudios.rafael.rsspodcastplayer.services.MusicService.class);
                    context.startService(intentPause);

                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    Intent intentNext = new Intent(MusicService.ACTION_NEXT);
                    intentNext.setClass(context,
                            com.blackboxstudios.rafael.rsspodcastplayer.services.MusicService.class);
                    context.startService(intentNext);

                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    Intent intentPrev = new Intent(MusicService.ACTION_PREVIOUS);
                    intentPrev.setClass(context,
                            com.blackboxstudios.rafael.rsspodcastplayer.services.MusicService.class);
                    context.startService(intentPrev);

                    break;
                default:
                    break;
            }
        }
    }
}
