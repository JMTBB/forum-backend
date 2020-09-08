package per.lai.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.lai.forum.pojo.Comment;
import per.lai.forum.pojo.Thread;
import per.lai.forum.pojo.User;
import per.lai.forum.pojo.dto.ReceivedComment;
import per.lai.forum.repository.CommentRepository;
import per.lai.forum.repository.ThreadRepository;
import per.lai.forum.repository.UserRepository;
import per.lai.forum.result.Result;
import per.lai.forum.result.ResultBuilder;
import per.lai.forum.utils.AvatarUtil;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    @Autowired
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    private ThreadRepository threadRepository;
    @Autowired
    public void setThreadRepository(ThreadRepository threadRepository) {
        this.threadRepository = threadRepository;
    }
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Result getByThreadId(int threadId) {
        Thread thread = null;
        if (!threadRepository.existsByThreadId(threadId)) {
            return ResultBuilder.buildResult(404, "thread doesn't exist", null);
        }else
            thread = threadRepository.findById(threadId).orElse(null);
        int level = (AvatarUtil.getCurrentUserDetail() != null) ? AvatarUtil.getCurrentUserDetail().getLevel() : 0;
        assert thread != null;
        if (level < thread.getThreadBoard().getBoardAccessLevel()&&!AvatarUtil.isAdmin()) {
            return ResultBuilder.buildFailResult("Level too low/Doesn't login");
        }
        return ResultBuilder.buildSuccessResult(commentRepository.getCommentsByCommentThread(thread));

    }
    public Result getByUserId(int userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResultBuilder.buildFailResult("user doesn't exist");
        }
        return ResultBuilder.buildSuccessResult(commentRepository.getCommentsByCommentUser(user));
    }
    public Result addComment(ReceivedComment receivedComment) {
        Thread thread = threadRepository.findById(receivedComment.getThreadId()).orElse(null);
        if (thread == null) {
            return ResultBuilder.buildResult(404, "thread doesn't exist", null);
        }else if (!AvatarUtil.checkLevel(thread) && !AvatarUtil.isAdmin())
            return ResultBuilder.buildFailResult("Level too low/Doesn't login");

        User user = userRepository.findById(AvatarUtil.getCurrentAuthenticatedUserId()).orElse(null);
        assert user != null;
        user.setUserExp(user.getUserExp()+5);           //经验+5
        Comment comment = new Comment(
                null,
                receivedComment.getContent(),
                new Date(),
                receivedComment.getNestedId(),
                thread,
                user
        );
        commentRepository.save(comment);
        return ResultBuilder.buildSuccessResult(thread.getThreadId());
    }

    public Result getCounterByThread(int threadId) {
        Thread thread = threadRepository.findById(threadId).orElse(null);
        if (thread == null)
            return ResultBuilder.buildFailResult("thread doesn't exist");
        return ResultBuilder.buildSuccessResult(commentRepository.countCommentsByCommentThread(thread));
    }

    public Result deleteComment(int id) {
        commentRepository.deleteById(id);
        return ResultBuilder.buildSuccessResult("deleted");
    }
}
