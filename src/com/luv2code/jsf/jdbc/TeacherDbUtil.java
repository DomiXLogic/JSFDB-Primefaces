/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.luv2code.jsf.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class TeacherDbUtil {

	private static TeacherDbUtil instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/student_tracker";
	
	public static TeacherDbUtil getInstance() throws Exception {
		if (instance == null) {
			instance = new TeacherDbUtil();
		}
		
		return instance;
	}
	
	private TeacherDbUtil() throws Exception {		
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
		
	public List<Teacher> getTeachers() throws Exception {

		List<Teacher> teachers = new ArrayList<>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from teacher order by LName";

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);

			// process result set
			while (myRs.next()) {
				
				// retrieve data from result set row
				int id = myRs.getInt("id");
				String firstName = myRs.getString("Name");
				String lastName = myRs.getString("LName");
				String email = myRs.getString("mail");

				// create new teacher object
				Teacher tempTeacher = new Teacher(id, firstName, lastName,
						email);

				// add it to the list of teachers
				teachers.add(tempTeacher);
			}
			
			return teachers;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}

	public void addTeacher(Teacher theTeacher) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "insert into teacher (Name, LName, mail) values (?, ?, ?)";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setString(1, theTeacher.getFirstName());
			myStmt.setString(2, theTeacher.getLastName());
			myStmt.setString(3, theTeacher.getEmail());
			
			myStmt.execute();			
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	public Teacher getTeacher(int teacherId) throws Exception {
	
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from teacher where id=?";

			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setInt(1, teacherId);
			
			myRs = myStmt.executeQuery();

			Teacher theTeacher = null;
			
			// retrieve data from result set row
			if (myRs.next()) {
				int id = myRs.getInt("id");
				String firstName = myRs.getString("Name");
				String lastName = myRs.getString("LName");
				String email = myRs.getString("mail");

				theTeacher = new Teacher(id, firstName, lastName,
						email);
			}
			else {
				throw new Exception("Could not find teacher id: " + teacherId);
			}

			return theTeacher;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	public void updateTeacher(Teacher theTeacher) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update teacher "
						+ " set Name=?, LName=?, mail=?"
						+ " where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setString(1, theTeacher.getFirstName());
			myStmt.setString(2, theTeacher.getLastName());
			myStmt.setString(3, theTeacher.getEmail());
			myStmt.setInt(4, theTeacher.getId());
			
			myStmt.execute();
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	public void deleteTeacher(int teacherId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "delete from teacher where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, teacherId);
			
			myStmt.execute();
		}
		finally {
			close (myConn, myStmt);
		}		
	}	
	
	private Connection getConnection() throws Exception {

		Connection theConn = dataSource.getConnection();
		
		return theConn;
	}
	
	private void close(Connection theConn, Statement theStmt) {
		close(theConn, theStmt, null);
	}
	
	private void close(Connection theConn, Statement theStmt, ResultSet theRs) {

		try {
			if (theRs != null) {
				theRs.close();
			}

			if (theStmt != null) {
				theStmt.close();
			}

			if (theConn != null) {
				theConn.close();
			}
			
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}	
}
