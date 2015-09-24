package barqsoft.footballscores.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import barqsoft.footballscores.helper.Utilies;
import barqsoft.footballscores.provider.ScoresContract;
import barqsoft.footballscores.R;


public class FetchingService extends IntentService {
    public static final String LOG_TAG = FetchingService.class.getSimpleName();

    public FetchingService() {
        super("FetchingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        getData("n2");
        getData("p2");
    }

    private void getData(String timeFrame) {
        //Creating fetch URL
        final String BASE_URL = "http://api.football-data.org/alpha/fixtures"; //Base URL
        final String QUERY_TIME_FRAME = "timeFrame"; //Time Frame parameter to determine days
        //final String QUERY_MATCH_DAY = "matchday";

        Uri fetchingUri = Uri.parse(BASE_URL).buildUpon().
                appendQueryParameter(QUERY_TIME_FRAME, timeFrame).build();
//        Log.v(LOG_TAG, "fetchingUri: " + fetchingUri.toString()); //log spam
        HttpURLConnection httpUrlConnection = null;
        BufferedReader bufferedReader = null;
        String JSONData = null;
        //Opening Connection
        try {
            URL fetchingUrl = new URL(fetchingUri.toString());
            httpUrlConnection = (HttpURLConnection) fetchingUrl.openConnection();
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.addRequestProperty("X-Auth-Token", getString(R.string.api_key));
            httpUrlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = httpUrlConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                stringBuffer.append(line + "\n");
            }
            if (stringBuffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            JSONData = stringBuffer.toString();
            Log.v(LOG_TAG, "JSONData: " + JSONData);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception here: " + e.getMessage());
        } finally {
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error Closing Stream");
                }
            }
        }
        try {
            if (JSONData != null) {
                //This bit is to check if the data contains any matches. If not, we call processJson on the dummy data
                JSONArray matches = new JSONObject(JSONData).getJSONArray("fixtures");
                if (matches.length() == 0) {
                    //if there is no data, call the function on dummy data
                    //this is expected behavior during the off season.
                    processJSONData(getString(R.string.dummy_data), getApplicationContext(), false);
                    return;
                }
                processJSONData(JSONData, getApplicationContext(), true);
            } else {
                //Could not Connect
                Log.d(LOG_TAG, "Could not connect to server.");
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    private void processJSONData(String JSONData, Context context, boolean isReal) {

        final String SEASON_LINK = "http://api.football-data.org/alpha/soccerseasons/";
        final String MATCH_LINK = "http://api.football-data.org/alpha/fixtures/";
        final String FIXTURES = "fixtures";
        final String LINKS = "_links";
        final String SOCCER_SEASON = "soccerseason";
        final String SELF = "self";
        final String MATCH_DATE = "date";
        final String HOME_TEAM_NAME = "homeTeamName";
        final String AWAY_TEAM_NAME = "awayTeamName";
        final String RESULT = "result";
        final String HOME_GOALS = "goalsHomeTeam";
        final String AWAY_GOALS = "goalsAwayTeam";
        final String MATCH_DAY = "matchday";
        final String HREF = "href";

        //Match data
        String league = null;
        String matchDateAndTime = null;
        String matchDate = null;
        String matchTime = null;
        String homeTeamName = null;
        String awayTeamName = null;
        String homeTeamGoals = null;
        String awayTeamGoals = null;
        String matchId = null;
        String matchDay = null;


        try {
            JSONArray matches = new JSONObject(JSONData).getJSONArray(FIXTURES);

            //ContentValues to be inserted
            Vector<ContentValues> values = new Vector<ContentValues>(matches.length());
            for (int i = 0; i < matches.length(); i++) {
                JSONObject matchData = matches.getJSONObject(i);
                league = matchData.getJSONObject(LINKS).getJSONObject(SOCCER_SEASON).
                        getString(HREF);
                league = league.replace(SEASON_LINK, "");
                // This if statement controls which leagues we're interested in the data from.
                // add leagues here in order to have them be added to the DB.
                // If you are finding no data in the app, check that this contains all the leagues.
                // If it doesn't, that can cause an empty DB, bypassing the dummy data routine.
                if (Utilies.Leagues.isLeagueIncluded(Integer.parseInt(league))) {
                    matchId = matchData.getJSONObject(LINKS).getJSONObject(SELF).
                            getString(HREF);
                    matchId = matchId.replace(MATCH_LINK, "");
                    if (!isReal) {
                        //This if statement changes the match ID of the dummy data so that it all goes into the database
                        matchId = matchId + Integer.toString(i);
                    }

                    matchDateAndTime = matchData.getString(MATCH_DATE);
                    matchTime = matchDateAndTime.substring(matchDateAndTime.indexOf("T") + 1, matchDateAndTime.indexOf("Z"));
                    matchDate = matchDateAndTime.substring(0, matchDateAndTime.indexOf("T"));
                    SimpleDateFormat matchDateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
                    matchDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    try {
                        Date parsedDate = matchDateFormat.parse(matchDate + matchTime);
                        SimpleDateFormat new_date = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
                        new_date.setTimeZone(TimeZone.getDefault());
                        matchDate = new_date.format(parsedDate);
                        matchTime = matchDate.substring(matchDate.indexOf(":") + 1);
                        matchDate = matchDate.substring(0, matchDate.indexOf(":"));

                        if (!isReal) {
                            //This if statement changes the dummy data's date to match our current date range.
                            Date fragmentDate = new Date(System.currentTimeMillis() + ((i - 2) * 86400000));
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            matchDate = simpleDateFormat.format(fragmentDate);
                        }
                    } catch (Exception e) {
                        Log.d(LOG_TAG, "error here!");
                        Log.e(LOG_TAG, e.getMessage());
                    }
                    homeTeamName = matchData.getString(HOME_TEAM_NAME);
                    awayTeamName = matchData.getString(AWAY_TEAM_NAME);
                    homeTeamGoals = matchData.getJSONObject(RESULT).getString(HOME_GOALS);
                    awayTeamGoals = matchData.getJSONObject(RESULT).getString(AWAY_GOALS);
                    matchDay = matchData.getString(MATCH_DAY);
                    ContentValues matchValues = new ContentValues();
                    matchValues.put(ScoresContract.ScoresTable.MATCH_ID, matchId);
                    matchValues.put(ScoresContract.ScoresTable.DATE_COL, matchDate);
                    matchValues.put(ScoresContract.ScoresTable.TIME_COL, matchTime);
                    matchValues.put(ScoresContract.ScoresTable.HOME_TEAM_NAME_COL, homeTeamName);
                    matchValues.put(ScoresContract.ScoresTable.AWAY_TEAM_NAME_COL, awayTeamName);
                    matchValues.put(ScoresContract.ScoresTable.HOME_GOALS_COL, homeTeamGoals);
                    matchValues.put(ScoresContract.ScoresTable.AWAY_GOALS_COL, awayTeamGoals);
                    matchValues.put(ScoresContract.ScoresTable.LEAGUE_COL, league);
                    matchValues.put(ScoresContract.ScoresTable.MATCH_DAY, matchDay);

                    Log.v(LOG_TAG, "matchId: " + matchId);
                    Log.v(LOG_TAG, "matchDate: " + matchDate);
                    Log.v(LOG_TAG, "matchTime: " + matchTime);
                    Log.v(LOG_TAG, "homeTeamName: " + homeTeamName);
                    Log.v(LOG_TAG, "awayTeamName: " + awayTeamName);
                    Log.v(LOG_TAG, "homeTeamGoals: " + homeTeamGoals);
                    Log.v(LOG_TAG, "awayTeamGoals: " + awayTeamGoals);

                    values.add(matchValues);
                }
            }

            int insertedColumnsNumber = 0;
            ContentValues[] insertData = new ContentValues[values.size()];
            values.toArray(insertData);
            insertedColumnsNumber = context.getContentResolver().bulkInsert(
                    ScoresContract.BASE_CONTENT_URI, insertData);

            Log.v(LOG_TAG, "Successfully Inserted " + String.valueOf(insertedColumnsNumber) + " rows.");
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }
}

