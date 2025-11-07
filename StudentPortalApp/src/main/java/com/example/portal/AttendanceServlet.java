package com.example.portal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Import java.sql.Date, not java.util.Date
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// This annotation maps the URL "/addAttendance" to this servlet.
// It matches the "action" attribute in the index.jsp form.
@WebServlet("/addAttendance")
public class AttendanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // !! REPLACE WITH YOUR ORACLE XE DB DETAILS !!
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String DB_USER = "NITISH_GG";
    private static final String DB_PASS = "root";
    private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";

    @Override
    public void init() throws ServletException {
        // Load the JDBC driver once when the servlet starts
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new ServletException("Oracle JDBC Driver not found", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // 1. Get parameters from the JSP form
        String studentId = request.getParameter("studentId");
        String dateStr = request.getParameter("attendanceDate");
        String status = request.getParameter("status");
        
        String sql = "INSERT INTO Attendance (StudentID, AttendanceDate, Status) VALUES (?, ?, ?)";
        
        // We will set either a success or error message to send back
        String successMessage = null;
        String errorMessage = null;

        try {
            // 2. Convert the form's date string into a java.sql.Date
            // This can fail if the date string is empty or invalid
            Date sqlDate = Date.valueOf(dateStr); 

            // 3. Connect to DB and execute INSERT
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, studentId);
                pstmt.setDate(2, sqlDate);
                pstmt.setString(3, status);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    // 4. Set a success message
                    successMessage = "Attendance recorded for " + studentId + " on " + dateStr;
                } else {
                    errorMessage = "Failed to record attendance. No rows were affected.";
                }
            }
        } catch (SQLException e) {
            // Check for specific Oracle error for "unique constraint violated"
            if (e.getErrorCode() == 1) { // ORA-00001
                errorMessage = "Error: Attendance for " + studentId + " on " + dateStr + " already exists.";
            } else {
                errorMessage = "Database Error: " + e.getMessage();
                e.printStackTrace();
            }
        } catch (IllegalArgumentException e) {
            // This catches errors from Date.valueOf(), e.g., if the date is missing
            errorMessage = "Invalid date format. Please select a date.";
            e.printStackTrace();
        } catch (Exception e) {
            errorMessage = "An unexpected error occurred: " + e.getMessage();
            e.printStackTrace();
        }

        // 5. Set attributes on the request to pass them to the JSP
        request.setAttribute("successMessage", successMessage);
        request.setAttribute("errorMessage", errorMessage);

        // 6. Forward the request (and its attributes) back to the index.jsp page
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }
}