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
			String sql = "SELECT * FROM (SELECT A.* ,Rownum Rnum FROM (SELECT * FROM board ORDER BY ref desc, re_step asc) A) WHERE Rnum >= ? and Rnum <= ?";
			
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
	
}
