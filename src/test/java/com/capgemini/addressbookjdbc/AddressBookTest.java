package com.capgemini.addressbookjdbc;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	
	@Test
	public void givenContactsData_WhenCountByCity_ShouldReturnProperValue() {
    	AddressBookService addBookService = new AddressBookService();
    	addBookService.readAddresBookData(IOService.DB_IO);
    	Map<String, Integer> countContactsByCity = addBookService.readCountContactsByCity(IOService.DB_IO);
    	Assert.assertTrue(countContactsByCity.get("Prayagraj").equals(2) && countContactsByCity.get("delhi").equals(1) && countContactsByCity.get("Ahmedabad").equals(1));
    }
	
	@Test
	public void givenContactsData_WhenCountByState_ShouldReturnProperValue() {
    	AddressBookService addBookService = new AddressBookService();
    	addBookService.readAddresBookData(IOService.DB_IO);
    	Map<String, Integer> countContactsByState = addBookService.readCountContactsByState(IOService.DB_IO);
    	Assert.assertTrue(countContactsByState.get("Uttar Pradesh").equals(2) && countContactsByState.get("Gujrat").equals(1) && countContactsByState.get("new delhi").equals(1));
    }
	
	@Test
    public void givenNewContact_WhenAdded_ShouldSyncWithDB() {
    	AddressBookService addBookService = new AddressBookService();
    	addBookService.readAddresBookData(IOService.DB_IO);
    	addBookService.addNewContact(4, "Shivangi", "Srivastava", "Lukerganj", "Kolkata", "West Bengal", 700055, "9191919191", "shivangi@gmail.com", LocalDate.of(2020, 11, 03));
    	AddressBookData contact = addBookService.checkAddressBookInSync("Shivangi");
    	Assert.assertEquals("shivangi@gmail.com", contact.email);
    }
	
	@Test
	public void given3Contacts_ShouldMatchCount() {
		
		List<AddressBookData> addContactList = new ArrayList<>();
		addContactList.add(new AddressBookData(4, "Shivangi", "Srivastava", "Lukerganj", "Kolkata", "West Bengal", 700055, "9191919191", "shivangi@gmail.com", LocalDate.of(2020, 11, 03)));
		addContactList.add(new AddressBookData(5, "Shikhar", "Agrawl", "Ashok Nagar", "Prayagraj", "Uttar Padesh", 211002, "9151236987", "shikhar@gmail.com", LocalDate.of(2017, 04, 30)));
		addContactList.add(new AddressBookData(6, "Jugnu", "Singhal", "Tonk", "Jaipur", "Rajasthan", 122012, "9898521425", "jugnu@gmail.com", LocalDate.of(2019, 06, 26)));
		AddressBookService addBookService = new AddressBookService();
		addBookService.readAddresBookData(IOService.DB_IO);
		Instant startTime = Instant.now();
		addBookService.addContactUsingThread(addContactList);
		Instant endTime = Instant.now();
		System.out.println("Duration with thread : " + Duration.between(startTime, endTime));
    	List<AddressBookData> addressBookData = addBookService.readAddresBookData(IOService.DB_IO);
    	System.out.println(addressBookData.size());
    	Assert.assertEquals(7, addressBookData.size());
	}

}
