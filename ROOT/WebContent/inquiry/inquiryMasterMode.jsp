<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ page import="inquiry.*"%>
<%@ page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<title>Insert title here</title>
<style>
.inquiryBox>div{
display:none;
display:block;
background:#ebebeb;
line-height:43px
}

.head>.inquiryBox>div{
	display:none;
	display:block;
	background:#4e545d;
	line-height:43px
}

.textform{
display:table-cell;
	width: 80%; 
	height: 40px; 
	margin: 3px;
	border: solid 2px #e3dcdc; 
	border-radius: 4px; 
	box-sizing: border-box;}

.btn{
	display:table-cell;
   width: 180px;
   height: 40px;
   background: #fff;
   border: 2px solid #e3dcdc;
   color:#043457;
   font-size: 14px;
   border-radius: 4px; 
   transition-duration: 0.4s;
}

.btn:hover{
  background-color: #ebebeb; 
  color: white;
  border: 2px solid #ebebeb;
  width: 180px;
  height: 40px;
  text-align: center;
  text-decoration: none;
  font-size: 14px;
}

.btn2{
	display:table-cell;
	background-color: #ffffff; 
   width: 15%;
   height: 40px;
   border: 2px solid #e3dcdc;
   color:#043457;
   font-size: 14px;
   border-radius: 4px; 
}



.homebtn{ display:inline-block; padding:4px; width:50px !important; color:#000000;
	background: url( "../images/home.svg" ) no-repeat; border:0px solid #f0efee; text-decoration:none; margin-left:5%; margin-top:25px;
}
.headname{margin-left:5%; margin-bottom:-3%;}

.namemagin{margin-left:10%;}

.formmargin{
	margin-left:2%;
	margin-top:10px;
	margin-bottom:10px;
	display: inline-block;
}

.formmargin2{
	margin-left:2%;
	margin-top:10px;
	margin-bottom:10px;
}

