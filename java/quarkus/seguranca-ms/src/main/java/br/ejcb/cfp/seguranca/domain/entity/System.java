package br.ejcb.cfp.seguranca.domain.entity;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@Table(name = "sg_system",
	uniqueConstraints = {
			@UniqueConstraint(name="uk_system_code", columnNames = {"code"}),
			@UniqueConstraint(name="uk_system_name", columnNames = {"name"})
})
public class System extends PanacheEntityBase {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(insertable = true, updatable = false, nullable = false, length = 30)
	private String code;
	
	@Column(insertable = true, updatable = false, nullable = false, length = 100)
	private String name;
	
	@Column(insertable = true, updatable = false, nullable = true, length = 255)
	private String description;
	
	private Boolean active;
	
	@ManyToMany(mappedBy = "systems", fetch = FetchType.LAZY)
	private List<User> users;
	
	public static synchronized System create() {
		return new System();
	}

}
