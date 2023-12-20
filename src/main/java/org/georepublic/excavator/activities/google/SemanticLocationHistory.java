/**
 * パッケージ名：org.georepublic.excavator.activities.google
 * ファイル名  ：SemanticLocationHistory.java
 * 
 * @author mbasa
 * @since Dec 19, 2023
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
public class SemanticLocationHistory {

    Connection con;
    String path;

    /**
     * コンストラクタ
     *
     */
    public SemanticLocationHistory(Connection con, String path) {
        this.con = con;
        this.path = path;
    }

    public void saveToDB() {
        JSONParser jp = new JSONParser();

        try {
            JSONObject jo = (JSONObject) jp.parse(
                    new FileReader(this.path));

            if (!jo.isEmpty()) {
                JSONArray timeLines = (JSONArray) jo.get("timelineObjects");

                for (int i = 0; i < timeLines.size(); i++) {
                    JSONObject timeLine = (JSONObject) timeLines.get(i);

                    if (timeLine.containsKey("activitySegment")) {
                        processActivitySegment(timeLine);
                    } else if (timeLine.containsKey("placeVisit")) {
                        processPlaceVisit(timeLine);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("SemanticLocationHistory Error: " + e.getMessage());
        }
    }

    public void processActivitySegment(JSONObject timeLine) throws Exception {
        JSONObject activitySegment = (JSONObject) timeLine.get("activitySegment");
        JSONObject startLocation = (JSONObject) activitySegment.get("startLocation");
        JSONObject endLocation = (JSONObject) activitySegment.get("endLocation");
        JSONObject duration = (JSONObject) activitySegment.get("duration");

        double sLat = (Long) startLocation.get("latitudeE7") / 10000000.00;
        double sLng = (Long) startLocation.get("longitudeE7") / 10000000.00;

        double eLat = (Long) endLocation.get("latitudeE7") / 10000000.00;
        double eLng = (Long) endLocation.get("longitudeE7") / 10000000.00;

        String startTimestamp = duration.get("startTimestamp").toString();
        String endTimestamp = duration.get("endTimestamp").toString();

        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO google_location_activity ");
        sb.append("(timestamp,lat,lng)  VALUES (");
        sb.append("'").append(startTimestamp + "," + endTimestamp).append("',");
        sb.append(sLat).append(",");
        sb.append(sLng).append(")");

        DbUtils.sqlExecute(con, sb.toString());

        if (activitySegment.containsKey("waypointPath")) {
            JSONObject waypointPath = (JSONObject) activitySegment.get("waypointPath");
            JSONArray wayPoints = (JSONArray) waypointPath.get("waypoints");

            for (int i = 0; i < wayPoints.size(); i++) {
                JSONObject wayPoint = (JSONObject) wayPoints.get(i);
                double wLat = (Long) wayPoint.get("latE7") / 10000000.00;
                double wLng = (Long) wayPoint.get("lngE7") / 10000000.00;

                sb = new StringBuffer();
                sb.append("INSERT INTO google_location_activity ");
                sb.append("(timestamp,lat,lng)  VALUES (");
                sb.append("'").append(startTimestamp + "," + endTimestamp).append("',");
                sb.append(wLat).append(",");
                sb.append(wLng).append(")");

                DbUtils.sqlExecute(con, sb.toString());
            }
        }
        sb = new StringBuffer();
        sb.append("INSERT INTO google_location_activity ");
        sb.append("(timestamp,lat,lng)  VALUES (");
        sb.append("'").append(startTimestamp + "," + endTimestamp).append("',");
        sb.append(eLat).append(",");
        sb.append(eLng).append(")");

        DbUtils.sqlExecute(con, sb.toString());

    }

    public void processPlaceVisit(JSONObject timeLine) throws Exception {

        JSONObject placeVisit = (JSONObject) timeLine.get("placeVisit");
        JSONObject location = (JSONObject) placeVisit.get("location");
        JSONObject duration = (JSONObject) placeVisit.get("duration");

        double lat = (Long) location.get("latitudeE7") / 10000000.00;
        double lng = (Long) location.get("longitudeE7") / 10000000.00;

        String address = location.get("address").toString();
        String name = location.get("name").toString();

        String startTimestamp = duration.get("startTimestamp").toString();
        String endTimestamp = duration.get("endTimestamp").toString();

        String editConfirmationStatus = placeVisit.get("editConfirmationStatus").toString();
        int locationConfidence = Integer.parseInt(
                placeVisit.get("locationConfidence").toString());

        String placeVisitType = placeVisit.get("placeVisitType").toString();
        String placeVisitImportance = placeVisit.get("placeVisitImportance").toString();

        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO google_location_placevisit (");
        sb.append("address,name,lat,lng,start_timestamp,end_timestamp,");
        sb.append("edit_confirmation_status,location_confidence,");
        sb.append("place_visit_type,place_visit_importance) ");
        sb.append("VALUES (");
        sb.append("'").append(address).append("',");
        sb.append("'").append(name).append("',");
        sb.append(lat).append(",");
        sb.append(lng).append(",");
        sb.append("'").append(startTimestamp).append("',");
        sb.append("'").append(endTimestamp).append("',");
        sb.append("'").append(editConfirmationStatus).append("',");
        sb.append(locationConfidence).append(",");
        sb.append("'").append(placeVisitType).append("',");
        sb.append("'").append(placeVisitImportance).append("'");
        sb.append(")");

        DbUtils.sqlExecute(con, sb.toString());

    }

}
