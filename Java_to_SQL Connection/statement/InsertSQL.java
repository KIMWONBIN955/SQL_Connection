package jdbc25.statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertSQL {

	private Connection conn;
	private Statement stmt;
	
	public InsertSQL() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orcl","JDBC","JDBC");
		}
		catch(ClassNotFoundException e) {
			System.out.println("드라이버 클래스가 없어요:"+e.getMessage());
		}
		catch(SQLException e) {
			System.out.println("데이터베이스 연결 실패:"+e.getMessage());
		}
	}
	
	private void execute() {
		try {
			stmt = conn.createStatement();
			String sql = "INSERT INTO MEMBER(ID,PWD,NAME) VALUES('PARK','1234','박길동')";
			
			try {
				int affected=stmt.executeUpdate(sql);
				System.out.println(affected+"행이 입력되었어요.");
			}
			catch(SQLException e){
				System.out.println("INSERT쿼리문 실행 오류:" + e.getMessage());
			}
		}
		catch(SQLException e) {
			System.out.println("Statement 객체 생성 실패:" +e.getMessage());
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
		catch(SQLException e) {}
	}
	
	public static void main(String[] args) {
		
		new InsertSQL().execute();

	}


}
