package per.lai.forum.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import per.lai.forum.pojo.Comment;
import per.lai.forum.pojo.User;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ThreadRepository threadRepository;
    @Autowired
    private UserRepository userRepository;
    @Test
    @Transactional
    @Rollback(false)
    public void addComment() {
        Comment comment = new Comment();
        comment.setNestedId(-1);
        comment.setCommentContent("another comment for unit test");
        comment.setCommentTime(new Date());
        comment.setCommentThread(threadRepository.getOne(1));
        comment.setCommentUser(userRepository.getOne(7));

        commentRepository.save(comment);

    }
}