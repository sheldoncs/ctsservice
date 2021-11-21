package cts.service.http;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Timer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import reconf.w3c.monitor.http.ServiceStarter;

import cts.service.remote.CTSRemoteService;
import cts.service.util.CTSRefreshingTask;
import cts.service.util.CourseProperties;
import cts.service.util.LecturerProperties;
import cts.service.util.MessageLogger;

/*
 * Created on Sep 2, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Owner
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class CTSServlet extends JAXMServlet {

	private ArrayList list;
	static final long serialVersionUID = 1;

	public void init(ServletConfig servletConfig) throws ServletException {

		super.init(servletConfig);

		leasingTimer = new Timer();

		Properties uddiProperties = (Properties) servletConfig
				.getServletContext().getAttribute("UDDIProperties");

		try {
			URL url = this.getClass().getResource(
					"/" + this.getClass().getName().replace('.', '/')
							+ ".class");

			servletConfig.getServletContext().setAttribute("classURL", url);
			servletConfig.getServletContext().setAttribute("MonitoredPoint",
					endPoint.getProperty("AccessPoint"));

			sv = new ServiceStarter(url);
			sv.startMonitor(endPoint.getProperty("Monitor"),
					endPoint.getProperty("ContextPath"));

			//CTSRefreshingTask refreshingTask = new CTSRefreshingTask(
				//	uddiProperties);
			//registerLeasingTask(refreshingTask);

		} catch (Exception e) {
			MessageLogger.out.println(e.getMessage());
		}

	}

	protected Document performTask(String requestName, int id,
			Iterator parameter) {

		CTSRemoteService rs = new CTSRemoteService();

		try {

			HashMap hmParameter = parseParameter(parameter, requestName);

			MessageLogger.out.println(requestName);

			if (requestName.equals("getLecturerList")) {
				list = rs.getLecturerList();

				return serializeLecturers(id, list);
			} else if (requestName.equals("getLecturerCourses")) {
				list = rs
						.getLecturerCourses(hmParameter.get("pidm").toString());
				return serializeLecturerCourses(id, list);
			} else if (requestName.equals("getCourseProperty")) {
				CourseProperties course = rs.getCourseProperty(hmParameter.get(
						"crn").toString());
				return serializeCourseProperties(id, course);
			} else if (requestName.equals("getLecturerProperty")) {
				LecturerProperties lecturer = rs
						.getLecturerproperty(hmParameter.get("pidm").toString());

				return serializeLecturerProperties(id, lecturer);

			} else if (requestName.equals("getSingleCourse")){
				CourseProperties course = rs.getSingleCourse(hmParameter.get("crn").toString(), 
						hmParameter.get("title").toString(), hmParameter.get("day").toString(), 
						hmParameter.get("startTime").toString());
				
				return serializeCourseProperties(id, course);
			}

		} catch (Exception e) {
			// DataFormatException
			e.printStackTrace(MessageLogger.out);
		}

		return null;
	}

	private HashMap parseParameter(Iterator parameter, String requestName) {

		HashMap hmParameter = new HashMap();

		while (parameter.hasNext()) {
			SOAPElement subElement = (SOAPElement) parameter.next();
			Name subElementName = subElement.getElementName();

			MessageLogger.out.println("ParameterName ==> "
					+ subElementName.getLocalName());
			MessageLogger.out.println(subElement.getValue());

			if (requestName.equals("getLecturerCourses")) {
				if (subElementName.getLocalName().equals("String_1"))
					hmParameter.put("pidm", subElement.getValue());
			} else if (requestName.equals("getCourseProperty")) {
				if (subElementName.getLocalName().equals("String_1"))
					hmParameter.put("crn", subElement.getValue());
			}

			else if (requestName.equals("getLecturerProperty")) {
				if (subElementName.getLocalName().equals("String_1"))
					hmParameter.put("pidm", subElement.getValue());
			} else if (requestName.equals("getSingleCourse")){
				if (subElementName.getLocalName().equals("String_1")){
					hmParameter.put("crn", subElement.getValue());
				}if (subElementName.getLocalName().equals("String_2")){
					hmParameter.put("title", subElement.getValue());
				}if (subElementName.getLocalName().equals("String_3")){
					hmParameter.put("day", subElement.getValue());
				}if (subElementName.getLocalName().equals("String_4")){
					hmParameter.put("startTime", subElement.getValue());
				}
			}

		}//String crn, String title, String day,
		//String startTime

		return hmParameter;
	}

	private Document serializeLecturerCourses(int id, ArrayList list)
			throws Exception {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = builder.newDocument();

		Element courseListElement = doc.createElement("anyType");
		doc.appendChild(courseListElement);
		courseListElement.setAttribute("id", "ID" + id);
		courseListElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
				"ns2" + ":arrayList");
		courseListElement.setAttribute(SOAP_ENC_PREFIX + ":arrayType",
				XMLSCHEMA_PREFIX + ":anyType[]");

		Iterator iterator = list.iterator();

		while (iterator.hasNext()) {

			Element courseInfoElement = doc.createElement("item");
			courseListElement.appendChild(courseInfoElement);
			courseInfoElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
					SERVICE_PREFIX + ":LecturerCourses");

			CourseProperties course = (CourseProperties) iterator.next();

			/*
			 * MessageLogger.out.println(course.getCourseName()
			 * +","+course.getEndTime()+","+course.getStartTime()+","+
			 * course.getCourseType
			 * ()+","+course.getBuilding()+","+course.getRoom
			 * ()+","+course.getDay()+
			 * ","+course.getEndDate()+","+course.getStartDate
			 * ()+","+course.getSubjCode()+","+course.getCrseNumb());
			 * 
			 * Intro to Cost and Mgmt
			 * Acctg,1200,1110,Tutorial/Discussions,Management
			 * Studies,null,R,2013-11-29,2013-08-26,ACCT,1003
			 */
			Element courseNameElement = doc.createElement("courseName");
			courseInfoElement.appendChild(courseNameElement);
			courseNameElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
					XMLSCHEMA_PREFIX + ":string");
			courseNameElement.appendChild(doc.createTextNode(course
					.getCourseName()));

			Element endTimeElement = doc.createElement("endTime");
			courseInfoElement.appendChild(endTimeElement);
			endTimeElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
					XMLSCHEMA_PREFIX + ":string");
			endTimeElement.appendChild(doc.createTextNode(course.getEndTime()));

			Element startTimeElement = doc.createElement("startTime");
			courseInfoElement.appendChild(startTimeElement);
			startTimeElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
					XMLSCHEMA_PREFIX + ":string");
			startTimeElement.appendChild(doc.createTextNode(course
					.getStartTime()));

			Element courseTypeElement = doc.createElement("courseType");
			courseInfoElement.appendChild(courseTypeElement);
			courseTypeElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
					XMLSCHEMA_PREFIX + ":string");
			courseTypeElement.appendChild(doc.createTextNode(course
					.getCourseType()));

			Element buildingElement = doc.createElement("building");
			courseInfoElement.appendChild(buildingElement);
			buildingElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
					XMLSCHEMA_PREFIX + ":string");
			buildingElement
					.appendChild(doc.createTextNode(course.getBuilding()));

			Element roomElement = doc.createElement("room");
			courseInfoElement.appendChild(roomElement);
			roomElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
					XMLSCHEMA_PREFIX + ":string");
			roomElement.appendChild(doc.createTextNode(course.getRoom()));

			Element dayElement = doc.createElement("day");
			courseInfoElement.appendChild(dayElement);
			dayElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
					XMLSCHEMA_PREFIX + ":string");
			dayElement.appendChild(doc.createTextNode(course.getDay()));

			Element endDateElement = doc.createElement("endDate");
			courseInfoElement.appendChild(endDateElement);
			endDateElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
					XMLSCHEMA_PREFIX + ":string");
			endDateElement.appendChild(doc.createTextNode(course.getEndDate()));

			Element startDateElement = doc.createElement("startDate");
			courseInfoElement.appendChild(startDateElement);
			startDateElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
					XMLSCHEMA_PREFIX + ":string");
			startDateElement.appendChild(doc.createTextNode(course
					.getStartDate()));

			Element subjCodeElement = doc.createElement("subjCode");
			courseInfoElement.appendChild(subjCodeElement);
			subjCodeElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
					XMLSCHEMA_PREFIX + ":string");
			subjCodeElement
					.appendChild(doc.createTextNode(course.getSubjCode()));

			Element crseNumbElement = doc.createElement("crseNumb");
			courseInfoElement.appendChild(crseNumbElement);
			crseNumbElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
					XMLSCHEMA_PREFIX + ":string");
			crseNumbElement
					.appendChild(doc.createTextNode(course.getCrseNumb()));
			
			Element crnElement = doc.createElement("crn");
			courseInfoElement.appendChild(crnElement);
			crnElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
					XMLSCHEMA_PREFIX + ":string");
			crnElement
					.appendChild(doc.createTextNode(course.getCrn()));
			
			Element idNumberElement = doc.createElement("idNumber");
			courseInfoElement.appendChild(idNumberElement);
			idNumberElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
					XMLSCHEMA_PREFIX + ":string");
			idNumberElement
					.appendChild(doc.createTextNode(course.getIdNumber()));
		}

		return doc;
	}

	private Document serializeCourseProperties(int id, CourseProperties obj)
			throws Exception {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = builder.newDocument();

		Element coursePropertiesInfoElement = doc
				.createElement("CourseProperties");
		doc.appendChild(coursePropertiesInfoElement);
		coursePropertiesInfoElement.setAttribute("id", "ID" + id);
		coursePropertiesInfoElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX
				+ ":type", SERVICE_PREFIX + ":CourseProperties");

		Element subjCodeElement = doc.createElement("subjCode");
		coursePropertiesInfoElement.appendChild(subjCodeElement);
		subjCodeElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
				XMLSCHEMA_PREFIX + ":string");
		subjCodeElement.appendChild(doc.createTextNode(obj.getSubjCode()));

		Element crseNumbElement = doc.createElement("crseNumb");
		coursePropertiesInfoElement.appendChild(crseNumbElement);
		crseNumbElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
				XMLSCHEMA_PREFIX + ":string");
		crseNumbElement.appendChild(doc.createTextNode(obj.getCrseNumb()));

		Element courseNameElement = doc.createElement("courseName");
		coursePropertiesInfoElement.appendChild(courseNameElement);
		courseNameElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
				XMLSCHEMA_PREFIX + ":string");
		courseNameElement.appendChild(doc.createTextNode(obj.getCourseName()));

		Element endTimeElement = doc.createElement("endTime");
		coursePropertiesInfoElement.appendChild(endTimeElement);
		endTimeElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
				XMLSCHEMA_PREFIX + ":string");
		endTimeElement.appendChild(doc.createTextNode(obj.getEndTime()));

		Element startTimeElement = doc.createElement("startTime");
		coursePropertiesInfoElement.appendChild(startTimeElement);
		startTimeElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
				XMLSCHEMA_PREFIX + ":string");
		startTimeElement.appendChild(doc.createTextNode(obj.getStartTime()));

		Element courseTypeElement = doc.createElement("courseType");
		coursePropertiesInfoElement.appendChild(courseTypeElement);
		courseTypeElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
				XMLSCHEMA_PREFIX + ":string");
		courseTypeElement.appendChild(doc.createTextNode(obj.getCourseType()));

		Element buildingElement = doc.createElement("building");
		coursePropertiesInfoElement.appendChild(buildingElement);
		buildingElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
				XMLSCHEMA_PREFIX + ":string");
		buildingElement.appendChild(doc.createTextNode(obj.getBuilding()));

		Element roomElement = doc.createElement("room");
		coursePropertiesInfoElement.appendChild(roomElement);
		roomElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
				XMLSCHEMA_PREFIX + ":string");
		roomElement.appendChild(doc.createTextNode(obj.getRoom()));

		Element dayElement = doc.createElement("day");
		coursePropertiesInfoElement.appendChild(dayElement);
		dayElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
				XMLSCHEMA_PREFIX + ":string");
		dayElement.appendChild(doc.createTextNode(obj.getDay()));

		Element endDateElement = doc.createElement("endDate");
		coursePropertiesInfoElement.appendChild(endDateElement);
		endDateElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
				XMLSCHEMA_PREFIX + ":string");
		endDateElement.appendChild(doc.createTextNode(obj.getEndDate()));

		Element startDateElement = doc.createElement("startDate");
		coursePropertiesInfoElement.appendChild(startDateElement);
		startDateElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
				XMLSCHEMA_PREFIX + ":string");
		startDateElement.appendChild(doc.createTextNode(obj.getStartDate()));
		
		Element idNumberElement = doc.createElement("idNumber");
		coursePropertiesInfoElement.appendChild(idNumberElement);
		idNumberElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
				XMLSCHEMA_PREFIX + ":string");
		idNumberElement
				.appendChild(doc.createTextNode(obj.getIdNumber()));
		

		return doc;

	}

	private Document serializeLecturerProperties(int id, LecturerProperties obj)
			throws Exception {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = builder.newDocument();

		Element LecturerPropertiesElement = doc
				.createElement("LecturerProperties");
		doc.appendChild(LecturerPropertiesElement);
		LecturerPropertiesElement.setAttribute("id", "ID" + id);
		LecturerPropertiesElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX
				+ ":type", SERVICE_PREFIX + ":LecturerProperties");

		Element pidmElement = doc.createElement("pidm");
		LecturerPropertiesElement.appendChild(pidmElement);
		pidmElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
				XMLSCHEMA_PREFIX + ":string");
		pidmElement.appendChild(doc.createTextNode(obj.getPidm()));

		Element firstNameElement = doc.createElement("firstName");
		LecturerPropertiesElement.appendChild(firstNameElement);
		firstNameElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
				XMLSCHEMA_PREFIX + ":string");
		firstNameElement.appendChild(doc.createTextNode(obj.getFirstName()));

		Element lastNameElement = doc.createElement("lastName");
		LecturerPropertiesElement.appendChild(lastNameElement);
		lastNameElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
				XMLSCHEMA_PREFIX + ":string");
		lastNameElement.appendChild(doc.createTextNode(obj.getLastName()));

		Element facultyElement = doc.createElement("faculty");
		LecturerPropertiesElement.appendChild(facultyElement);
		facultyElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
				XMLSCHEMA_PREFIX + ":string");
		facultyElement.appendChild(doc.createTextNode(obj.getFaculty()));

		Element emailElement = doc.createElement("email");
		LecturerPropertiesElement.appendChild(emailElement);
		emailElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
				XMLSCHEMA_PREFIX + ":string");
		emailElement.appendChild(doc.createTextNode(obj.getEmail()));

		return doc;

	}

	private Document serializeLecturers(int id, ArrayList list)
			throws Exception {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = builder.newDocument();

		Element lecturerListElement = doc.createElement("anyType");
		doc.appendChild(lecturerListElement);
		lecturerListElement.setAttribute("id", "ID" + id);
		lecturerListElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
				"ns2" + ":arrayList");
		lecturerListElement.setAttribute(SOAP_ENC_PREFIX + ":arrayType",
				XMLSCHEMA_PREFIX + ":anyType[]");

		Iterator iterator = list.iterator();

		while (iterator.hasNext()) {

			Element lecturerInfoElement = doc.createElement("item");
			lecturerListElement.appendChild(lecturerInfoElement);
			lecturerInfoElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX
					+ ":type", SERVICE_PREFIX + ":LecturerProperties");

			LecturerProperties lecturer = (LecturerProperties) iterator.next();

			Element pidmElement = doc.createElement("pidm");
			lecturerInfoElement.appendChild(pidmElement);
			pidmElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
					XMLSCHEMA_PREFIX + ":string");
			pidmElement.appendChild(doc.createTextNode(lecturer.getPidm()));

			Element firstNameElement = doc.createElement("firstName");
			lecturerInfoElement.appendChild(firstNameElement);
			firstNameElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
					XMLSCHEMA_PREFIX + ":string");
			firstNameElement.appendChild(doc.createTextNode(lecturer
					.getFirstName()));

			Element lastNameElement = doc.createElement("lastName");
			lecturerInfoElement.appendChild(lastNameElement);
			lastNameElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
					XMLSCHEMA_PREFIX + ":string");
			lastNameElement.appendChild(doc.createTextNode(lecturer
					.getLastName()));

			Element facultyElement = doc.createElement("faculty");
			lecturerInfoElement.appendChild(facultyElement);
			facultyElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
					XMLSCHEMA_PREFIX + ":string");
			facultyElement
					.appendChild(doc.createTextNode(lecturer.getFaculty()));

			Element emailElement = doc.createElement("email");
			lecturerInfoElement.appendChild(emailElement);
			emailElement.setAttribute(XMLSCHEMA_INSTANCE_PREFIX + ":type",
					XMLSCHEMA_PREFIX + ":string");
			emailElement.appendChild(doc.createTextNode(lecturer.getEmail()));

		}

		return doc;
	}

}
