package com.user.servlet;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.UserDAO;
import com.dao.UserDAOImpl;
import com.db.DbConnect;
import com.entity.User;

@WebServlet("/forgot")
public class ForgotPassword extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String accno = req.getParameter("accno");
			String username = req.getParameter("uname");

			UserDAO dao = new UserDAOImpl(DbConnect.getConn());
			User us = dao.checkUsernameAndAccno(accno, username);

			HttpSession session = req.getSession();

			if (us != null) {
				String msg = "Hi " + us.getFirstName() + " " + us.getLastName() + ",\n" + "Your Password="
						+ us.getPassword();
				String sub = "Netbanking Password";
				String to = us.getEmail();
				String from = "daspabitra55@gmail.com";
				SendEmail(msg, sub, to, from);
				session.setAttribute("succmsg", "Check your Email password is send");
				resp.sendRedirect("forgot.jsp");
			} else {
				session.setAttribute("failedmsg", "Account no or Username Invalid");
				resp.sendRedirect("forgot.jsp");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void SendEmail(String msg, String subject, String to, String from) {
		// TODO Auto-generated method stub

		// variable gmail host
		String host = "smtp.gmail.com";

		// get system properties
		Properties properties = System.getProperties();

		// setting important information to properties object
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		// step-1 get session
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication("daspabitra55@gmail.com", "daspabitra@123");
			}
		});

		// step-2 compose msg
		MimeMessage m = new MimeMessage(session);
		try {
			// from email
			m.setFrom(from);

			// add reciepnt
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// add subject
			m.setSubject(subject);
			m.setText(msg);

			// step-3 send messgae using transport class
			Transport.send(m);

		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

}
