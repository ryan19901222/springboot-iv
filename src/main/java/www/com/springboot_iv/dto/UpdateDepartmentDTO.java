package www.com.springboot_iv.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class UpdateDepartmentDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4546227500280940668L;

	@NotNull(message="Id is not nullable")
	private Integer id;

	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
