package com.example.miwokvocab;

import androidx.annotation.NonNull;

/**
 * {@link Word} represents a vocabulary word that the user wants to learn.
 * It contains a default translation and a Miwok translation for that word.
 */
public class Word {
    //Default translation of word"
    private final String mDefaultTranslation;
    //Miwok translation for the word
    private final String mMiwokTranslation;
    //Image resource variable;
    private final int NO_IMAGE_PROVIDED = -1;
    //audio resource id
    private final int audioResourceID;
    private int imageResourceID = NO_IMAGE_PROVIDED;

    /*
     * Create a new Word object.
     *
     * @param defaultTranslation is the word in a language that the user is already familiar with
     *                           (such as English)
     * @param miwokTranslation is the word in the Miwok language
     */
    public Word(String defaultTranslation, String miwokTranslation, int audioID) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        audioResourceID = audioID;
    }

    public Word(String defaultTranslation, String miwokTranslation, int imageID, int audioID) {
        mMiwokTranslation = miwokTranslation;
        mDefaultTranslation = defaultTranslation;
        imageResourceID = imageID;
        audioResourceID = audioID;
    }

    /*Define the getter methods*/
    public String getmDefaultTranslation() {
        return mDefaultTranslation;
    }

    public String getmMiwokTranslation() {
        return mMiwokTranslation;
    }

    public int getImageResourceID() {
        return imageResourceID;
    }

    /*hasImage() method returns a boolean value- true if image is set
    and false if image is not set*/
    public boolean hasImage() {
        return imageResourceID != NO_IMAGE_PROVIDED;
    }

    public int getAudioResourceID() {
        return audioResourceID;
    }

    @NonNull
    @Override
    public String toString() {
        return "Word{" +
                "mDefaultTranslation='" + mDefaultTranslation + '\'' +
                ", mMiwokTranslation='" + mMiwokTranslation + '\'' +
                ", NO_IMAGE_PROVIDED=" + NO_IMAGE_PROVIDED +
                ", audioResourceID=" + audioResourceID +
                ", imageResourceID=" + imageResourceID +
                '}';
    }
}
