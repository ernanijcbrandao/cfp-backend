package br.ejcb.cfp.seguranca.ms.domain.model;

import java.time.LocalDateTime;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@Entity
@Table(name = "seg_bloqueio",
		indexes = @Index(name = "iuk_bloqueio_chave_senha", columnList = "chave,senha", unique = true))
@Getter
@Setter
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bloqueio extends PanacheEntityBase {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "chave", length = 1000, nullable = false, insertable = true, updatable = false)
	private String chave;
	
	@Column(name = "dt_hr_bloqueio", nullable = false, insertable = true, updatable = false)
	private LocalDateTime dataCriacao;
	
	@Column(name = "dt_hr_expiracao", nullable = true, insertable = true, updatable = false)
	private LocalDateTime dataExpiracao;
	
	@Column(name = "motivo", length = 100, nullable = false, insertable = true, updatable = false)
	private String motivo;
	
	@Column(name = "ativo", nullable = false, insertable = true, updatable = true)
	private Boolean ativo;
	
	public static synchronized Bloqueio create() {
		return new Bloqueio();
	}

}
