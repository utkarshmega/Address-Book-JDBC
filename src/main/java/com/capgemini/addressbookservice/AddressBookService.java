package com.capgemini.addressbookservice;

import java.util.List;

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
		if(ioService.equals(IOService.DB_IO)) {
			this.addBookList = addBookDB.readData();
		}
		return this.addBookList;
	}

}
