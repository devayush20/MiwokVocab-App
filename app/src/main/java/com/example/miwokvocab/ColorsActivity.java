package com.example.miwokvocab;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
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

public class ColorsActivity extends AppCompatActivity {
    /*AudioManager instance to manage the Audio Focus*/
    AudioManager mAudioManager;
    //AudioAttributes Instance to set the playback
    //attributes for the media player instance
    //these attributes specify what type of media is
    //to be played and used to callback the audioFocusChangeListener
    AudioAttributes playbackAttributes;
    /*To set the playback attributes for the focus requester*/
    AudioFocusRequest mAudioFocusRequest;
    /*Handles playback of all the sound files*/
    private MediaPlayer mMediaPlayer;
    /*Setup a listener for AudioFocus Gain or Loss*/
    //media player is handled according to the change in the
    //focus which Android system grants for it*/
    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                    mMediaPlayer.start();
                   // Toast.makeText(ColorsActivity.this,"Audio started",Toast.LENGTH_LONG).show();

                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    releaseMediaPlayer();
                    //Toast.makeText(ColorsActivity.this,"Resources released",Toast.LENGTH_LONG).show();


                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    //if(mMediaPlayer!=null) {
                    //Log.e("ColorsActivity/: ","Error Occured");
                    //Toast.makeText(ColorsActivity.this,"Pausing",Toast.LENGTH_LONG).show();
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                    //}
            }
        }
    };

    /**
     * This listener gets tiggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private final MediaPlayer.OnCompletionListener mOnCompletionListener = new OnCompletionListener() {
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
        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("red", "weṭeṭṭi", R.drawable.color_red, R.raw.color_red));
        words.add(new Word("mustard yellow", "chiwiiṭә", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));
        words.add(new Word("dusty yellow", "ṭopiisә", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        words.add(new Word("green", "chokokki", R.drawable.color_green, R.raw.color_green));
        words.add(new Word("brown", "ṭakaakki", R.drawable.color_brown, R.raw.color_brown));
        words.add(new Word("gray", "ṭopoppi", R.drawable.color_gray, R.raw.color_gray));
        words.add(new Word("black", "kululli", R.drawable.color_black, R.raw.color_black));
        words.add(new Word("white", "kelelli", R.drawable.color_white, R.raw.color_white));
        //create a new array adapter
        WordAdapter adapter = new WordAdapter(this, words, R.color.category_colors);
        ListView listview = findViewById(R.id.list);
        listview.setAdapter(adapter);
        //Get the audio system service for the audio playback attributes
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
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                releaseMediaPlayer();
                Word word = words.get(position);
                // Request audio focus so in order to play the audio file. The app needs to play a
                // short audio file, so we will request audio focus with a short amount of time
                // with AUDIOFOCUS_GAIN_TRANSIENT.
                //request the audio focus by the Android System
                int result = mAudioManager.requestAudioFocus(mAudioFocusRequest);
                //Toast.makeText(ColorsActivity.this,"Granted!",Toast.LENGTH_LONG).show();
                mMediaPlayer = MediaPlayer.create(ColorsActivity.this, word.getAudioResourceID());
                //if the system grants the permission
                //then start playing the audio file
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    //specifying just this as the context can become ambiguous
                    //therefore we write NumberActivity.this
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
                }
                //Toast.makeText(ColorsActivity.this,"Playing: "+words.get(position).getmDefaultTranslation(),Toast.LENGTH_SHORT).show();
                //Log.i("ColorsActivity","Current WordL "+word);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void releaseMediaPlayer() {
        if (mMediaPlayer != null)
            mMediaPlayer.release();
        mMediaPlayer = null;
        try {
            mAudioManager.abandonAudioFocusRequest(mAudioFocusRequest);

        }
        catch(Exception e)
        {
            Log.e("ColorsActivity/: ","Error occured 2: "+e.toString());
        }
    }
}