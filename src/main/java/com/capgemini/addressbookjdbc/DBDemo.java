package com.capgemini.addressbookjdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

public class DBDemo {

	public static void main(String[] args) {
		
		String jdbcURL = "jdbc:mysql://localhost:3306/address_book_service?useSSL=false";
		String username = "root";
		String password = "sqldatabase@1252";
		Connection connection;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver loaded");
		} catch(ClassNotFoundException e) {
			throw new IllegalStateException("Cannot find the driver in the classpath! "+e);
		}
		listDrivers();
		try {
			System.out.println("Connecting to the database: "+jdbcURL);
			connection = DriverManager.getConnection(jdbcURL, username, password);
			System.out.println("Connection Sucessful!!" +connection);
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	private static void listDrivers() {
		Enumeration<Driver> driverList = DriverManager.getDrivers();
		while(driverList.hasMoreElements()) {
			Driver driverClass = (Driver) driverList.nextElement();
			System.out.println(" "+ driverClass.getClass().getName());
		}
	}


}
