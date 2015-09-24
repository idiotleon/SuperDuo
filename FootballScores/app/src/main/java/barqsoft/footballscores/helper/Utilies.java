package barqsoft.footballscores.helper;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import barqsoft.footballscores.R;
import barqsoft.footballscores.model.MatchModel;
import barqsoft.footballscores.provider.ScoresContract;

public class Utilies {

    private static final String LOG_TAG = Utilies.class.getSimpleName();

    public static final String SAVE_INSTANCE_STATE_PAGER_FRAMENT = "pagerFragment";
    public static final String SAVE_INSTANCE_STATE_SELECTED_MATCH_ID = "selectedMatchId";
    public static final String SAVE_INSTANCE_STATE_SCURRENT_PAGE = "currentPage";

    public static final String INTENT_EXTRAS_MATCH_ID_IDENTIFIER = "intentExtrasMatchId";

    public static class Leagues {

        public static final int BUNDESLIGA_2014_2015 = 351;
        public static final int PREMIER_LEAGUE_2014_2015 = 354;
        public static final int SERIES_A_2014_2015 = 357;
        public static final int PRIMERA_DIVISION_2014_2015 = 358;
        public static final int BUNDESLIGA1_2015_2016 = 394;
        public static final int BUNDESLIGA2_2015_2016 = 395;
        public static final int LIGUE1_2015_2016 = 396;
        public static final int LIGUE2_2015_2016 = 397;
        public static final int PREMIER_LEAGUE_2015_2016 = 398;
        public static final int PRIMERA_DIVISION_2015_2016 = 399;
        public static final int SEGUNDA_DIVISION_2015_2016 = 400;
        public static final int SERIES_A_2015_2016 = 401;
        public static final int PRIMERA_LIGA = 402;
        public static final int BUNDESLIGA3_2015_2016 = 403;
        public static final int EREDIVISIE_2015_2016 = 404;

        // Based on this code, no resources' been found
        public static final int CHAMPIONS_LEAGUE = 362;

        public static boolean isLeagueIncluded(int league) {
            return league == BUNDESLIGA_2014_2015 ||
                    league == PREMIER_LEAGUE_2014_2015 ||
                    league == SERIES_A_2014_2015 ||
                    league == PRIMERA_DIVISION_2014_2015 ||
                    league == BUNDESLIGA1_2015_2016 ||
                    league == BUNDESLIGA2_2015_2016 ||
                    league == LIGUE1_2015_2016 ||
                    league == LIGUE2_2015_2016 ||
                    league == PREMIER_LEAGUE_2015_2016 ||
                    league == PRIMERA_DIVISION_2015_2016 ||
                    league == SEGUNDA_DIVISION_2015_2016 ||
                    league == SERIES_A_2015_2016 ||
                    league == PRIMERA_LIGA ||
                    league == BUNDESLIGA3_2015_2016 ||
                    league == EREDIVISIE_2015_2016 ||
                    league == CHAMPIONS_LEAGUE;
        }
    }

    public static String getLeague(Context context, int leagueNum) {
        switch (leagueNum) {
            case Leagues.SERIES_A_2014_2015:
//                return "Seria A, 2014/2015";
                return context.getResources().getString(R.string.seriaa);
            case Leagues.SERIES_A_2015_2016:
//                return "Seria A, 2015/2016";
                return context.getResources().getString(R.string.seriaa);
            case Leagues.PREMIER_LEAGUE_2014_2015:
//                return "Premier League, 2014/2015";
                return context.getResources().getString(R.string.premierleague);
            case Leagues.PREMIER_LEAGUE_2015_2016:
//                return "Premier League, 2015/2016";
                return context.getResources().getString(R.string.premierleague);
            case Leagues.PRIMERA_DIVISION_2015_2016:
//                return "Premier League, 2015/2016";
                return context.getResources().getString(R.string.premierleague);
            case Leagues.BUNDESLIGA1_2015_2016:
//                return "Bundesliga1, 2015/16";
                return context.getResources().getString(R.string.bundesliga);
            case Leagues.BUNDESLIGA2_2015_2016:
//                return "Bundesliga2, 2015/16";
                return context.getResources().getString(R.string.bundesliga);
            case Leagues.BUNDESLIGA3_2015_2016:
//                return "Bundesliga3, 2015/16";
                return context.getResources().getString(R.string.bundesliga);
            case Leagues.BUNDESLIGA_2014_2015:
//                return "Bundesliga1, 2014/15";
                return context.getResources().getString(R.string.bundesliga);
            case Leagues.LIGUE1_2015_2016:
//                return "Ligue1, 2015/16";
                return context.getResources().getString(R.string.lingue);
            case Leagues.LIGUE2_2015_2016:
//                return "Ligue2, 2015/16";
                return context.getResources().getString(R.string.lingue);
            case Leagues.SEGUNDA_DIVISION_2015_2016:
//                return "Segunda Division, 2015/16";
                return context.getResources().getString(R.string.segunda);
            case Leagues.PRIMERA_LIGA:
//                return "Primeira Liga, 2015/16";
                return context.getResources().getString(R.string.primeira);
            case Leagues.EREDIVISIE_2015_2016:
//                return "Eredivisie, 2015/16";
                return context.getResources().getString(R.string.eredivisie);
            case Leagues.PRIMERA_DIVISION_2014_2015:
//                return "Primera Division, 2014/2015";
                return context.getResources().getString(R.string.primeradivison);
            default:
                return context.getResources().getString(R.string.unkown_league);
        }
    }

