package com.admin.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.AdminDAOImpl;
import com.db.DbConnect;
import com.entity.AccountTransaction;

@WebServlet("/serch_trans")
public class SerchTransaction extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String accno = req.getParameter("accno");

			AdminDAOImpl dao = new AdminDAOImpl(DbConnect.getConn());
			List<AccountTransaction> list = dao.getTransByAccnt(accno);

			HttpSession session = req.getSession();

			if (list.isEmpty()) {
				
			} else {
				session.setAttribute("translist", list);
				resp.sendRedirect("admin/all_trans.jsp");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
