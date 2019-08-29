package it.firegloves.mempoi;

import it.firegloves.mempoi.exception.MempoiRuntimeException;

import java.sql.*;
import java.util.Random;

/**
 * A simple class to populate database in order to run tests
 */

public class DBPopulator {

    Connection conn = null;

    public static void main(String[] args) {

        DBPopulator instance = new DBPopulator();
        instance.initConn();

        // instance.createTableSpeedTest();
        // instance.populateSpeedTest();

        instance.createTableMergedRegionsTest();
        instance.populateMergedRegionsTest();

        instance.closeConn();
    }

    private void initConn() {
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mempoi", "root", "");
        } catch (Exception e) {
            throw new MempoiRuntimeException(e);
        }
    }

    private void closeConn() {
        try {
            this.conn.close();
        } catch (Exception e) {
            throw new MempoiRuntimeException(e);
        }
    }


    /********************************************************************************************
     *                  SPEED TEST TABLE
     *******************************************************************************************/

    private void createTableSpeedTest() {

        String speedTestTbl = "CREATE TABLE " + TestConstants.TABLE_SPEED_TEST + " (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `creation_date` date NOT NULL,\n" +
                "  `dateTime` datetime DEFAULT NULL,\n" +
                "  `timeStamp` timestamp NULL DEFAULT NULL,\n" +
                "  `name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,\n" +
                "  `valid` tinyint(1) DEFAULT NULL,\n" +
                "  `usefulChar` char(1) COLLATE utf8mb4_general_ci DEFAULT NULL,\n" +
                "  `decimalOne` decimal(10,0) DEFAULT NULL,\n" +
                "  `bitTwo` bit(1) DEFAULT NULL,\n" +
                "  `doublone` double DEFAULT NULL,\n" +
                "  `floattone` float DEFAULT NULL,\n" +
                "  `interao` int(11) DEFAULT NULL,\n" +
                "  `mediano` mediumint(9) DEFAULT NULL,\n" +
                "  `attempato` time DEFAULT NULL,\n" +
                "  `interuccio` tinyint(4) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci";

        try {
            PreparedStatement stmt = this.conn.prepareStatement(speedTestTbl);
            stmt.execute();
        } catch (Exception e) {
            throw new MempoiRuntimeException(e);
        }
    }

    private void populateSpeedTest() {
        String sqlQuery = "insert into " + TestConstants.TABLE_SPEED_TEST + " (creation_date, dateTime, timeStamp, name, valid, usefulChar, decimalOne, bitTwo, doublone, floattone, interao, mediano, attempato, interuccio) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Random rand = new Random(System.currentTimeMillis());
        PreparedStatement pstmt = null;

        try {
            pstmt = this.conn.prepareStatement(sqlQuery);
            this.conn.setAutoCommit(false);

            for (int i = 1; i <= 200000; i++) {
                pstmt.setDate(1, new Date(System.currentTimeMillis()));
                pstmt.setDate(2, new Date(System.currentTimeMillis()));
                pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                pstmt.setString(4, "calogero " + i);
                pstmt.setBoolean(5, i % 2 == 0 ? true : false);
                pstmt.setString(6, i % 2 == 0 ? "C" : "B");
                pstmt.setFloat(7, 50.23f + rand.nextFloat());
                pstmt.setBoolean(8, i % 2 == 0 ? false: true);
                pstmt.setDouble(9, 7806d + rand.nextDouble());
                pstmt.setFloat(10, 93.4f + rand.nextFloat());
                pstmt.setInt(11, 97 + rand.nextInt(1000));
                pstmt.setInt(12, 12 + rand.nextInt(50));
                pstmt.setTime(13, new Time(System.currentTimeMillis()));
                pstmt.setInt(14, 3 + rand.nextInt(10));
                pstmt.addBatch();
            }

            int[] result = pstmt.executeBatch();
            System.out.println("The number of rows inserted: "+ result.length);
            this.conn.commit();
        } catch (Exception e) {
            throw new MempoiRuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                throw new MempoiRuntimeException(e);
            }
        }
    }



    /********************************************************************************************
     *                  MERGED REGIONS TABLE
     *******************************************************************************************/


    private void createTableMergedRegionsTest() {

        String speedTestTbl = "CREATE TABLE " + TestConstants.TABLE_MERGED_REGIONS + " (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `creation_date` date NOT NULL,\n" +
                "  `dateTime` datetime DEFAULT NULL,\n" +
                "  `timeStamp` timestamp NULL DEFAULT NULL,\n" +
                "  `name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,\n" +
                "  `valid` tinyint(1) DEFAULT NULL,\n" +
                "  `usefulChar` char(1) COLLATE utf8mb4_general_ci DEFAULT NULL,\n" +
                "  `decimalOne` decimal(10,0) DEFAULT NULL,\n" +
                "  `bitTwo` bit(1) DEFAULT NULL,\n" +
                "  `doublone` double DEFAULT NULL,\n" +
                "  `floattone` float DEFAULT NULL,\n" +
                "  `interao` int(11) DEFAULT NULL,\n" +
                "  `mediano` mediumint(9) DEFAULT NULL,\n" +
                "  `attempato` time DEFAULT NULL,\n" +
                "  `interuccio` tinyint(4) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci";

        try {
            PreparedStatement stmt = this.conn.prepareStatement(speedTestTbl);
            stmt.execute();
        } catch (Exception e) {
            throw new MempoiRuntimeException(e);
        }
    }

    private void populateMergedRegionsTest() {
        String sqlQuery = "insert into " + TestConstants.TABLE_MERGED_REGIONS + " (creation_date, dateTime, timeStamp, name, valid, usefulChar, decimalOne, bitTwo, doublone, floattone, interao, mediano, attempato, interuccio) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Random rand = new Random(System.currentTimeMillis());
        PreparedStatement pstmt = null;

        try {
            pstmt = this.conn.prepareStatement(sqlQuery);
            this.conn.setAutoCommit(false);

            Date[] creationDate = new Date[] { new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 50_000)};
            Date[] dateTime = new Date[] { new Date(System.currentTimeMillis() + 10_000), new Date(System.currentTimeMillis() - 50_000)};
            Timestamp[] timestamps = new Timestamp[] { new Timestamp(System.currentTimeMillis() - 30_000), new Timestamp(System.currentTimeMillis() + 80_000)};
            String[] names = new String[] { "hello dog", "hi dear" };
            boolean[] bools = new boolean[] { true, false };
            String[] chars = new String[] { "C", "B" };
            float[] floats = new float[] { 24.6f, 82.23f };
            boolean[] bools2 = new boolean[] { false, true };
            double[] doubles = new double[] { 2834.3d, 1399.34d };
            float[] floats2 = new float[] { 13.45f, 27.43f };
            int[] ints = new int[] { 2, 34 };
            int[] ints2 = new int[] { 93, 23 };
            Time[] times = new Time[] { new Time(System.currentTimeMillis() + 62_023), new Time(System.currentTimeMillis() - 23_546) };
            int[] ints3 = new int[] { 4, 13 };

//            for (int i = 1; i <= 2000; i++) {
            for (int i = 1; i <= 200_000; i++) {

                int ind = (int) Math.ceil(i / 100) % 2;

                pstmt.setDate(1, creationDate[ind]);
                pstmt.setDate(2, dateTime[ind]);
                pstmt.setTimestamp(3, timestamps[ind]);
                pstmt.setString(4, names[ind]);
                pstmt.setBoolean(5, bools[ind]);
                pstmt.setString(6, chars[ind]);
                pstmt.setFloat(7, floats[ind]);
                pstmt.setBoolean(8, bools2[ind]);
                pstmt.setDouble(9, doubles[ind]);
                pstmt.setFloat(10, floats2[ind]);
                pstmt.setInt(11, ints[ind]);
                pstmt.setInt(12, ints2[ind]);
                pstmt.setTime(13, times[ind]);
                pstmt.setInt(14, ints3[ind]);
                pstmt.addBatch();
            }

            int[] result = pstmt.executeBatch();
            System.out.println("The number of rows inserted: "+ result.length);
            this.conn.commit();
        } catch (Exception e) {
            throw new MempoiRuntimeException(e);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException e) {
                throw new MempoiRuntimeException(e);
            }
        }
    }
}

