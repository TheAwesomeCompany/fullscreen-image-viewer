package com.fullscreen.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fullscreen.R;
import com.fullscreen.activity.FullScreenActivity;
import com.fullscreen.util.ImageLoaderUtils;
import com.fullscreen.zoom.HideBarController;
import com.fullscreen.zoom.TouchImageView;
import com.fullscreen.zoom.callback.IHideControllerCallback;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by dima on 4/15/15.
 */
public class FullScreenPhotoFragment extends Fragment {
    private static final String TAG = FullScreenPhotoFragment.class.getSimpleName();

    public static final String BUNDLE_IMAGE_URL = "resource_id";
    public static final String BUNDLE_MARKET_PACKAGE = "market_package";

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private TextView mIndicator;

    private String mMarketPackage;
    private int mPromosSize;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(getTargetLayout(), container, false);

        setHasOptionsMenu(true);

        Intent intent = getActivity().getIntent();
        final String[] promosUrl = intent.getStringArrayExtra(BUNDLE_IMAGE_URL);
        mPromosSize = promosUrl.length;
        mMarketPackage = intent.getStringExtra(BUNDLE_MARKET_PACKAGE);

        mPager = (ViewPager) root.findViewById(R.id.viewPager);
        mIndicator = (TextView) root.findViewById(R.id.pageIndicator);
        mPagerAdapter = new ImagePagerAdapter(getActivity(), promosUrl);
        mPager.setAdapter(mPagerAdapter);
        setItemIndicator(0);

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setItemIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return root;
    }

    private void setItemIndicator(int position) {
        if (mPromosSize > 0) {
            mIndicator.setText(position + 1 + "/" + mPromosSize);
        } else {
            mIndicator.setText(position + 0 + "/" + mPromosSize);
        }
    }

    protected int getTargetLayout() {
        return R.layout.fragment_image_pager;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (mMarketPackage != null) {
            inflater.inflate(R.menu.menu_details, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_market) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String uri = String.format("market://details?id=%s", mMarketPackage);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String uri = String.format("http://play.google.com/store/apps/details?id=%s", mMarketPackage);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ImagePagerAdapter extends PagerAdapter {
        public String[] mPortfolioPromos;
        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private ImageLoader imageLoader;
        private HideBarController mController;

        private Handler mHideHandler = new Handler();

        public ImagePagerAdapter(Context context, String[] promos) {
            mContext = context;
            mPortfolioPromos = promos;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            imageLoader = ImageLoader.getInstance();
            ImageLoaderUtils.initImageLoader(imageLoader, getActivity().getApplicationContext());
        }

        @Override
        public int getCount() {
            return mPortfolioPromos.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position + 1 + "/" + mPortfolioPromos.length;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            mController.onDestroy();
            container.removeView((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mLayoutInflater.inflate(R.layout.layout_big_photo, container, false);

            ImageView imageView = (TouchImageView) view.findViewById(R.id.img_big_photo);
            initHideToolBarController(imageView);

            displayImage(mPortfolioPromos[position], imageView);

            container.addView(view);

            return view;
        }

        private void initHideToolBarController(ImageView imageView) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            mController = new HideBarController(imageView);
            mController.setHandler(mHideHandler);
            mController.setHideControllerCallback(new IHideControllerCallback() {
                @Override
                public void onVisibilityChange(boolean visible) {
                    ((FullScreenActivity) getActivity()).moveActionBar(visible);
                    mIndicator.setVisibility(visible ? View.VISIBLE : View.GONE);

                }
            });
            mController.onPostCreate();
        }

        private void displayImage(final String url, final ImageView imageView) {
            imageLoader.displayImage(url, imageView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    imageView.setImageBitmap(loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                }
            });
        }
    }
}
