package com.capgemini.addressbookjdbc;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.capgemini.addressbookdata.AddressBookData;
import com.capgemini.addressbookservice.AddressBookService;
import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AddressBookAPITest {

	@Before
	public void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}

	private AddressBookData[] getContactList() {
		Response response = RestAssured.get("/contacts");
		AddressBookData[] arrOfContacts = new Gson().fromJson(response.asString(), AddressBookData[].class);
		return arrOfContacts;
	}

	private Response addContactToJSONServer(AddressBookData addressBookData) {
		String conJson = new Gson().toJson(addressBookData);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(conJson);
		return request.post("/contacts");
	}

	private Response updateContactToJSONServer(AddressBookData addressBookData) {
		String conJson = new Gson().toJson(addressBookData);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(conJson);
		return request.put("/contacts/" + addressBookData.first_name);
	}

	private Response deleteContactFromJSONServer(AddressBookData addressBookData) {
		String conJson = new Gson().toJson(addressBookData);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(conJson);
		return request.delete("/contacts/" + addressBookData.first_name);
	}

	@Test
	public void givenContactsDataInJsonServer_WhenRetrived_ShouldMatchCount() {
		AddressBookData[] arrOfCon = getContactList();
		AddressBookService addBookService;
		addBookService = new AddressBookService(Arrays.asList(arrOfCon));
		long entries = addBookService.noOfEntries(AddressBookService.IOService.REST_IO);
		assertEquals(4, entries);
	}

	@Test
	public void givenNewContact_WhenAdded_ShouldReturn201ResponseAndCount() {
		AddressBookData[] arrOfCon = getContactList();
		AddressBookService addBookService;
		addBookService = new AddressBookService(Arrays.asList(arrOfCon));

		AddressBookData addBookData = null;
		addBookData = new AddressBookData(5, "Sreyansh", "Sharma", "NJP", "Siliguri", "West Bengal", 700087,
				"9875987534", "sreyansh@gmail.com", LocalDate.of(2020, 02, 20));
		Response response = addContactToJSONServer(addBookData);
		int statusCode = response.getStatusCode();
		assertEquals(201, statusCode);

		AddressBookData[] arrOfContacts = getContactList();
		addBookService = new AddressBookService(Arrays.asList(arrOfContacts));
		long entries = addBookService.noOfEntries(AddressBookService.IOService.REST_IO);
		assertEquals(5, entries);
	}

	@Test
	public void givenFirstName_WhenUpdated_ShouldReturn200Response() {
		AddressBookData[] arrOfCon = getContactList();
		AddressBookService addBookService;
		addBookService = new AddressBookService(Arrays.asList(arrOfCon));

		addBookService.updateFirstName("Sreyansh", "Shreyansh", AddressBookService.IOService.REST_IO);
		AddressBookData addBookData = addBookService.getContactsData("Shreyansh");

		Response response = updateContactToJSONServer(addBookData);
		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);
	}

	@Test
	public void givenContactToDelete_WhenDeleted_ShouldReturn200ResponseAndCount() {
		AddressBookData[] arrOfCon = getContactList();
		AddressBookService addBookService;
		addBookService = new AddressBookService(Arrays.asList(arrOfCon));

		AddressBookData addBookData = addBookService.getContactsData("Shreyansh");

		Response response = deleteContactFromJSONServer(addBookData);
		int statusCode = response.getStatusCode();
		assertEquals(200, statusCode);

		addBookService.deleteContactFromAddressBook("Shreyansh", AddressBookService.IOService.REST_IO);
		int entries = addBookService.noOfEntries(AddressBookService.IOService.REST_IO);
		assertEquals(4, entries);
	}

}
