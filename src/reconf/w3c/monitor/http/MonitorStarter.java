package reconf.w3c.monitor.http;

import java.sql.SQLException;

public class MonitorStarter {

	public static void main(String[] args) throws SQLException {
		
		System.out.println( "<td>" +(false?"Shutting Down,Stopped":" ACTIVE ")+"</td>");
	}
	
}
