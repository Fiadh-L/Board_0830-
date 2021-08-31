<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>BoardList</title>
<link href="./bootstrap-3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<center>
	<h2>전체 게시글 보기</h2>
	<table width="700" border="1" bordercolor="skyblue">
		<tr height="40">
			<td colspan="5" align="right"><button onclick="location.href='BoardWriteForm.jsp'">글쓰기</button></td>
		</tr>
		<tr height="40">
			<td width="20" align="center">번호</td>
			<td width="120" align="center">제목</td>
			<td width="70" align="center">글쓴이</td>
			<td width="20" align="center">작성일</td>
			<td width="20" align="center">조회수</td>
		</tr>
		<c:set var="number" value="${number }"/>
		<!-- vector에 있는 모든 리스트 가져오기 -->
		
		<c:forEach var="bean" items="${v }">
			<tr height="40">
				<!-- setattribute로 number를 따로 넘겼기 때문에 'bean.'으로 받지 않아도 됨 -->
				<td width="20" align="left">${number }</td> 
				<td width="120" align="left">
				<!-- 댓글에서 들여쓰기(첫번째 원글은 들여쓰기가 필요없으므로 두번째부터 들여쓰기 시작) -->
				<c:if test="${bean.re_step > 1 }">
				<c:forEach var="j" begin="1" end="${bean.re_step -1}*5">&nbsp;[re]</c:forEach>
				</c:if>
				<!-- do로 넘어간다는 것은 servlet으로 넘긴다는 것! -->
				<a href="BoardInfoControl.do?num=${bean.num }" style='text-decoration:none'>${bean.subject }</a>
				</td>
				<td width="70" align="center">${bean.writer }</td>
				<td width="20" align="center">${bean.reg_date }</td>
				<td width="20" align="center">${bean.readcount }</td>
			</tr>
			<!-- 글카운트 최신글로 하나씩 감소  -->
			<c:set var="number" value="${number-1 }"></c:set>
		</c:forEach>
	</table>
	<!-- ---------------------------------------------------------------- -->
	<p/>
	<!-- 페이지 카운터링 소스 구현 -->
	<!-- 전체글 10 -> 10/10=1+0 (나누어 떨어지면 증가값 없음)
	전체글 34 -> 34/10=3+1 (나누어 떨어지지 않으면 1씩 더하기) -->
	<c:set var="pageCount" value="${count/pageSize+(count%pageSize==0?0:1) }"/>
	<!-- 시작페이지 숫자지정 -->
	<c:set var="startPage" value="${1 }"/>
	<c:if test="${currentPage%10 != 0 }"> <!-- 1~9/ 11~19  -->
		<fmt:parseNumber var="result" value="${currentPage/10 }" integerOnly="true"/>
		<!-- 시작페이지 모양 잡기 -->
		<c:set var="startPage" value="${result*10+1 }"/>
	</c:if>
	<!-- 화면에 보여지는 페이지 처리 숫자 표현 구현 -->
	<c:set var="pageBlock" value="${10 }"/>
	<!-- 첫번째 페이지 블럭의 경우 : 1+10-1=10 -->
	<c:set var="endPage" value="${startPage+pageBlock-1 }"/>
	<!-- 페이지가 너무 크면 -->
	<c:if test="${endPage>pageCount }">
		<c:set var="endPage" value="${pageCount }"/>
	</c:if>
	<!-- ---------------------------------------------------------------- -->
	<!-- 이전 링크페이지 -->
	<c:if test="${startPage > 10 }">
		<a href="BoardListCon.do?pageNum=${startPage-10 }" style='text-decoration:none'>&lt;&lt;이전</a>
	</c:if>
	<!-- 페이징 처리 -->
	<c:forEach var="i" begin="${startPage }" end="${endPage }">
		<a href="BoardListCon.do?pageNum=${i }" style='text-decoration:none'>[${i }]&nbsp;</a>
	</c:forEach>
	<!-- 이후 링크페이지 -->	
	<c:if test="${endPage > pageCount }">
		<a href="BoardListCon.do?pageNum=${startPage+10 }" style='text-decoration:none'>다음&gt;&gt;</a>
	</c:if>
</center>
</body>
</html>