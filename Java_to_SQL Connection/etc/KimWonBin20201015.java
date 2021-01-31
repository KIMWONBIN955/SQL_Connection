package jdbc25.etc;

import java.sql.DriverManager;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import jdbc25.service.IConnectImpl;


public class KimWonBin20201015 extends IConnectImpl {
	String querys="";
	String query="";
	StringBuffer sb=new StringBuffer();
	String id, pwd;
	int titleNum;
	Scanner sc = new Scanner(System.in);



	String getValue() {

		while(true) {
			sb.delete(0, sb.length());
			querys=getQueryString();
			exit(querys);
			if((querys.length()==0)) {}
			else if(conn(querys)) {
				continue;
			}
			else {
				if(querys.contains(";")) {
					querys=querys.substring(0, querys.length()-1);
					sb.append(querys);
					return sb.toString().trim();
				}
				sb.append(" ").append(querys);
				break;
			}///else
		}///while

		titleNum=1;
		while(true){
			titleNum++;	
			System.out.print("  "+titleNum+" ");
			querys=sc.nextLine();
			if(querys.contains(";")) {
				querys=querys.substring(0, querys.length()-1);
				sb.append(" ").append(querys);
				return sb.toString().trim();
			}///if
			sb.append(" ").append(querys);
		}////while
	}///getValue

	void exit(String query) {
		if("EXIT".equalsIgnoreCase(query.trim())) {
			System.out.println("Oracle Database 11g Enterprise Edition Release 11.2.0.1.0 - 64bit Production\r\n" + 
					"With the Partitioning, OLAP, Data Mining and Real Application Testing options에서 분리되었습니다.");
			//자원반납]
			close();
			//프로그램 종료]
			System.exit(0);
		}
	}///exit

