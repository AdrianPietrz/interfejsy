package com.example.interfejsy;

import com.example.models.UserAccount;
import com.example.utils.AppUtils;
import com.example.utils.DBUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/doRegister"})
public class DoRegisterServlet extends HttpServlet {

    public void init() {
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        UserAccount user = null;
        boolean hasError = false;
        String errorString = null;
        if (userName == null || password == null || userName.length() == 0 || password.length() == 0) {
            hasError = true;
            errorString = "Required username and password!";
        } else {
            Connection conn = null;
            try {
                conn = DriverManager.getConnection("jdbc:h2:mem:default");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                DBUtils.save_user(conn, userName, password);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                hasError = true;
                errorString = e.getMessage();
            }
        }

        // If error, forward to /WEB-INF/views/login.jsp
        if (hasError) {
            user = new UserAccount();
            user.setUserName(userName);
            user.setPassword(password);

            request.setAttribute("errorString", errorString);
            request.setAttribute("user", user);

            // Forward to /WEB-INF/views/login.jsp
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/register.jsp");
            dispatcher.forward(request, response);
        }

        // If no error
        // Store user information in Session
        // And redirect to userInfo page.
        else {
            HttpSession session = request.getSession();
            AppUtils.storeLoginedUser(session, user);
            response.sendRedirect(request.getContextPath() +
                    "/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
