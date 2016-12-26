import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONObject;

/**
 * Created by gokul
 */
public class TweetExtract {
    // Use your own api keys
    private static String ack = "xxxxxxxxx";
    private static String cs = "xxxxxxxxx";
    private static String at = "xxxxxxxxx";
    private static String ats = "xxxxxxxxx";
    private static int geoloc = 23424848; // location code for India


    public static void main(String[] args) {

        String[] topics, cleanTweets;
        double[] opScore;
        double[][] scores = new double[5][5];

        // Getting and displaying Topics
        topics = getTopics();
        System.out.println(Arrays.toString(topics));

        // For accuracy and consistency tweets of each topic is queried 5 times.
        // The average of 5 times is taken as final score

        JSONObject Json;

        for(int j=0; j<5; j++){
            cleanTweets = getTweets(topics);
            for (int i = 0; i < 5; i++) {
                Json = JsonOperator.jsonConv(cleanTweets[i]);
                scores[j][i] = SentimentExtract.getSent(Json);
            }
        }
        // Average is computed and displayed
        opScore = computeavg(scores);
        System.out.println(Arrays.toString(opScore));

    }

    private static String[] getTopics() {

        ConfigurationBuilder cf = new ConfigurationBuilder();
        cf.setDebugEnabled(true).setOAuthConsumerKey(ack)
                .setOAuthConsumerSecret(cs).setOAuthAccessToken(at).setOAuthAccessTokenSecret(ats);

        TwitterFactory tf = new TwitterFactory(cf.build());
        Twitter twitter = tf.getInstance();

        String[] trendTopics = new String[5];

        Trends trends = null;

        try {
            trends = twitter.getPlaceTrends(geoloc);
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 5; i++) {
            trendTopics[i] = trends.getTrends()[i].getName();
        }

        return trendTopics;
    }


    private static String[] getTweets(String[] topics) {

        String cleanTweet;
        String fullTweets;
        String[] allTweet = new String[5];
        ConfigurationBuilder cf = new ConfigurationBuilder();

        cf.setDebugEnabled(true)
                .setOAuthConsumerKey(ack)
                .setOAuthConsumerSecret(cs)
                .setOAuthAccessToken(at)
                .setOAuthAccessTokenSecret(ats);
        TwitterFactory tf = new TwitterFactory(cf.build());
        Twitter twitter = tf.getInstance();

        int i = 0;
        for (String topic : topics) {
            fullTweets = "";
            Query query = new Query(topic);
            query.setCount(50);
            query.setLang("en");
            try {
                QueryResult result = twitter.search(query);

                for (Status status : result.getTweets()) {
                    //Every tweet is passed to cleaning where symbols, URLs, other extra entities are remove.
                    cleanTweet = cleaning(status.getText());
                    //Appending all tweets of topic into to a single string
                    fullTweets = fullTweets + cleanTweet + ". \n";
                    //System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
                }
                allTweet[i] = fullTweets;
            } catch (TwitterException te) {
                System.out.println("Couldn't connect: " + te);
            }
            i++;
        }
        return allTweet;
    }

    private static String cleaning(String str) {

        String urlPattern = "((https?|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        // Removing URL from tweet
        int i = 0;
        while (m.find()) {
            str = str.replaceAll(m.group(i), "").trim();
            i++;
        }
        // Removing hashtag text
        Pattern p2 = Pattern.compile("#(\\S+)");
        Matcher m2 = p2.matcher(str);
        while (m2.find()) {
            str = str.replaceAll(m2.group(1), "").trim();
        }
        // Removing RT text
        Pattern p3 = Pattern.compile("RT @(\\S+)");
        Matcher m3 = p3.matcher(str);
        while (m3.find()) {
            str = str.replaceAll(m3.group(0), "").trim();
        }
        // Removing all special chracters
        str = str.replaceAll("[^a-zA-Z0-9 ]+", "").trim();
        str = str.trim().replaceAll("\n ", "");
        return str;
    }



    private static double[] computeavg(double[][] arr){

        double[] avgScores = new double[5];
        for(int row=0; row<5; row++){
            double tmp = 0;
            for(int col=0; col<5; col++){
                tmp += arr[col][row];
            }
            avgScores[row] = 100*(tmp/5);
        }
        return avgScores;
    }
}





