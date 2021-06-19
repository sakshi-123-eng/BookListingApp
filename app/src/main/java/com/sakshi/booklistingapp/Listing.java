package com.sakshi.booklistingapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Listing extends AppCompatActivity {


    EditText keyword;
    ImageView searchBtn;
    LinearLayout linearLayout;
    ListView listView;
    TextView textView;
    private BookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        keyword = findViewById( R.id.editText );
        linearLayout = findViewById( R.id.idLLsearch );
        listView = findViewById( R.id.list );
        textView = findViewById( R.id.view_text );

        searchBtn = findViewById( R.id.search );
        searchBtn.setOnClickListener( new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                // checking if our editText field is empty or not.
                if (keyword.getText().toString().isEmpty()) {
                    keyword.setError( "Please enter search query" );
                    return;
                }

                // if listView will be shown then, below code will be used to hide actionbar
                if (getSupportActionBar() != null) {
                    getSupportActionBar().hide();
                }
                // to make linearlayout used for searching invisible so that listView can be shown on the screen
                linearLayout.setVisibility( View.INVISIBLE );

                // To make " List of Books [Book Name entered in editText]" visible
                textView.setVisibility( View.VISIBLE );

                // To show " List of Books [Book Name entered in editText]" text on the top of listView
                textView.setText( "List of Books of " + keyword.getText().toString() );

                // To make it Visible before showing list of books in ListView
                listView.setVisibility( View.VISIBLE );

                // if the search query is not empty then we are
                // calling getBookInfo method to load all
                // the books from the API
                getBookInfo( keyword.getText().toString() );


            }
        });

    }




    private void getBookInfo(String query){
        /** URL for books data from the Google Book API */
        final String USGS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q="+ query+"&orderBy=relevance&printType=books&maxResults=30";

        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of BookInfo's as input
        mAdapter = new BookAdapter(this, new ArrayList<BookInfo>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated
        bookListView.setAdapter(mAdapter);
        bookListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // find the current that was clicked on
                BookInfo currentBook = mAdapter.getItem( position );

                // Convert the string URL into a URI object (to pass into the Intent constructor)
                Uri bookInfoUri = Uri.parse( currentBook.getUrl() );

                // Create a new intent to launch a new activity
                Intent websiteIntent = new Intent( Intent.ACTION_VIEW, bookInfoUri );

                // Send the intent to launch a new activity
                startActivity( websiteIntent );
            }
        } );




        // Start the AsyncTask to fetch the BookInfo data
        BooksAsyncTask task = new BooksAsyncTask();
        task.execute(USGS_REQUEST_URL);

    }



    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the list of earthquakes in the response.
     *
     * AsyncTask has three generic parameters: the input type, a type used for progress updates, and
     * an output type. Our task will take a String URL, and return an BookInfo. We won't do
     * progress updates, so the second generic is just Void.
     *
     * We'll only override two of the methods of AsyncTask: doInBackground() and onPostExecute().
     * The doInBackground() method runs on a background thread, so it can run long-running code
     * (like network activity), without interfering with the responsiveness of the app.
     * Then onPostExecute() is passed the result of doInBackground() method, but runs on the
     * UI thread, so it can use the produced data to update the UI.
     */
    private class BooksAsyncTask extends AsyncTask<String, Void, List<BookInfo>> {

        /**
         * This method runs on a background thread and performs the network request.
         * We should not update the UI from a background thread, so we return a list of
         * {@link BookInfo}s as the result.
         */
        @Override
        protected List<BookInfo> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<BookInfo> result = QueryUtils.fetchBookInfoData(urls[0]);
            return result;
        }

        /**
         * This method runs on the main UI thread after the background work has been
         * completed. This method receives as input, the return value from the doInBackground()
         * method. First we clear out the adapter, to get rid of BookInfo data from a previous
         * query to Google Book API. Then we update the adapter with the new list of bookinfo,
         * which will trigger the ListView to re-populate its list items.
         */
        @Override
        protected void onPostExecute(List<BookInfo> data) {
            // Clear the adapter of previous bookinfo data
            mAdapter.clear();


            // If there is a valid list of {@link BookInfo}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }
    }
}
