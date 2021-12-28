package com.rasmoo.client.financescontroll.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.rasmoo.client.financescontroll.v1.constant.TypeEnum;

import lombok.Data;

@Data
@Entity
@Table(name = "ENTRY")
public class Entry {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "type")
	private TypeEnum tipo;
	
	@Column(name = "date")
	private Date data = new Date();
	
	@Column(name = "value")
	private int valor;
	
	@OneToOne(cascade = CascadeType.REFRESH)
	private Category categoria = new Category();
}
