package com.capgemini.addressbookservice;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.capgemini.addressbookjdbc.AddressBookDBService;
import com.capgemini.addressbookdata.*;

public class AddressBookService {

	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO;
	}

	private List<AddressBookData> addBookList;
	private AddressBookDBService addBookDB;

	public AddressBookService() {
		addBookDB = AddressBookDBService.getInstance();
	}

	public AddressBookService(List<AddressBookData> addBookList) {
		this();
		this.addBookList = addBookList;
	}

	public List<AddressBookData> readAddresBookData(IOService ioService) {
		if (ioService.equals(IOService.DB_IO)) {
			this.addBookList = addBookDB.readData();
		}
		return this.addBookList;
	}

	public void updateFirstName(String oldFirstName, String newFirstName) {
		int result = addBookDB.updateData(oldFirstName, newFirstName);
		if (result == 0)
			return;
		AddressBookData data = this.checkAddressBookInSync(newFirstName);
		if (data != null) {
			data.first_name = newFirstName;
		}

	}

	public AddressBookData checkAddressBookInSync(String newFirstName) {
		List<AddressBookData> list = addBookDB.getAddressBookData(newFirstName);
		return list.stream().filter(con -> con.first_name.equals(newFirstName)).findFirst().orElse(null);
	}
	
	public List<AddressBookData> readAddressBookForDateRange(IOService ioService, LocalDate startDate, LocalDate endDate) {
		if(ioService.equals(IOService.DB_IO)) {
			return addBookDB.getAddressBookForDateRange(startDate, endDate);
		}
		return null;
	}
	
	public Map<String, Integer> readCountContactsByCity(IOService ioService) {
		if(ioService.equals(IOService.DB_IO)) {
			return addBookDB.getCountByCity();
		}
		return null;
	}
	
	public Map<String, Integer> readCountContactsByState(IOService ioService) {
		if(ioService.equals(IOService.DB_IO)) {
			return addBookDB.getCountByState();
		}
		return null;
	}

}
