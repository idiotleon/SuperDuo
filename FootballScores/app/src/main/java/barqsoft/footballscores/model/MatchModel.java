package barqsoft.footballscores.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MatchModel implements Parcelable {

    private long matchId;
    private String homeTeamName;
    private String awayTeamName;
    private String matchDate;
    private String matchTime;
    private int matchDay;
    private int leagueNumber;
    private int homeTeamGoals;
    private int awayTeamGoals;
    private String homeTeamCrestUrl;
    private String awayTeamCrestUrl;

    public MatchModel(long matchId, String homeTeamName, String awayTeamName, String matchDate, String matchTime, int matchDay, int leagueNumber, int homeTeamGoals, int awayTeamGoals) {
        this.matchId = matchId;
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.matchDate = matchDate;
        this.matchTime = matchTime;
        this.matchDay = matchDay;
        this.leagueNumber = leagueNumber;
        this.homeTeamGoals = homeTeamGoals;
        this.awayTeamGoals = awayTeamGoals;
    }

    protected MatchModel(Parcel in) {
        matchId = in.readLong();
        homeTeamName = in.readString();
        awayTeamName = in.readString();
        matchDate = in.readString();
        matchTime = in.readString();
        matchDay = in.readInt();
        leagueNumber = in.readInt();
        homeTeamGoals = in.readInt();
        awayTeamGoals = in.readInt();
        homeTeamCrestUrl = in.readString();
        awayTeamCrestUrl = in.readString();
    }

    public static final Creator<MatchModel> CREATOR = new Creator<MatchModel>() {
        @Override
        public MatchModel createFromParcel(Parcel in) {
            return new MatchModel(in);
        }

        @Override
        public MatchModel[] newArray(int size) {
            return new MatchModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(matchId);
        dest.writeString(homeTeamName);
        dest.writeString(awayTeamName);
        dest.writeString(matchDate);
        dest.writeString(matchTime);
        dest.writeInt(matchDay);
        dest.writeInt(leagueNumber);
        dest.writeInt(homeTeamGoals);
        dest.writeInt(awayTeamGoals);
        dest.writeString(homeTeamCrestUrl);
        dest.writeString(awayTeamCrestUrl);
    }

    public long getMatchId() {
        return matchId;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public int getMatchDay() {
        return matchDay;
    }

    public int getLeagueNumber() {
        return leagueNumber;
    }

    public int getHomeTeamGoals() {
        return homeTeamGoals;
    }

    public int getAwayTeamGoals() {
        return awayTeamGoals;
    }

    public String getHomeTeamCrestUrl() {
        return homeTeamCrestUrl;
    }

    public String getAwayTeamCrestUrl() {
        return awayTeamCrestUrl;
    }
}
