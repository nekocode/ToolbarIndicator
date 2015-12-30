package cn.nekocode.toolbarindicatorsample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.nekocode.toolbarindicator.ToolbarIndicator;

public class MainActivity extends AppCompatActivity {
    private ToolbarIndicator toolbarIndicator;
    private ViewPager viewPager;
    private MyFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbarIndicator = (ToolbarIndicator) this.findViewById(R.id.indicator);
        viewPager = (ViewPager) this.findViewById(R.id.viewPager);
        adapter = new MyFragmentAdapter(this.getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        toolbarIndicator.setViewPager(viewPager);
    }

    class MyFragmentAdapter extends FragmentPagerAdapter {
        private TestFragment fragments[] = new TestFragment[3];
        private String title[] = new String[]{
              "Tab1", "Tab2", "Tab3"
        };

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
            fragments[0] = new TestFragment();
            fragments[1] = new TestFragment();
            fragments[2] = new TestFragment();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public String getPageTitle(int position) {
            return title[position];
        }
    }
}
