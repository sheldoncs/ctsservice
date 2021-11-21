package cts.service.db;

import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import cts.service.remote.CTSRemoteService;
import cts.service.util.CourseProperties;
import cts.service.util.LecturerProperties;
import cts.service.util.MessageLogger;

public class CTSDb extends OracleDBConnection {

	String sqlstmt;
	private HashMap dayMap = new HashMap();

	public CTSDb(int v) {
		super(v);
		// TODO Auto-generated constructor stub
	}

	private String getLecturersSQL() {

		sqlstmt = "select distinct SPRIDEN.SPRIDEN_PIDM, "
				+ "SPRIDEN.SPRIDEN_ID, SPRIDEN.SPRIDEN_LAST_NAME, "
				+ "SPRIDEN.SPRIDEN_FIRST_NAME, "
				+ "GOREMAL.GOREMAL_EMAIL_ADDRESS "
				+ "from SATURN.SIRASGN SIRASGN, "
				+ "SATURN.SPRIDEN SPRIDEN, "
				+ "SATURN.SSBSECT SSBSECT, "
				+ "SATURN.SCBCRSE SCBCRSE, "
				+ "SATURN.STVDEPT STVDEPT, "
				+ "GENERAL.GOREMAL GOREMAL "
				+ "where ( SIRASGN.SIRASGN_PIDM = SPRIDEN.SPRIDEN_PIDM "
				+ "and SIRASGN.SIRASGN_CRN = SSBSECT.SSBSECT_CRN "
				+ "and SCBCRSE.SCBCRSE_SUBJ_CODE = SSBSECT.SSBSECT_SUBJ_CODE "
				+ "and SCBCRSE.SCBCRSE_CRSE_NUMB = SSBSECT.SSBSECT_CRSE_NUMB "
				+ "and STVDEPT.STVDEPT_CODE = SCBCRSE.SCBCRSE_DEPT_CODE "
				+ "and GOREMAL.GOREMAL_PIDM = SIRASGN.SIRASGN_PIDM ) "
				+ "and ( SPRIDEN.SPRIDEN_CHANGE_IND is null "
				+ "and SIRASGN.SIRASGN_TERM_CODE = ? "
				+ "and GOREMAL.GOREMAL_EMAL_CODE = ? ) ORDER BY SPRIDEN_LAST_NAME  ";

		return sqlstmt;

	}

	private String getLecturerDetailSQL() {

		sqlstmt = "select distinct SPRIDEN.SPRIDEN_PIDM, "
				+ "SPRIDEN.SPRIDEN_ID, SPRIDEN.SPRIDEN_LAST_NAME, "
				+ "SPRIDEN.SPRIDEN_FIRST_NAME, STVDEPT.STVDEPT_DESC, "
				+ "GOREMAL.GOREMAL_EMAIL_ADDRESS "
				+ "from SATURN.SIRASGN SIRASGN, " + "SATURN.SPRIDEN SPRIDEN, "
				+ "SATURN.SSBSECT SSBSECT, " + "SATURN.SCBCRSE SCBCRSE, "
				+ "SATURN.STVDEPT STVDEPT, " + "GENERAL.GOREMAL GOREMAL "
				+ "where ( SIRASGN.SIRASGN_PIDM = SPRIDEN.SPRIDEN_PIDM "
				+ "and SIRASGN.SIRASGN_CRN = SSBSECT.SSBSECT_CRN "
				+ "and SCBCRSE.SCBCRSE_SUBJ_CODE = SSBSECT.SSBSECT_SUBJ_CODE "
				+ "and SCBCRSE.SCBCRSE_CRSE_NUMB = SSBSECT.SSBSECT_CRSE_NUMB "
				+ "and STVDEPT.STVDEPT_CODE = SCBCRSE.SCBCRSE_DEPT_CODE "
				+ "and GOREMAL.GOREMAL_PIDM = SIRASGN.SIRASGN_PIDM ) "
				+ "and ( SPRIDEN.SPRIDEN_CHANGE_IND is null "
				+ "and SIRASGN.SIRASGN_TERM_CODE = ? "
				+ "and SPRIDEN.SPRIDEN_PIDM = ? "
				+ "and GOREMAL.GOREMAL_EMAL_CODE = ? ) ";

		return sqlstmt;

	}

