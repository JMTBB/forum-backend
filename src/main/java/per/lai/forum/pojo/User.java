package per.lai.forum.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_exp")
    private Integer userExp;

    @Column(name = "user_job")
    private String userJob;

    @Column(name = "user_address")
    private String userAddress;

    @Column(name = "user_phone")
    private String userPhone;

    @Column(name = "user_avatar")
    private String userAvatar;

    @Column(name = "user_token")
    private String userToken;
    /*
    * Every user own its role(s)
    * */
    @ManyToMany(targetEntity = Role.class,fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    /*
    * A user can manager more than one board
    * */
    @OneToMany(mappedBy = "boardManager")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    private Set<Board> boards;
    /*
    * A user can create many threads
    * */
    @OneToMany(mappedBy = "threadOwner")
    private Set<Thread> threads;

    /*
    * User owns many comments.
    * */
    @OneToMany(mappedBy = "commentUser")
    private Set<Comment> comments;




}
