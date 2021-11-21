package cts.service.util;

public class CRNProperties {

	
	/**
	 * @return the cRN
	 */
	public String getCRN() {
		return CRN;
	}
	/**
	 * @param cRN the cRN to set
	 */
	public void setCRN(String cRN) {
		CRN = cRN;
	}
	/**
	 * @return the subjCode
	 */
	public String getSubjCode() {
		return subjCode;
	}
	/**
	 * @param subjCode the subjCode to set
	 */
	public void setSubjCode(String subjCode) {
		this.subjCode = subjCode;
	}
	/**
	 * @return the crseCode
	 */
	public String getCrseCode() {
		return crseCode;
	}
	/**
	 * @param crseCode the crseCode to set
	 */
	public void setCrseCode(String crseCode) {
		this.crseCode = crseCode;
	}
	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}
	/**
	 * @param courseName the courseName to set
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
	private String CRN;
	private String subjCode;
	private String crseCode;
	private String courseName;
	
}
