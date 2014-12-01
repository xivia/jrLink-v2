package ch.ffhs.jee.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the trole database table.
 * 
 */
@Entity
@Table(name="trole")
@NamedQueries({
	@NamedQuery(name="Role.findAll", 
				query="SELECT r FROM Role r"),
	@NamedQuery(name="Role.findByName", 
				query="SELECT r FROM Role r WHERE r.nameShort = :name")
}) 

public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ROLE_ID_GENERATOR", sequenceName="SEQ_ROLKEY", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ROLE_ID_GENERATOR")
	@Column(name="rolkey")
	private Long id;

	@Column(name="rolnamelong")
	private String nameLong;

	@Column(name="rolnameshort")
	private String nameShort;

	//bi-directional many-to-one association to User
	@OneToMany(mappedBy="role")
	private List<User> users;

	public Role() { }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNameLong() {
		return this.nameLong;
	}

	public void setNameLong(String nameLong) {
		this.nameLong = nameLong;
	}

	public String getNameShort() {
		return this.nameShort;
	}

	public void setNameShort(String nameShort) {
		this.nameShort = nameShort;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User addUser(User user) {
		getUsers().add(user);
		user.setRole(this);

		return user;
	}

	public User removeUser(User user) {
		getUsers().remove(user);
		user.setRole(null);

		return user;
	}

	public Boolean isAdministrator() {
		return (this.getNameShort().compareTo("admin") == 0);
	}
	
	public Boolean isSynchronizer() {
		return (this.getNameShort().compareTo("sync") == 0);
	}
	
	public Boolean isUser() {
		return (this.getNameShort().compareTo("user") == 0);
	}
	
}