<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="algorithm.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
.add_al {
	margin-left: 12.5%;
	padding: 20px;
}

.tablehead {
	display: table;
	width: 85%;
	border-bottom: 2px solid #444444;
}

.tablebody {
	display: table;
	height: 40px;
	width: 73.5%;
}

.tableselect {
	dispaly: table;
	height: 40px;
	width: 15%;
	border: 2px solid #eff1f1;
	border-radius: 5%;
	font-family: 배달의민족 도현;
}

.line {
	width: 85%;
	border-bottom: solid 2px #eff1f1;
	margin-top: 25px;
	margin-bottom: 25px;
}

.textform {
	dispaly: table;
	width: 100%;
	height: 40px;
	padding: 10px;
	border: solid 2px #eff1f1;
	border-radius: 5px;
	box-sizing: border-box;
	font-family: 배달의민족 도현;
}

.textareaform {
	dispaly: table;
	width: 85%;
	height: 200px;
	padding: 10px;
	box-sizing: border-box;
	border: solid 2px #eff1f1;
	border-radius: 5px;
	resize: none;
	font-family: 배달의민족 도현;
}

.textareaform2 {
	dispaly: table;
	width: 42.5%;
	height: 200px;
	padding: 10px;
	box-sizing: border-box;
	border: solid 2px #eff1f1;
	border-radius: 5px;
	resize: none;
	font-family: 배달의민족 도현;
}

.right {
	margin-top: 25px;
	margin-left: 82%;
}

.homebtn {
	display: inline-block;
	padding: 4px;
	width: 50px !important;
	color: #000000;
	background: url( "../images/home.svg" ) no-repeat;
	border: 0px solid #f0efee;
	text-decoration: none;
	margin-left: 18px;
	margin-top: 18px;
}
</style>
<script>
	function goindex() {
		location.href = "../index.jsp"
	}
</script>
</head>
<body>

	<input type="button" style="float =: left" onclick="goindex()"
		class="homebtn">
	<%
		request.setCharacterEncoding("UTF-8");

	if ((String) session.getAttribute("authority") == null)//관리자가 아닐경우 index로 돌아감
		response.sendRedirect("../index.jsp");

	algorithmVO vo = new algorithmVO();
	algorithmDAO dao = new algorithmDAO();
	int algorithmNum;
	vo.setName("");//default 값
	vo.setExplanation("설명");
	vo.setExInput("exinput");
	vo.setExOutput("exOutpout");
	vo.setInput("realInput");
	vo.setOutput("realOutput");
	%>
	<div class="add_al">

		<div class="tablehead">
			<h2 style="margin-left: 15px;">알고리즘 추가</h2>
		</div>

		<form action="insertAlgorithm.jsp" method="post">

			<%
				if (request.getParameter("algorithmNum") != null) {
				algorithmNum = Integer.parseInt(request.getParameter("algorithmNum"));
				vo = dao.getAlgorithm(algorithmNum);
			%>
			<!-- 수정하기를 클릭하였을 경우 번호도 넘겨줌 -->
			<input type="hidden" name="algorithmNum" value='<%=algorithmNum%>'>
			<%
				}
			%>


			<div class="tablebody">
				<h4 class="h4body">제목</h4>
				<div class="table">
					<nobr>
						<select name="category" class="tableselect">
							<option value="level1">level1</option>
							<option value="level2">level2</option>
							<option value="level3">level3</option>
							<option value="level4">level4</option>
						</select> <input type="text" name="title" class="textform" value="<%=vo.getName() %>">
					</nobr>
				</div>
			</div>

			<div class=line></div>


			<textarea name="explanation" class="textareaform"><%=vo.getExplanation()%></textarea>

			<div class=line></div>

			<textarea name="exInput" class="textareaform2"><%=vo.getExInput()%></textarea>
			<textarea name="exOutput" class="textareaform2"><%=vo.getExOutput()%></textarea>

			<div class=line></div>

			<textarea name="input" class="textareaform2"><%=vo.getInput()%></textarea>
			<textarea name="output" class="textareaform2"><%=vo.getOutput()%></textarea>

			<input type="submit" value="저장" class="right">
		</form>
	</div>

</body>
</html>