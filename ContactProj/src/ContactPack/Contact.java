package ContactPack;

import java.io.Serializable;
import java.util.List;

public class Contact implements Comparable<Contact>, Serializable {
	
	
	int contactId;
	String contactName;
	String emailAddress;
	List<String> contactNumber;
	
	public Contact(int contactId, String contactName, String emailAddress, List<String> contactNumber) {
		this.contactId = contactId;
		this.contactName = contactName;
		this.emailAddress = emailAddress;
		this.contactNumber = contactNumber;
	}
	
	@Override
	public String toString() {
		return contactName + " [contactId=" + contactId + ", contactName=" + contactName + ", emailAddress=" + emailAddress
				+ ", contactNumber=" + contactNumber + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + contactId;
		result = prime * result + ((contactName == null) ? 0 : contactName.hashCode());
		result = prime * result + ((contactNumber == null) ? 0 : contactNumber.hashCode());
		result = prime * result + ((emailAddress == null) ? 0 : emailAddress.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		if (contactId != other.contactId)
			return false;
		if (contactName == null) {
			if (other.contactName != null)
				return false;
		} else if (!contactName.equals(other.contactName))
			return false;
		if (contactNumber == null) {
			if (other.contactNumber != null)
				return false;
		} else if (!contactNumber.equals(other.contactNumber))
			return false;
		if (emailAddress == null) {
			if (other.emailAddress != null)
				return false;
		} else if (!emailAddress.equals(other.emailAddress))
			return false;
		return true;
	}

	public int getContactId() {
		return contactId;
	}
	
	public void setContactId(int contactId) {
		this.contactId = contactId;
	}
	
	public String getContactName() {
		return contactName;
	}
	
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public List<String> getContactNumber() {
		return contactNumber;
	}
	
	public void setContactNumber(List<String> contactNumber) {
		this.contactNumber = contactNumber;
	}

	@Override
	public int compareTo(Contact o) {
		return contactName.compareTo(o.getContactName());
	}
}