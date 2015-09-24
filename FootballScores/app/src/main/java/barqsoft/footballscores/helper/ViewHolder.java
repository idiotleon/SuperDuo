package barqsoft.footballscores.helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import barqsoft.footballscores.R;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ViewHolder {
    public TextView homeTeamName;
    public TextView awayTeamName;
    public TextView score;
    public TextView date;
    public ImageView homeTeamCrest;
    public ImageView awayTeamCrest;
    public long id = 0;

    public ViewHolder(View view) {
        homeTeamName = (TextView) view.findViewById(R.id.home_name);
        awayTeamName = (TextView) view.findViewById(R.id.away_name);
        score = (TextView) view.findViewById(R.id.score_textview);
        date = (TextView) view.findViewById(R.id.date_textview);
        homeTeamCrest = (ImageView) view.findViewById(R.id.home_crest);
        awayTeamCrest = (ImageView) view.findViewById(R.id.away_crest);
    }
}
