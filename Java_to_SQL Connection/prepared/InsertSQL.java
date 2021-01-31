package jdbc25.prepared;

import java.sql.Date;

import jdbc25.service.IConnectImpl;

public class InsertSQL extends IConnectImpl {

	public InsertSQL(String url,String user, String password) {
		super(url,user,password);
	}
	
	@Override
	public void execute() throws Exception {
		String sql = "INSERT INTO member VALUES(?,?,?,?)";
		
		psmt = conn.prepareStatement(sql);
		
		psmt.setString(1,getValue("아이디"));
		psmt.setString(2,getValue("비밀번호"));
		psmt.setString(3,getValue("이름"));
		
		//psmt.setString(4,getValue("가입일"));
		java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
		
		psmt.setDate(4, date);
		
		
		System.out.println(psmt.executeUpdate()+"행이 입력되었어요.");
		
		close();
	}
	
	public static void main(String[] args) {
		try {
			new InsertSQL(ORACLE_URL,"JDBC","JDBC").execute();
		}
		catch(Exception e) {
			System.out.println("오류:"+e.getMessage());
		}
	}

}
