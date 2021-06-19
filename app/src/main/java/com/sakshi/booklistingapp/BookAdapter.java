package com.sakshi.booklistingapp;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.bumptech.glide.Glide;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class BookAdapter extends ArrayAdapter<BookInfo> {

    public BookAdapter(@NonNull Context context, List<BookInfo> earthquakes) {
        super( context, 0, earthquakes);
    }


    /**
     * Returns a list item view that displays information about the book given at position
     * in the list of books
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from( getContext() ).inflate(
                    R.layout.book_list, parent, false );
        }


        // Find the book at the given position in the list of books
        BookInfo currentBook = getItem( position );


        // Find the TextView with view ID title
        TextView title = (TextView) listItemView.findViewById( R.id.title );

        // Display the title of the current book in that TextView
        title.setText( currentBook.getTitle() );


        // Find the ImageView with view ID img
        ImageView imageView = (ImageView)listItemView.findViewById( R.id.img );

        // loading image from web url link to imageview
        try{
            URL imageUrl = new URL( currentBook.getImg() );
            Glide.with(getContext()).load(imageUrl).into(imageView);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }




        return listItemView;
    }
}