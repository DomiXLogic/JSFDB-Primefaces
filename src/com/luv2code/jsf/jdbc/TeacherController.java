package com.luv2code.jsf.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
//import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class TeacherController {

	private List<Teacher> teachers;
	private TeacherDbUtil teacherDbUtil;
     //   private Logger logger = Logger.getLogger(getClass().getName());
	
	public TeacherController() throws Exception {
		teachers = new ArrayList<>();
                
		teacherDbUtil = TeacherDbUtil.getInstance();
	}
	
	public List<Teacher> getTeachers() {
		return teachers;
	}
        
	public void loadTeachers() {

		//logger.info("Loading teachers");
		
		teachers.clear();

		try {
			
			// get all teachers from database
			teachers = teacherDbUtil.getTeachers();
			
		} catch (Exception exc) {
			// send this to server logs
		//	logger.log(Level.SEVERE, "Error loading teachers", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
		}
	}
        
	public String addTeacher(Teacher theTeacher) {

	//	logger.info("Adding teacher: " + theTeacher);

		try {
			
			// add teacher to the database
			teacherDbUtil.addTeacher(theTeacher);
			
		} catch (Exception exc) {
			// send this to server logs
			//logger.log(Level.SEVERE, "Error adding teachers", exc);
			                 System.out.println(exc);
			// add error message for JSF page
			addErrorMessage(exc);

			return null;
		}
		
		return "list-teachers?faces-redirect=true";
	}

	public String loadTeachers(int teacherId) {
		
	//	logger.info("loading teacher: " + teacherId);
		
		try {
			// get teacher from database
			Teacher theTeacher = teacherDbUtil.getTeacher(teacherId);
			
			// put in the request attribute ... so we can use it on the form page
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();		

			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("teacher", theTeacher);	
			
		} catch (Exception exc) {
			// send this to server logs
			//logger.log(Level.SEVERE, "Error loading teacher id:" + teacherId, exc);
			  System.out.println(exc);
			// add error message for JSF page
			addErrorMessage(exc);
			
			return null;
		}
				
		return "update-teacher-form.xhtml";
	}	
	
	public String updateStudent(Teacher theTeacher) {

		//logger.info("updating teacher: " + theTeacher);
		
		try {
			
			// update student in the database
			teacherDbUtil.updateTeacher(theTeacher);
			
		} catch (Exception exc) {
			// send this to server logs
			//logger.log(Level.SEVERE, "Error updating techer: " + theTeacher, exc);
			  System.out.println(exc);
			// add error message for JSF page
			addErrorMessage(exc);
			
			return null;
		}
		
		return "list-teacher?faces-redirect=true";		
	}
	
	public String deleteTeacher(int teacherId) {

		//logger.info("Deleting student id: " + teacherId);
		
		try {

			// delete the student from the database
			teacherDbUtil.deleteTeacher(teacherId);
			
		} catch (Exception exc) {
			// send this to server logs
			//logger.log(Level.SEVERE, "Error deleting teacher id: " + teacherId, exc);
			  System.out.println(exc);
			// add error message for JSF page
			addErrorMessage(exc);
			
			return null;
		}
		
		return "list-teadcher";	
	}	
	
	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
}
