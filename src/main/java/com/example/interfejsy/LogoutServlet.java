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
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    public void init() {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        Connection conn =
                AppUtils.getStoredConnection(request);
        UserAccount loginedUser = AppUtils.getLoginedUser(request.getSession());
        try {
            DBUtils.logout(conn, loginedUser.getUserName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        HttpSession session = request.getSession();
        session.invalidate();
        RequestDispatcher dispatcher =
                this.getServletContext().getRequestDispatcher("/login");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
