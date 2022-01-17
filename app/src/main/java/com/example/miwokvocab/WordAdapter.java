package com.example.miwokvocab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> {
    /*A data member to store the color attribute*/
    private final int mColorID;
    public WordAdapter(Context context, ArrayList<Word> words,int colorID) {
        //https://github.com/devayush20/ud839_CustomAdapter_Example/blob/master/app/src/main/java/com/example/android/flavor/AndroidFlavorAdapter.java
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, words);
        mColorID=colorID;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent); we don't need this as wee need to modify this
        View listItemView = convertView; //assigned for more clearance that the
        //scrapped view is a ListView type
        //Now check if the view is null
        if (listItemView == null) {
            //if the view is null, inflate a new view from the XML
            //layout file
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            //we pass the layout resource id, the parent view and the boolean false because we don't want to
            //attach the list item view to the list view just yet.
        }
        Word currentWord = getItem(position);
        //int: The position of the item within the adapter's
        // data set of the item whose view we want.
        TextView miwokTranslation = (TextView) listItemView.findViewById(R.id.miwok_textview);
        //the listItemView is referencing the root linear view, therefore on calling the findViewById on it
        //we get the corresponding TextView which is stored to miwokTranslation.
        //next, set the new text
        miwokTranslation.setText(currentWord.getmMiwokTranslation());
        //now similarly for the english translation
        TextView defaultTranslation = (TextView) listItemView.findViewById(R.id.default_textview);
        defaultTranslation.setText(currentWord.getmDefaultTranslation());
        //now similarly for the image
        ImageView image = (ImageView) listItemView.findViewById(R.id.image);

        if (currentWord.hasImage()) {
            //ImageView image = (ImageView) listItemView.findViewById(R.id.image);
            image.setImageResource(currentWord.getImageResourceID());
            //Ensure that the view is visible again, if it was hidden before.
            image.setVisibility(View.VISIBLE);
        } else {
            image.setVisibility(View.GONE);//GONE means that the image is not
            // visible and doesn't take any space for the view
        }
        //Add the background color to the list item in the activity
        View wordContainer=listItemView.findViewById(R.id.text_container);
        int color= ContextCompat.getColor(getContext(),mColorID);
        wordContainer.setBackgroundColor(color);
        View playButtonContainer=listItemView.findViewById(R.id.play_button_image_view);
        playButtonContainer.setBackgroundColor(color);
        //Now return the whole List item layout (containing 2 TextViews) so that it can be shown
        //in the ListView
        return listItemView;

    }
}
