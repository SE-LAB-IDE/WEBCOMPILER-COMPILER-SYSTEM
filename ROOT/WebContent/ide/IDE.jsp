<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
* {
	background-color: #eff1f1;
	margin: 0;
	padding: 0;
	border: 0;
}

body {
	width: 100%;
	height: 100%;
}

iframe {
	margin: 0;
	padding: 0;
	border-width: thin;
	border-radius:1px;
}


#workSpaceList {
	width: 15%;
	height: 69vh;
	float: left;
	display: inline-block;
	background-color:#ffffff; 
}

#editor {
	width: 65%;
	height: 69vh;
	display: inline-block;
	background-color: #1e1e1e;
}

#ocr {
	width: 19%;
	height: 69vh;
	display: inline-block;
	background-color: #858181;
}

#run {
	width: 98%; height: 29vh; display: block; 
	margin-left: 1%; margin-top:1vh; box-sizing: border-box; border: solid 2px #000000;
	background-color: #ffffff;
}

#frame{
	position:absolute;
}

#top {
	position:relative;
	width: 100vw;
	height: 69vh;
}

#bottom {
	width: 100vw;
	height: 30vh;
}

</style>
</head>
<body>
	<div id ="frame">
		<div id="top">
			<iframe id="workSpaceList" name="workSpaceList" src="workSpaceList.jsp"> </iframe>
			<iframe id="editor" name="editor" src="editor.jsp"> </iframe>
			<iframe id="ocr" name="ocr" src="ocr.jsp"> </iframe>
		</div>

		<div id="bottom">
			<iframe id="run" name="run"> run</iframe>
		</div>
	</div>
</body>
</html>