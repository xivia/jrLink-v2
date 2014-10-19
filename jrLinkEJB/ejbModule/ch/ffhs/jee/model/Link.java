package ch.ffhs.jee.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tlink database table.
 * 
 */
@Entity
@Table(name="tlink")
@NamedQuery(name="Link.findAll", query="SELECT l FROM Link l")
public class Link implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LINK_ID_GENERATOR", sequenceName="SEQ_LNKKEY", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LINK_ID_GENERATOR")
	@Column(name="lnkkey")
	private Long id;

	@Column(name="lnkname")
	private String name;

	@Column(name="lnkurl")
	private String url;

	public Link() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}