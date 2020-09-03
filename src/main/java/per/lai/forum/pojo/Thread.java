package per.lai.forum.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "thread")
public class Thread {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thread_id")
    private Integer threadId;

    @Column(name = "thread_title")
    private String threadTitle;

    @Column(name = "thread_content")
    private String threadContent;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "thread_time")
    private Date threadTime;

    /*
    * Set for topping the thread
    * */
    @Column(name = "thread_priority")
    private Integer threadPriority;

    /*
    * The creator of this thread.
    * */
    @ManyToOne(targetEntity = User.class,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "thread_owner", referencedColumnName = "user_id")
    @JsonManagedReference
    private User threadOwner;

    /*
    * The board which this thread belong to.
    * */
    @ManyToOne(targetEntity = Board.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "thread_board", referencedColumnName = "board_id")
    @JsonManagedReference
    private Board threadBoard;


    /*
    * A thread contains many comments.
    * */
//    @OneToMany(mappedBy = "commentThread")
//    @JsonBackReference
//    private Set<Comment> comments;
}
