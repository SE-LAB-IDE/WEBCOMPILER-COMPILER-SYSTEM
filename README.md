# 📋안전한 컴파일 시스템을 이용한 웹 IDE📋

> WEB IDE USING SECURE COMPILATION SYSTEM

<br>

### ©CopyRight

> SE WEBCOMPILER PROJECT TEAM

> Department of Computer Engineering, Hanshin University

<br>

### 📒Contents
> 특정 환경에서 작동되는 프로그램 없이도 간단하게 웹에서 여러 언어를 사용할 수 있다.부가적으로 작성한 코드를 저장하거나 또는 알고리즘 풀이기능과 편의를 위한 유저 정보 수정, 찾기 기능, 유저끼리의 커뮤니티를 위한 게시판과 문의 기능이 제공된다. OPENCV를 활용한 OCR 분석과 사용자 활동에 따른 데이터 분석을 추가해 4가지 언어로 사용자가 시스템을 사용할 수 있게 제작했다. 

<br>

### 🔧Environment
  - AWS : Naver Cloud Platform basic EC2
  - Server : Ubuntu 18.04 LTS, Apache Tomcat 9.0
  - Framework : MVC2 JSP
  - Database : MySQL
  - JDK-14.0.2
  - Python : 2.7.17
  - gcc (Ubuntu 7.5.0-3ubuntu1~18.04) 7.5.0
  - nodeJS v8.10.0

<br>

### 🚘Principle Of Operation

