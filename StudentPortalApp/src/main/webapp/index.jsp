<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Attendance Portal</title>
    <style>
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
            display: grid;
            place-items: center;
            min-height: 90vh;
            background-color: #f0f4f8;
        }
        .container {
            background: #ffffff;
            border: 1px solid #dde;
            padding: 2rem;
            border-radius: 12px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
            width: 400px;
        }
        h2 {
            text-align: center;
            margin-top: 0;
            color: #2c3e50;
        }
        form div {
            margin-bottom: 1rem;
        }
        label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: 600;
            color: #34495e;
        }
        input[type="text"], input[type="date"], select {
            width: 100%;
            padding: 0.75rem;
            box-sizing: border-box; 
            border: 1px solid #bdc3c7;
            border-radius: 8px;
            font-size: 1rem;
            transition: border-color 0.2s ease, box-shadow 0.2s ease;
        }
        input:focus, select:focus {
            outline: none;
            border-color: #2980b9;
            box-shadow: 0 0 0 3px rgba(41, 128, 185, 0.2);
        }
        button {
            width: 100%;
            padding: 0.85rem;
            background-color: #2980b9;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: background-color 0.2s ease;
        }
        button:hover {
            background-color: #2c3e50;
        }
        
        /* Message styles */
        .message {
            padding: 1rem;
            border-radius: 8px;
            margin-bottom: 1rem;
            text-align: center;
            font-weight: 600;
        }
        .success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
    </style>
</head>
<body>

    <div class="container">
        <h2>Attendance Portal</h2>

        <%-- 
          This is the core of the JSP. We use Java scriptlets (<%...%>) 
          to check for messages sent back from the servlet.
        --%>
        <%
            // Get the attributes from the request
            String successMessage = (String) request.getAttribute("successMessage");
            String errorMessage = (String) request.getAttribute("errorMessage");
        
            // Display success message if it exists
            if (successMessage != null) {
        %>
                <div class="message success">
                    <%= successMessage %> 
                </div>
        <%
            }
        
            // Display error message if it exists
            if (errorMessage != null) {
        %>
                <div class="message error">
                    <%= errorMessage %>
                </div>
        <%
            }
        %>
        
        <!-- 
          This form submits data to the "addAttendance" URL,
          which we will map to our servlet.
        -->
        <form action="addAttendance" method="POST">
            <div>
                <label for="studentId">Student ID:</label>
                <input type="text" id="studentId" name="studentId" placeholder="e.g., 2023CS101" required>
            </div>
            <div>
                <label for="attendanceDate">Date:</label>
                <input type="date" id="attendanceDate" name="attendanceDate" required>
            </div>
            <div>
                <label for="status">Status:</label>
                <select id="status" name="status" required>
                    <option value="Present">Present</option>
                    <option value="Absent">Absent</option>
                </select>
            </div>
            <button type="submit">Submit Attendance</button>
        </form>
    </div>

</body>
</html>