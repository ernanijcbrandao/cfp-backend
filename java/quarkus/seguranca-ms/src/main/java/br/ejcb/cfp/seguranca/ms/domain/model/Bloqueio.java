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
@Table(name = "seg_bloqueio")
@Getter
@Setter
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bloqueio { //extends PanacheEntityBase {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="id_usuario", referencedColumnName = "id", 
				nullable = false, insertable = true, updatable = false,
				foreignKey = @ForeignKey(name = "fk_bloqueio_usuario"))
	private Usuario usuario;
	
	@Column(name = "dh_bloqueio", nullable = false, insertable = true, updatable = false)
	private LocalDateTime dataHoraBloqueio;
	
	@Column(name = "dh_expiracao", nullable = true, insertable = true, updatable = false)
	private LocalDateTime dataHoraExpiracaoBloqueio;
	
	@Column(name = "motivo", length = 200, nullable = false, insertable = true, updatable = false)
	private String motivo;
	
	@Column(name = "fg_ativo", nullable = false, insertable = true, updatable = true)
	private Boolean ativo;
	
	public static synchronized Bloqueio create() {
		return new Bloqueio();
	}

}
