/**
 * パッケージ名：org.georepublic.excavator
 * ファイル名  ：Main.java
 * 
 * @author mbasa
 * @since Dec 6, 2023
 */
package org.georepublic.excavator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Iterator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.georepublic.excavator.activities.google.LocationHistory;
import org.georepublic.excavator.activities.google.SemanticLocationHistory;
import org.georepublic.excavator.db.Schema;

/**
 * 説明：
 *
 */

public class Main {

    /**
     * コンストラクタ
     *
     */
    public Main() {
    }

    /**
     * 
     * 
     * @param args
     */
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("d", "outputDB", true, "sqlite3 output file.");
        options.addOption("i", "inputDir", true, "Takeout directory");
        options.addOption("h", "help", false, "Help");

        HelpFormatter hf = new HelpFormatter();

        CommandLine cli;
        CommandLineParser parser = new DefaultParser();
        try {
            cli = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            hf.printHelp("excavator [opts]", options);
            return;
        }

        if (cli.hasOption("h") || !cli.hasOption("d") || !cli.hasOption('i')) {
            hf.printHelp("excavator [opts]", options);
            return;
        }

        String takeoutDir = cli.getOptionValue("i");
        String takeoutDb = cli.getOptionValue("d");

        Connection connection = null;

        try {
            System.out.println("Opening Database: " + takeoutDb);

            /**
             * Creating/Opening SQLite DB
             */
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + takeoutDb);

            Schema cs = new Schema(connection);
            cs.buildGoogleTables();
            // cs.buildFacebookTables();

            System.out.println("Processing Takeout Directory: " + takeoutDir);

            /**
             * Parsing Takeout Directory
             */
            Iterator<Path> paths = Files.walk(Paths.get(takeoutDir)).iterator();

            while (paths.hasNext()) {
                Path path = paths.next();
                if (!Files.isDirectory(path)) {

                    String fileName = path.getFileName().toString();

                    if (path.getParent().toString().contains(("Location History")) &&
                            fileName.compareTo("Records.json") == 0) {

                        System.out.println(" - " + path.toString());

                        LocationHistory lh = new LocationHistory(
                                connection,path.toString());
                        lh.saveToDB();
                    }
                    else if (path.getParent().toString().contains("Semantic Location History") &&
                            path.getFileName().toString().contains(".json")) {

                        System.out.println(" - " + path.toString());

                        SemanticLocationHistory slh = new SemanticLocationHistory(
                                connection, path.toString());
                        slh.saveToDB();
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
            return;
        }

        if (connection != null) {
            try {
                if (!connection.isClosed())
                    connection.close();
            } catch (Exception e) {
                ;
            }
        }
    }

}