	private String getLecturerCoursesSQL() {



		sqlstmt = "select SPRIDEN.SPRIDEN_PIDM,SPRIDEN.SPRIDEN_ID,STVDEPT.STVDEPT_DESC,GOREMAL.GOREMAL_EMAL_CODE,SIRASGN.SIRASGN_CRN, "
				+ "SCBCRSE.SCBCRSE_TITLE,SSRMEET.SSRMEET_BEGIN_TIME,SSRMEET.SSRMEET_END_TIME,SPRIDEN.SPRIDEN_LAST_NAME, "
				+ "SPRIDEN.SPRIDEN_FIRST_NAME,SSRMEET.SSRMEET_START_DATE,SSRMEET.SSRMEET_END_DATE,SSRMEET.SSRMEET_SUN_DAY, "
				+ "SSRMEET.SSRMEET_MON_DAY,SSRMEET.SSRMEET_TUE_DAY,SSRMEET.SSRMEET_WED_DAY,SSRMEET.SSRMEET_THU_DAY, "
				+ "SSRMEET.SSRMEET_FRI_DAY,SSRMEET.SSRMEET_SAT_DAY,STVSCHD.STVSCHD_DESC,SSRMEET.SSRMEET_ROOM_CODE, STVBLDG.STVBLDG_DESC, "
				+ "SCBCRSE.SCBCRSE_SUBJ_CODE, SCBCRSE.SCBCRSE_CRSE_NUMB "
				+ "from SATURN.SIRASGN SIRASGN,SATURN.SPRIDEN SPRIDEN,SATURN.SSBSECT SSBSECT, "
				+ "SATURN.SCBCRSE SCBCRSE,SATURN.STVDEPT STVDEPT,GENERAL.GOREMAL GOREMAL,SATURN.SSRMEET SSRMEET,SATURN.STVBLDG STVBLDG, "
				+ "SATURN.STVSCHD STVSCHD "
				+ "where ( SSBSECT.SSBSECT_CRSE_NUMB = SCBCRSE.SCBCRSE_CRSE_NUMB "
				+ "and SSBSECT.SSBSECT_SUBJ_CODE = SCBCRSE.SCBCRSE_SUBJ_CODE "
				+ "and GOREMAL.GOREMAL_PIDM = SPRIDEN.SPRIDEN_PIDM "
				+ "and SIRASGN.SIRASGN_TERM_CODE = SSBSECT.SSBSECT_TERM_CODE "
				+ "and SIRASGN.SIRASGN_CRN = SSBSECT.SSBSECT_CRN "
				+ "and SPRIDEN.SPRIDEN_PIDM = SIRASGN.SIRASGN_PIDM "
				+ "and SCBCRSE.SCBCRSE_DEPT_CODE = STVDEPT.STVDEPT_CODE "
				+ "and SSRMEET.SSRMEET_TERM_CODE = SIRASGN.SIRASGN_TERM_CODE "
				+ "and SSRMEET.SSRMEET_CRN = SIRASGN.SIRASGN_CRN "
				+ "and SSRMEET.SSRMEET_BLDG_CODE = STVBLDG.STVBLDG_CODE "
				+ "and SSBSECT.SSBSECT_SCHD_CODE = STVSCHD.STVSCHD_CODE ) "
				+ "and ( SIRASGN.SIRASGN_TERM_CODE = ? "
				+ "and SPRIDEN.SPRIDEN_PIDM = ? "
				+ "and GOREMAL.GOREMAL_EMAL_CODE = ? "
				+ "and ( SCBCRSE.SCBCRSE_SUBJ_CODE, SCBCRSE.SCBCRSE_CRSE_NUMB, SCBCRSE.SCBCRSE_EFF_TERM )  = "
				+ "( select SCBCRSE1.SCBCRSE_SUBJ_CODE, "
				+ "SCBCRSE1.SCBCRSE_CRSE_NUMB, "
				+ "Max( SCBCRSE1.SCBCRSE_EFF_TERM ) AS Max_SCBCRSE_EFF_TERM "
				+ "from SATURN.SCBCRSE SCBCRSE1 "
				+ "where SCBCRSE1.SCBCRSE_SUBJ_CODE = SCBCRSE.SCBCRSE_SUBJ_CODE "
				+ "and SCBCRSE1.SCBCRSE_CRSE_NUMB = SCBCRSE.SCBCRSE_CRSE_NUMB "
				+ "group by SCBCRSE1.SCBCRSE_SUBJ_CODE, "
				+ "SCBCRSE1.SCBCRSE_CRSE_NUMB ) )";

		return sqlstmt;
	}

