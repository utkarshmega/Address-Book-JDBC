package com.capgemini.addressbookdata;

import java.time.LocalDate;

public class AddressBookData {

	public int id;
	public String first_name;
	public String last_name;
	public String address;
	public String city;
	public String state;
	public int zip;
	public String phone_number;
	public String email;
	public LocalDate start_date;

	public AddressBookData(int id, String first_name, String last_name, String address, String city, String state,
			int zip, String phone_number, String email) {
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phone_number = phone_number;
		this.email = email;
	}
	public AddressBookData(int id, String first_name, String last_name, String address, String city, String state,
			int zip, String phone_number, String email, LocalDate start_date) {
		this(id, first_name, last_name, address, city, state, zip, phone_number, email);
		this.start_date = start_date;
	}
}
