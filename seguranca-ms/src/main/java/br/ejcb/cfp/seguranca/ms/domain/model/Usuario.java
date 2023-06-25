package br.ejcb.cfp.seguranca.ms.domain.model;

import java.time.LocalDate;

import br.ejcb.cfp.seguranca.ms.domain.enums.Perfil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@Entity
@Table(name = "seg_usuario",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = "nome", name = "uk_usuario_nome"),
				@UniqueConstraint(columnNames = "login", name = "uk_usuario_login"),
				@UniqueConstraint(columnNames = "chave", name = "uk_usuario_chave")
		})
@Getter
@Setter
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Usuario { // extends PanacheEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "chave", length = 500, nullable = false, insertable = true, updatable = false)
	private String chave;
	
	@Column(name = "nome", length = 100, nullable = false, insertable = true, updatable = true)
	@NotEmpty
	private String nome;
	
	@Column(name = "login", length = 50, nullable = false, insertable = true, updatable = false)
	@NotEmpty
	private String login;
	
	@Column(name = "dt_criacao", nullable = false, insertable = true, updatable = false)
	@NotNull
	private LocalDate dataCriacao;
	
	@Column(name = "ativo", nullable = false, insertable = true, updatable = true)
	@NotNull
	private Boolean ativo;
	
	@Column(length = 30, insertable = true, updatable = true, nullable = false)
	@Enumerated(EnumType.STRING)
	@NotNull
	private Perfil perfil;
	
	
	public static synchronized Usuario create() {
		return new Usuario();
	}
	
	public boolean isNew() {
		return this.id == null;
	}

}