	private String getSingleCourseSQL(String day) {

		HashMap daysMap = addDays();

		System.out.println(daysMap.get(day));

		sqlstmt = "select distinct SPRIDEN.SPRIDEN_PIDM, SPRIDEN.SPRIDEN_ID, STVDEPT.STVDEPT_DESC, GOREMAL.GOREMAL_EMAL_CODE, "
				+ "SIRASGN.SIRASGN_CRN,SCBCRSE.SCBCRSE_TITLE,SSRMEET.SSRMEET_BEGIN_TIME,SSRMEET.SSRMEET_END_TIME, "
				+ "SPRIDEN.SPRIDEN_LAST_NAME,SPRIDEN.SPRIDEN_FIRST_NAME,SSRMEET.SSRMEET_START_DATE,SSRMEET.SSRMEET_END_DATE, "
				+ "SSRMEET.SSRMEET_SUN_DAY,SSRMEET.SSRMEET_MON_DAY,SSRMEET.SSRMEET_TUE_DAY,SSRMEET.SSRMEET_WED_DAY,SSRMEET.SSRMEET_THU_DAY, "
				+ "SSRMEET.SSRMEET_FRI_DAY,SSRMEET.SSRMEET_SAT_DAY,STVSCHD.STVSCHD_DESC,SSRMEET.SSRMEET_ROOM_CODE,STVBLDG.STVBLDG_DESC, "
				+ "SCBCRSE.SCBCRSE_SUBJ_CODE,SCBCRSE.SCBCRSE_CRSE_NUMB "
				+ "from SATURN.SIRASGN SIRASGN, "
				+ "SATURN.SPRIDEN SPRIDEN,SATURN.SSBSECT SSBSECT,SATURN.SCBCRSE SCBCRSE,SATURN.STVDEPT STVDEPT,GENERAL.GOREMAL GOREMAL,SATURN.SSRMEET SSRMEET, "
				+ "SATURN.STVBLDG STVBLDG, SATURN.STVSCHD STVSCHD "
				+ "where ( SSBSECT.SSBSECT_CRSE_NUMB = SCBCRSE.SCBCRSE_CRSE_NUMB "
				+ "and SSBSECT.SSBSECT_SUBJ_CODE = SCBCRSE.SCBCRSE_SUBJ_CODE "
				+ "and GOREMAL.GOREMAL_PIDM = SPRIDEN.SPRIDEN_PIDM "
				+ "and SIRASGN.SIRASGN_TERM_CODE = SSBSECT.SSBSECT_TERM_CODE "
				+ "and SIRASGN.SIRASGN_CRN = SSBSECT.SSBSECT_CRN "
				+ "and SPRIDEN.SPRIDEN_PIDM = SIRASGN.SIRASGN_PIDM "
				+ "and SCBCRSE.SCBCRSE_DEPT_CODE = STVDEPT.STVDEPT_CODE "
				+ "and SSRMEET.SSRMEET_TERM_CODE = SIRASGN.SIRASGN_TERM_CODE "
				+ "and SSRMEET.SSRMEET_CRN = SIRASGN.SIRASGN_CRN "
				+ "and SSRMEET.SSRMEET_BLDG_CODE = STVBLDG.STVBLDG_CODE "
				+ "and SSBSECT.SSBSECT_SCHD_CODE = STVSCHD.STVSCHD_CODE ) "
				+ "and ( SIRASGN.SIRASGN_CRN = ? "
				+ "and SCBCRSE.SCBCRSE_TITLE = ? "
				+ "and "
				+ daysMap.get(day)
				+ " = ? "
				+ "and SSRMEET.SSRMEET_begin_time = ? "
				+ "and ( SCBCRSE.SCBCRSE_SUBJ_CODE, SCBCRSE.SCBCRSE_CRSE_NUMB, SCBCRSE.SCBCRSE_EFF_TERM )  = "
				+ "( select SCBCRSE1.SCBCRSE_SUBJ_CODE, "
				+ "SCBCRSE1.SCBCRSE_CRSE_NUMB, "
				+ "Max( SCBCRSE1.SCBCRSE_EFF_TERM ) as Max_SCBCRSE_EFF_TERM "
				+ "from SATURN.SCBCRSE SCBCRSE1 "
				+ "where SCBCRSE1.SCBCRSE_SUBJ_CODE = SCBCRSE.SCBCRSE_SUBJ_CODE "
				+ "and SCBCRSE1.SCBCRSE_CRSE_NUMB = SCBCRSE.SCBCRSE_CRSE_NUMB "
				+ "group by SCBCRSE1.SCBCRSE_SUBJ_CODE, "
				+ "SCBCRSE1.SCBCRSE_CRSE_NUMB ) )";

		return sqlstmt;

	}

