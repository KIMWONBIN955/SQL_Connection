package jdbc25.prepared;

import java.sql.SQLException;

import jdbc25.service.IConnectImpl;

public class DeleteSQL extends IConnectImpl{
	
	@Override
	public void execute() throws Exception {
		connect(ORACLE_URL, "JDBC", "JDBC");

		psmt = conn.prepareStatement("DELETE FROM member WHERE id=?");
		
		psmt.setString(1, getValue("삭제할 아이디"));
		
		
		try {
			System.out.println(psmt.executeUpdate()+"행이 삭제되었어요");
		}
		catch(SQLException e) {
			System.out.println("수정시 오류:" + e.getMessage());
		}
		finally {
			close();
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		new DeleteSQL().execute();
	}
}
