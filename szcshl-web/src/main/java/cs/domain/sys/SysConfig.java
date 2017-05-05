package cs.domain.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "cs_sysConfig")
public class SysConfig {
	@Id
	//@SequenceGenerator(name = "generator_increment", sequenceName = "seq_increment" )
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator_increment")
	private String id;
	//@Column(columnDefinition = "boolean")
	private boolean isInit;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isInit() {
		return isInit;
	}
	public void setInit(boolean isInit) {
		this.isInit = isInit;
	}
}
