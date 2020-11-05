package com.capgemini.addressbookservice;

import java.time.LocalDate;
import java.util.HashMap;
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

	public List<AddressBookData> readAddressBookForDateRange(IOService ioService, LocalDate startDate,
			LocalDate endDate) {
		if (ioService.equals(IOService.DB_IO)) {
			return addBookDB.getAddressBookForDateRange(startDate, endDate);
		}
		return null;
	}

	public Map<String, Integer> readCountContactsByCity(IOService ioService) {
		if (ioService.equals(IOService.DB_IO)) {
			return addBookDB.getCountByCity();
		}
		return null;
	}

	public Map<String, Integer> readCountContactsByState(IOService ioService) {
		if (ioService.equals(IOService.DB_IO)) {
			return addBookDB.getCountByState();
		}
		return null;
	}

	public void addNewContact(int id, String firstName, String LastName, String address, String city, String state,
			int zip, String phone, String email, LocalDate start) {
		addBookList.add(
				addBookDB.addnewContactToDB(id, firstName, LastName, address, city, state, zip, phone, email, start));
	}

	public void addContactUsingThread(List<AddressBookData> addContactList) {
		Map<Integer, Boolean> addContactStatus = new HashMap<>();
		addContactList.forEach(addBookData -> {
			Runnable task = () -> {
				addContactStatus.put(addBookData.hashCode(), false);
				this.addNewContact(addBookData.id, addBookData.first_name, addBookData.last_name, addBookData.address,
						addBookData.city, addBookData.state, addBookData.zip, addBookData.phone_number,
						addBookData.email, addBookData.start_date);
				addContactStatus.put(addBookData.hashCode(), true);
			};
			Thread thread = new Thread(task, addBookData.first_name);
			thread.start();
		});
		while(addContactStatus.containsValue(false))
		{
			try {
				Thread.sleep(100);
			}catch(InterruptedException e) {
				
			}
		}
	}

}