- [Compile System Using Container Maintenance Based Technology](https://github.com/SE-LAB-IDE/WEBCOMPILER-COMPILER-SYSTEM/blob/master/ROOT/src/linux/shellCompile_basic.java)
> 해당 프로그램의 컴파일 시스템은 JAVA환경에서 SH를 통해 Docker로 생성된 Container에 명령을 주고받을 수 있는 형태로 구현했다. 해당 시스템을 구현하기 위해서 컨테이너 전용 우분투 이미지를 생성하였다. 사용자는 웹 IDE를 통해 데이터를 서버로 전송한다. 전송된 데이터는 Docker를 통해 Container로 전송된다. 전송된 데이터는 컨테이너 안에서 컴파일이 진행되며, 진행된 결과값은 Container와 메인서버가 공유하는 폴더로 이동된다. 서버는 해당 데이터를 사용자의 웹화면에 제공한다.

- C
```
#!bin/bash

docker restart se01

docker exec se01 sh -c "cd compile; rm -r cTest;"
docker exec se01 sh -c "cd compile; rm -r cError.txt;"
docker exec se01 sh -c "cd compile; rm -r cResult.txt;"

docker exec se01 sh -c "cd data; mv cTest.txt ../compile"
docker exec se01 sh -c "cd compile; cp cTest.txt cTest.c"
docker exec se01 sh -c "cd compile; gcc -o cTest cTest.c 2> cError.txt"
docker exec se01 sh -c "cd compile; ./cTest > cResult.txt"
docker exec se01 sh -c "cd compile; mv cResult.txt ../data"
docker exec se01 sh -c "cd compile; mv cError.txt ../data "
```

- JAVA
```
#!bin/bash

docker restart se01

docker exec se01 sh -c "cd compile; rm -r SELAB.class;"
docker exec se01 sh -c "cd compile; rm -r javaError.txt;"
docker exec se01 sh -c "cd compile; rm -r javaResult.txt;"

docker exec se01 sh -c "cd data; mv javaTest.txt ../compile"
docker exec se01 sh -c "cd compile; cp javaTest.txt javaTest.java"
docker exec se01 sh -c "cd compile; javac javaTest.java 2> javaError.txt"
docker exec se01 sh -c "cd compile; java SELAB > javaResult.txt"
docker exec se01 sh -c "cd compile; mv javaResult.txt ../data"
docker exec se01 sh -c "cd compile; mv javaError.txt ../data "
```

- PYTHON
```
#!bin/bash

docker restart se01

docker exec se01 sh -c "cd compile; rm -r pythonTest.py;"
docker exec se01 sh -c "cd compile; rm -r pythonError.txt;"
docker exec se01 sh -c "cd compile; rm -r pythonResult.txt;"

docker exec se01 sh -c "cd data; mv pythonTest.txt ../compile"
docker exec se01 sh -c "cd compile; cp pythonTest.txt pythonTest.py"
docker exec se01 sh -c "cd compile; python pythonTest.py > pythonResult.txt 2> pythonError.txt"
docker exec se01 sh -c "cd compile; mv pythonResult.txt ../data"
docker exec se01 sh -c "cd compile; mv pythonError.txt ../data "
```

- JS
```
#!bin/bash

docker restart se01

docker exec se01 sh -c "cd compile; rm -r javascriptTest.js;"
docker exec se01 sh -c "cd compile; rm -r javascriptError.txt;"
docker exec se01 sh -c "cd compile; rm -r javascriptResult.txt;"

docker exec se01 sh -c "cd data; mv javascriptTest.txt ../compile"
docker exec se01 sh -c "cd compile; cp javascriptTest.txt javascriptTest.js"
docker exec se01 sh -c "cd compile; node javascriptTest.js > javascriptResult.txt 2> javascriptError.txt"
docker exec se01 sh -c "cd compile; mv javascriptResult.txt ../data"
docker exec se01 sh -c "cd compile; mv javascriptError.txt ../data "
```

- 다수의 클라이언트를 도커의 컨테이너로 분배하는 방법
> 하나의 Container를 다수의 사용자가 이용하게 되면 Container에 부하가 발생할수 있다. 해당 문제를 해결하기 위해 컨테이너를 사용자에게 순차분배하는 방법을 사용했다.
<img src="https://github.com/SE-LAB-IDE/WEBCOMPILER-COMPILER-SYSTEM/blob/master/ROOT/picture/docker.png">

- 사용자 LOG 기록
> 컴파일 시스템을 이용한 사용자에 대한 정보를 저장한다.    
```
docker = docker.replace("<br>", "");
FileWriter pw = new FileWriter("/usr/local/apache/log.txt", true);
HttpSession session = req.getSession();
String a = (String) session.getAttribute("id"); // 회원 아이디
Date b = new Date(session.getCreationTime()); // 최초 세션 생성 시각
Date c = new Date(session.getLastAccessedTime()); // 최근 세션 접근 시각
pw.write(a + " / " + b + " / " + c + " / " + docker + "\n");
pw.close();
```

- 클라이언트의 IP주소 받아오기
```
public static String getClientIp(HttpServletRequest req) {

	String[] header_IPs = { "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED",
				"HTTP_X_CLUSTER_CLIENT_IP", "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "X-Forwarded-For",
				"Proxy-Client-IP", "WL-Proxy-Client-IP" };

	for (String header : header_IPs) {
		String ip = req.getHeader(header);

		if (ip != null && !"unknown".equalsIgnoreCase(ip) && ip.length() != 0) {
			return ip;
			}
		}

	 return req.getRemoteAddr();

}   
```

- 저장된 LOG
<img src="https://github.com/SE-LAB-IDE/WEBCOMPILER-COMPILER-SYSTEM/blob/master/ROOT/picture/%EC%9D%B4%EC%9A%A9%EA%B8%B0%EB%A1%9D.png">    

- [Compile System For Solving Algorithms](https://github.com/SE-LAB-IDE/WEBCOMPILER-COMPILER-SYSTEM/blob/master/ROOT/src/linux/shellCompile_algorithm.java)
> 데이터베이스에 저장된 문제와 문제에 해당하는 입력값, 출력값을 불러와서 사용자가 입력한 코드에 대입해 문제를 해결한다. 

- 서버의 안정성을 확보하는 방법
> 사용자의 입력이 서버에 영항을 끼치는 경우, 해당 입력에 대해 제한을 걸도록 2가지의 방법을 채택했다.

1. 사용자 입력 검사
```
public static String wordCheck(String code) {
		String[] data = { "system(", "sudo shutdown -h 0", "sudo init 0", "sudo poweroff", "shutdown -r now",
				"shutdown", "docker restart", "docker exec", "docker stop", "docker rm", "docker rmi", "docker-compose",
				"shutdown -r", "init 0", "init 6", "halt -f", "reboot -f", "shutdown -h" };

		int dangerWord = 0;
		for (int i = 0; i < data.length; i++) {
			if (code.contains(data[i])) {
				dangerWord = dangerWord + 1;
				break;
			}
		}

		if (dangerWord == 0) {
			return code;
		} else {
			return "접근금지";
		}

	}
```
2. Container 정지
```
// 어떤 도커가 살아있는지 확인
			String playDocker = "";

			String docker = Reader("/usr/local/apache/docker/basic/docker.txt");
			FileWriter dockerCycle = new FileWriter("/usr/local/apache/docker/basic/docker.txt");

			ArrayList<String> dockerList = new ArrayList<String>();

			shellCmd("sh /usr/local/apache/docker/basic/check.sh");
			File check01 = new File("/usr/local/apache/docker/basic/check01.txt");
			File check02 = new File("/usr/local/apache/docker/basic/check02.txt");
			File check03 = new File("/usr/local/apache/docker/basic/check03.txt");

			if (check01.length() == 0) {
				dockerList.add("se01");
			}
			if (check02.length() == 0) {
				dockerList.add("se02");
			}
			if (check03.length() == 0) {
				dockerList.add("se03");
			}
			int length = dockerList.size();

			if (length == 1) {
				playDocker = dockerList.get(0);
				dockerCycle.write(dockerList.get(0));
				dockerCycle.close();
			} else if (length == 2) {
				if (docker.indexOf(dockerList.get(0)) != -1) {
					playDocker = dockerList.get(0);
					dockerCycle.write(dockerList.get(1));
					dockerCycle.close();
				} else if (docker.indexOf(dockerList.get(1)) != -1) {
					playDocker = dockerList.get(1);
					dockerCycle.write(dockerList.get(0));
					dockerCycle.close();
				}
			} else if (length == 3) {
				if (docker.indexOf("se01") != -1) {
					playDocker = dockerList.get(0);
					dockerCycle.write("se02");
					dockerCycle.close();
				} else if (docker.indexOf("se02") != -1) {
					playDocker = dockerList.get(1);
					dockerCycle.write("se03");
					dockerCycle.close();
				} else if (docker.indexOf("se03") != -1) {
					playDocker = dockerList.get(2);
					dockerCycle.write("se01");
					dockerCycle.close();
				} else {
					docker = "se01";
					playDocker = dockerList.get(0);
					dockerCycle.write("se02");
					dockerCycle.close();
				}
			}
```

<br>

### 📥File
- [All-Source](https://github.com/SE-LAB-IDE/WEBCOMPILER-COMPILER-SYSTEM/tree/master/ROOT)
- [Sql](https://github.com/DongGeon0908/Building-a-coding-test-site-using-WEB-IDE/blob/master/sql/kko_final.sql)
- [ROOT.war](https://github.com/SE-LAB-IDE/WEBCOMPILER-COMPILER-SYSTEM/blob/master/FILE/ROOT.war)
- [프로그램 설명서](https://github.com/SE-LAB-IDE/WEBCOMPILER-COMPILER-SYSTEM/blob/master/FILE/%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%A8%20%EC%84%A4%EB%AA%85%EC%84%9C.pdf)
<br>

### 👪Contributers

- [16학번 김동건](https://github.com/DongGeon0908)
- 16학번 황인준
- 18학번 이상호
- [19학번 안병욱](https://github.com/uuuugi)

<br>
<!-- ### 🤟Result -->

### 📖References
- [Dcoker](https://www.docker.com/get-started)
- [Monaco Editor](https://microsoft.github.io/monaco-editor/)
- [Docker 사용법](https://github.com/DongGeon0908/Docker-Container)
- [리눅스 개요](https://github.com/DongGeon0908/Linux)

<br>

### 🔗Link
- [WEBCOMPILER-WEB](https://github.com/SE-LAB-IDE/WEBCOMPILER-WEB)
- [제작 과정 전체](https://github.com/SE-LAB-IDE/WEBCOMPILER-COMPILER-SYSTEM/blob/master/ROOT/README.md)