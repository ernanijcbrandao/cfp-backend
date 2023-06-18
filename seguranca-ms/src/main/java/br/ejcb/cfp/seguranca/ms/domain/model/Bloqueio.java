package br.ejcb.cfp.seguranca.ms.domain.model;

//@Entity
//@Table(name = "seg_bloqueio",
//		indexes = @Index(name = "idx_bloqueio_chave_ativo", columnList = "chave, ativo", unique = false))
//@Getter
//@Setter
//@With
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
public class Bloqueio { //extends PanacheEntityBase {
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//	
//	@Column(name = "chave", length = 1000, nullable = false, insertable = true, updatable = false)
//	private String chave;
//	
//	@Column(name = "dt_hr_bloqueio", nullable = false, insertable = true, updatable = false)
//	private LocalDateTime dataCriacao;
//	
//	@Column(name = "dt_hr_expiracao", nullable = true, insertable = true, updatable = false)
//	private LocalDateTime dataExpiracao;
//	
//	@Column(name = "motivo", length = 100, nullable = false, insertable = true, updatable = false)
//	private String motivo;
//	
//	@Column(name = "ativo", nullable = false, insertable = true, updatable = true)
//	private Boolean ativo;
	
	public static synchronized Bloqueio create() {
		return new Bloqueio();
	}

}
