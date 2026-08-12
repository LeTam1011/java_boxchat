package com.javaboxchat.dao;

import com.javaboxchat.database.DBConnection;
import com.javaboxchat.model.CallHistory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CallHistoryDAO {


    // =====================================================
    // LƯU CUỘC GỌI
    // =====================================================

    public void saveCall(
            String caller,
            String receiver,
            java.time.LocalDateTime startTime,
            java.time.LocalDateTime endTime,
            int durationSeconds
    ) {

        String sql =
                """
                INSERT INTO call_history
                (
                    caller,
                    receiver,
                    start_time,
                    end_time,
                    duration_seconds,
                    status
                )
                VALUES
                (
                    ?, ?, ?, ?, ?, ?
                )
                """;

        try {

            Connection con =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setString(
                    1,
                    caller
            );

            ps.setString(
                    2,
                    receiver
            );

            ps.setTimestamp(
                    3,
                    java.sql.Timestamp.valueOf(
                            startTime
                    )
            );

            ps.setTimestamp(
                    4,
                    java.sql.Timestamp.valueOf(
                            endTime
                    )
            );

            ps.setInt(
                    5,
                    durationSeconds
            );

            ps.setString(
                    6,
                    "COMPLETED"
            );


            ps.executeUpdate();


            System.out.println(
                    "CALL HISTORY SAVED: "
                            + caller
                            + " -> "
                            + receiver
                            + " | "
                            + durationSeconds
                            + " seconds"
            );


        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    // =====================================================
    // LẤY LỊCH SỬ CUỘC GỌI
    // =====================================================

    public List<CallHistory> getCallHistory(
            String username
    ) {

        List<CallHistory> list =
                new ArrayList<>();


        String sql =
                """
                SELECT *
                FROM call_history
                WHERE caller = ?
                   OR receiver = ?
                ORDER BY id DESC
                """;


        try {

            Connection con =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setString(
                    1,
                    username
            );

            ps.setString(
                    2,
                    username
            );


            ResultSet rs =
                    ps.executeQuery();


            while (rs.next()) {

                CallHistory call =
                        new CallHistory(

                                rs.getInt(
                                        "id"
                                ),

                                rs.getString(
                                        "caller"
                                ),

                                rs.getString(
                                        "receiver"
                                ),

                                rs.getTimestamp(
                                        "start_time"
                                ).toString(),

                                rs.getTimestamp(
                                        "end_time"
                                ).toString(),

                                rs.getInt(
                                        "duration_seconds"
                                ),

                                rs.getString(
                                        "status"
                                )
                        );


                list.add(call);
            }


        } catch (Exception e) {

            e.printStackTrace();
        }


        return list;
    }
}