	boolean conn(String query) {
		this.query=query;
		if(query.toLowerCase().contains("conn")){
			try {

				if(query.equalsIgnoreCase("conn")){
					try {
				         System.out.print("사용자명:");
				         id = sc.nextLine();
				         userId = id;
				         System.out.print("비밀번호:");
				         pwd = sc.nextLine();
				         conn = DriverManager.getConnection(ORACLE_URL, id, pwd);
				         System.out.println("연결되었습니다.");
				         return true;
				      }
				      catch (SQLException e) {
				         System.out.println(e.getMessage());
				         return false;
				      }

				}///if
				else if(query.trim().startsWith("conn")) {
		               if(query.trim().contains("/")) {
		                  conn = DriverManager.getConnection(ORACLE_URL, 
		                        query.substring(query.lastIndexOf(" ")+1,query.lastIndexOf("/")).trim(), query.substring(query.lastIndexOf("/")+1).trim());
		                  userId = query.substring(query.lastIndexOf(" ")+1,query.lastIndexOf("/")).trim();
		                  System.out.println("연결되었습니다.");
		                  return true;
		               }else if(query.trim().contains(" ")) {
		                  System.out.print("비밀번호:");
		                  String pwd = sc.nextLine();
		                  conn = DriverManager.getConnection(ORACLE_URL,query.substring(query.lastIndexOf(" ")+1).trim(), pwd);
		                  userId = query.substring(query.lastIndexOf(" ")+1).trim();
		                  System.out.println("연결되었습니다.");
		                  return true;
		               }
		            }

			}///try
			catch(Exception e) {
				System.out.println("ORA-01017: invalid username/password; logon denied\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"경고: 이제는 ORACLE에 연결되어 있지 않습니다.");
				return false;
			}///catch
		}///else if
		return false;
	}///conn

	@Override
	public void execute() throws Exception {
		connect(ORACLE_URL, "JDBC", "JDBC");
		while(true) {
			String query = getValue();

			try {
	            
	            psmt = conn.prepareStatement(query);
	            
	            if("SHOW USER".equalsIgnoreCase(query)) {
	               System.out.println(userId);
	               continue;
	            }
	            
	            if(query.trim().toUpperCase().startsWith("DESC") && query.trim().length() != 4 && query.trim().charAt(4)==' '){
	        		
					String descTable = query.trim().substring(4, query.trim().length()).trim();
					String descQuery = String.format("select * from %s",descTable);
					psmt = conn.prepareStatement(descQuery);
					rs=psmt.getResultSet();
					try {
						psmt.execute();
					}catch(SQLException e){
						System.out.println(e.getMessage());
						continue;
					}
					ResultSetMetaData rsmd=rs.getMetaData();
					
					
					System.out.println(String.format("%-15s%-10s%-15s","NAME","NULL","TYPE"));
					System.out.println("-------------- --------- ---------------");
					for(int i = 1 ; i <= rsmd.getColumnCount(); i++) {
						
					String type = rsmd.getScale(i) != 0 ? String.format("%s(%s,%s)",rsmd.getColumnTypeName(i),rsmd.getPrecision(i),rsmd.getScale(i))
						: rsmd.getPrecision(i) == 0 ? rsmd.getColumnTypeName(i)
							: String.format("%s(%s)",rsmd.getColumnTypeName(i),rsmd.getPrecision(i));	
					
					
						
					System.out.println(String.format("%-15s%-10s%-15s",
							rsmd.getColumnName(i),rsmd.isNullable(i) == 1 ? "":"NOT NULL" ,type));
					}
					System.out.println();
					
					continue;		
				}
	          
	            boolean Flag=psmt.execute();
	            if(Flag) {//쿼리문이 SELECT
	               //ResultSet객체 얻기]
	               rs=psmt.getResultSet();
	               //가]ResultSet객체의 getMetaData()로 ResultSetMetaData얻기
	               ResultSetMetaData rsmd= rs.getMetaData();
	               //나]총 컬럼수 얻기-ResultSetMetaData의 int getColumnCount()
	               int columnCount = rsmd.getColumnCount();
	               List<Integer> dashCount = new Vector<Integer>();
	               for(int i=1; i <=columnCount ;i++) {
	                  //컬럼타입]
	                  int types = rsmd.getColumnType(i);
	                  //컬럼의 자리수]
	                  int length = rsmd.getPrecision(i);
	                  switch(types) {
	                  case Types.NCHAR:
	                  case Types.NVARCHAR:
	                     dashCount.add(length*2);break;
	                  case Types.TIMESTAMP:
	                  case Types.NUMERIC:
	                     dashCount.add(10);break;
	                  default:dashCount.add(length);
	                  }
	                  //컬럼명 출력]            
	                  //컬럼명이 대쉬의 숫자보다 크다면
	                  //예]GENDER CHAR(1) 
	                  /*
	                   * GENDER
	                   * -
	                   * 
	                   */
	                  String columnName = rsmd.getColumnName(i).length() > dashCount.get(i-1) ?
	                        rsmd.getColumnName(i).substring(0,dashCount.get(i-1)) :
	                           rsmd.getColumnName(i);
	                        System.out.print(String.format("%-"+(dashCount.get(i-1)+1)+"s", columnName));

	               }////////for
	               System.out.println();//줄바꿈
	               //(-)DASH출력]
	               for(Integer dash:dashCount) {
	                  for(int i=0;i < dash;i++) System.out.print("-");
	                  System.out.print(" ");
	               }
	               System.out.println();//줄바꿈
	               //데이터 출력]
	               while(rs.next()) {
	                  //각 컬럼값 뽑아오기]
	                  for(int i=1;i<=columnCount;i++) {
	                     int type=rsmd.getColumnType(i);
	                     if(type == Types.TIMESTAMP) {
	                        System.out.print(String.format("%-11s",rs.getDate(i)==null ? "" : rs.getDate(i)));
	                     }
	                     else {
	                        System.out.print(String.format("%-"+(dashCount.get(i-1)+1)+"s",rs.getString(i)==null ? "" : rs.getString(i)));
	                     }
	                  }///
	                  System.out.println();//줄바꿈
	               }///////////while
	            }
	            else {//기타 쿼리문
	               //영향받은 행의 수 얻기]
	               int affected=psmt.getUpdateCount();
	               if(query.trim().toUpperCase().startsWith("UPDATE")) {
	                  System.out.println(affected+"행이 수정되었어요");
	               }
	               else if(query.trim().toUpperCase().startsWith("DELETE")) {
	                  System.out.println(affected+"행이 삭제되었어요");
	               }
	               else if(query.trim().toUpperCase().startsWith("INSERT")) {
	                  System.out.println(affected+"행이 입력되었어요");
	               }

	            }
	         }
	         catch(SQLException e) {
	            System.out.println(e.getMessage());
	         }
	         //while(true)시에는 불필요
	         /*
	         finally {
	            close();
	         }*/
	      
	      }/////////while



	}
	public static void main(String[] args) throws Exception {
		new KimWonBin20201015().execute();
	}

}