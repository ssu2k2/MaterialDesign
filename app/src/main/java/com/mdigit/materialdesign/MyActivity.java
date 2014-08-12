package com.mdigit.materialdesign;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MyActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private final static String TAG = "MyActivity";
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    public static int[] iColorArray = {
            R.array.red,
            R.array.pink,
            R.array.purple,
            R.array.deep_purple,
            R.array.indigo,
            R.array.blue,
            R.array.light_blue,
            R.array.cyan,
            R.array.teal,
            R.array.green,
            R.array.light_green,
            R.array.lime,
            R.array.yellow,
            R.array.amber,
            R.array.orange,
            R.array.deep_orange,
            R.array.brown,
            R.array.grey,
            R.array.blue_grey
    };
    private String[] sColorName;

    private String   sSelColor;
    private String[] sColorList;
    private int curPosition = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        sColorName = getResources().getStringArray(R.array.color_list);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(getBaseContext(), position))
                .commit();
        curPosition = position;
    }

    public void onSectionAttached(int number) {
        if(sColorName.length >= number) {
            sColorList = getResources().getStringArray(iColorArray[number]);
            sSelColor = sColorList[0];
            mTitle = sColorName[number] +"(" + sSelColor+")";
        }
        Log.d(TAG, "Set Title : " + mTitle);
    }

    public void changeColor() {
        Log.d(TAG, "Select Color : " + sSelColor);
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(sSelColor)));
    }
    private static final int SET_ACTIONBAR = 0x0100;
    private static final int SET_CONTENT_BACK = 0x0101;
    private static String sBarColor = "#212121";
    private static String sBackColor = "#ffffff";
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case SET_ACTIONBAR:
                    getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(sBarColor)));
                    break;
                case SET_CONTENT_BACK:
                    PlaceholderFragment.newInstance(getBaseContext(), curPosition).setBackColor(sBackColor);
                    break;
                default:
                    break;
            }
        }
    };



    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        actionBar.hide();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.my, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static int iSection;
        private static Context mContext;
        private static String sBackColor = "#ffffff";
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(Context context, int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            Log.d(TAG, "sectionNumber : " + sectionNumber);
            iSection = sectionNumber;
            mContext = context;
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {

        }
        public void setBackColor(String backColor) {
            sBackColor = backColor;
            rootView.setBackgroundColor(Color.parseColor(sBackColor));
        }
        View rootView;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_my, container, false);
            ListView lvItem = (ListView)rootView.findViewById(R.id.lvColorTable);
            ItemAdapter iAdapter = new ItemAdapter(mContext, R.layout.color_item, iSection );
            lvItem.setAdapter(iAdapter);
            setBackColor(sBackColor);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MyActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    private static class ItemAdapter extends ArrayAdapter<String> {
        Context mContext;
        String[] sColorValue;
        int resId;
        public ItemAdapter(Context context, int resId, int color){
            super(context, resId);
            this.mContext = context;
            this.resId = resId;
            sColorValue = mContext.getResources().getStringArray(iColorArray[color]);
        }
        @Override
        public String getItem(int position) {
            return sColorValue[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return sColorValue.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if(convertView == null) {
                LayoutInflater li = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = li.inflate(resId, null);
            } else {
                view = convertView;
            }

            int color = Color.parseColor(sColorValue[position]);
            ((TextView)view.findViewById(R.id.tvItem)).setText( sColorValue[position] );
            ((TextView)view.findViewById(R.id.tvItem)).setShadowLayer(2, 0, 0, Color.WHITE);
            ((TextView)view.findViewById(R.id.tvItem)).setBackgroundColor(color);

            return view;
        }
    }

}
