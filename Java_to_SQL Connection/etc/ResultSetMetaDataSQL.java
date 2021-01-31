package jdbc25.etc;

import java.sql.ResultSetMetaData;

import jdbc25.service.IConnectImpl;

public class ResultSetMetaDataSQL extends IConnectImpl {

	public ResultSetMetaDataSQL() {
		connect(ORACLE_URL, "JDBC", "JDBC");
	}
	
	@Override
	public void execute() throws Exception {
		try {
			String sql = getQueryString();
			
			psmt = conn.prepareStatement(sql);
			
			rs = psmt.executeQuery();
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			System.out.println("총 컬럼수:" + columnCount);
		}
		finally {
			close();
		}
	}
	
	public static void main(String[] args) throws Exception {
		new ResultSetMetaDataSQL().execute();

	}

}
