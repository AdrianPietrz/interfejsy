package com.example.interfejsy;

import com.example.models.UserAccount;
import com.example.utils.AppUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "userInfoServlet", value = "/userInfo")
public class UserInfoServlet extends HttpServlet {

    public void init() {
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        // Check User has logged on
        UserAccount loginedUser = AppUtils.getLoginedUser(session);
        // Not logged in
        if (loginedUser == null) {

            // Redirect to login page.
            response.sendRedirect(request.getContextPath() +
                    "/login");
            return;
        }
        // Store info in request attribute
        request.setAttribute("user", loginedUser);
        RequestDispatcher dispatcher =
                this.getServletContext().getRequestDispatcher("/userInfo.jsp");
        dispatcher.forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}