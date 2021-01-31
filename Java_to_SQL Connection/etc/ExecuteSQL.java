package jdbc25.etc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Vector;

import jdbc25.service.IConnectImpl;

public class ExecuteSQL extends IConnectImpl {

	public ExecuteSQL() {
		super(ORACLE_URL,"JDBC","JDBC");
	}
	@Override
	public void execute() throws Exception {
		while(true) {
			//1]쿼리문 준비
			String query = getQueryString();
			if("EXIT".equalsIgnoreCase(query.trim())) {
				System.out.println("Oracle Database 11g Enterprise Edition Release 11.2.0.1.0 - 64bit Production\r\n" + 
						"With the Partitioning, OLAP, Data Mining and Real Application Testing options에서 분리되었습니다.");
				//자원반납]
				close();
				//프로그램 종료]
				System.exit(0);
			}
			//2]Statement계열 객체 생성-쿼리 실행용
			psmt = conn.prepareStatement(query);
			
			try {
				boolean flag=psmt.execute();
				if(flag) {//쿼리문이 SELECT
					//ResultSet객체 얻기]
					rs=psmt.getResultSet();
					ResultSetMetaData rsmd=rs.getMetaData();
					int columnCount=rsmd.getColumnCount();
					
					List<Integer> dashCount = new Vector<Integer>();
					for(int i=1;i <=columnCount;i++) {					
						int types=rsmd.getColumnType(i);					
						int length= rsmd.getPrecision(i);
						switch(types) {
							case Types.NCHAR:
							case Types.NVARCHAR:
								dashCount.add(length*2);break;
							case Types.TIMESTAMP:
							case Types.NUMERIC:
								dashCount.add(10);break;
							default:dashCount.add(length);
						}////switch					
						String columnName = rsmd.getColumnName(i).length() > dashCount.get(i-1) ?
								            rsmd.getColumnName(i).substring(0, dashCount.get(i-1)):
								            rsmd.getColumnName(i);	
						System.out.print(String.format("%-"+(dashCount.get(i-1)+1)+"s",columnName));
						
					}//////for
					System.out.println();//줄바꿈				
					for(Integer dash:dashCount) {
						for(int i=0;i < dash;i++) System.out.print("-");
						System.out.print(" ");
					}
					System.out.println();//줄바꿈
					
					while(rs.next()) {					
						for(int i=1;i <=columnCount;i++) {
							int type=rsmd.getColumnType(i);
							if(type==Types.TIMESTAMP) {
								System.out.print(String.format("%-11s",rs.getDate(i)));
							}
							else {
								System.out.print(String.format("%-"+(dashCount.get(i-1)+1)+"s",rs.getString(i)));
							}
						}
						System.out.println();//줄바꿈
					}////////while
				}
				else {//기타 쿼리문
					//영향받은 행의 수 얻기]
					int affected = psmt.getUpdateCount();
					if(query.trim().toUpperCase().startsWith("UPDATE")) {
						System.out.println(affected+"행이 수정 되었어요");
					}
					else if(query.trim().toUpperCase().startsWith("DELETE")) {
						System.out.println(affected+"행이 삭제 되었어요");
					}
					else if(query.trim().toUpperCase().startsWith("INSERT")) {
						System.out.println(affected+"행이 입력 되었어요");
					}
					
				}
			}
			catch(SQLException e) {
				System.out.println(e.getMessage());
			}
		}//while
	}
	public static void main(String[] args) throws Exception {
		new ExecuteSQL().execute();
	}//////////main
}/////////////class
