/**
 * パッケージ名：org.georepublic.excavator.db
 * ファイル名  ：Schema.java
 * 
 * @author mbasa
 * @since Dec 7, 2023
 */
package org.georepublic.excavator.db;

import java.sql.Connection;

/**
 * 説明：
 *
 */
public class Schema {

    Connection con = null;

    /**
     * コンストラクタ
     *
     */
    public Schema(Connection con) {
        this.con = con;
    }

    public void buildGoogleTables() {
        buildGoogleLocationTables();
        // buildGooglePersonalTables();
    }

    public void buildGoogleLocationTables() {
        DbUtils.sqlExecute(con,
                "CREATE TABLE IF NOT EXISTS google_location_activitysegment ("
                        + "id               INTEGER PRIMARY KEY,"
                        + "start_timestamp  TIMESTAMP NOT NULL,"
                        + "end_timestamp    TIMESTAMP NOT NULL,"
                        + "start_lat        FLOAT NOT NULL,"
                        + "start_lng        FLOAT NOT NULL,"
                        + "end_lat          FLOAT NOT NULL,"
                        + "end_lng          FLOAT NOT NULL,"
                        + "distance         INTEGER,"
                        + "activity_type    TEXT,"
                        + "confidence       TEXT,"
                        + "wkt              TEXT,"
                        + "UNIQUE(start_timestamp,end_timestamp,"
                        + "start_lat,start_lng,end_lat,end_lng) "
                        + ")");

        DbUtils.sqlExecute(con,
                "CREATE TABLE IF NOT EXISTS google_location_activity ("
                        + "id               INTEGER PRIMARY KEY,"
                        + "start_timestamp  TIMESTAMP,"
                        + "end_timestamp    TIMESTAMP,"
                        + "lat              FLOAT NOT NULL,"
                        + "lng              FLOAT NOT NULL,"
                        + "UNIQUE(start_timestamp,end_timestamp,lat,lng)"
                        + ")");

        DbUtils.sqlExecute(con,
                "CREATE TABLE IF NOT EXISTS google_location_placevisit ("
                + "id               INTEGER PRIMARY KEY,"
                + "address          TEXT,"
                + "name             TEXT,"
                + "lat              FLOAT NOT NULL,"
                + "lng              FLOAT NOT NULL,"
                        + "start_timestamp TIMESTAMP NOT NULL,"
                        + "end_timestamp   TIMESTAMP NOT NULL,"
                        + "edit_confirmation_status TEXT,"
                + "location_confidence     INTEGER,"
                + "place_visit_type        TEXT,"
                + "place_visit_importance  TEXT,"
                + "UNIQUE(start_timestamp,end_timestamp,lat,lng)"
                + ")");

        DbUtils.sqlExecute(con,
                "CREATE TABLE IF NOT EXISTS google_location_history ("
                        + "id               INTEGER PRIMARY KEY,"
                        + "source           TEXT,"
                        + "activity         TEXT,"
                        + "address          TEXT,"
                        + "place_name       TEXT,"
                        + "timestamp_msec   BIGINT NOT NULL,"
                        + "accuracy         INTEGER,"
                        + "verticalaccuracy INTEGER,"
                        + "altitude         INTEGER,"
                        + "lat              FLOAT NOT NULL,"
                        + "lng              FLOAT NOT NULL,"
                        + "UNIQUE(timestamp_msec,lat,lng)"
                        + ")");

        DbUtils.sqlExecute(con,
                "CREATE TABLE IF NOT EXISTS google_location_history_updated ( "
                        + "id               INTEGER PRIMARY KEY,"
                        + "origin           TEXT,"
                        + "platform_type    TEXT,"
                        + "form_factor      TEXT,"
                        + "source           TEXT,"
                        + "activity         TEXT,"
                        // + "address TEXT,"
                        // + "place_name TEXT,"
                        + "timestamp        TIMESTAMP NOT NULL,"
                        + "accuracy         INTEGER,"
                        + "verticalaccuracy INTEGER,"
                        + "altitude         INTEGER,"
                        + "lat              FLOAT NOT NULL,"
                        + "lng              FLOAT NOT NULL, "
                        + "UNIQUE(timestamp,lat,lng)"
                        + ")");
    }

