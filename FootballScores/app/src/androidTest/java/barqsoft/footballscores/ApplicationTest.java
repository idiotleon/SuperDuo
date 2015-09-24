package barqsoft.footballscores;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.test.ApplicationTestCase;
import android.test.ProviderTestCase2;
import android.util.Log;

import java.util.ArrayList;

import barqsoft.footballscores.helper.Utilies;
import barqsoft.footballscores.model.MatchModel;
import barqsoft.footballscores.provider.ScoresProvider;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ProviderTestCase2 {


    /**
     * Constructor.
     *
     * @param providerClass     The class name of the provider under test
     * @param providerAuthority The provider's authority string
     */
    public ApplicationTest(Class providerClass, String providerAuthority) {
        super(providerClass, providerAuthority);
    }

    public ApplicationTest() {
        super(ScoresProvider.class, "Test ContentProvider");
    }

    public void testMethod() {
        Context context = MainActivity.getContext();
        ArrayList<MatchModel> allMatchesOfTodayAsArrayList = Utilies.getAllMatchesOfTodayAsArrayList(context);
        for (int i = 0; i < allMatchesOfTodayAsArrayList.size(); i++) {
            Log.v("TestCase", allMatchesOfTodayAsArrayList.get(i).getHomeTeamName());
        }
        assertNotNull(allMatchesOfTodayAsArrayList);
    }
}