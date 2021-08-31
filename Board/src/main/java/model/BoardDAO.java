package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	
	//---------------------------------------------
	//1.데이터 연결 메서드
	public void getCon() {
		try {
			
			Context initctx = new InitialContext();
			//서버단에서 context xml에서 가져오기
			Context envctx = (Context)initctx.lookup("java:/comp/env");
			DataSource ds= (DataSource)envctx.lookup("jdbc/xe");
			
			con=ds.getConnection();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//---------------------------------------------
	//2. 전체 게시글의 갯수를 반환하는 메서드
	public int getAllCount() {
		
		getCon();
		int count=0;
		
		try {
			//전체가 몇줄인지 반환해주는 sql문
			String sql = "select count(*) from board";
			pstmt=con.prepareStatement(sql); //쿼리 실행
			rs=pstmt.executeQuery();
			
			//읽을 데이터가 있냐?
			if(rs.next()) {
				//전체 글의 갯수를 반환
				rs.getInt(1);
			}
			
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return count;
	}
	
	public Vector<BoardBean> getAllBoard(int startRow, int endRow){
		getCon();
		Vector<BoardBean> v=new Vector();
		
		try {
			//10개씩 가져오는 쿼리문(Rownum :오라클 에만 있는 컬럼 // 최상위 숫자 중에 몇개)
			String sql = "SELECT * FROM (SELECT A.* ,Rownum Rnum FROM "
					+ "(SELECT * FROM board ORDER BY ref desc, re_step asc) A) "
					+ "WHERE Rnum >= ? and Rnum <= ?";
			
			pstmt =con.prepareStatement(sql);
			//?에 들어가는 내용 채우기
			//1번째 ?에는 startRow, 2번째 ?에는 endRow
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				
				BoardBean bean = new BoardBean();
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSubject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getString(6));
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContents(rs.getString(11));
				
				v.add(bean);
			}
			
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return v;
	}
	//---------------------------------------------
	//#3 게시글 저장 메서드
	public void insertBoard(BoardBean bean) {
		
		getCon();
		
		//bean 객체에서 넘어오지 않았던 데이터를 초기화
		int ref = 0; //쿼리를 실행시켜서 가장 큰 ref값을 가져온 후 +1 해줌
		int re_step=1;//새글=부모글
		int re_level=1;
		
		try {
			//BoardBean에 비어있는 데이터 질의문
			String refsql="select max(ref) from board";
			pstmt=con.prepareStatement(refsql);
			rs=pstmt.executeQuery();
			
			//글 그룹 설정
			if(rs.next()) {
				ref=rs.getInt(1)+1;
			}
			
			String sql="insert into board values(BOARD_SEQ.nextval,?,?,?,?,sysdate,?,?,?,0,?)";
			pstmt=con.prepareStatement(sql);
			
			pstmt.setString(1, bean.getWriter());
			pstmt.setString(2, bean.getEmail());
			pstmt.setString(3, bean.getSubject());
			pstmt.setString(4, bean.getPassword());
			pstmt.setInt(5, ref);
			pstmt.setInt(6, re_step);
			pstmt.setInt(7, re_level);
			pstmt.setString(8, bean.getContents());
			pstmt.executeUpdate();
			
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	//---------------------------------------------
	//#4 게시글 저장 메서드
	public void getOneBoard(int num) {
		
		getCon();
		
		BoardBean bean =null;
		try {
			
			//하나의 게시글 가져오기
			String sql="select * from board where num=?";
			pstmt=con.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				bean.setWriter(rs.getString(1));
				bean.setEmail(rs.getString(2));
				bean.setSubject(rs.getString(3));
				bean.setPassword(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getString(6));
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContents(rs.getString(11));
			}
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
