package br.ejcb.cfp.seguranca.ms.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@Entity
@Table(name = "seg_senha")
@Getter
@Setter
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Senha { // extends PanacheEntityBase {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="id_usuario", referencedColumnName = "id", 
				nullable = false, insertable = true, updatable = false,
				foreignKey = @ForeignKey(name = "fk_senha_usuario"))
	private Usuario usuario;
	
	@Column(name = "senha", length = 500, nullable = false, insertable = true, updatable = false)
	private String senha;
	
	@Column(name = "dt_hr_expiracao", nullable = true, insertable = true, updatable = false)
	private LocalDateTime dataExpiracao;
	
	@Column(name = "dt_hr_criacao", nullable = false, insertable = true, updatable = false)
	private LocalDateTime dataHoraCriacao;
	
	@Column(name = "ativo", nullable = false, insertable = true, updatable = true)
	private Boolean ativo;
	
	public static synchronized Senha create() {
		return new Senha();
	}

}
