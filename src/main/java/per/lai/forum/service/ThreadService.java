package per.lai.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.lai.forum.pojo.Board;
import per.lai.forum.pojo.Thread;
import per.lai.forum.pojo.User;
import per.lai.forum.pojo.dto.ReceivedThread;
import per.lai.forum.repository.BoardRepository;
import per.lai.forum.repository.ThreadRepository;
import per.lai.forum.repository.UserRepository;
import per.lai.forum.result.Result;
import per.lai.forum.result.ResultBuilder;
import per.lai.forum.utils.AvatarUtil;

import java.util.Date;
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

    public Result addThread(ReceivedThread receivedThread){
        if (AvatarUtil.getCurrentAuthenticatedUserId() != receivedThread.getUserId())
            return ResultBuilder.buildFailResult("user doesn't match");
        User user = userRepository.getOne(receivedThread.getUserId());
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
            return ResultBuilder.buildResult(404,"thread doesn't exist", null);
        Optional<Thread> optional = threadRepository.findById(id);
        Thread thread = optional.orElse(null);
        /*
        * Check the level
        * */
        int level = (AvatarUtil.getCurrentUserDetail() != null) ? AvatarUtil.getCurrentUserDetail().getLevel() : 0;
        assert thread != null;
        if (level < thread.getThreadBoard().getBoardAccessLevel()) {
            return ResultBuilder.buildFailResult("Level too low/Doesn't login");
        }
        return ResultBuilder.buildSuccessResult(thread);
    }

    public Result getByBoardId(int boardId) {
        Board board = null;
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        if(optionalBoard.isPresent())
            board = optionalBoard.get();
        else
            return ResultBuilder.buildFailResult("board doesn't exist");
        /*
         * Check the level
         * */
        int level = (AvatarUtil.getCurrentUserDetail() != null) ? AvatarUtil.getCurrentUserDetail().getLevel() : 0;
        if (level < board.getBoardAccessLevel())
            return ResultBuilder.buildResult(403, "access deny", null);
        return ResultBuilder.buildSuccessResult(threadRepository.findThreadsByThreadBoard(board));
    }
}
