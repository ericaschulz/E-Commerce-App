package com.els.yourwaycafe;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class FoodMenuActivity extends AppCompatActivity {

    CursorAdapter mCursorAdapterSe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        ListView mProteinListView;
        ListView mSidesListView;
        ListView mDrinksListView;
        CursorAdapter mProteinAdapter;
        CursorAdapter mSidesAdapter;
        CursorAdapter mDrinksAdapter;
        AdapterView.OnItemClickListener mListener;





//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        mProteinListView = (ListView)findViewById(R.id.list_view_proteins);
        mSidesListView = (ListView)findViewById(R.id.list_view_sides);
        mDrinksListView = (ListView)findViewById(R.id.list_view_drinks);

        YWCSQLiteOpenHelper db = new YWCSQLiteOpenHelper(FoodMenuActivity.this);
        Cursor cursor = db.getProteinsList();
        cursor.moveToFirst();

        mProteinAdapter = new SimpleCursorAdapter(this,R.layout.activity_proteins,cursor,new String[]{YWCSQLiteOpenHelper.COL_ITEM_NAME},new int[]{R.id.proteins_text},0);
        assert mProteinListView != null;
        mProteinListView.setAdapter(mProteinAdapter);

        YWCSQLiteOpenHelper dbS = new YWCSQLiteOpenHelper(FoodMenuActivity.this);
        Cursor c = dbS.getSidesList();
        cursor.moveToFirst();
        mSidesAdapter = new SimpleCursorAdapter(this,R.layout.activity_sides,c,new String[]{YWCSQLiteOpenHelper.COL_ITEM_NAME},new int[]{R.id.sides_text},0);
        assert mSidesListView != null;
        mSidesListView.setAdapter(mSidesAdapter);

        YWCSQLiteOpenHelper dbD = new YWCSQLiteOpenHelper(FoodMenuActivity.this);
        Cursor cu = dbD.getDrinksList();
        cu.moveToFirst();
        mDrinksAdapter = new SimpleCursorAdapter(this,R.layout.activity_drinks,cu,new String[]{YWCSQLiteOpenHelper.COL_ITEM_NAME}, new int[]{R.id.drinks_text},0);
        assert mDrinksListView != null;
        mDrinksListView.setAdapter(mDrinksAdapter);

        mListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position, long id){

                Toast.makeText(FoodMenuActivity.this,"Added to your cart!",Toast.LENGTH_SHORT).show();

            }
        };

        mProteinListView.setOnItemClickListener(mListener);
        mSidesListView.setOnItemClickListener(mListener);
        mDrinksListView.setOnItemClickListener(mListener);


        handleIntent(getIntent());




        ImageButton mCheckout = (ImageButton) findViewById(R.id.checkout);
        assert mCheckout != null;
        mCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntentCart = new Intent(FoodMenuActivity.this, ShoppingCartActivity.class);
                startActivity(mIntentCart);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Look for your favorites here");

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {


        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            YWCSQLiteOpenHelper dbSe = new YWCSQLiteOpenHelper(FoodMenuActivity.this);
            dbSe.getReadableDatabase();
            String query = intent.getStringExtra(SearchManager.QUERY);
            Cursor cs = dbSe.searchSidesList(query);
            Cursor cp = dbSe.searchProteinList(query);
            Cursor cd = dbSe.searchDrinkList(query);
            mCursorAdapterSe.changeCursor(cs);
            mCursorAdapterSe.changeCursor(cp);
            mCursorAdapterSe.changeCursor(cd);
            mCursorAdapterSe.notifyDataSetChanged();

       }



    }
}




