package com.capgemini.addressbookjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.capgemini.addressbookdata.*;

public class AddressBookDBService {

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
		String jdbcURL = "jdbc:mysql://localhost:3306/address_book_service?useSSL=false";
		String username = "root";
		String password = "sqldatabase@1252";
		Connection connection;
		connection = DriverManager.getConnection(jdbcURL, username, password);
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
				addressBookList
						.add(new AddressBookData(id, firstName, lastName, address, city, state, zipcode, phone, email));
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
		String sql = String.format("update contact_details set first_name = '%s' where first_name = '%s';", newFirstName,
				oldFirstName);
		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
