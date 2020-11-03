package com.capgemini.addressbookjdbc;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.capgemini.addressbookdata.AddressBookData;
import com.capgemini.addressbookservice.AddressBookService;
import com.capgemini.addressbookservice.AddressBookService.IOService;

public class AddressBookTest {
	
	@Test
	public void GivenData_ShouldMatchCount() {
		AddressBookService addBookService = new AddressBookService();
		List<AddressBookData> addBookData = addBookService.readAddresBookData(IOService.DB_IO);
    	Assert.assertEquals(4, addBookData.size());
    }

}
