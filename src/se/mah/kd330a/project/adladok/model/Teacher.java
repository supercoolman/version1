package se.mah.kd330a.project.adladok.model;

public class Teacher {
	public static String getFirstName() {
		return firstName;
	}
	public static void setFirstName(String firstName) {
		Teacher.firstName = firstName;
	}
	public static String getLastName() {
		return lastName;
	}
	public static void setLastName(String lastName) {
		Teacher.lastName = lastName;
	}
	public static String getEmail() {
		return email;
	}
	public static void setEmail(String email) {
		Teacher.email = email;
	}
	public static String getDispayName() {
		return dispayName;
	}
	public static void setDispayName(String dispayName) {
		Teacher.dispayName = dispayName;
	}
	public static boolean isStaff() {
		return isStaff;
	}
	public static void setStaff(boolean isStaff) {
		Teacher.isStaff = isStaff;
	}
	public static boolean isStudent() {
		return isStudent;
	}
	public static void setStudent(boolean isStudent) {
		Teacher.isStudent = isStudent;
	}
	public static String getUserID() {
		return userID;
	}
	public static void setUserID(String userID) {
		Teacher.userID = userID;
	}
	private static String firstName="";
	private static String lastName="";
	private static String email="";
	private static String dispayName="";
	private static boolean isStaff = false;
	private static boolean isStudent = false;
	private static String userID="";
    private static String TAG ="UserInfo";
}
