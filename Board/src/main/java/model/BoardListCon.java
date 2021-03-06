package model;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/BoardListCon.do")
public class BoardListCon extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		reqProc(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		reqProc(request, response);
		
	}
	
	protected void reqProc(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		//#1
		//한화면에 보여질 게시글의 숫자를 정함 = 10개씩 끊어보기
		int pageSize=10;
		//현재 보여지고 있는 페이지의 넘버값을 읽어들임
		String pageNum=req.getParameter("pageNum"); //[1][2]...
		
		if(pageNum==null) {
			pageNum="1";
		}
		
		//총 게시글의 갯수;
		int count=0;
		//화면에 보여지는 글 번호
		int number=0;
		//현재 보이고 있는 페이지 번호 형변환
		int currentPage=Integer.parseInt(pageNum);
		
		BoardDAO bdao = new BoardDAO();
		//총게시글의 숫자를 얻어오는 메소드
		count=bdao.getAllCount();
		//-----------------------------------------------------------------------
		
		//#2
		//현재 보여질 페이지 시작 번호를 설정
		//1page > (1-1)*10+1 => 1
		//2page > (2-1)*10+1 => 11
		//3page > (3-1)*10+1 => 21
		int startRow = (currentPage-1) * pageSize + 1;
		//1page > 1*10 => 10
		int endRow = (currentPage) * pageSize;
		
		//최신글 10개를 기준으로 게시글을 리턴받아주는 메서드 호출
		Vector<BoardBean> v = bdao.getAllBoard(startRow, endRow);
		//#3 start end까지의 데이터만 가져옴
		//-----------------------------------------------------------------------
		//전체 글이 9개 있을 때, 9-(1-1)*10 = 9
		//11번째 글의 위치, 11-(2-1)*10 = 1
		//23번째 글의 위치, 23-(3-1)*10 = 3
		number = count - (currentPage-1) * pageSize; //글목록에 표시할 글 번호
		
		//jsp페이지에서 사용할 수 있도록 서버에 넘겨줌(저장)
		req.setAttribute("v", v);//전체글 getallboard로 가져온것
		//-----------------------------------------------------------------------
		req.setAttribute("number", number);
		req.setAttribute("pageSize", pageSize);
		req.setAttribute("count", count);
		req.setAttribute("currentPage", currentPage);
		
		RequestDispatcher dis = req.getRequestDispatcher("BoardList.jsp");
		dis.forward(req, res);
		
		
	}
}
