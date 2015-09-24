package barqsoft.footballscores.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.fragment.adapter.ScoresAdapter;
import barqsoft.footballscores.helper.Utilies;
import barqsoft.footballscores.helper.ViewHolder;
import barqsoft.footballscores.provider.ScoresContract;
import barqsoft.footballscores.service.FetchingService;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainScreenFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public ScoresAdapter scoresAdapter;
    public static final int SCORES_LOADER_ID = 0;
    private String[] fragmentDate = new String[1];
    private int lastSelectedItem = -1;

    public MainScreenFragment() {
    }

    private void updateScores() {
        Intent serviceStart = new Intent(getActivity(), FetchingService.class);
        getActivity().startService(serviceStart);
    }

    public void setFragmentDate(String date) {
        fragmentDate[0] = date;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        updateScores();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final ListView scoreList = (ListView) rootView.findViewById(R.id.scores_list);
        scoresAdapter = new ScoresAdapter(getActivity(), null, 0);
        scoreList.setAdapter(scoresAdapter);
        getLoaderManager().initLoader(SCORES_LOADER_ID, null, this);
        scoresAdapter.DETAIL_MATCH_ID = MainActivity.SELECTED_MATCH_ID;
        scoreList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewHolder selectedView = (ViewHolder) view.getTag();
                scoresAdapter.DETAIL_MATCH_ID = selectedView.id;
                MainActivity.SELECTED_MATCH_ID = (int) selectedView.id;
                scoresAdapter.notifyDataSetChanged();
            }
        });
        TextView emptyTextView = (TextView) rootView.findViewById(R.id.no_match_textview);
        scoreList.setEmptyView(emptyTextView);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), ScoresContract.ScoresTable.buildScoreWithDate(),
                null, null, fragmentDate, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        Log.v(FetchingService.LOG_TAG, "Loader finished");
/*        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Log.v(FetchingService.LOG_TAG, cursor.getString(1));
            cursor.moveToNext();
        }*/

        int i = 0;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                i++;
                cursor.moveToNext();
            }
            Log.v(FetchingService.LOG_TAG, "Loader query: " + String.valueOf(i));
            scoresAdapter.swapCursor(cursor);
            scoresAdapter.notifyDataSetChanged();
        } else {
            if (!Utilies.isNetworkConnected(getActivity())) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rootView = inflater.inflate(R.layout.fragment_main, null);
                TextView textView = (TextView) rootView.findViewById(R.id.no_match_textview);
                scoresAdapter.setEmptyView(textView);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        scoresAdapter.swapCursor(null);
    }
}