    public void buildGooglePersonalTables() {
        DbUtils.sqlExecute(con,
                "CREATE TABLE IF NOT EXISTS  google_my_activity ("
                + "uuid      TEXT NOT NULL PRIMARY KEY,"
                + "header    TEXT NOT NULL,"
                + "title     TEXT NOT NULL,"
                + "title_url TEXT,"
                        + "time      TIMESTAMP NOT NULL,"
                + "UNIQUE(header,title,time)"
                + ")");

        DbUtils.sqlExecute(con,
                "CREATE TABLE IF NOT EXISTS activity_location_info ("
                + "id     INTEGER PRIMARY KEY,"
                + "a_uuid TEXT NOT NULL ,"
                + "name   TEXT,"
                + "url    TEXT,"
                + "source TEXT,"
                + "FOREIGN KEY (a_uuid) REFERENCES google_my_activity(uuid) ON DELETE CASCADE"
                + ") ");

        DbUtils.sqlExecute(con,
                "CREATE INDEX IF NOT EXISTS gact1 ON "
                + "activity_location_info(a_uuid)");

        DbUtils.sqlExecute(con,
                "CREATE TABLE IF NOT EXISTS activity_sub_title ("
                + "id     INTEGER PRIMARY KEY,"
                + "a_uuid TEXT NOT NULL,"
                + "name   TEXT,"
                + "url    TEXT,"
                + "FOREIGN KEY (a_uuid) REFERENCES google_my_activity(uuid) ON DELETE CASCADE"
                + ")");

        DbUtils.sqlExecute(con,
                "CREATE INDEX IF NOT EXISTS gact2 ON "
                + "activity_sub_title(a_uuid)");

        DbUtils.sqlExecute(con,
                "CREATE TABLE IF NOT EXISTS activity_details ("
                + "id     INTEGER PRIMARY KEY,"
                + "a_uuid TEXT NOT NULL,"
                + "name   TEXT,"
                + "FOREIGN KEY (a_uuid) REFERENCES google_my_activity(uuid) ON DELETE CASCADE"
                + ")");

        DbUtils.sqlExecute(con,
                "CREATE INDEX IF NOT EXISTS gact3 ON "
                + "activity_details(a_uuid) ");

        DbUtils.sqlExecute(con,
                "CREATE TABLE IF NOT EXISTS activity_products ("
                + "id     INTEGER PRIMARY KEY,"
                + "a_uuid TEXT NOT NULL,"
                + "name   TEXT,"
                + "FOREIGN KEY (a_uuid) REFERENCES google_my_activity(uuid) ON DELETE CASCADE"
                + ")");

        DbUtils.sqlExecute(con,
                "CREATE INDEX IF NOT EXISTS gact4 ON "
                + "activity_products(a_uuid)");

        DbUtils.sqlExecute(con,
                "CREATE TABLE IF NOT EXISTS google_saved_places ("
                + "id               INTEGER PRIMARY KEY,"
                + "activity         TEXT,"
                + "timestamp_msec   BIGINT NOT NULL,"
                + "accuracy         INTEGER,"
                + "verticalaccuracy INTEGER,"
                + "altitude         INTEGER,"
                + "lat              FLOAT NOT NULL,"
                + "lng              FLOAT NOT NULL,"
                + "UNIQUE(timestamp_msec,lat,lng)"
                + ")");

        DbUtils.sqlExecute(con,
                "CREATE TABLE IF NOT EXISTS google_fit_activity ("
                + "uuid              TEXT NOT NULL PRIMARY KEY,"
                + "activity          TEXT,"
                + "start             TEXT,"
                + "end               TEXT,"
                        + "timestamp         TIMESTAMP"
                + ")");

    }
    public void buildFacebookTables() {
        DbUtils.sqlExecute(con,
                "CREATE TABLE IF NOT EXISTS facebook_device_location ("
                + "uuid             TEXT PRIMARY KEY,"
                + "spn              TEXT,"
                + "country_code     TEXT"
                + ")");

        DbUtils.sqlExecute(con,
                "CREATE TABLE IF NOT EXISTS facebook_primary_location ("
                + "uuid              TEXT PRIMARY KEY,"
                + "city_region_pairs TEXT,"
                + "zipcode           TEXT"
                + ")");

        DbUtils.sqlExecute(con,
                "CREATE TABLE IF NOT EXISTS facebook_primary_public_location ("
                + "uuid              TEXT PRIMARY KEY,"
                + "city              TEXT,"
                + "region            TEXT,"
                + "country           TEXT"
                + ")");

        DbUtils.sqlExecute(con,
                "CREATE TABLE IF NOT EXISTS facebook_last_location ("
                + "time                  TEXT PRIMARY KEY,"
                + "latitude              TEXT,"
                + "longitude             TEXT"
                + ")");

        DbUtils.sqlExecute(con,
                "CREATE TABLE IF NOT EXISTS facebook_location_history ("
                + "time                  TEXT PRIMARY KEY,"
                + "name                  TEXT,"
                + "latitude              TEXT,"
                + "longitude             TEXT"
                + ")");
    }

}
