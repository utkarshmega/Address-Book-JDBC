package com.capgemini.addressbookjdbc;

import java.time.LocalDate;
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
    	addBookService.updateFirstName("Prasant", "Prashant");
    	AddressBookData contact = addBookService.checkAddressBookInSync("Prashant");
    	Assert.assertEquals("Prashant", contact.first_name);
    }
	
	@Test 
    public void givenDateRange_WhenRetrieved_ShouldMatchContactsCount() {
    	AddressBookService addBookService = new AddressBookService();
    	addBookService.readAddresBookData(IOService.DB_IO);
    	LocalDate startDate = LocalDate.of(2017, 01, 01);
    	LocalDate endDate = LocalDate.now();
    	List<AddressBookData> addBookData = addBookService.readAddressBookForDateRange(IOService.DB_IO, startDate, endDate);
    	Assert.assertEquals(4, addBookData.size());
    }

}
