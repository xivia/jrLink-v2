package ch.ffhs.jee.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tuser database table.
 * 
 */
@Entity
@Table(name="tuser")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USER_ID_GENERATOR", sequenceName="SEQ_USRKEY", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_ID_GENERATOR")
	@Column(name="usrkey")
	private Long id;

	@Column(name="usractiveyn")
	private Boolean active;

	@Column(name="usrname")
	private String name;

	@Column(name="usrpassword")
	private String password;

	//bi-directional many-to-one association to Role
	@ManyToOne
	@JoinColumn(name="usrrolid")
	private Role role;

	public User() { }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean isActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}