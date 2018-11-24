package mjimeno.mybooks2.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import mjimeno.mybooks2.Fragments.BookDetailFragment;
import mjimeno.mybooks2.Models.Book;
import mjimeno.mybooks2.R;

import static mjimeno.mybooks2.Fragments.BookDetailFragment.ARG_ITEM_ID;

//import android.support.v4.app.

/**
 * An activity representing a single Book detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link BookListActivity}.
 */
public class BookDetailActivity extends AppCompatActivity {
private WebView myWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        /*
        myWebview = (WebView)findViewById(R.id.myWebView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myWebview.setVisibility(View.VISIBLE);
                myWebview.getSettings().setJavaScriptEnabled(true);
                myWebview.loadUrl("file:///android_asset/form.html");
                myWebview.setWebViewClient(new MyWebClient());
            }
        });

*/
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
          //Crea un objeto bundle,captura los argumentos y añade un fragmento a la actividad

            Bundle arguments = new Bundle();
            arguments.putString(ARG_ITEM_ID,
                    getIntent().getStringExtra(ARG_ITEM_ID));
            BookDetailFragment fragment = new BookDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.book_detail_container, fragment)
                    //.addToBackStack(null)
                    .commit();


        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) { //este ID representa el boton home para luego volver a la actividad BookListActivity

            navigateUpTo(new Intent(this, BookListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyWebClient extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            Uri uri = Uri.parse(request.getUrl().toString());
            String name = uri.getQueryParameter("name");
            String num = uri.getQueryParameter("num");
            String date = uri.getQueryParameter("date");

            if (!name.isEmpty() && !num.isEmpty() && !date.isEmpty())
            {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.compra_correcta),Toast.LENGTH_LONG).show();
                myWebview.setVisibility(View.GONE);
            }
            else{
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.compra_faltandatos),Toast.LENGTH_LONG).show();

            }

            return  false;
        }
    }
}
