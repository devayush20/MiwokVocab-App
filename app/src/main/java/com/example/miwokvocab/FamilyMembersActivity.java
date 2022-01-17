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

public class FamilyMembersActivity extends AppCompatActivity {
    MediaPlayer mMediaPlayer;
    AudioManager mAudioManager;
    //AudioAttributes Instance to set the playback
    //attributes for the media player instance
    //these attributes specify what type of media is
    //to be played and used to callback the audioFocusChangeListener
    AudioAttributes playbackAttributes;
    /*To set the playback attributes for the focus requester*/
    AudioFocusRequest mAudioFocusRequest;
    //Creating a single instance of the OnCompletionListener is better because if we create an Anonymous class
    //in onCItem, we will have to create new instance of it every time.
    private final MediaPlayer.OnCompletionListener mOnCompletionListener=new MediaPlayer.OnCompletionListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };
    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                    mMediaPlayer.start();
                    //Toast.makeText(FamilyMembersActivity.this,"Audio started",Toast.LENGTH_LONG).show();

                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    releaseMediaPlayer();
                   // Toast.makeText(FamilyMembersActivity.this,"Resources released",Toast.LENGTH_LONG).show();


                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    //if(mMediaPlayer!=null) {
                    //Log.e("ColorsActivity/: ","Error Occured");
                   //Toast.makeText(FamilyMembersActivity.this,"Pausing",Toast.LENGTH_LONG).show();
                    mMediaPlayer.pause();
                    mMediaPlayer.seekTo(0);
                    //}
            }
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("father", "әpә", R.drawable.family_father, R.raw.family_father));
        words.add(new Word("mother", "әṭa", R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word("son", "angsi", R.drawable.family_son, R.raw.family_son));
        words.add(new Word("daughter", "tune", R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word("older brother", "taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        words.add(new Word("younger brother", "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word("older sister", "teṭe", R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word("younger sister", "kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        words.add(new Word("grandmother ", "ama", R.drawable.family_grandmother, R.raw.family_grandmother));
        words.add(new Word("grandfather", "paapa", R.drawable.family_grandfather, R.raw.family_grandfather));
        //Create a custom adapter
        WordAdapter adapter = new WordAdapter(this, words, R.color.category_family);
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
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //First release any resources on older audio from the mediaplayer. This will be required when
                //the user clicks on one sound and then clicks on any other before its playback finishes.
                //Thus, in this case the resources will not be released on that MediaPlayer instance.
                releaseMediaPlayer();
                Word word = words.get(position);

                int result = mAudioManager.requestAudioFocus(mAudioFocusRequest);
                mMediaPlayer = MediaPlayer.create(FamilyMembersActivity.this, word.getAudioResourceID());

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    //specifying just this as the context can become ambiguous
                    //therefore we write NumberActivity.this
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
                }
                //Toast.makeText(FamilyMembersActivity.this, "Playing: " + words.get(position).getmDefaultTranslation(), Toast.LENGTH_LONG).show();
            }

        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onStop()
    {
        super.onStop();
        releaseMediaPlayer();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void releaseMediaPlayer()
    {
        //Check if the MediaPlayer object is non-null, release the resources using release() method
        if(mMediaPlayer!=null)
        {
            mMediaPlayer.release();
        }
        //make the MediaPlayer object null (In our code this is an easy way to tell that the media player
        //is not configured to play any audio.
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