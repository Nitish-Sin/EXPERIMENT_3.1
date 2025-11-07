// Assumes this file is at src/main/java/com/example/EmployeeServlet.java
package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Step 3: Create a servlet to handle the request.
 * We use @WebServlet to map this servlet to the "/employees" URL pattern.
 */
@WebServlet("/employees")
public class EmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // --- Database Configuration ---
    // !! REPLACE WITH YOUR ORACLE DB DETAILS !!
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String DB_USER = "NITISH_GG";
    private static final String DB_PASS = "root";
    private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get EmpID parameter from the form
        String empIdParam = request.getParameter("empId");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;

        // Apply shared styling for the response page
        out.println("<html><head><title>Employee List</title><style>");
        out.println("body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; display: grid; place-items: center; min-height: 90vh; background-color: #f0f4f8; }");
        out.println(".container { padding: 2rem; background: #fff; border-radius: 12px; box-shadow: 0 5px 15px rgba(0,0,0,0.08); width: 80%; max-width: 600px; }");
        out.println("h1 { color: #2c3e50; text-align: center; }");
        out.println("table { width: 100%; border-collapse: collapse; margin-top: 1rem; }");
        out.println("th, td { border: 1px solid #ddd; padding: 0.75rem; text-align: left; }");
        out.println("th { background-color: #f4f6f8; color: #34495e; }");
        out.println("a { display: inline-block; margin-top: 1.5rem; color: #3498db; text-decoration: none; font-weight: 600; }");
        out.println("a:hover { text-decoration: underline; }");
        out.println("</style></head><body><div class='container'>");

        try {
            // Step 4: Use JDBC to establish a connection
            Class.forName(DB_DRIVER);
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            // Step 5: If EmpID is entered, filter; otherwise, display full list.
            if (empIdParam != null && !empIdParam.trim().isEmpty()) {
                // Search for a specific employee
                sql = "SELECT EmpID, Name, Department FROM Employee WHERE EmpID = ?";
                pstmt = conn.prepareStatement(sql);
                
                try {
                    // Set parameter - must convert to number for Oracle
                    pstmt.setInt(1, Integer.parseInt(empIdParam.trim()));
                } catch (NumberFormatException e) {
                    out.println("<h1>Error</h1><p>Invalid Employee ID. Please enter a number.</p>");
                    out.println("<a href='index.html'>Back to Search</a></div></body></html>");
                    return; // Stop processing
                }
                
            } else {
                // Show all employees
                sql = "SELECT EmpID, Name, Department FROM Employee ORDER BY EmpID";
                pstmt = conn.prepareStatement(sql);
            }

            // Execute the query
            rs = pstmt.executeQuery();

            // Step 6: Display the result in a formatted HTML table
            out.println("<h1>Employee List</h1>");
            out.println("<table>");
            out.println("<tr><th>ID</th><th>Name</th><th>Department</th></tr>");

            boolean found = false;
            while (rs.next()) {
                found = true;
                out.println("<tr>");
                out.println("<td>" + rs.getInt("EmpID") + "</td>");
                out.println("<td>" + rs.getString("Name") + "</td>");
                out.println("<td>" + rs.getString("Department") + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");

            if (!found) {
                out.println("<p>No employees found.</p>");
            }

            out.println("<a href='index.html'>Back to Search</a>");

        } catch (ClassNotFoundException e) {
            out.println("<h1>Error</h1><p>Oracle JDBC Driver not found.</p>");
            out.println("<p>Please check that 'ojdbc.jar' is in your WEB-INF/lib folder.</p>");
            e.printStackTrace(out); // Log error
        } catch (SQLException se) {
            out.println("<h1>SQL Error</h1>");
            se.printStackTrace(out); // Log error
        } catch (Exception e) {
            out.println("<h1>Error</h1>");
            e.printStackTrace(out); // Log error
        } finally {
            // Step 7: Handle exceptions and close resources properly
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) { e.printStackTrace(); }
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) { e.printStackTrace(); }
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
            
            out.println("</div></body></html>");
            out.close();
        }
    }
}