package barqsoft.footballscores.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import barqsoft.footballscores.R;
import barqsoft.footballscores.helper.Utilies;
import barqsoft.footballscores.model.MatchModel;

public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private final String LOG_TAG = StackRemoteViewsFactory.class.getSimpleName();

        private ArrayList<MatchModel> matchesArrayList;

        private Context context;
        private int appWidgetId;

        public StackRemoteViewsFactory(Context context, Intent intent) {
            this.context = context;
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            initData();
        }

        @Override
        public void onDataSetChanged() {
            initData();
        }

        @Override
        public void onDestroy() {
            matchesArrayList.clear();
        }

        @Override
        public int getCount() {
            return matchesArrayList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.scores_item);

            if (position < getCount()) {
                MatchModel match = matchesArrayList.get(position);

                String homeTeamName = match.getHomeTeamName();
                Log.v(LOG_TAG, "homeTeamName: " + homeTeamName);
                remoteViews.setTextViewText(R.id.home_name, homeTeamName);
                remoteViews.setTextColor(R.id.home_name, getResources().getColor(R.color.blue03));
                String awayTeamName = match.getAwayTeamName();
                Log.v(LOG_TAG, "awayTeamName: " + awayTeamName);
                remoteViews.setTextViewText(R.id.away_name, awayTeamName);
                remoteViews.setTextColor(R.id.away_name, getResources().getColor(R.color.blue03));
                remoteViews.setTextViewText(R.id.score_textview, Utilies.getScores(match.getHomeTeamGoals(), match.getAwayTeamGoals()));
                remoteViews.setTextColor(R.id.score_textview, getResources().getColor(R.color.orange03));
                remoteViews.setTextViewText(R.id.date_textview, match.getMatchDate());
                remoteViews.setTextColor(R.id.date_textview, getResources().getColor(R.color.green03));
                remoteViews.setImageViewResource(R.id.home_crest, Utilies.getTeamCrestByTeamName(match.getHomeTeamName()));
                remoteViews.setImageViewResource(R.id.away_crest, Utilies.getTeamCrestByTeamName(match.getAwayTeamName()));

                Bundle extras = new Bundle();
                extras.putLong(Utilies.INTENT_EXTRAS_MATCH_ID_IDENTIFIER, match.getMatchId());
                Intent fillIntent = new Intent();
                fillIntent.putExtras(extras);
                remoteViews.setOnClickFillInIntent(R.id.scores_item, fillIntent);
            }

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        private void initData() {
            matchesArrayList = new ArrayList<>();
            matchesArrayList.clear();
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        matchesArrayList = Utilies.getAllMatchesOfTodayAsArrayList(context);
                    }
                }).start();
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
