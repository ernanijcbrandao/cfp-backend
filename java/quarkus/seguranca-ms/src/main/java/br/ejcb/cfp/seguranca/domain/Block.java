package br.ejcb.cfp.seguranca.domain;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sg_block")
public class Block extends PanacheEntityBase {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "userId", insertable = true, updatable = false, nullable = false)
	private User user;

	@Column(insertable = true, updatable = false, nullable = false)
	private String reason;
	
	@Column(insertable = true, updatable = false, nullable = true)
	private String description;
	
	@Column(insertable = true)
	private LocalDateTime blocked;
	private LocalDateTime expire;
	
	private Boolean active;
	
	public static synchronized Block create() {
		return new Block();
	}
	
}
