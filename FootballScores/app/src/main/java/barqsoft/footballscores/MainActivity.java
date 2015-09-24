package barqsoft.footballscores;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import barqsoft.footballscores.fragment.AboutFragment;
import barqsoft.footballscores.fragment.PagerFragment;
import barqsoft.footballscores.helper.Utilies;
import barqsoft.footballscores.model.MatchModel;
import barqsoft.footballscores.service.FetchingService;

public class MainActivity extends ActionBarActivity {
    public static String LOG_TAG = MainActivity.class.getSimpleName();

    public static String ACTION_VIEW = "barqsoft.footballscores.MainActivity.ACTION_VIEW";

    public static int SELECTED_MATCH_ID;
    public static int CURRENT_FRAGMENT = 2;
    private PagerFragment pagerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            pagerFragment = new PagerFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, pagerFragment)
                    .commit();
        }

        ArrayList<MatchModel> allMatchesOfTodayAsArrayList = Utilies.getAllMatchesOfTodayAsArrayList(this);
        for (int i = 0; i < allMatchesOfTodayAsArrayList.size(); i++) {
            Log.v(LOG_TAG, allMatchesOfTodayAsArrayList.get(i).getHomeTeamName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            //noinspection SimplifiableIfStatement
            case R.id.action_about:
                AboutFragment aboutFragment = new AboutFragment();
                aboutFragment.show(getFragmentManager(), "Dialog");
                return true;
            case R.id.action_refresh:
                updateData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

/*    private void refreshPage() {
        updateData();
        pagerFragment = new PagerFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, pagerFragment)
                .commit();
    }*/

    private void updateData() {
        Intent serviceIntent = new Intent(MainActivity.this, FetchingService.class);
        startService(serviceIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Utilies.SAVE_INSTANCE_STATE_SCURRENT_PAGE, pagerFragment.viewPager.getCurrentItem());
        outState.putInt(Utilies.SAVE_INSTANCE_STATE_SELECTED_MATCH_ID, SELECTED_MATCH_ID);
        getSupportFragmentManager().putFragment(outState, Utilies.SAVE_INSTANCE_STATE_PAGER_FRAMENT, pagerFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        CURRENT_FRAGMENT = savedInstanceState.getInt(Utilies.SAVE_INSTANCE_STATE_SCURRENT_PAGE);
        SELECTED_MATCH_ID = savedInstanceState.getInt(Utilies.SAVE_INSTANCE_STATE_SELECTED_MATCH_ID);
        pagerFragment = (PagerFragment) getSupportFragmentManager().getFragment(savedInstanceState, Utilies.SAVE_INSTANCE_STATE_PAGER_FRAMENT);
        super.onRestoreInstanceState(savedInstanceState);
    }

    public static Context getContext(){
        return MainActivity.getContext();
    }

}
