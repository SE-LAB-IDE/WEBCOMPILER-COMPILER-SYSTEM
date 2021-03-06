package linux;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DBconnection.DBconnection;

@WebServlet("/shellCompile_algorithm")
public class shellCompile_algorithm extends HttpServlet {
   protected void doHandle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

      // utf-8 인코딩
      resp.setContentType("text/html;charset=UTF-8");
      req.setCharacterEncoding("UTF-8");

      // DB 사용 객체 생성
      Connection conn;
      PreparedStatement pstmt = null;
      ResultSet rs = null;

      // Session 받기
      HttpSession session = req.getSession(true);
      int result = 0;

      // 시간 평균
      double timeAvg = 0;

      // 생성자 모임
      File log, algoTestOld;
      BufferedWriter bf;
      PrintWriter out = resp.getWriter();

      try {

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

         log = new File("/usr/local/apache/log/algoLog.txt");
         bf = new BufferedWriter(new FileWriter(log, true));
         conn = DBconnection.getConnection();

         String userId = (String) session.getAttribute("id");
         int algorithmNum = Integer.parseInt(req.getParameter("algorithmNum"));
         ////강제 값 추가
         
         ///강제 값 추가
         String code = req.getParameter("code");
         String codeType = req.getParameter("codeType");
         String dbCode="";
         String[] splitCode = code.split("D3EA7KG44QW1ER0458,|D3EA7KG44QW1ER0458");// split해서 라인별로 배열에 저장
         code = "";
         for (int i = 0; i < splitCode.length; i++) {// 라인+개행문자를 통해 하나의 변수에 저장
            code += splitCode[i];
            dbCode += splitCode[i];
            code += "\n";
            dbCode+="\\n";
         }

         // 1차 방어막
         String wordCheck = wordCheck(code);
         if (wordCheck.equals("접근금지")) {
            out.print(wordCheck);

            Date initTime = new Date(session.getCreationTime()); // 최초 세션 생성 시각
            Date recentTime = new Date(session.getLastAccessedTime()); // 최근 세션 접근 시각
            bf.write(userId + " / " + getClientIp(req) + " / " + initTime + " / " + recentTime + " / " + playDocker
                  + " / " + codeType + " / 불법 접근입니다.   \n");

            bf.write(wordCheck + "\n");
            bf.flush();
            bf.close();

         } else {

            bf.write("user_id = " + userId + "\n");
            bf.write("algorithmNum = " + algorithmNum + "\n");
            bf.flush();

            String algo_data = "select * from user_algorithm_data where id=? and algorithmNum=?;"; // 도전여부
            pstmt = conn.prepareStatement(algo_data);
            pstmt.setString(1, userId);
            pstmt.setInt(2, algorithmNum);
            rs = pstmt.executeQuery();

            if (rs.next() == false) {
               String algo_insert = "insert into user_algorithm_data(id,algorithmNum,result) values(?,?,?);"; // 도전

               pstmt = conn.prepareStatement(algo_insert);
               pstmt.setString(1, userId);
               pstmt.setInt(2, algorithmNum);
               pstmt.setInt(3, result);
               pstmt.executeUpdate();
            }

            String testInput = "", testOutput = "";
            String sql_db = "select * from algorithm where algorithmNum=?;";
            pstmt = conn.prepareStatement(sql_db);
            pstmt.setInt(1, algorithmNum);
            rs = pstmt.executeQuery();

            if (rs.next()) { // 디비 입력, 결과 받아오기
               testInput = rs.getString("input");
               testOutput = rs.getString("output");
            }

            // 맞은 개수
            int problemCheck = 0;

            String[] algoInput = testInput.split("\\^\\^");// 디비 입력

            String[] output = testOutput.split("\\$\\$"); // 디비 결과
            String[] input = new String[output.length];

            bf.write("언어 종류 : " + codeType + "\n");
            bf.write(code + "\n");
            bf.flush();

            String code_insert = "insert into user_algorithm_code(id, algorithmNum, code, codeType, result) values(?,?,?,?,?);";
            pstmt = conn.prepareStatement(code_insert);
            pstmt.setString(1, userId);
            pstmt.setInt(2, algorithmNum);
            pstmt.setString(3, dbCode);
            pstmt.setString(4, codeType);
            pstmt.setInt(5, result);
            // pstmt.setInt(6, result);
            pstmt.executeUpdate();

            int codeNum = 0;
            String code_num = "select max(codeNum) from user_algorithm_code where id = ?;";
            pstmt = conn.prepareStatement(code_num);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next() == true) {
               codeNum = rs.getInt("max(codeNum)");
            }

            if (codeType.equals("c")) {
               algoTestOld = new File("/usr/local/apache/share/algotest");
               if (algoTestOld.exists()) {
                  shellCmd("rm -r /usr/local/apache/share/algotest");
               }
               FileWriter codew = new FileWriter("/usr/local/apache/share/algotest.c");
               codew.write(code);
               codew.close();

               FileWriter algo_c = new FileWriter("/usr/local/apache/share/algo_c.sh");
               algo_c.write("docker restart " + playDocker + "\n");
               algo_c.write("docker exec " + playDocker + " sh -c 'cd data; gcc -o algotest algotest.c'");
               algo_c.close();
               shellCmd("sh /usr/local/apache/share/algo_c.sh"); // shellcmd로 바로 실행 불가능 그래서
               // sh파일 먼저 돌림
               timeCheck("./algotest");
               FileWriter algoMid_c = new FileWriter("/usr/local/apache/share/algoMid_c.sh");
               algoMid_c.write("docker restart " + playDocker + "\n");
               algoMid_c.write("docker exec " + playDocker + " sh -c 'cd data; sh ./timeCheck.sh'" + "\n");
               algoMid_c.write("docker stop " + playDocker + "\n");
               algoMid_c.close();
               for (int i = 0; i < output.length; i++) {
                  FileWriter testinput = new FileWriter("/usr/local/apache/share/testinput");
                  testinput.write(algoInput[i]);
                  testinput.close(); // 하나씩 txt파일에 둠

                  try {
                     String playResult = shellCmd("sh ./share/algoMid_c.sh"); // shellcmd로 바로 실행 불가능 그래서 sh파일 먼저
                     // 돌림
                     playResult = playResult.replace("<br>", ""); // 알고리즘 코드 실행시 추가적으로 출력되는 문자열
                     playResult = playResult.replace(playDocker, ""); // 알고리즘 코드 실행시 추가적으로 출력되는 문자열

                     if (playResult.equals(output[i])) {
                        problemCheck++;
                        out.print("<h5 style=\"color:green\">[" + (i + 1) + "번] [실행시간]"
                              + Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "")
                              + "    [실행 O]");
                        out.print("    [성공]<h5>");
                        result = 1;
                     } else {
                        result = -1;
                        out.print("<h5 style=\"color:red\">[" + (i + 1) + "번] [실행시간]"
                              + Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "")
                              + "    [실행 O]");
                        out.print("    [실패]</h5>");
                     }

                     String timeData = Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "");
                     if (timeData.length() > 2) {
                        timeAvg = timeAvg + Double.parseDouble(timeData);
                     }

                  } catch (Exception e) {
                     out.print("<h5 style=\"color:red\">[" + (i + 1) + "번] [실행시간]"
                           + Reader("/usr/local/apache/share/timeCheck.txt") + "    [실행 X]</h5>");
                  }
               }

            } else if (codeType.equals("java")) {
               algoTestOld = new File("/usr/local/apache/share/algotest.class");
               if (algoTestOld.exists()) {
                  shellCmd("rm -r /usr/local/apache/share/algotest.class");
               }
               FileWriter codew = new FileWriter("/usr/local/apache/share/algotest.java");
               codew.write(code);
               codew.close();

               FileWriter algo_java = new FileWriter("/usr/local/apache/share/algo_java.sh");
               algo_java.write("docker restart " + playDocker + "\n");
               algo_java.write("docker exec " + playDocker
                     + " sh -c 'export LC_ALL=C.UTF-8; cd data; javac -encoding utf-8 algotest.java'");
               algo_java.close();
               shellCmd("sh /usr/local/apache/share/algo_java.sh"); // shellcmd로 바로 실행 불가능 그래서 sh파일 먼저 돌림 --> 내부 코드

               timeCheck("java -Dfile.encoding=utf-8 algotest");
               FileWriter algoMid_java = new FileWriter("/usr/local/apache/share/algoMid_java.sh");
               algoMid_java.write("docker restart " + playDocker + "\n");
               algoMid_java.write("docker exec " + playDocker + " sh -c 'cd data; sh ./timeCheck.sh'" + "\n");
               algoMid_java.write("docker stop " + playDocker + "\n");
               algoMid_java.close();

               for (int i = 0; i < output.length; i++) {
                  FileWriter testinput = new FileWriter("/usr/local/apache/share/testinput");
                  testinput.write(algoInput[i]);
                  testinput.close(); // 하나씩 txt파일에 둠

                  try {
                     String playResult = shellCmd("sh ./share/algoMid_java.sh"); // shellcmd로 바로 실행 불가능 그래서 sh파일
                     // 먼저 돌림
                     playResult = playResult.replace("<br>", ""); // 알고리즘 코드 실행시 추가적으로 출력되는 문자열
                     playResult = playResult.replace(playDocker, ""); // 알고리즘 코드 실행시 추가적으로 출력되는 문자열

                     if (playResult.equals(output[i])) {
                        problemCheck++;
                        out.print("<h5 style=\"color:green\">[" + (i + 1) + "번] [실행시간]"
                              + Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "")
                              + "    [실행 O]");
                        out.print("    [성공]<h5>");
                        result = 1;
                     } else {
                        result = -1;
                        out.print("<h5 style=\"color:red\">[" + (i + 1) + "번] [실행시간]"
                              + Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "")
                              + "    [실행 O]");
                        out.print("    [실패]</h5>");
                     }

                     String timeData = Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "");
                     if (timeData.length() > 2) {
                        timeAvg = timeAvg + Double.parseDouble(timeData);
                     }

                  } catch (Exception e) {
                     out.print("<h5 style=\"color:red\">[" + (i + 1) + "번] [실행시간]"
                           + Reader("/usr/local/apache/share/timeCheck.txt") + "    [실행 X]</h5>");
                  }
               }

            } else if (codeType.equals("python")) {
               algoTestOld = new File("/usr/local/apache/share/algotest.py");
               if (algoTestOld.exists()) {
                  shellCmd("rm -r /usr/local/apache/share/algotest.py");
               }
               FileWriter codew = new FileWriter("/usr/local/apache/share/algotest.py");
               codew.write("# -*- coding: utf-8 -*-\n" + code);
               codew.close();

               timeCheck("python algotest.py");
               FileWriter algoMid_python = new FileWriter("/usr/local/apache/share/algoMid_python.sh");
               algoMid_python.write("docker restart " + playDocker + "\n");
               algoMid_python.write("docker exec " + playDocker + " sh -c 'cd data; sh ./timeCheck.sh'" + "\n");
               algoMid_python.write("docker stop " + playDocker + "\n");
               algoMid_python.close();

               for (int i = 0; i < output.length; i++) {
                  FileWriter testinput = new FileWriter("/usr/local/apache/share/testinput");
                  testinput.write(algoInput[i]);
                  testinput.close(); // 하나씩 txt파일에 둠

                  try {
                     String playResult = shellCmd("sh ./share/algoMid_python.sh"); // shellcmd로 바로 실행 불가능 그래서
                     // sh파일 먼저 돌림
                     playResult = playResult.replace("<br>", ""); // 알고리즘 코드 실행시 추가적으로 출력되는 문자열
                     playResult = playResult.replace(playDocker, ""); // 알고리즘 코드 실행시 추가적으로 출력되는 문자열

                     if (playResult.equals(output[i])) {
                        problemCheck++;
                        out.print("<h5 style=\"color:green\">[" + (i + 1) + "번] [실행시간]"
                              + Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "")
                              + "    [실행 O]");
                        out.print("    [성공]<h5>");
                        result = 1;
                     } else {
                        result = -1;
                        out.print("<h5 style=\"color:red\">[" + (i + 1) + "번] [실행시간]"
                              + Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "")
                              + "    [실행 O]");
                        out.print("    [실패]</h5>");
                     }

                     String timeData = Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "");
                     if (timeData.length() > 2) {
                        timeAvg = timeAvg + Double.parseDouble(timeData);
                     }

                  } catch (Exception e) {
                     out.print("<h5 style=\"color:red\">[" + (i + 1) + "번] [실행시간]"
                           + Reader("/usr/local/apache/share/timeCheck.txt") + "    [실행 X]</h5>");
                  }
               }

            } else if (codeType.equals("javascript")) {
               algoTestOld = new File("/usr/local/apache/share/algotest.js");
               if (algoTestOld.exists()) {
                  shellCmd("rm -r /usr/local/apache/share/algotest.js");
               }
               FileWriter codew = new FileWriter("/usr/local/apache/share/algotest.js");
               codew.write(code);
               codew.close();

               timeCheck("js algotest.js");
               FileWriter algoMid_js = new FileWriter("/usr/local/apache/share/algoMid_js.sh");
               algoMid_js.write("docker restart " + playDocker + "\n");
               algoMid_js.write("docker exec " + playDocker + " sh -c 'cd data; sh ./timeCheck.sh'" + "\n");
               algoMid_js.write("docker stop " + playDocker + "\n");
               algoMid_js.close();

               for (int i = 0; i < output.length; i++) {
                  FileWriter testinput = new FileWriter("/usr/local/apache/share/testinput");
                  testinput.write(algoInput[i]);
                  testinput.close(); // 하나씩 txt파일에 둠

                  try {
                     String playResult = shellCmd("sh ./share/algoMid_js.sh"); // shellcmd로 바로 실행 불가능 그래서 sh파일 먼저
                     // 돌림
                     playResult = playResult.replace("<br>", ""); // 알고리즘 코드 실행시 추가적으로 출력되는 문자열
                     playResult = playResult.replace(playDocker, ""); // 알고리즘 코드 실행시 추가적으로 출력되는 문자열

                     if (playResult.equals(output[i])) {
                        problemCheck++;
                        out.print("<h5 style=\"color:green\">[" + (i + 1) + "번] [실행시간]"
                              + Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "")
                              + "    [실행 O]");
                        out.print("    [성공]<h5>");
                        result = 1;
                     } else {
                        result = -1;
                        out.print("<h5 style=\"color:red\">[" + (i + 1) + "번] [실행시간]"
                              + Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "")
                              + "    [실행 O]");
                        out.print("    [실패]</h5>");
                     }

                     String timeData = Reader("/usr/local/apache/share/timeCheck.txt").replace("<br>", "");
                     if (timeData.length() > 2) {
                        timeAvg = timeAvg + Double.parseDouble(timeData);
                     }

                  } catch (Exception e) {
                     out.print("<h5 style=\"color:red\">[" + (i + 1) + "번] [실행시간]"
                           + Reader("/usr/local/apache/share/timeCheck.txt") + "    [실행 X]</h5>");
                  }
               }

            }

            /// if -else문은 코드보기를 에디터에 삽입 && 소스성공여부 프린트함
            if (problemCheck == 1) {
               bf.write("성공 문 실행\n");
               bf.flush();
               //result = 1;
               //////
               timeAvg = timeAvg / output.length;
               ////
               bf.write("로그 1번\n");
               bf.flush();

               //req.setAttribute("num", algorithmNum);
               bf.write("1) 알고리즘 번호\n");
               bf.flush();
               //req.setAttribute("result", result);
               bf.write("2) 실행 결과\n");
               bf.flush();
               ServletContext context = this.getServletContext();
               req.setAttribute("num", algorithmNum);
               req.setAttribute("algoResult", result);
               req.setAttribute("retry", 1);
               //req.setAttribute("algoTime", );
               RequestDispatcher dispatcher = context.getRequestDispatcher("/algorithm/algorithmList.jsp");

               bf.write("3) 디스패처 처리\n");
               bf.flush();

               String result_in = "update user_algorithm_code set time=?, result=1 where codeNum=?";
               pstmt = conn.prepareStatement(result_in);
               pstmt.setString(1, Double.toString(timeAvg));
               //pstmt.setInt(2, result);
               //pstmt.setString(2, userId);
               //pstmt.setInt(3, algorithmNum);
               pstmt.setInt(2, codeNum);
               pstmt.executeUpdate();
               
               bf.write("   fuck    "+codeNum+" fuck  " + problemCheck);
               bf.write("로그 3번\n");
               bf.flush();
               String result_update = "update user_algorithm_data set result=1 where id=? and algorithmNum=?";
               pstmt = conn.prepareStatement(result_update);
               
               pstmt.setString(1, userId);
               pstmt.setInt(2, algorithmNum);
               pstmt.executeUpdate();
               pstmt.close();

               bf.write("로그 4번\n");
               bf.flush();

               Date initTime = new Date(session.getCreationTime()); // 최초 세션 생성 시각
               Date recentTime = new Date(session.getLastAccessedTime()); // 최근 세션 접근 시각

               bf.write(userId + " / " + getClientIp(req) + " / " + initTime + " / " + recentTime + " / "
                     + playDocker + " / " + codeType + " 문제 해결  \n");
               bf.flush();
               bf.close();
               conn.close();
               pstmt.close();
               rs.close();

               out.print("<h5 style=\"color:green\">문제풀이 성공</h5>");
               dispatcher.forward(req, resp);
            } else {
               bf.write("실패 문 실행\n");
                bf.flush();
                //result = -1;
                //////
                timeAvg = timeAvg / output.length;
                ////
                bf.write("로그 1번\n");
                bf.flush();

                //req.setAttribute("num", algorithmNum);
                bf.write("1) 알고리즘 번호\n");
                bf.flush();
                //req.setAttribute("result", result);
                bf.write("2) 실행 결과\n");
                bf.flush();
                ServletContext context = this.getServletContext();
                req.setAttribute("num", algorithmNum);
                req.setAttribute("algoResult", result);
                req.setAttribute("retry", 1);
                //req.setAttribute("algoTime", );
                RequestDispatcher dispatcher = context.getRequestDispatcher("/algorithm/algorithmList.jsp");

                bf.write("3) 디스패처 처리\n");
                bf.flush();

                String result_in = "update user_algorithm_code set time=?, result=-1 where codeNum=?";
                pstmt = conn.prepareStatement(result_in);
                pstmt.setString(1, Double.toString(timeAvg));
                //pstmt.setInt(2, result);
                pstmt.setInt(2, codeNum);
                pstmt.executeUpdate();
                conn.close();
                pstmt.close();
             
                bf.write("   fuck    "+codeNum+" fuck  " + problemCheck);
                bf.write("로그 4번\n");
                bf.flush();

                Date initTime = new Date(session.getCreationTime()); // 최초 세션 생성 시각
                Date recentTime = new Date(session.getLastAccessedTime()); // 최근 세션 접근 시각

                bf.write(userId + " / " + getClientIp(req) + " / " + initTime + " / " + recentTime + " / "
                      + playDocker + " / " + codeType + " 문제 해결  \n");
                bf.flush();
                bf.close();
                //conn.close();
                //pstmt.close();
                rs.close();

                out.print("<h5 style=\"color:green\">문제풀이 성공</h5>");
                dispatcher.forward(req, resp);
            }
         }
      } catch (Exception e) {
         log = new File("/usr/local/apache/log/algoLog.txt");
         bf = new BufferedWriter(new FileWriter(log, true));
         bf.write("오류 문 실행");
         bf.flush();
         out.print("<h5 style=\"color:red\">문제풀이 오류</h5>");
         result = -1;

         String userId = (String) session.getAttribute("id");
         int algorithmNum = Integer.parseInt(req.getParameter("algorithmNum"));

         try {
            conn = DBconnection.getConnection();

            bf.write("로그 1번");
            bf.flush();
            String result_update1 = "update user_algorithm_code set result=? where id=? and algorithmNum=?";
            pstmt = conn.prepareStatement(result_update1);
            pstmt.setInt(1, result);
            pstmt.setString(2, userId);
            pstmt.setInt(3, algorithmNum);
            pstmt.executeUpdate();

            bf.write("로그 2번");
            bf.flush();
            String result_update2 = "update user_algorithm_data set result=? where id=? and algorithmNum=?";
            pstmt = conn.prepareStatement(result_update2);
            pstmt.setInt(1, result);
            pstmt.setString(2, userId);
            pstmt.setInt(3, algorithmNum);
            pstmt.executeUpdate();
            pstmt.close();

            bf.write("로그 3번");
            bf.flush();
         } catch (Exception ex) {

         }

      }
   }

   @Override
   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      // TODO Auto-generated method stub
      doHandle(req, resp);
   }

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      // TODO Auto-generated method stub
      doHandle(req, resp);
   }

   public static String shellCmd(String command) throws Exception {
      Runtime runTime = Runtime.getRuntime();
      Process process = runTime.exec(command);
      InputStream inputStream = process.getInputStream();

      InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
      BufferedReader bufferReader = new BufferedReader(inputStreamReader);

      String line;
      String result = "";
      if (bufferReader.readLine() != null) {
         while ((line = bufferReader.readLine()) != null) {
            result = result + "<br>" + line;
         }
      } else
         result = "컴파일 오류";

      return result;
   }

   public static String Reader(String fileName) throws IOException {
      BufferedReader bufferReader = new BufferedReader(new FileReader(fileName));
      String result = "";
      while (true) {
         String line = bufferReader.readLine();
         if (line == null)
            break;
         result = result + "<br>" + line;
      }
      bufferReader.close();
      return result;
   }

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

   public static void timeCheck(String excuteSentence) {

      try {
         FileWriter timeCheck = new FileWriter("/usr/local/apache/share/timeCheck.sh");
         timeCheck.write("beginTime=$(date +%s%N)" + "\n");
         timeCheck.write(excuteSentence + " < testinput" + "\n");
         timeCheck.write("endTime=$(date +%s%N)" + "\n");
         timeCheck.write("elapsed=`echo \"($endTime - $beginTime) / 1000000\" | bc`" + "\n");
         timeCheck.write("elapsedSec=`echo \"scale=6;$elapsed / 1000\" | bc | awk '{printf \"%.6f\", $1}'`" + "\n");
         timeCheck.write("echo $elapsedSec > timeCheck.txt");
         timeCheck.close();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

}