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

- What is Compile System?
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


> Compile System For Solving Algorithms



<br>

### 📸 Picture

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

### 🤟Result

<br>

### 📖References

<br>

### 🔗Link
