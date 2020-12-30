<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="algorithm.*"%>
<%@ page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
div>div {
	display: none;
}
ul li, ul {
	list-style: none;
	padding-left: 0px;
}
.homebtn{
	display:inline-block;
	padding:4px;
	width:50px !important;
	color:#000000;
	background: url( "../images/home.svg" ) no-repeat;
	border:0px solid #f0efee;
	text-decoration:none;
	margin-left:3%;
	margin-top:18px;
}
.tablehead{display:table; width:90%; border-bottom: 2px solid #444444; margin: 3%;}

.margin{
font-size: 30px;
margin:3%;
}
.margin2{
margin-left:5%;
margin-top:10px;
margin-bottom:10px;
font-size: 20px;
}

.btn{
   float:right;
   margin-right:8%;
   margin-top:18px;
   width: 150px;
   height: 30px;
   background: #fff;
   border: 2px solid #043457;
   color:#043457;
   font-size: 14px;
   font-weight: bold; 
   border-radius: 3px; 
   transition-duration: 0.4s;
}

.btn:hover{
  float:right;
  margin-right:8%;
  margin-top:18px;
  background-color: #043457; 
  color: white;
  border: 2px solid #043457;
  width: 150px;
  height: 30px;
  text-align: center;
  text-decoration: none;
  font-size: 14px;
}
</style>

<script>
	function showOrHide(id) {
		var box = document.getElementById(id);
		if (box.style.display != 'block')
			box.style.display = 'block';
		else
			box.style.display = 'none';
	}
	function go() {
		location.href = "./addAlgorithm.jsp"
	}
</script>
</head>
<body>
	<input type="button" onclick="location.href='../index.jsp'" value="" class="homebtn">
	<input type="button" value="알고리즘 추가" onclick="go()" class="btn">
	<span class="tablehead"></span>
	<%
		request.setCharacterEncoding("UTF-8");
	if ((String) session.getAttribute("authority") == null) //관리자가 아닐경우 index로 돌아감
		response.sendRedirect("../index.jsp");
	algorithmDAO dao = new algorithmDAO();
	ArrayList<algorithmVO> algorithmList1 = new ArrayList<algorithmVO>(dao.getAlgorithmList("level1"));
	ArrayList<algorithmVO> algorithmList2 = new ArrayList<algorithmVO>(dao.getAlgorithmList("level2"));
	ArrayList<algorithmVO> algorithmList3 = new ArrayList<algorithmVO>(dao.getAlgorithmList("level3"));
	ArrayList<algorithmVO> algorithmList4 = new ArrayList<algorithmVO>(dao.getAlgorithmList("level4"));
	%>
	<div>
		<span onclick="showOrHide('list1')" class="margin">level1</span>
		<!-- 카테고리별로 생성해야함 -->
		<div id="list1">
			<ul>
				<%
					for (int i = 0; i < algorithmList1.size(); i++) {
				%>
				<li><%=algorithmList1.get(i).getName()%>
					<form action="addAlgorithm.jsp" target="_self"
						style="display: inline">
						<input type="hidden" name="algorithmNum" class="margin2"
							value='<%=algorithmList1.get(i).getNum()%>'> <input
							type="submit" value="수정하기">
					</form></li>
				<%
					}
				%>
			</ul>
		</div>
	</div>
	<div>
		<span onclick="showOrHide('list2')" class="margin">level2</span>
		<!-- 카테고리별로 생성해야함 -->
		<div id="list2">
			<ul>
				<%
					for (int i = 0; i < algorithmList2.size(); i++) {
				%>
				<li><%=algorithmList2.get(i).getName()%>
					<form action="addAlgorithm.jsp" target="_self" 
						style="display: inline">
						<input type="hidden" name="algorithmNum" class="margin2"
							value='<%=algorithmList2.get(i).getNum()%>'> <input
							type="submit" value="수정하기">
					</form></li>
				<%
					}
				%>
			</ul>
		</div>
	</div>
	<div>
		<span onclick="showOrHide('list3')" class="margin">level3</span>
		<!-- 카테고리별로 생성해야함 -->
		<div id="list3">
			<ul>
				<%
					for (int i = 0; i < algorithmList3.size(); i++) {
				%>
				<li><%=algorithmList3.get(i).getName()%>
					<form action="addAlgorithm.jsp" target="_self"
						style="display: inline">
						<input type="hidden" name="algorithmNum" class="margin2"
							value='<%=algorithmList3.get(i).getNum()%>'> <input
							type="submit" value="수정하기">
					</form></li>
				<%
					}
				%>
			</ul>
		</div>
	</div>
	<div>
		<span onclick="showOrHide('list4')" class="margin">level4</span>
		<!-- 카테고리별로 생성해야함 -->
		<div id="list4">
			<ul>
				<%
					for (int i = 0; i < algorithmList4.size(); i++) {
				%>
				<li><%=algorithmList4.get(i).getName()%>
					<form action="addAlgorithm.jsp" target="_self"
						style="display: inline">
						<input type="hidden" name="algorithmNum" class="margin2"
							value='<%=algorithmList4.get(i).getNum()%>'> <input
							type="submit" value="수정하기">
					</form></li>
				<%
					}
				%>
			</ul>
		</div>
	</div>
</body>
</html>