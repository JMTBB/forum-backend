package per.lai.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import per.lai.forum.pojo.Comment;
import per.lai.forum.pojo.Thread;
import per.lai.forum.pojo.User;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {
    List<Comment> getCommentsByCommentThread(Thread thread);

    List<Comment> getCommentsByCommentUser(User user);

    int countCommentsByCommentThread(Thread thread);

    void deleteCommentsByCommentThread(Thread thread);

}
