package com.capgemini.addressbookjdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.capgemini.addressbookdata.*;

public class AddressBookDBService {
	
	private int connectionCounter = 0;
	private static AddressBookDBService addBookDB;
	private PreparedStatement preparedStatement;

	public AddressBookDBService() {
	}

	public static AddressBookDBService getInstance() {
		if (addBookDB == null) {
			addBookDB = new AddressBookDBService();
		}
		return addBookDB;
	}

	private Connection getConnection() throws SQLException {
		connectionCounter++;
		String jdbcURL = "jdbc:mysql://localhost:3306/address_book_service?useSSL=false";
		String username = "root"; 
		String password = "sqldatabase@1252";
		Connection connection;
		System.out.println("Processing Thread: " + Thread.currentThread().getName() + " Connecting to Database ID: " + connectionCounter);
		connection = DriverManager.getConnection(jdbcURL, username, password);
		System.out.println("Processing Thread: " + Thread.currentThread().getName() + " Connecting to Database ID: " + connectionCounter + " Connection is established!!");
		return connection;
	}

	public List<AddressBookData> readData() {
		String sql = "SELECT * FROM contact_details;";
		List<AddressBookData> addBookList = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			addBookList = this.getAddressBookData(result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return addBookList;
	}

	private List<AddressBookData> getAddressBookData(ResultSet result) {
		List<AddressBookData> addressBookList = new ArrayList<>();
		try {
			while (result.next()) {
				int id = result.getInt("id");
				String firstName = result.getString("first_name");
				String lastName = result.getString("last_name");
				String address = result.getString("address");
				String city = result.getString("city");
				String state = result.getString("state");
				int zipcode = result.getInt("zip");
				String phone = result.getString("phone_number");
				String email = result.getString("email");
				LocalDate start_date = result.getDate("start_date").toLocalDate();
				addressBookList.add(new AddressBookData(id, firstName, lastName, address, city, state, zipcode, phone,
						email, start_date));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return addressBookList;
	}

	public List<AddressBookData> getAddressBookData(String firstName) {
		List<AddressBookData> addBookList = null;
		if (this.preparedStatement == null) {
			this.prepareStatementForEmployeeData();
		}
		try {
			preparedStatement.setString(1, firstName);
			ResultSet result = preparedStatement.executeQuery();
			addBookList = this.getAddressBookData(result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return addBookList;
	}

	private void prepareStatementForEmployeeData() {
		try {
			Connection connection = this.getConnection();
			String sql = "SELECT * FROM contact_details WHERE first_name = ?";
			preparedStatement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int updateData(String oldFirstName, String newFirstName) {
		return this.updateAddressBookUsingStatement(oldFirstName, newFirstName);
	}

	private int updateAddressBookUsingStatement(String oldFirstName, String newFirstName) {
		String sql = String.format("update contact_details set first_name = '%s' where first_name = '%s';",
				newFirstName, oldFirstName);
		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<AddressBookData> getAddressBookForDateRange(LocalDate startDate, LocalDate endDate) {
		String sql = String.format("SELECT * FROM contact_details WHERE start_date BETWEEN '%s' AND '%s'",
				Date.valueOf(startDate), Date.valueOf(endDate));
		return this.getAddressBookDataUSingDB(sql);
	}

	private List<AddressBookData> getAddressBookDataUSingDB(String sql) {
		List<AddressBookData> addBookList = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			addBookList = this.getAddressBookData(result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return addBookList;
	}

	public Map<String, Integer> getCountByCity() {
		String sql = "SELECT city, COUNT(city) AS City_Count from contact_details group by city;";
		Map<String, Integer> countByCity = new HashMap<String, Integer>();
		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				String city = result.getString("city");
				int count = result.getInt("City_Count");
				countByCity.put(city, count);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return countByCity;
	}

	public Map<String, Integer> getCountByState() {
		String sql = "SELECT state, COUNT(state) AS State_Count from contact_details group by state;";
		Map<String, Integer> countByState = new HashMap<String, Integer>();
		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while (result.next()) {
				String city = result.getString("state");
				int count = result.getInt("State_Count");
				countByState.put(city, count);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return countByState;
	}

	public AddressBookData addnewContactToDB(int id, String firstName, String lastName, String address, String city, String state, int zip,
			String phone, String email, LocalDate start) {
		AddressBookData addBookData = null;
		String sql = String.format(
				"Insert into contact_details(first_name, last_name, address, city, state, zip, phone_number, email, start_date) values('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",
				firstName, lastName, address, city, state, zip, phone, email, start);
		try(Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
			if(rowAffected == 1) {
				ResultSet result = statement.getGeneratedKeys();
				if(result.next())	
					firstName = result.getString("first_name");
			}
			addBookData = new AddressBookData(id, firstName, lastName, address, city, state, zip, phone, email, start);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return addBookData;
	}
}
