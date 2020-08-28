package per.lai.forum.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role {
    @Id
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "role_name")
    private String roleName;
    /*
    * Role means user type, every user has its type.
    * */
    @JsonIgnore
    @OneToMany(mappedBy = "userRole")
    private Set<User> users;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return roleId.equals(role.roleId) &&
                roleName.equals(role.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, roleName);
    }
}
