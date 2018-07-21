package www.com.springboot_iv.dto;

import java.io.Serializable;

public class CreateDepartmentDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -83552449034869567L;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