    public static String getMatchProgress(Context context, int matchDay, int leagueNum) {
        if (leagueNum == Leagues.CHAMPIONS_LEAGUE) {
            if (matchDay <= 6) {
                return context.getResources().getString(R.string.group_stage_text);
            } else if (matchDay == 7 || matchDay == 8) {
                return context.getResources().getString(R.string.first_knockout_round);
            } else if (matchDay == 9 || matchDay == 10) {
                return context.getResources().getString(R.string.quarter_final);
            } else if (matchDay == 11 || matchDay == 12) {
                return context.getResources().getString(R.string.semi_final);
            } else {
                return context.getResources().getString(R.string.final_text);
            }
        } else {
            return context.getResources().getString(R.string.match_day) + String.valueOf(matchDay);
        }
    }

    public static String getScores(int home_goals, int awaygoals) {
        if (home_goals < 0 || awaygoals < 0) {
            return " - ";
        } else {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static boolean isNetworkConnected(Context context) {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }

    // todo: write a test method for this method
    public static ArrayList<MatchModel> getAllMatchesOfTodayAsArrayList(Context context) {
        Log.v(LOG_TAG, "getAllMatchesOfTodayAsArrayList() executed");
        ContentResolver contentResolver = context.getContentResolver();
        ArrayList<MatchModel> matchesByDateArrayList = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] currentDate = new String[]{simpleDateFormat.format(new Date(System.currentTimeMillis()))};

//        String sortBy = " DESC";
        String sortBy = null;

        Cursor cursor = contentResolver.query(ScoresContract.ScoresTable.buildScoreWithDate(), null, null, currentDate, sortBy);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                long matchId = cursor.getLong(cursor.getColumnIndex(ScoresContract.ScoresTable.MATCH_ID));
                String homeTeamName = cursor.getString(cursor.getColumnIndex(ScoresContract.ScoresTable.HOME_TEAM_NAME_COL));
                Log.v(LOG_TAG, "homeTeamName: " + homeTeamName);
                String awayTeamName = cursor.getString(cursor.getColumnIndex(ScoresContract.ScoresTable.AWAY_TEAM_NAME_COL));
                String matchDate = cursor.getString(cursor.getColumnIndex(ScoresContract.ScoresTable.DATE_COL));
                String matchTime = cursor.getString(cursor.getColumnIndex(ScoresContract.ScoresTable.TIME_COL));
                int matchDay = cursor.getInt(cursor.getColumnIndex(ScoresContract.ScoresTable.MATCH_DAY));
                int leagueNumber = cursor.getInt(cursor.getColumnIndex(ScoresContract.ScoresTable.LEAGUE_COL));
                int homeTeamGoals = cursor.getInt(cursor.getColumnIndex(ScoresContract.ScoresTable.HOME_GOALS_COL));
                int awayTeamGoals = cursor.getInt(cursor.getColumnIndex(ScoresContract.ScoresTable.AWAY_GOALS_COL));

                MatchModel match = new MatchModel(matchId, homeTeamName, awayTeamName, matchDate, matchTime, matchDay, leagueNumber, homeTeamGoals, awayTeamGoals);
                matchesByDateArrayList.add(match);
                cursor.moveToNext();
            }
        }
        return matchesByDateArrayList;
    }

    public static int getTeamCrestByTeamName(String teamName) {
        if (teamName == null) {
            return R.drawable.no_icon;
        }
        switch (teamName) { //This is the set of icons that are currently in the app. Feel free to find and add more
            //as you go.
            case "Arsenal London FC":
                return R.drawable.arsenal;
            case "Manchester United FC":
                return R.drawable.manchester_united;
            case "Swansea City":
                return R.drawable.swansea_city_afc;
            case "Leicester City":
                return R.drawable.leicester_city_fc_hd_logo;
            case "Everton FC":
                return R.drawable.everton_fc_logo1;
            case "West Ham United FC":
                return R.drawable.west_ham;
            case "Tottenham Hotspur FC":
                return R.drawable.tottenham_hotspur;
            case "West Bromwich Albion":
                return R.drawable.west_bromwich_albion_hd_logo;
            case "Sunderland AFC":
                return R.drawable.sunderland_logo;
            case "Stoke City FC":
                return R.drawable.stoke_city_logo;
            case "VfL Bochum":
                return R.drawable.vfl_bochum_logo;
            case "1. FC Heidenheim 1846":
                return R.drawable._1_fc_heidenheim_1846_logo;
            case "Red Bull Leipzig":
                return R.drawable.red_bull_leipzig_logo;
            case "Arminia Bielefeld":
                return R.drawable.arminia_bielefeld_log;
            case "SC Freiburg":
                return R.drawable.sc_freiburg_logo;
            case "Magdeburg":
                return R.drawable.magdeburg_logo;
            case "Fortuna Düsseldorf":
                return R.drawable.fortuna_dusseldorf_logo;
            case "VfR Aalen":
                return R.drawable.vfr_aalen_logo;
            case "AJ Auxerre":
                return R.drawable.aj_auxerre_logo;
            case "Clermont Foot Auvergne":
                return R.drawable.clearmont_foot_auvergne;
            case "Stade Brestois":
                return R.drawable.stade_brestois_logo;
            case "FC Bourg-en-Bresse Péronnas":
                return R.drawable.fbbp_logo;
            case "Ajaccio AC":
                return R.drawable.ajaccio_ac_logo;
            case "FC Stade Lavallois Mayenne":
                return R.drawable.fc_stade_lavallois_mayenne_logo;
            case "Hallescher FC":
                return R.drawable.hallescher_fc_logo;
            case "SG Sonnenhof Großaspach":
                return R.drawable.sg_sonnenhof_grossaspach_logo;
            case "VfL Osnabrück":
                return R.drawable.vfl_osnabruck_logo;
            case "VfB Stuttgart II":
                return R.drawable.vfb_stuttgart_logo;
            case "SC Heerenveen":
                return R.drawable.sc_heerenveen_logo;
            case "1. FSV Mainz 05":
                return R.drawable.mainz_05_ii_logo;
            case "Mainz 05 II":
                return R.drawable.mainz_05_ii_logo;
            case "Erzgebirge Aue":
                return R.drawable.erzgebirge_aue_logo;
            case "Energie Cottbus":
                return R.drawable.energie_cottbus_logo;
            case "Red Star 93":
                return R.drawable.red_star_93_logo;
            case "Sochaux FC":
                return R.drawable.sochaux_fc_logo;
            case "NEC Nijmegen":
                return R.drawable.nec_nijmegen_logo;
            case "TSG 1899 Hoffenheim":
                return R.drawable.tsg_1899_hoffenheim_logo;
            case "Stade Rennais FC":
                return R.drawable.stade_rennais_fc_logo;
            case "Getafe CF":
                return R.drawable.getafe_cf_logo;
            case "OSC Lille":
                return R.drawable.osc_lille_logo;
            case "Málaga CF":
                return R.drawable.malaga_cf_logo;
            case "US Créteil":
                return R.drawable.us_creteil_logo;
            case "FC Valenciennes":
                return R.drawable.fc_valenciennes_logo;
            case "Dijon FCO":
                return R.drawable.dijon_fco_logo;
            case "Le Havre AC":
                return R.drawable.le_havre_ac;
            case "RC Lens":
                return R.drawable.rc_lens_logo;
            case "RC Tours":
                return R.drawable.rc_lens_logo;
            case "FC Metz":
                return R.drawable.fc_metz_logo;
            case "AS Nancy":
                return R.drawable.as_nancy_logo;
            case "Chamois Niortais FC":
                return R.drawable.chamois_niortais_fc_logo;
            case "Nîmes Olympique":
                return R.drawable.nimes_olympique_logo;
            case "Montpellier Hérault SC":
                return R.drawable.montpelleir_herault_sc;
            case "AS Monaco FC":
                return R.drawable.as_monaco_fc_logo;
            case "Empoli FC":
                return R.drawable.empoli_fc_logo;
            case "Real Betis":
                return R.drawable.real_betis_logo;
            case "RC Deportivo La Coruna":
                return R.drawable.rc_deportivo_la_coruna_logo;
            case "Atalanta BC":
                return R.drawable.atalanta_bc_logo;
            case "1. FC Kaiserslautern":
                return R.drawable._1_fc_kaiserslautern_logo;
            case "SV Sandhausen":
                return R.drawable.sv_sandhausen_logo;
            case "1. FC Nürnberg":
                return R.drawable._1_fv_nurnberg_logo;
            case "Chemnitzer FC":
                return R.drawable.chemnitzer_fc_logo;
            case "Würzburger Kickers":
                return R.drawable.wurzburger_kickers_logo;
            case "Évian Thonon Gaillard FC":
                return R.drawable.evian_thonon_gaillard_fc_logo;
            case "Holstein Kiel":
                return R.drawable.holstein_kiel_logo;
            case "Fortuna Köln":
                return R.drawable.fortuna_koln_logo;
            case "Stuttgarter Kickers":
                return R.drawable.stuttgarter_kickers_logo;
            case "Dynamo Dresden":
                return R.drawable.dynamo_dresden_logo;
            case "Borussia Dortmund":
                return R.drawable.borussia_dortmund_logo;
            default:
                return R.drawable.no_icon;
        }
    }
}
