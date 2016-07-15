package com.example.subhrajyoti.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements MultiSelectRecyclerViewAdapter.ViewHolder.ClickListener {


    private android.support.v7.view.ActionMode actionMode;

    private RecyclerView mRecyclerView;
    private MultiSelectRecyclerViewAdapter mAdapter;
    private ArrayList<String> mArrayList  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        for (int i = 1; i <= 50; i++) {

            mArrayList.add ("Item " + i);
        }

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar()!=null)
                getSupportActionBar().setTitle("MultiSelectRecyclerView");

        }

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        assert mRecyclerView != null;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MultiSelectRecyclerViewAdapter (MainActivity.this,mArrayList,this);
        mRecyclerView.setAdapter (mAdapter);

        SwipeableListener swipeableListener = new SwipeableListener(mRecyclerView, new SwipeableListener.SwipeListener() {
            @Override
            public boolean canSwipe(int position) {
                return true;
            }

            @Override
            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                for (int i : reverseSortedPositions)
                    mArrayList.remove(i);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                for (int i : reverseSortedPositions)
                    mArrayList.remove(i);
                mAdapter.notifyDataSetChanged();
            }
        });

        mRecyclerView.addOnItemTouchListener(swipeableListener);


    }

    @Override
    public void onItemClicked (int position) {
        if (actionMode!=null)
            toggleSelection(position);
    }

    @Override
    public boolean onItemLongClicked (final int position) {

        if (actionMode==null) {
            actionMode = this.startSupportActionMode(new android.support.v7.view.ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.menu, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(android.support.v7.view.ActionMode mode, MenuItem item) {
                    for (int i = mAdapter.getSelectedItemCount(); i >= 1; i--) {
                        mArrayList.remove(mArrayList.get(mAdapter.getSelectedItems().get(i - 1)));
                        Toast.makeText(MainActivity.this, mAdapter.getSelectedItems().get(i - 1) + "", Toast.LENGTH_SHORT).show();
                    }
                    mAdapter.notifyDataSetChanged();
                    mAdapter.clearSelection();
                    mode.finish();
                    return true;
                }

                @Override
                public void onDestroyActionMode(android.support.v7.view.ActionMode mode) {
                    actionMode = null;
                    mAdapter.clearSelection();
                }
            });
            mRecyclerView.getChildAt(position).setSelected(true);
            toggleSelection(position);
        }


        return true;

    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection (position);

        if (mAdapter.getSelectedItemCount()==0)
            actionMode.finish();

    }

}