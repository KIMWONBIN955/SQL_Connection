package jdbc25.prepared;

import java.sql.SQLException;

import jdbc25.service.IConnectImpl;

public class UpdateSQLMore extends IConnectImpl {

	@Override
	public void execute() throws Exception {
		connect(ORACLE_URL, "JDBC", "JDBC");
		
		psmt = conn.prepareStatement("UPDATE member SET pwd=?,name=? WHERE id=?");
		
		while(true) {
			psmt.setString(3, getValue("수정할 아이디"));
			psmt.setString(1, getValue("비밀번호"));
			psmt.setString(2, getValue("이름"));
			
			try {
				System.out.println(psmt.executeUpdate()+"행이 수정되었어요");
			}
			catch(SQLException e) {
				System.out.println("수정시 오류:" + e.getMessage());
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		new UpdateSQLMore().execute();
	}

}