.bx{width:90%;height:100%;background:#e3dcdc;margin:5%; border-radius: 5px; padding:1.5%;}
.bx .qeustion{text-decoration:none; display:block;background:center no-repeat #f9fafa;color:#000000;font-size:14px;line-height:43px;}

.bx .my_menu{width:90%; position:inline-block;;height:30%; border-bottom:1px solid #ebebeb;background:#f0f2f2;font-size:0; margin-left:3.5%;}
.bx .my_menu li{width:90%; dposition:inline-block;;height:12%;border-bottom:1px solid #ebebeb;background:#f0f2f2;}

.txt{width : 90%; margin-left:2%; height:40px; margin-top:20px; box-sizing: border-box; border: solid 2px #f0f2f2; border-radius: 5px;}
.txarea{width : 90%; margin-left:2%; float:20%; box-sizing: border-box; border: solid 2px #f0f2f2; border-radius: 5px; resize: none;}

.content{margin-left:2%; margin-right:2%; font-size:12px;}
</style>

<script>
   function showOrHide(id) {
      var box = document.getElementById(id);
      if (box.style.display != 'block')// display 상태가 block이 아니라면 block로 설정 == show
         box.style.display = "block";
      else
         // block라면 none로 설정 == hide
         box.style.display = "none";
   }
</script>
</head>
<body>
   <input type="button" style="float=:left" onclick="location.href='../index.jsp'" class="homebtn">
   <h2 class="headname">문의답변</h2>
	<div class="bx">
   &nbsp;
   <form action="inquiryMasterMode.jsp" target="_self" method="post" class="formmargin">
      <input type="hidden" name="type" value="normal"> 
      <input type="submit" value="답변안된 리스트 출력" class="btn">
   </form>
   
   <form action="inquiryMasterMode.jsp" target="_self" method="post" class="formmargin">
      <input type="hidden" name="type" value="all"> 
      <input type="submit" value="전체 출력" class="btn">
   </form>
   
   <form action="inquiryMasterMode.jsp" target="_self" method="post" class="formmargin2">
      <input type="hidden" name="type" value="sId">
      <input type="text" name="text" class="textform"> 
      <input type="submit" value="id검색" class="btn2">
   </form>
   
   
   <%
      request.setCharacterEncoding("UTF-8");
      inquiryDAO dao = new inquiryDAO();

   if ((String) session.getAttribute("authority") == null)//관리자가 아닐경우 index로 돌아감
      response.sendRedirect("../index.jsp");
      String id = (String) session.getAttribute("id");

   if (request.getParameter("type") != null)
      if ("normal".equals(request.getParameter("type"))) {
         ArrayList<inquiryVO> inquiryList = new ArrayList<inquiryVO>(dao.getInquiryList());//문의중 답변이 안달린 것들의 list

         for (int i = 0; i < inquiryList.size(); i++) {
   %>
   <div class="inquiryBox">
      <span onclick="showOrHide('<%=i%>')"><h4 style="margin-left:5%;">문의내역 : &nbsp;&nbsp;<%=inquiryList.get(i).getTitle() %>&nbsp;&nbsp;&nbsp; 문의자 ID : &nbsp;&nbsp;<%=inquiryList.get(i).getId() %>
      <% if(inquiryList.get(i).getComment()==1)
         out.print("&nbsp; <span style='color:green'>O</span>");
      else
         out.print("&nbsp; <span style='color:red'>X</span>"); %>
      </h4></span>
      <!-- 문의제목과 id출력 -->
      <div id='<%=i%>'>
      <h4 class="content">- <%=inquiryList.get(i).getText()%></h4><!-- 문의내용 -->
         <form action="writeInquiryComment.jsp" method="post">
            <!-- 답변 form -->
            <input type="text" name="title" placeholder="제목" class="txt"><br>
            <textarea cols="40" rows="8" name="text" class="txarea"></textarea>
            <input type="hidden" name="inquiryNum" value='<%=inquiryList.get(i).getNum()%>'> <input type="submit" value="작성">
         </form>
      </div>
   </div>
   
   <%
      }
   }
      else if("sId".equals(request.getParameter("type"))){
         String sId = (String)request.getParameter("text");
         
         ArrayList <inquiryVO> inquiryList = new ArrayList<inquiryVO> (dao.getInquiryList(sId));
         
         for(int i=0; i<inquiryList.size(); i++){ %>
            
         <div class="inquiryBox">
             <span onclick="showOrHide('<%=i%>')"><h4 style="margin-left:5%;">문의내역 : &nbsp;&nbsp;<%=inquiryList.get(i).getTitle() %>&nbsp;&nbsp;&nbsp; 문의자 ID : &nbsp;&nbsp;<%=inquiryList.get(i).getId() %>
		      <% if(inquiryList.get(i).getComment()==1)
		         out.print("&nbsp; <span style='color:green'>O</span>");
		      else
		         out.print("&nbsp; <span style='color:red'>X</span>"); %>
		      </h4></span>
            <!-- 문의제목과 id 출력 -->
               <div id='<%=i %>'>
               <h4 class="content">- <%=inquiryList.get(i).getText()%></h4><!-- 문의내용 -->
               <% if(inquiryList.get(i).getComment()==1){ // 답변이 있을경우 답변 출력
                  inquiryCommentVO vo = new inquiryCommentVO();
                  vo = dao.getInquiryComment(inquiryList.get(i).getNum()); %>
               
                  <h4 style="margin-left:5%;">▶ 문의답변 : &nbsp;&nbsp;<%= vo.getTitle() %>&nbsp;&nbsp;&nbsp; 문의자 ID : &nbsp;&nbsp; <%= vo.getId() %></h4> <!-- 답변제목과 답변자 id출력 -->
                  	<h4 class="content">▶ <%= vo.getText() %></h4>
               <%} else{%>
                  <form action="writeInquiryComment.jsp" method="post">
                  <!-- 답변 form -->
                  <input type="text" name="title" placeholder="제목" class="txt"><br>
                  <textarea cols="40" rows="8" name="text" class="txarea"></textarea>
                  <input type="hidden" name="inquiryNum" value='<%=inquiryList.get(i).getNum()%>'> <input type="submit" value="작성">
               </form>
               <% }%> 
               </div>
         </div>
         <% }
      }
      else if ("all".equals(request.getParameter("type"))){
         ArrayList <inquiryVO> inquiryList = new ArrayList<inquiryVO> (dao.getInquiryListAll());
         for(int i=0; i<inquiryList.size(); i++){ %>
         
         <div class="inquiryBox">
             <span onclick="showOrHide('<%=i%>')"><h4 style="margin-left:5%;">문의내역 : &nbsp;&nbsp;<%=inquiryList.get(i).getTitle() %>&nbsp;&nbsp;&nbsp; 문의자 ID : &nbsp;&nbsp;<%=inquiryList.get(i).getId() %>
		      <% if(inquiryList.get(i).getComment()==1)
		         out.print("&nbsp; <span style='color:green'>O</span>");
		      else
		         out.print("&nbsp; <span style='color:red'>X</span>"); %>
		      </h4></span>
            <!-- 문의제목과 id 출력 -->
               <div id='<%=i %>'>
               <h4 class="content">- <%=inquiryList.get(i).getText()%></h4><!-- 문의내용 -->
               <% if(inquiryList.get(i).getComment()==1){ // 답변이 있을경우 답변 출력
                  inquiryCommentVO vo = new inquiryCommentVO();
                  vo = dao.getInquiryComment(inquiryList.get(i).getNum()); %>
               
                  <h4 style="margin-left:5%;">▶ 문의답변 : &nbsp;&nbsp;<%= vo.getTitle() %>&nbsp;&nbsp;&nbsp; 문의자 ID : &nbsp;&nbsp; <%= vo.getId() %></h4> <!-- 답변제목과 답변자 id출력 -->
                  <h4 class="content">▶ <%= vo.getText() %></h4>
               <%} else{%>
                  <form action="writeInquiryComment.jsp" method="post">
                  <!-- 답변 form -->
                  <input type="text" name="title" placeholder="제목" class="txt"><br>
                  <textarea cols="40" rows="8" name="text" class="txarea"></textarea>
                  <input type="hidden" name="inquiryNum" value='<%=inquiryList.get(i).getNum()%>'> <input type="submit" value="작성">
               </form>
               <% }%> 
               </div>
         </div>
         <% }
      }
   %>
   </div>
</body>
</html>