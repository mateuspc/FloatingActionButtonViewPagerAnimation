package danielrocha.floatingbuttonanimation;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.Toast;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private ViewPagerAdapter adapter;
    private int widthFab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FloatingActionButton Animation");

        initViewPager();
        initTabLayout();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewPager.getCurrentItem() == 0) {
                    Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Menu", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        if(viewPager != null && viewPager.getCurrentItem() == 1) {
            viewPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }

    private void initViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "HOME");
        adapter.addFragment(new MenuFragment(), "MENU");
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                final float scale = getResources().getDisplayMetrics().density;
                int dp = (int) (16 * scale);
                widthFab = viewPager.getWidth();
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    widthFab = viewPager.getWidth() - dp;
                }
                if(position == 0) {
                    // Transition from page 0 to page 1 (horizontal shift)
                    int translationFabX = (int) (((widthFab - fab.getWidth()) / 2f) * positionOffset);
                    fab.setTranslationX(translationFabX);
                    fab.setTranslationY(0);
                }
            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        fab.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_share_white_24dp));
                        break;
                    case 1:
                        fab.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_done_white_24dp));
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        changePagerScroller(viewPager);

        viewPager.setCurrentItem(0);
    }

    private void initTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
    }

    public void changePagerScroller(ViewPager mViewPager) {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(mViewPager.getContext());
            mScroller.set(mViewPager, scroller);
        } catch (Exception e) {
            Log.e("SCROLL", "error of change scroller ", e);
        }
    }

    public class ViewPagerScroller extends Scroller {
        private int mScrollDuration = 600;
        public ViewPagerScroller(Context context) {
            super(context);
        }
        public ViewPagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }
        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        }
        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        }
    }

}
