package barqsoft.footballscores.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.helper.Utilies;
import barqsoft.footballscores.service.StackWidgetService;

public class StackWidgetProvider extends AppWidgetProvider {

    private static final String DISPLAY_ACTION = "barqsoft.footablscores.DISPLAY_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == DISPLAY_ACTION) {
            long matchId = intent.getExtras().getLong(Utilies.INTENT_EXTRAS_MATCH_ID_IDENTIFIER);
            Intent displayIntent = new Intent(context, MainActivity.class);
            displayIntent.putExtra(Utilies.INTENT_EXTRAS_MATCH_ID_IDENTIFIER, matchId);
            displayIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(displayIntent);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {

            // set intent for widget service that will create the views
            Intent serviceIntent = new Intent(context, StackWidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.stack_widget_provider_layout);
            remoteViews.setRemoteAdapter(appWidgetIds[i], R.id.stack_widget_view, serviceIntent);
            remoteViews.setEmptyView(R.id.stack_widget_view, R.id.stack_widget_empty_view);

            // set intent for item click (opens main activity)
            Intent viewIntent = new Intent(context, StackWidgetProvider.class);
            viewIntent.setAction(StackWidgetProvider.DISPLAY_ACTION);
            viewIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            viewIntent.setData(Uri.parse(viewIntent.toUri(Intent.URI_INTENT_SCHEME)));

            PendingIntent viewPendingIntent = PendingIntent.getBroadcast(context, 0, viewIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.stack_widget_view, viewPendingIntent);

            // update widget
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
