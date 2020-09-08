package per.lai.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import per.lai.forum.pojo.Board;
import per.lai.forum.pojo.Comment;
import per.lai.forum.pojo.Thread;
import per.lai.forum.pojo.User;
import per.lai.forum.pojo.dto.ReceivedThread;
import per.lai.forum.repository.BoardRepository;
import per.lai.forum.repository.CommentRepository;
import per.lai.forum.repository.ThreadRepository;
import per.lai.forum.repository.UserRepository;
import per.lai.forum.result.Result;
import per.lai.forum.result.ResultBuilder;
import per.lai.forum.utils.AvatarUtil;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class ThreadService {
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

    private BoardRepository boardRepository;

    @Autowired
    public void setBoardRepository(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    private CommentRepository commentRepository;

    @Autowired
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Result addThread(ReceivedThread receivedThread) {
        if (AvatarUtil.getCurrentAuthenticatedUserId() != receivedThread.getUserId())
            return ResultBuilder.buildFailResult("user doesn't match");
        User user = userRepository.getOne(receivedThread.getUserId());
        user.setUserExp(user.getUserExp() + 10);  //经验+10
        Board board = boardRepository.getOne(receivedThread.getBoardId());
        Thread thread = new Thread(
                null,
                receivedThread.getTitle(),
                receivedThread.getContent(),
                new Date(),
                0,
                user,
                board

        );
        Thread save = threadRepository.save(thread);
        return ResultBuilder.buildSuccessResult(save.getThreadId());
    }

    public Result getThreadById(int id) {
        if (!threadRepository.existsByThreadId(id))
            return ResultBuilder.buildResult(404, "thread doesn't exist", null);
        Optional<Thread> optional = threadRepository.findById(id);
        Thread thread = optional.orElse(null);
        /*
         * Check the level
         * */
        int level = (AvatarUtil.getCurrentUserDetail() != null) ? AvatarUtil.getCurrentUserDetail().getLevel() : 0;
        assert thread != null;
        if ((level < thread.getThreadBoard().getBoardAccessLevel()) && !AvatarUtil.isAdmin()) {
            return ResultBuilder.buildFailResult("Level too low/Doesn't login");
        }
        return ResultBuilder.buildSuccessResult(thread);
    }

    public Result getByBoardId(int boardId, int pageNumber) {
        Board board = null;
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        if (optionalBoard.isPresent())
            board = optionalBoard.get();
        else
            return ResultBuilder.buildFailResult("board doesn't exist");
        /*
         * Check the level
         * */
        int level = (AvatarUtil.getCurrentUserDetail() != null) ? AvatarUtil.getCurrentUserDetail().getLevel() : 0;
        if ((level < board.getBoardAccessLevel()) && !AvatarUtil.isAdmin())
            return ResultBuilder.buildResult(403, "access deny", null);
        Sort sort = Sort.by(Sort.Direction.DESC, "threadPriority", "threadTime");
        Pageable pageable = PageRequest.of(pageNumber - 1, 10, sort);
        return ResultBuilder.buildSuccessResult(threadRepository.findThreadsByThreadBoard(board, pageable));
    }

    public Result getUserThread(int userId, int pageNumber) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null)
            return ResultBuilder.buildFailResult("user doesn't exist");
        Pageable pageable = PageRequest.of(pageNumber - 1, 10);
        return ResultBuilder.buildSuccessResult(threadRepository.findThreadsByThreadOwner(user, pageable));
    }

    public Result deleteByThreadId(int threadId) {
        Thread thread = threadRepository.findById(threadId).orElse(null);
        if (thread == null)
            return ResultBuilder.buildFailResult("don't exist");
        if (AvatarUtil.getCurrentAuthenticatedUserId() != thread.getThreadOwner().getUserId() && !AvatarUtil.isAdmin())
            return ResultBuilder.buildFailResult("权限不足");
        commentRepository.deleteCommentsByCommentThread(thread);
        threadRepository.deleteById(threadId);
        return ResultBuilder.buildSuccessResult(null);
    }

    public Result updateThread(Map<String, String> data, int id) {

        Thread thread = threadRepository.findById(id).orElse(null);
        if (thread == null)
            return ResultBuilder.buildFailResult("doesn't exist");
        if (thread.getThreadOwner().getUserId() != AvatarUtil.getCurrentAuthenticatedUserId() && !AvatarUtil.isAdmin())
            return ResultBuilder.buildFailResult("access deny");
        if (StringUtils.hasText(data.get("title")))
            thread.setThreadTitle(data.get("title"));
        if (StringUtils.hasText(data.get("content")))
            thread.setThreadContent(data.get("content"));
        if (StringUtils.hasText(data.get("boardId")))
            thread.setThreadBoard(boardRepository.getOne(Integer.parseInt(data.get("boardId"))));
        threadRepository.save(thread);
        return ResultBuilder.buildSuccessResult(thread.getThreadId());
    }

    public Result setTop(int id) {
        Thread thread = threadRepository.findById(id).orElse(null);
        if (thread == null)
            return ResultBuilder.buildFailResult("don't exist");
        if (thread.getThreadPriority() > 0) {
            thread.setThreadPriority(0);
        } else
            thread.setThreadPriority(10);
        threadRepository.save(thread);
        return ResultBuilder.buildSuccessResult("topped");
    }
}
