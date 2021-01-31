package jdbc25.statement;

import java.sql.Date;
import java.sql.SQLException;

import jdbc25.service.IConnectImpl;

public class SelectSQL extends IConnectImpl {
	
	@Override
	public void execute() throws Exception {
		connect(ORACLE_URL, "scott", "scott");
		try {
			stmt = conn.createStatement();
			
			//String sql = "SELECT * FROM emp ORDER BY hiredate DESC";
			
			//String sql = "SELECT * FROM emp WHERE empno="+getValue("사원번호")+"";
			
			//String sql = "SELECT AVG(sal) FROM emp";
			
			String sql = "SELECT * FROM emp WHERE ename LIKE '%"+getValue("찾는 문자열")+"%'";
			
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				int empno = rs.getInt(1);
				String ename = rs.getString("ename");
				String job = rs.getString("job");
				String comm = rs.getString("comm")==null?"": rs.getString("comm");
				String hiredate = rs.getString(5).substring(0,10);
				
				System.out.println(String.format("%-5d%-10s%-10s%-5s%s", empno,ename,job,comm,hiredate));
			}
		/*
			if(rs.next()) {
				System.out.println(String.format("%-5s%-10s%-10s%-5s%s", 
						rs.getString(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(7)==null?"": rs.getString(7),
						rs.getString(5)));
			}*/
			
			//rs.next();
			//System.out.println("평균 연봉:"+rs.getFloat(1));
			
		}
		catch(Exception e) {
			System.out.println("오류발생: "+e.getMessage());
		}
		finally {
			close();
		}
	}
	
	public static void main(String[] args) throws Exception {
		new SelectSQL().execute();

	}

}
