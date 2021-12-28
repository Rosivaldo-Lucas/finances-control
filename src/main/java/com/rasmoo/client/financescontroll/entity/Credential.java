package com.rasmoo.client.financescontroll.entity;

import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class Credential {
	
	private String email;

	private String senha;
	
}
