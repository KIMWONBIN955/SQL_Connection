package jdbc25.statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteSQLMore {
	private Connection conn;
	private Statement stmt;
	
	public DeleteSQLMore() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orcl","JDBC","JDBC");
		}
		catch(ClassNotFoundException e) {
			System.out.println("드라이버 클래스가 없어요. 로딩 실패!");
		}
		catch (SQLException e) {
			System.out.println("데이타베이스 연결 실패!");
		}
	}
	
	private void execute() {
		try {
			stmt=conn.createStatement();
			while(true) {
				try {
					int affected = stmt.executeUpdate("DELETE FROM member WHERE id='"+InsertSQLMore.getValue("삭제할 아이디").toString()+"'");
					System.out.println(affected+"행이 삭제되었습니다.");
				}
				catch(SQLException e) {
					System.out.println("DELETE쿼리 문 실패!"+e.getMessage());
				}
				catch(NullPointerException e) {
					System.out.println("수고하셨어요.");
					break;
				}
			}
		}
		catch (SQLException e) {
			System.out.println("Statement객체 생성 실패!"); 
		}
		finally {
			close();
		}
	}	
	private void close() {
		try {
	        if(stmt != null) stmt.close();
	        if(conn != null) conn.close();
	     }
	     catch (SQLException e){}
		}
	  
	public static void main(String[] args) {
		 new DeleteSQLMore().execute();
	}
}
