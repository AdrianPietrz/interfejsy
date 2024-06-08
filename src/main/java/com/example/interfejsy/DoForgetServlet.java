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

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

@WebServlet(urlPatterns = {"/doForget"})
public class DoForgetServlet extends HttpServlet {

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

        UserAccount user = null;
        boolean hasError = false;
        String errorString = null;
        if (userName == null || userName.isEmpty()) {
            hasError = true;
            errorString = "Required username!";
        } else {
            Connection conn = null;
            try {
                conn = DriverManager.getConnection("jdbc:h2:mem:default");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                UserAccount userAccount = DBUtils.findUserByEmail(conn, userName);
                sendEmail(userAccount.getUserName(), userAccount.getPassword());
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                hasError = true;
                errorString = e.getMessage();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // If error, forward to /WEB-INF/views/login.jsp
        if (hasError) {
            user = new UserAccount();
            user.setUserName(userName);

            request.setAttribute("errorString", errorString);
            request.setAttribute("user", user);

            // Forward to /WEB-INF/views/login.jsp
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
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

    public void sendEmail(String email, String token) throws InterruptedException {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.from", "adrian.pietrzak.inz@gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        "adrian.pietrzak.inz@gmail.com", "vyefacpcywfklstl");
            }
        });

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom();
            msg.setRecipients(Message.RecipientType.TO,
                    email);
            msg.setSubject("Przypomnienie hasła");
            msg.setSentDate(new Date());
            msg.setText(token);
            Transport.send(msg);
        } catch (MessagingException mex) {
            System.out.println("send failed, exception: " + mex);
        }
    }
}
