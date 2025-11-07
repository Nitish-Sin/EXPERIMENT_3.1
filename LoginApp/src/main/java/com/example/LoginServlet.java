// Assumes this file is located in src/main/java/com/example/LoginServlet.java
// You will need the servlet-api.jar in your classpath to compile this.
// If using Maven, add the javax.servlet-api dependency.

package com.example;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Step 2: Create a servlet class that extends HttpServlet.
 * Note: We are using web.xml for mapping, so @WebServlet annotation is commented out.
 * If you prefer annotations, uncomment the line below and remove the
 * <servlet> and <servlet-mapping> from web.xml.
 */
// @WebServlet("/login") 
public class LoginServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    /**
     * Step 2 (cont.): ...and override doPost().
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Set the response content type
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        /**
         * Step 3: Retrieve username and password.
         * The string "username" and "password" must match the 'name' 
         * attribute in the HTML form <input> tags.
         */
        String user = request.getParameter("username");
        String pass = request.getParameter("password");

        out.println("<html><head><title>Login Result</title><style>");
        out.println("body { font-family: Arial, sans-serif; display: grid; place-items: center; min-height: 90vh; background-color: #f9f9f9; }");
        out.println(".message { padding: 2rem; background: #fff; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); text-align: center; }");
        out.println("h1 { color: #333; }");
        out.println("a { color: #007aff; text-decoration: none; font-weight: 600; }");
        out.println("</style></head><body>");
        out.println("<div class='message'>");

        /**
         * Step 4: Check credentials against hardcoded values.
         * For this example, we'll check for "John" and "pass123".
         */
        if ("NITISH".equals(user) && "jyoti@123".equals(pass)) {
            /**
             * Step 5: If valid, display welcome message.
             */
            out.println("<h1>Welcome, " + user + "! Login Successful.</h1>");
        } else {
            // Handle invalid login
            out.println("<h1>Login Failed!</h1>");
            out.println("<p>Invalid username or password.</p>");
            out.println("<a href='index.html'>Try Again</a>");
        }

        out.println("</div>");
        out.println("</body></html>");
        out.close();
    }
}