package ContactPack;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class TestContact {

	private Connection dbConn;
	
	public TestContact() {
		dbConn = dbConnection.getConnection();
	}
	
	public void addContact(Contact contact, List<Contact> contacts) {
		contacts.add(contact);
	}
	
	public void displayContactList(List<Contact> contacts) {
		for (Contact contact : contacts) {
			System.out.println(contact);
		}
		System.out.println();
	}
	
	public void displayContactSet(Set<Contact> contacts) {
		for (Contact contact : contacts) {
			System.out.println(contact);
		}
		System.out.println();
	}
	
	
	
	public String getRandomPhoneNumber() {
		return String.valueOf((long)(Math.random() * 9999999999l) + 1000000000l); 
	}
	
	public Contact getDummyContact() {
		int contactId;
		String contactName;
		String emailAddress;
		List<String> contactNumber;
		Random random = new Random();
		String names[] = new String[] {"Tom", "Bob", "Kat", "Tina", "Sage", "Hillary", "Modi"};
		String emails[] = new String[] {"asfd@sdf.com", "rocker@sdf.com", "megaBoost@sdf.com", "hellToy@xcv.com", "sdfa@vbb.com"};
		
		
		
		contactId = random.nextInt(10000);
		contactName = names[random.nextInt(names.length)];
		emailAddress = emails[random.nextInt(emails.length)];
		contactNumber = new ArrayList<String>();
		int numOfContacts = random.nextInt(4) + 1;
		for (int i = 0; i < numOfContacts; i++) {
			contactNumber.add(getRandomPhoneNumber());
		}
		
		
		Contact dummyContact = new Contact(contactId, contactName, emailAddress, contactNumber);
		
		return dummyContact;
	}
	
	public void removeContact(Contact contact, List<Contact> contacts) throws ContactNotFoundException {
		Iterator<Contact> iter = contacts.iterator();
		boolean found = false;
		
		while(iter.hasNext()) {
			if (iter.next().equals(contact)) {
				iter.remove();
				found = true;
			}
		}
		
		if (! found) {
			throw new ContactNotFoundException(contact);
		}
	}
	
	public Contact searchContactByName(String name, List<Contact> contacts) throws ContactNotFoundException {
		Contact contact = null;
		
		for (Contact curContact : contacts) {
			if (curContact.getContactName().equals(name)) {
				contact = curContact;
				break;
			}
		}
		
		if (contact == null) {
			throw new ContactNotFoundException(name);
		}
		
		return contact;
	}
	
	public List<Contact> searchContactByNumber(String number, List<Contact> contacts) throws ContactNotFoundException {
		List <Contact> matchedContacts = new ArrayList<>();
		
		for (Contact contact : contacts) {
			for (String curNumber : contact.getContactNumber()) {
				if (curNumber.contains(number)) {
					matchedContacts.add(contact);
					break;
				}
			}
		}
		
		if (matchedContacts.isEmpty()) {
			throw new ContactNotFoundException(number, 1);
		}
		return matchedContacts;
	}
	
	public void addContactNumber(int keyContactId, String newContactNumber, List<Contact> contacts) {
		for (Contact contact : contacts) {
			if (contact.getContactId() == keyContactId) {
				contact.getContactNumber().add(newContactNumber);
				break;
			}
		}
	}
	
	public void sortContactsByName(List<Contact> contacts) {
		Collections.sort(contacts);
		
	}
	
	public void readContactsFromFile(List<Contact> contacts, String fileName) {
        try  {    
            Path path = Paths.get("src/ContactPack/"+fileName);
            List<String> lines = Files.readAllLines(path);
            for(String line: lines){
                
                   
                    Contact dummy= parseAndAddContact(contacts,line);
                
                }
            }

            catch (IOException | NullPointerException ioe) {
                ioe.printStackTrace();
            }
        
	}

    public Contact parseAndAddContact(List<Contact> contacts,String line){
        String[] splitText = line.split(",");
        int contactId;
        String contactName;
        String emailAddress;
        List<String> contactNumber;
    
            
        contactId = Integer.parseInt(splitText[0]);
        contactName = splitText[1];
        emailAddress = splitText[2];
        contactNumber = new ArrayList<String>();
        
        for (int i = 3; i < splitText.length; i++) {
            contactNumber.add(splitText[i]);
        }
        Contact mm= new Contact(contactId, contactName, emailAddress, contactNumber);
        contacts.add(mm);
        return mm;
    }
    public Contact justParseContact(List<Contact> contacts,String line){
        String[] splitText = line.split(",");
        int contactId;
        String contactName;
        String emailAddress;
        List<String> contactNumber;
    
            
        contactId = Integer.parseInt(splitText[0]);
        contactName = splitText[1];
        emailAddress = splitText[2];
        contactNumber = new ArrayList<String>();
        
        for (int i = 3; i < splitText.length; i++) {
            contactNumber.add(splitText[i]);
        }
        Contact mm= new Contact(contactId, contactName, emailAddress, contactNumber);
        return mm;
    }
	
	public void serializeContactDetails(List<Contact> contacts, String fileName) {
		File file = new File(fileName);
		
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
			out.writeObject(contacts);
		} catch (IOException fnfe) {
			fnfe.printStackTrace();
		}
	}
	
	public List<Contact> deserializeContact(String fileName) {
		File file = new File(fileName);
		List <Contact> contacts = new ArrayList<>();
		
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
			
			contacts = (ArrayList<Contact>) in.readObject();
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return contacts;
	}
	
	public Set<Contact> populateContactFromDb() throws SQLException {
		Set <Contact> contacts = new HashSet<Contact>();
		
		String sql = "select * from contact_tbl";
		Statement stmt = dbConn.createStatement();
		
		ResultSet rs = stmt.executeQuery(sql);
		
		int contactId;
		String contactName;
		String emailAddress;
		List<String> contactNumber;
		
		try {
			while (rs.next()) {
				contactId = rs.getInt(1);
				contactName = rs.getString(2);
				emailAddress = rs.getString(3);
				String contactStr = rs.getString(4);
				
				if (contactStr != null) {
					contactNumber = new ArrayList(Arrays.asList(contactStr.split(",")));
				} else {
					contactNumber = new ArrayList<>();
				}
				Contact curContact = new Contact(contactId, contactName, emailAddress, contactNumber);
				
				contacts.add(curContact);
			}
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
		}
		
		return contacts;
	}
	
	public void addContacts(List<Contact> contacts, Set<Contact> fromDb) {
		for (Contact contact : fromDb) {
			contacts.add(contact);
		}
	}
	
	public static void main(String[] args) {
		TestContact cs = new TestContact();
		List <Contact> contacts = new ArrayList<>();
		Scanner sc= new Scanner(System.in);
		// Add contact in contacts
		System.out.println("Add contact in contacts");
		System.out.println("Enter contact Details");
        String cd1= sc.nextLine();
        cs.parseAndAddContact(contacts, cd1);
		
        System.out.println("Enter contact Details (To search up later)");
        String cd2= sc.nextLine();
		Contact toBeSearchedLater = cs.parseAndAddContact(contacts,cd2);
		
        System.out.println("Enter contact Details (To remove later)");
        String cd3= sc.nextLine();
		Contact toRemoveLater =cs.parseAndAddContact(contacts,cd3);
		cs.displayContactList(contacts);

		
		System.out.println("Remove contact from contacts - " + toRemoveLater.getContactName() + " contact exists");
		try {
			cs.removeContact(toRemoveLater, contacts);
		} catch (ContactNotFoundException e) {
			e.printStackTrace();
		}
		cs.displayContactList(contacts);
		
        System.out.println("Enter contact Details (To check contactnotfound, this won't be added)");
        String cd4= sc.nextLine();
		Contact nonExistentContact = cs.justParseContact(contacts, cd4);
		System.out.println("Remove contact from contacts - " + nonExistentContact.getContactName() + " contact doesn't exists");
		try {
			cs.removeContact(nonExistentContact, contacts);
		} catch (ContactNotFoundException e) {
			e.printStackTrace();
		}
		cs.displayContactList(contacts);
		

		System.out.println("Search contact in contacts - " + toBeSearchedLater.getContactName() + " - contact exists");
		try {
			cs.searchContactByName(toBeSearchedLater.getContactName(), contacts);
		} catch (ContactNotFoundException e) {
			e.printStackTrace();
		}
		cs.displayContactList(contacts);
		
	
		String nameNotExists = "randomName";
		System.out.println("Search contact in contacts - " + nameNotExists + " - contact doesn't exist");
		try {
			cs.searchContactByName(nameNotExists, contacts);
		} catch (ContactNotFoundException e) {
			e.printStackTrace();
		}
		cs.displayContactList(contacts);
		

		Random random = new Random();
		String numberExists = toBeSearchedLater.getContactNumber().get(0);
		numberExists = numberExists.substring(random.nextInt(numberExists.length()));
		System.out.println("Search contact by number " + numberExists + " - number exists");
		List<Contact> matchedContacts = new ArrayList<>();
		try {
			matchedContacts = cs.searchContactByNumber(numberExists, contacts);
		} catch (ContactNotFoundException e) {
			e.printStackTrace();
		}
		cs.displayContactList(matchedContacts);
		
	
		String numberNotExists = "-1";
		System.out.println("Search contact by number " + numberNotExists + " - number doesn't exist");
		matchedContacts = new ArrayList<>();
		try {
			matchedContacts = cs.searchContactByNumber(numberNotExists, contacts);
		} catch (ContactNotFoundException e) {
			e.printStackTrace();
		}
		cs.displayContactList(matchedContacts);
		

		int keyContactId = toBeSearchedLater.getContactId();
		String newContactNumber = cs.getRandomPhoneNumber();
		System.out.println("Add contact number - " + newContactNumber + " to contact with id - " + keyContactId);
		cs.addContactNumber(keyContactId, newContactNumber, contacts);
		cs.displayContactList(contacts);
		

		System.out.println("Sort contacts by name");
		cs.sortContactsByName(contacts);
		cs.displayContactList(contacts);
		

		String fileName = "Contact.txt";
		System.out.println("Read contacts from file - " + fileName);
		cs.readContactsFromFile(contacts, fileName);
		cs.displayContactList(contacts);
		

		fileName = "serializedContacts";
		System.out.println("Serialize contact details in file - " + fileName);
		cs.serializeContactDetails(contacts, fileName);
		

		System.out.println("Deserialize contact details from file - " + fileName);
		List<Contact> deserializedContacts = cs.deserializeContact(fileName);
		cs.displayContactList(deserializedContacts);

		System.out.println("Populate contact from db");
		Set<Contact> fromDb = null;
		try {
			fromDb = cs.populateContactFromDb();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		cs.displayContactSet(fromDb);

		System.out.println("Add new contacts to the existing contacts");
		cs.addContacts(contacts, fromDb);
		cs.displayContactList(contacts);
		
	}
}