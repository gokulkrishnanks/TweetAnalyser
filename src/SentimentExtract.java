import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by gokul
 */


class SentimentExtract {

    static double getSent(JSONObject obj) {
        String resp;
        double score = 0.0;
        HttpClient httpclient = HttpClients.createDefault();

        try
        {
            URIBuilder builder = new URIBuilder("https://westus.api.cognitive.microsoft.com/text/analytics/v2.0/sentiment");


            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "text/json");
            request.setHeader("Ocp-Apim-Subscription-Key", "e212c65f4e1441ceae69068d5bdca4bd");

            // Request body
            StringEntity reqEntity = new StringEntity(obj.toString());
            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                resp = EntityUtils.toString(entity);
                //System.out.println(EntityUtils.toString(entity));
                //System.out.println(resp);
                JSONParser parser = new JSONParser();
                JSONObject resJson;
                //Convert String to JSON Object
                resJson = (JSONObject) parser.parse(resp);
                //Passing to JSON object to get score
                score = JsonOperator.jsonToScore(resJson);
            }
        }
        catch (Exception e)
        {
                System.out.println(e.getMessage());
        }
        return score;
    }
}

