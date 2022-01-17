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

public class NumbersActivity extends AppCompatActivity {
    private MediaPlayer mMediaPlayer;
    /*AudioManager instance to manage the Audio Focus*/
    AudioManager mAudioManager;
    //AudioAttributes Instance to set the playback
    //attributes for the media player instance
    //these attributes specify what type of media is
    //to be played and used to callback the audioFocusChangeListener
    AudioAttributes playbackAttributes;
    /*To set the playback attributes for the focus requester*/
    AudioFocusRequest mAudioFocusRequest;
    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                    mMediaPlayer.start();
                   // Toast.makeText(NumbersActivity.this,"Audio started",Toast.LENGTH_LONG).show();

                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    releaseMediaPlayer();
                    //Toast.makeText(NumbersActivity.this,"Resources released",Toast.LENGTH_LONG).show();


                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    //if(mMediaPlayer!=null) {
                    //Log.e("ColorsActivity/: ","Error Occured");
                    Toast.makeText(NumbersActivity.this,"Pausing",Toast.LENGTH_LONG).show();
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
        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("two", "otitko", R.drawable.number_two, R.raw.number_three));
        words.add(new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("five", "massokka", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("nine", "wo'e", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("ten", "na'aacha", R.drawable.number_ten, R.raw.number_ten));
        //LinearLayout ll=(LinearLayout) findViewById(R.id.rootview);
        /*
        Corresponding activity_numbers.xml
        <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:id="@+id/rootView"
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:paddingBottom="@dimen/activity_vertical_margin"
        android:paddingLeft="@dimen/activity_horizontal_margin"
        android:paddingRight="@dimen/activity_horizontal_margin"
        android:paddingTop="@dimen/activity_vertical_margin"
        tools:context="com.example.android.miwok.NumbersActivity">

</LinearLayout>
         */

        //ll=linear layout
        //

        // Create an {@link ArrayAdapter}, whose data source is a list of Strings. The
        // adapter knows how to create layouts for each item in the list, using the
        // simple_list_item_1.xml layout resource defined in the Android framework.
        // This list item layout contains a single {@link TextView}, which the adapter will set to
        // display a single word.
        //ArrayAdapter<Word> itemAdapter=new ArrayAdapter<>(this,R.layout.list_item, words);

        //Create a custom word adapter
        WordAdapter adapter = new WordAdapter(this, words, R.color.category_numbers);
        //Log.i("running ", "WorkAdapter object not created");
        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // activity_numbers.xml layout file.
        ListView listview = findViewById(R.id.list);
        //Log.i("running: ", "ListView object created successfully");
        // Make the {@link ListView} use the {@link ArrayAdapter} we created above, so that the
        // {@link ListView} will display list items for each word in the list of words.
        // Do this by calling the setAdapter method on the {@link ListView} object and pass in
        // 1 argument, which is the {@link ArrayAdapter} with the variable name itemsAdapter.
        listview.setAdapter(adapter);
        //Log.i("runnng: ", "listview.setAdapter(adapter) called");
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

        //Adding listener to handle click events-play audio
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                /*Also release the existing MediaPlayer resources before starting a new audio playback*/
                releaseMediaPlayer();

                mMediaPlayer = MediaPlayer.create(NumbersActivity.this, words.get(position).getAudioResourceID());
                //specifying just this as the context can become ambiguous
                //therefore we write NumberActivity.this
                mMediaPlayer.start();
                //Toast.makeText(NumbersActivity.this,"Playing: "+words.get(position).getmDefaultTranslation(),Toast.LENGTH_SHORT).show();
                /*
                  We need to setup an OnCompletionListener to release the resources once, the audio playback
                  finishes and retrieve the resources.
                  Note- The ListenerObject can also be created outside the onCreate() method and then the
                  object be passed to mMediaPlayer.setOnClickListener(mCompletionListener);
                  https://github.com/udacity/ud839_Miwok/blob/c2a89bc69a3af9e8aa6d37f1fdaa16e3f6dc4514/app/src/main/java/com/example/android/miwok/NumbersActivity.java
                 */
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        releaseMediaPlayer();
                    }
                });
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStop() {
        super.onStop();
        /*This is important because we should release the resources once the user switches to some
        other activity and the current activity is removed from the screen. The audio will stop playing
        once the user hits the home button/back button or recent apps button.
        For more details refer the Android Documentation of release(), onPause() and onStop() methods.
         */
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void releaseMediaPlayer() {
        //If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null)
            //Regardless of the current state of the media player, release its resources
            //because we no longer need it.
            mMediaPlayer.release();
        //Set the media player back to null. For our code setting the media player
        //back to null is an easy way of telling that the mediaplayer is not
        //configured to play an audio file at the moment.
        mMediaPlayer = null;
        Log.i("NumbersActivity/: ", "resources released successfully");
        try {
            mAudioManager.abandonAudioFocusRequest(mAudioFocusRequest);

        }
        catch(Exception e)
        {
            Log.e("ColorsActivity/: ","Error occured 2: "+e.toString());
        }
        //Toast.makeText(NumbersActivity.this,"resources released",Toast.LENGTH_LONG).show();
    }
}