	private String getCourseSQL() {

		sqlstmt = "select distinct "
				+ "SIRASGN.SIRASGN_CRN,SCBCRSE.SCBCRSE_TITLE, "
				+ "SSRMEET.SSRMEET_BEGIN_TIME, SSRMEET.SSRMEET_END_TIME,SSRMEET.SSRMEET_START_DATE,SSRMEET.SSRMEET_END_DATE, "
				+ "SSRMEET.SSRMEET_SUN_DAY, SSRMEET.SSRMEET_MON_DAY, SSRMEET.SSRMEET_TUE_DAY, SSRMEET.SSRMEET_WED_DAY, "
				+ "SSRMEET.SSRMEET_THU_DAY, SSRMEET.SSRMEET_FRI_DAY, SSRMEET.SSRMEET_SAT_DAY, SSRMEET.SSRMEET_ROOM_CODE, "
				+ "STVBLDG.STVBLDG_DESC, STVSCHD.STVSCHD_DESC, SCBCRSE.SCBCRSE_SUBJ_CODE, SCBCRSE.SCBCRSE_CRSE_NUMB "
				+ "from SATURN.STVBLDG STVBLDG, SATURN.SIRASGN SIRASGN, SATURN.SPRIDEN SPRIDEN, SATURN.SSBSECT SSBSECT, SATURN.SCBCRSE SCBCRSE, "
				+ "SATURN.STVDEPT STVDEPT, GENERAL.GOREMAL GOREMAL, SATURN.SSRMEET SSRMEET, SATURN.STVSCHD STVSCHD "
				+ "where ( SIRASGN.SIRASGN_PIDM = SPRIDEN.SPRIDEN_PIDM "
				+ "and SIRASGN.SIRASGN_CRN = SSBSECT.SSBSECT_CRN "
				+ "and SCBCRSE.SCBCRSE_SUBJ_CODE = SSBSECT.SSBSECT_SUBJ_CODE "
				+ "and SCBCRSE.SCBCRSE_CRSE_NUMB = SSBSECT.SSBSECT_CRSE_NUMB "
				+ "and STVDEPT.STVDEPT_CODE = SCBCRSE.SCBCRSE_DEPT_CODE "
				+ "and GOREMAL.GOREMAL_PIDM = SIRASGN.SIRASGN_PIDM "
				+ "and SIRASGN.SIRASGN_TERM_CODE = SSRMEET.SSRMEET_TERM_CODE "
				+ "and SIRASGN.SIRASGN_CRN = SSRMEET.SSRMEET_CRN "
				+ "and SSRMEET.SSRMEET_BLDG_CODE = STVBLDG.STVBLDG_CODE "
				+ "and SSBSECT.SSBSECT_SCHD_CODE = STVSCHD.STVSCHD_CODE ) "
				+ "and ( SPRIDEN.SPRIDEN_CHANGE_IND is null "
				+ "and SIRASGN.SIRASGN_TERM_CODE = ? "
				+ "and SIRASGN.SIRASGN_CRN = ?)";

		return sqlstmt;
	}

	private HashMap addDays() {

		HashMap hmap = new HashMap();

		hmap.put("U", "SSRMEET.SSRMEET_SUN_DAY");
		hmap.put("M", "SSRMEET.SSRMEET_MON_DAY");
		hmap.put("T", "SSRMEET.SSRMEET_TUE_DAY");
		hmap.put("W", "SSRMEET.SSRMEET_WED_DAY");
		hmap.put("R", "SSRMEET.SSRMEET_THU_DAY");
		hmap.put("F", "SSRMEET.SSRMEET_FRI_DAY");
		hmap.put("S", "SSRMEET.SSRMEET_SAT_DAY");

		return hmap;

	}

