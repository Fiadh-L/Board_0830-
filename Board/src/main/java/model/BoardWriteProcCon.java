package model;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/BoardWriteProcCon.do")
public class BoardWriteProcCon extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		reqProc(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		reqProc(request, response);
	}
	
	protected void reqProc(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		BoardBean bean = new BoardBean();
		
		bean.setWriter(req.getParameter("writer"));
		bean.setEmail(req.getParameter("email"));
		bean.setSubject(req.getParameter("subject"));
		bean.setPassword(req.getParameter("password"));
		bean.setContents(req.getParameter("contents"));

		BoardDAO mbdao=new BoardDAO();
		mbdao.insertBoard(bean);
		
		RequestDispatcher dis = req.getRequestDispatcher("BoardListCon.do");
		dis.forward(req, res);
		
	}
}
