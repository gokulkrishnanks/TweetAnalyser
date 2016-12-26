/**
 * Created by gokul on 24/12/16.
 */

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

class JsonOperator {

    static JSONObject jsonConv(String str) {

        JSONArray arr = new JSONArray();
        JSONObject obj = new JSONObject();
        obj.put("language", "en");
        obj.put("id", "file1");
        obj.put("text", str);
        arr.add(0, obj);
        JSONObject fullobj = new JSONObject();
        fullobj.put("documents",arr);
        return fullobj;

    }

    static double jsonToScore(JSONObject obj1){

        JSONArray doc = (JSONArray) obj1.get("documents");
        JSONObject op = (JSONObject) doc.get(0);
        return (double) op.get("score");
    }

}
