package www.com.springboot_iv.dto;

import java.io.Serializable;

import www.com.springboot_iv.entity.Department;

public class CreateEmployeeDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3863730825884754255L;

	private String name;

	private Department department;

	private String gender;

	private String phone;

	private String address;

	private Integer age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

}
