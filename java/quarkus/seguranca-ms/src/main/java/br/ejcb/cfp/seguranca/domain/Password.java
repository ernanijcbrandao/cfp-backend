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
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "sg_password",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"userId","password"})
})
public class Password extends PanacheEntityBase {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "userId", insertable = true, updatable = false, nullable = false)
	private User user;

	@Column(insertable = true, updatable = false, nullable = false)
	private String password;
	
	private Integer invalidAttempt;
	
	@Column(insertable = true)
	private LocalDateTime created;
	private LocalDateTime expire;
	
	private Boolean active;
	
	public static synchronized Password create() {
		return new Password();
	}
	
	// metodos auxiliares
	public Password incrementalInvalidAttempt() {
		this.invalidAttempt = this.invalidAttempt == null
				? 1 : ++this.invalidAttempt;
		return this;
	}
	public Password resetInvalidAttempt() {
		this.invalidAttempt = 0;
		return this;
	}

}
