/**
 * パッケージ名：org.georepublic.excavator.activities.google
 * ファイル名  ：LocationHistory.java
 * 
 * @author mbasa
 * @since Dec 14, 2023
 */
package org.georepublic.excavator.activities.google;

import java.io.FileReader;
import java.sql.Connection;

import org.georepublic.excavator.db.DbUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * 説明：
 *
 */
public class LocationHistory {

    Connection con;
    String path;

    /**
     * コンストラクタ
     *
     */
    public LocationHistory(Connection con, String path) {
        this.con = con;
        this.path = path;
    }

    public void saveToDB() {
        JSONParser jp = new JSONParser();

        try {
            JSONObject jo = (JSONObject) jp.parse(
                    new FileReader(this.path));

            if (!jo.isEmpty()) {
                JSONArray locations = (JSONArray) jo.get("locations");

                for (int i = 0; i < locations.size(); i++) {
                    JSONObject location = (JSONObject) locations.get(i);
                    String activityStr = "";
                    String source = "";
                    String platformType = "";
                    String formFactor = "";

                    if (location.containsKey("activity")) {
                        JSONObject acts = (JSONObject) ((JSONArray) location.get("activity")).get(0);

                        if (acts != null) {
                            JSONObject activity = (JSONObject) ((JSONArray) acts.get("activity")).get(0);
                            activityStr = activity.get("type").toString();
                        }
                    }

                    if (location.containsKey("source")) {
                        source = location.get("source").toString();
                    }
                    if (location.containsKey("platformType")) {
                        platformType = location.get("platformType").toString();
                    }
                    if (location.containsKey("formFactor")) {
                        formFactor = location.get("formFactor").toString();
                    }

                    StringBuffer sb = new StringBuffer();

                    sb.append("INSERT into google_location_history ");
                    sb.append("(activity, timestamp,accuracy, "
                            + "verticalaccuracy, altitude, lat, "
                            + "lng, origin,source,platform_type,form_factor) ");

                    sb.append("values(");
                    sb.append("'").append(activityStr).append("',");
                    sb.append("datetime('").append(location.get("timestamp")).append("'),");
                    sb.append(location.get("accuracy")).append(",");
                    sb.append(location.get("verticalAccuracy")).append(",");
                    sb.append(location.get("altitude")).append(",");
                    sb.append(location.get("latitudeE7")).append("/10000000.0,");
                    sb.append(location.get("longitudeE7")).append("/10000000.0,");
                    sb.append("'location_history',");
                    sb.append("'").append(source).append("',");
                    sb.append("'").append(platformType).append("',");
                    sb.append("'").append(formFactor).append("')");

                    DbUtils.sqlExecute(con, sb.toString());
                }
            }
        } catch (Exception e) {
            System.out.println("LocationHistory Error: " + e.getMessage());
        }
    }

}
