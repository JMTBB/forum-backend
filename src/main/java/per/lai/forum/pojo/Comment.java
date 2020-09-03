package per.lai.forum.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer commentId;

    @Column(name = "comment_content")
    private String commentContent;

    @Column(name = "comment_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date commentTime;

    /*
    * Id of the comment which this post nested in
    * */
    @Column(name = "nested_id")
    private Integer nestedId;


    /*
    * A thread contains many comments.
    * */
    @JoinColumn(name = "comment_thread", referencedColumnName = "thread_id")
    @ManyToOne(targetEntity = Thread.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonManagedReference
    private Thread commentThread;

    /*
    * A user comment many times.
    * */
    @ManyToOne(targetEntity = User.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "comment_user", referencedColumnName = "user_id")
    @JsonManagedReference
    private User commentUser;
}
