package osa.ora.camel.beans;

public class Account {

		int id;
		String name;
		String address;
		String phone;
		int active;
		public Account() {
			
		}
		@Override
		public String toString() {
			return "{\"id\": " + id + ", \"name\":\"" + name + "\", \"address\":\""+
					address+"\", \"phone\":\""+phone+"\", \"active\":"+active+"}";
		}
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public int getActive() {
			return active;
		}
		public void setActive(int active) {
			this.active = active;
		}
		
}
