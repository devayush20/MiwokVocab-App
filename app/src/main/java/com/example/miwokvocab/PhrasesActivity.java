package com.example.miwokvocab;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {
    /*AudioManager instance to manage the Audio Focus*/
    AudioManager mAudioManager;
    //AudioAttributes Instance to set the playback
    //attributes for the media player instance
    //these attributes specify what type of media is
    //to be played and used to callback the audioFocusChangeListener
    AudioAttributes playbackAttributes;
    /*To set the playback attributes for the focus requester*/
    AudioFocusRequest mAudioFocusRequest;
    private MediaPlayer mMediaPlayer;
    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                    mMediaPlayer.start();
                    //Toast.makeText(PhrasesActivity.this,"Audio started",Toast.LENGTH_LONG).show();

                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    releaseMediaPlayer();
                    //Toast.makeText(PhrasesActivity.this,"Resources released",Toast.LENGTH_LONG).show();


                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    //if(mMediaPlayer!=null) {
                    //Log.e("ColorsActivity/: ","Error Occured");
                   // Toast.makeText(PhrasesActivity.this,"Pausing",Toast.LENGTH_LONG).show();
                    mMediaPlayer.pause();
                    mMediaPlayer.seekTo(0);
                    //}
            }
        }
    };
    private final MediaPlayer.OnCompletionListener mOnCompletionListener=new MediaPlayer.OnCompletionListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Where are you going?", "minto wuksus", R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?", "tinnә oyaase'nә", R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...", "oyaaset...", R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?", "michәksәs?", R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I’m feeling good.", "kuchi achit", R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?", "әәnәs'aa?", R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I’m coming.", "hәә’ әәnәm", R.raw.phrase_yes_im_coming));
        words.add(new Word("I’m coming.", "әәnәm", R.raw.phrase_im_coming));
        words.add(new Word("Let’s go.", "yoowutis", R.raw.phrase_lets_go));
        words.add(new Word("Come here.", "әnni'nem", R.raw.phrase_come_here));
        WordAdapter adapter = new WordAdapter(this, words, R.color.category_phrases);
        ListView listview = findViewById(R.id.list);
        listview.setAdapter(adapter);
        try {
            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            //initiate the audio playback attributes
            playbackAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(AudioAttributes.USAGE_MEDIA).build();

            mAudioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                    .setAudioAttributes(playbackAttributes)
                    .setOnAudioFocusChangeListener(audioFocusChangeListener).build();
        }
        catch(Exception e)
        {
            Log.e("ColorsActivity/:","Error occured 1: "+e.toString());
        }
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> apdaterView, View view, int position, long id) {
                //request the audio focus by the Android System
                releaseMediaPlayer();
                Word word = words.get(position);
                // Request audio focus so in order to play the audio file. The app needs to play a
                // short audio file, so we will request audio focus with a short amount of time
                // with AUDIOFOCUS_GAIN_TRANSIENT.
                //request the audio focus by the Android System
                int result = mAudioManager.requestAudioFocus(mAudioFocusRequest);
               // Toast.makeText(PhrasesActivity.this,"Granted!",Toast.LENGTH_LONG).show();
                //if the system grants the permission
                //then start playing the audio file
                mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, word.getAudioResourceID());

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    //specifying just this as the context can become ambiguous
                    //therefore we write NumberActivity.this
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void releaseMediaPlayer()
    {
        if(mMediaPlayer!=null)
        {
            mMediaPlayer.release();
        }
        mMediaPlayer=null;
        try {
            mAudioManager.abandonAudioFocusRequest(mAudioFocusRequest);

        }
        catch(Exception e)
        {
            Log.e("ColorsActivity/: ","Error occured 2: "+e.toString());
        }
    }
}