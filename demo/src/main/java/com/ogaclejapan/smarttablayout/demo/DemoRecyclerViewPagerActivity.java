package com.ogaclejapan.smarttablayout.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import me.relex.recyclerpager.FragmentRecyclerAdapter;
import me.relex.smarttablayout.SmartTabLayout2;

public class DemoRecyclerViewPagerActivity extends AppCompatActivity {

    private static final String KEY_DEMO = "demo";

    public static void startActivity(Context context, Demo demo) {
        Intent intent = new Intent(context, DemoRecyclerViewPagerActivity.class);
        intent.putExtra(KEY_DEMO, demo.name());
        context.startActivity(intent);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_pager);

        Demo demo = getDemo();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(demo.titleResId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewGroup tab = (ViewGroup) findViewById(R.id.tab);
        tab.addView(LayoutInflater.from(this).inflate(demo.layoutResId, tab, false));

        SmartTabLayout2 tabLayout = (SmartTabLayout2) findViewById(R.id.viewpagertab);

        final FragmentAdapter adapter =
                new FragmentAdapter(demo.tabs(), getSupportFragmentManager());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);

        tabLayout.attachToRecyclerView(recyclerView, pagerSnapHelper);
        adapter.registerAdapterDataObserver(tabLayout.getAdapterDataObserver());

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                adapter.remove();
            }
        });
    }

    private class FragmentAdapter extends FragmentRecyclerAdapter {

        private int[] tabs;

        FragmentAdapter(int[] tabs, FragmentManager fm) {
            super(fm);
            this.tabs = tabs;
        }

        @Override public Fragment getItem(int position) {
            return FragmentPagerItem.of("", DemoFragment.class)
                    .instantiate(DemoRecyclerViewPagerActivity.this, position);
        }

        @Override public int getItemCount() {
            return tabs.length;
        }

        @Nullable @Override public CharSequence getPageTitle(int position) {
            return getString(tabs[position]);
        }

        void remove() {
            if (tabs.length == 2) {
                return;
            }
            tabs = removeIntArray(tabs);
            notifyItemRemoved(tabs.length);
        }

        private int[] removeIntArray(int[] arrays) {
            int size = arrays.length - 1;
            int[] result = new int[size];
            System.arraycopy(arrays, 0, result, 0, size);
            return result;
        }
    }

    private Demo getDemo() {
        return Demo.valueOf(getIntent().getStringExtra(KEY_DEMO));
    }
}
