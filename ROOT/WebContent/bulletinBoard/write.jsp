<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
.tablesize{border: 1px solid #dddddd; width:80%; margin-left:10%; margin-top:3%}
.write{padding:3px; border:2px solid #d2e1e7; background:#f9f9f9}
.input_txt{display:block; width:90%; height:20px; border:0 none; background:#f9f9f9; padding-left:5%; padding-right:5%; padding-top:7px;}
.input_txtarea{width:90%; border: 0; resize: none; padding-left:5%; padding-right:5%; padding-top:7px; font-family: "Nanum Gothic", sans-serif;}
.btnfloat{text-align: right; margin-top:10px; padding-right:10%; font-family: "Nanum Gothic", sans-serif;}
.btnwrite{ background: #eeeeee; border: none; padding:7px; font-family: "Nanum Gothic", sans-serif;}

input:focus, textarea:focus{outline: none; }

.btn2{
   width: 50px;
   height: 30px;
   background: #fff;
   border: 2px solid #043457;
   color:#043457;
   font-size: 14px;
   font-weight: bold;
   
   border-radius: 3px; 
   transition-duration: 0.4s;
}

.btn2:hover{
  background-color: #043457; 
  color: white;
  border: 2px solid #043457;
  width: 50px;
  height: 30px;
  text-align: center;
  text-decoration: none;
  font-size: 14px;
}

</style>
<script>
function goboard() {
    location.href="../bulletinBoard/bulletinBoardList.jsp";
}
</script>
</head>
<body>
<form action="doWrite.jsp" method="post">
<table class="tablesize">
		<tr>
		<th colspan="2" style="background-color: #eeeeee; padding-top:10px;padding-bottom:10px;">게시판 글 작성</th>
		</tr>
		<tbody>
		<tr class="write">
		<td><input type="text" placeholder="글 제목" name="name" class="input_txt"></td>
		</tr>
		<tr>
		<th colspan="2" style="background-color: #eeeeee; padding-top:5px;padding-bottom:5px;">내용</th>
		</tr>
		<tr>
		<td><textarea placeholder="글 내용" name="text" rows="20" class="input_txtarea"></textarea></td>
</tr>
</tbody>
</table>
<div class="btnfloat">
<input type="submit" class="btn2"/>
<input type="button" value="취소"class="btn2" onclick="goboard()"/>
</div>
</form>
</body>
</html>