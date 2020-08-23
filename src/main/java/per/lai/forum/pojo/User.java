package per.lai.forum.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    /*
    * Every user belong one of user types(Role)
    * */
    @ManyToOne(targetEntity = Role.class, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "user_role", referencedColumnName = "role_id")
    private Role userRole;
    /*
    * A user can manager more than one board
    * */
    @OneToMany(mappedBy = "boardManager")
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
