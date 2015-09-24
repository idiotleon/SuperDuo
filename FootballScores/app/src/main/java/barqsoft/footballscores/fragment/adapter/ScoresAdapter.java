package barqsoft.footballscores.fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import barqsoft.footballscores.R;
import barqsoft.footballscores.helper.Utilies;
import barqsoft.footballscores.helper.ViewHolder;
import barqsoft.footballscores.provider.ScoresContract;

public class ScoresAdapter extends CursorAdapter {
    private static final String LOG_TAG = ScoresAdapter.class.getSimpleName();

    private Context context;

    public double DETAIL_MATCH_ID = 0;
    private String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";

    public ScoresAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        this.context = context;

        View mItem = LayoutInflater.from(context).inflate(R.layout.scores_item, parent, false);
        ViewHolder mHolder = new ViewHolder(mItem);
        mItem.setTag(mHolder);
        Log.v(LOG_TAG, "A new View inflated.");
        return mItem;
    }

    public View setEmptyView(TextView textView) {
        return textView;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.homeTeamName.setText(cursor.getString(cursor.getColumnIndex(ScoresContract.ScoresTable.HOME_TEAM_NAME_COL)));
        viewHolder.awayTeamName.setText(cursor.getString(cursor.getColumnIndex(ScoresContract.ScoresTable.AWAY_TEAM_NAME_COL)));
        viewHolder.date.setText(cursor.getString(cursor.getColumnIndex(ScoresContract.ScoresTable.DATE_COL)));
        viewHolder.score.setText(Utilies.getScores(cursor.getInt(cursor.getColumnIndex(ScoresContract.ScoresTable.HOME_GOALS_COL)),
                cursor.getInt(cursor.getColumnIndex(ScoresContract.ScoresTable.AWAY_GOALS_COL))));
        viewHolder.id = cursor.getLong(cursor.getColumnIndex(ScoresContract.ScoresTable.MATCH_ID));
        viewHolder.homeTeamCrest.setImageResource(Utilies.getTeamCrestByTeamName(
                cursor.getString(cursor.getColumnIndex(ScoresContract.ScoresTable.HOME_TEAM_NAME_COL))));
        viewHolder.awayTeamCrest.setImageResource(Utilies.getTeamCrestByTeamName(
                cursor.getString(cursor.getColumnIndex(ScoresContract.ScoresTable.AWAY_TEAM_NAME_COL))
        ));

        LayoutInflater inflater = (LayoutInflater) context.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View detailedPage = inflater.inflate(R.layout.detail_fragment, null);
        ViewGroup container = (ViewGroup) view.findViewById(R.id.details_fragment_container);
        if (viewHolder.id == DETAIL_MATCH_ID) {
            Log.v(LOG_TAG, "Insert an extra View.");

            container.addView(detailedPage, 0,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
            TextView textViewMatchDay = (TextView) detailedPage.findViewById(R.id.matchday_textview);
            textViewMatchDay.setText(Utilies.getMatchProgress(context, cursor.getInt(cursor.getColumnIndex(ScoresContract.ScoresTable.MATCH_DAY)),
                    cursor.getInt(cursor.getColumnIndex(ScoresContract.ScoresTable.MATCH_DAY))));
            TextView textViewLeague = (TextView) detailedPage.findViewById(R.id.league_textview);
            textViewLeague.setText(Utilies.getLeague(context, cursor.getInt(cursor.getColumnIndex(ScoresContract.ScoresTable.LEAGUE_COL))));
            Button shareButton = (Button) detailedPage.findViewById(R.id.share_button);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add Share Action
                    context.startActivity(createShareForecastIntent(viewHolder.homeTeamName.getText() + " "
                            + viewHolder.score.getText() + " " + viewHolder.awayTeamName.getText()));
                }
            });
        } else {
            container.removeAllViews();
        }
    }

    public Intent createShareForecastIntent(String ShareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + FOOTBALL_SCORES_HASHTAG);
        return shareIntent;
    }
}