	public ArrayList collectLecturers() throws SQLException, RemoteException {

		list = new ArrayList();
		// TODO Auto-generated method stub

		String selectStatement = getLecturersSQL();

		try {

			PreparedStatement prepStmt = conn.prepareStatement(selectStatement);

			prepStmt.setString(1, getCurrentTerm());
			prepStmt.setString(2, "CA");

			ResultSet rs = prepStmt.executeQuery();

			MessageLogger.out.println("Query Executed");
			int i = 0;

			while (rs.next()) {

				i++;

				LecturerProperties lecturer = new LecturerProperties();

				lecturer.setPidm(Integer.toString(rs.getInt(1)));
				lecturer.setLastName(rs.getString(3) + ", " + rs.getString(4));
				lecturer.setFirstName(rs.getString(4));
				lecturer.setFaculty("");
				lecturer.setEmail(rs.getString(5));

				list.add(lecturer);

			}

			prepStmt.close();
			rs.close();

			return list;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public LecturerProperties collectLecturerDetail(String pidm)
			throws SQLException, RemoteException {

		LecturerProperties lecturer = new LecturerProperties();

		// TODO Auto-generated method stub

		String selectStatement = getLecturerDetailSQL();

		try {

			PreparedStatement prepStmt = conn.prepareStatement(selectStatement);

			prepStmt.setString(1, getCurrentTerm());
			prepStmt.setInt(2, Integer.parseInt(pidm));
			prepStmt.setString(3, "CA");

			ResultSet rs = prepStmt.executeQuery();

			MessageLogger.out.println("Query Executed");
			int i = 0;

			if (rs.next()) {

				i++;

				lecturer.setPidm(Integer.toString(rs.getInt(1)));
				lecturer.setLastName(rs.getString(3) + ", " + rs.getString(4));
				lecturer.setFirstName(rs.getString(4));
				lecturer.setFaculty(rs.getString(5));
				lecturer.setEmail(rs.getString(6));

			}

			prepStmt.close();
			rs.close();

			return lecturer;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public CourseProperties collectSingleCourse(String crn, String title,
			String day, String startTime) throws SQLException, RemoteException {

		String selectStatement = getSingleCourseSQL(day);

		PreparedStatement prepStmt = conn.prepareStatement(selectStatement);

		prepStmt.setString(1, crn);
		prepStmt.setString(2, title);
		prepStmt.setString(3, day);
		prepStmt.setString(4, startTime);

		ResultSet rs = prepStmt.executeQuery();

		MessageLogger.out.println("Query Executed");
		int i = 0;

		CourseProperties course = new CourseProperties();

		if (rs.next()) {

			course.setCrn(rs.getString("SIRASGN_CRN"));
			course.setCourseName(rs.getString("SCBCRSE_TITLE"));
			course.setStartTime(rs.getString("SSRMEET_START_DATE"));
			course.setEndTime(rs.getString("SSRMEET_END_TIME"));
			course.setStartDate(rs.getDate("SSRMEET_START_DATE").toString());
			course.setEndDate(rs.getDate("SSRMEET_END_DATE").toString());
			course.setDay(nullTest(rs.getString("SSRMEET_SUN_DAY"))
					+ nullTest(rs.getString("SSRMEET_MON_DAY"))
					+ nullTest(rs.getString("SSRMEET_TUE_DAY"))
					+ nullTest(rs.getString("SSRMEET_WED_DAY"))
					+ nullTest(rs.getString("SSRMEET_THU_DAY"))
					+ nullTest(rs.getString("SSRMEET_FRI_DAY"))
					+ nullTest(rs.getString("SSRMEET_SAT_DAY")));

			if (rs.getString("SSRMEET_ROOM_CODE") == null)
				course.setRoom("No Room Available");
			else
				course.setRoom(rs.getString("SSRMEET_ROOM_CODE"));

			if (rs.getString("STVBLDG_DESC") == null)
				course.setBuilding("No Building Set");
			else
				course.setBuilding(rs.getString("STVBLDG_DESC"));

			course.setCourseType(rs.getString("STVSCHD_DESC"));
			course.setSubjCode(rs.getString("SCBCRSE_SUBJ_CODE"));
			course.setCrseNumb(rs.getString("SCBCRSE_CRSE_NUMB"));
			course.setIdNumber(rs.getString("SPRIDEN_ID"));

		}

		prepStmt.close();
		rs.close();

		return course;

	}

	public ArrayList collectLecturerCourses(String pidm) throws SQLException,
			RemoteException {

		list = new ArrayList();
		// TODO Auto-generated method stub

		String selectStatement = getLecturerCoursesSQL();

		try {
			
			

			PreparedStatement prepStmt = conn.prepareStatement(selectStatement);

			prepStmt.setString(1, getCurrentTerm());
			prepStmt.setString(3, "CA");
			prepStmt.setInt(2, Integer.parseInt(pidm));

			ResultSet rs = prepStmt.executeQuery();

			MessageLogger.out.println("Query Executed");
			int i = 0;

			while (rs.next()) {

				i++;

				CourseProperties course = new CourseProperties();
				course.setCrn(rs.getString("SIRASGN_CRN"));
				course.setCourseName(rs.getString("SCBCRSE_TITLE"));
				course.setStartTime(rs.getString("SSRMEET_BEGIN_TIME"));
				course.setEndTime(rs.getString("SSRMEET_END_TIME"));
				course.setStartDate(rs.getDate("SSRMEET_START_DATE").toString());
				course.setEndDate(rs.getDate("SSRMEET_END_DATE").toString());
				course.setDay(nullTest(rs.getString("SSRMEET_SUN_DAY"))
						+ nullTest(rs.getString("SSRMEET_MON_DAY"))
						+ nullTest(rs.getString("SSRMEET_TUE_DAY"))
						+ nullTest(rs.getString("SSRMEET_WED_DAY"))
						+ nullTest(rs.getString("SSRMEET_THU_DAY"))
						+ nullTest(rs.getString("SSRMEET_FRI_DAY"))
						+ nullTest(rs.getString("SSRMEET_SAT_DAY")));

				if (rs.getString("SSRMEET_ROOM_CODE") == null)
					course.setRoom("No Building Available");
				else
					course.setRoom(rs.getString("SSRMEET_ROOM_CODE"));

				if (rs.getString("STVBLDG_DESC") == null)
					course.setBuilding("No Building Set");
				else
					course.setBuilding(rs.getString("STVBLDG_DESC"));

				course.setCourseType(rs.getString("STVSCHD_DESC"));
				course.setSubjCode(rs.getString("SCBCRSE_SUBJ_CODE"));
				course.setCrseNumb(rs.getString("SCBCRSE_CRSE_NUMB"));
				course.setIdNumber(rs.getString("SPRIDEN_ID"));

				list.add(course);

			}

			prepStmt.close();
			rs.close();

			return list;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public CourseProperties collectCourse(String crn) throws SQLException,
			RemoteException {

		// TODO Auto-generated method stub

		String selectStatement = getCourseSQL();

		try {

			PreparedStatement prepStmt = conn.prepareStatement(selectStatement);

			prepStmt.setString(1, getCurrentTerm());
			prepStmt.setString(2, crn);

			ResultSet rs = prepStmt.executeQuery();

			MessageLogger.out.println("Query Executed");
			int i = 0;

			if (rs.next()) {

				i++;

				CourseProperties course = new CourseProperties();
				course.setCrn(rs.getString(1));
				course.setCourseName(rs.getString(2));
				course.setStartTime(rs.getString(3));
				course.setEndTime(rs.getString(4));
				course.setStartDate(rs.getDate(5).toString());
				course.setEndDate(rs.getDate(6).toString());
				course.setDay(nullTest(rs.getString(7))
						+ nullTest(rs.getString(8)) + nullTest(rs.getString(9))
						+ nullTest(rs.getString(10))
						+ nullTest(rs.getString(11))
						+ nullTest(rs.getString(12))
						+ nullTest(rs.getString(13)));
				course.setRoom(rs.getString(14));
				course.setBuilding(rs.getString(15));
				course.setCourseType(rs.getString(16));
				course.setSubjCode(rs.getString(17));
				course.setCrseNumb(rs.getString(18));

				return course;

			}

			prepStmt.close();
			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public String nullTest(String s) {
		if (s == null)
			return "";

		return s;
	}

	public static void main(String[] args) throws RemoteException, SQLException {
		CTSDb db = new CTSDb(0);
		db.openConn();
		
		CTSRemoteService cts = new CTSRemoteService(); 
		cts.getCourseProperty("20167");
		//getCourseProperty
		System.out.println(db.getCurrentTerm());

		/*
		 * and ( SIRASGN.SIRASGN_CRN = '21380' and SCBCRSE.SCBCRSE_TITLE =
		 * 'Intro to Cost and Mgmt Acctg' and SSRMEET.SSRMEET_WED_DAY = 'W' and
		 * SSRMEET.SSRMEET_begin_time = '0810'
		 */

		//db.collectSingleCourse("21380", "Intro to Cost and Mgmt Acctg", "W",
			//	"0810");
		//db.collectLecturerCourses(Long.toString(99002979));

		// db.collectCourse("10616");
		System.exit(0);
		// db.collectLecturerCourses("30156");
		// db.collectLecturerDetail("30156");
		// db.collectLecturers();
	}

}
