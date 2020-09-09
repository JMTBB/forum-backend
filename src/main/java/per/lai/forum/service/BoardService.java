package per.lai.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import per.lai.forum.pojo.*;
import per.lai.forum.pojo.Thread;
import per.lai.forum.pojo.dto.ReceivedBoard;
import per.lai.forum.repository.*;
import per.lai.forum.result.Result;
import per.lai.forum.result.ResultBuilder;
import per.lai.forum.result.ResultCode;
import per.lai.forum.security.UserDetailsImpl;
import per.lai.forum.utils.AvatarUtil;

import java.util.List;


@Service
public class BoardService {


    private BoardRepository boardRepository;
    @Autowired
    public void setBoardRepository(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private RoleRepository roleRepository;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    private ThreadRepository threadRepository;

    @Autowired
    public void setThreadRepository(ThreadRepository threadRepository) {
        this.threadRepository = threadRepository;
    }
    private CommentRepository commentRepository;

    @Autowired
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Result getBoardListByLevel(int userId) {
        int level = 1;

        if (AvatarUtil.isAdmin())
            return ResultBuilder.buildSuccessResult(boardRepository.findAll());
        /*
        * User id equals 0 if user didn't login
        * */
        if (userId != 0){
            if (AvatarUtil.getCurrentAuthenticatedUserId() != userId)  {
                return ResultBuilder.buildResult(ResultCode.UNAUTHORIZED, "Id unauthorized", userId);
            }else {
                UserDetailsImpl userDetails = AvatarUtil.getCurrentUserDetail();
                if (userDetails != null)
                    level = userDetails.getLevel();
            }
        }
        return  ResultBuilder.buildSuccessResult(boardRepository.findBoardsByBoardAccessLevelIsLessThanEqual(level));
    }


    public Result getAllBoard() {
        return ResultBuilder.buildSuccessResult(boardRepository.findAll());
    }

    public Result getBoardSeq(int id) {
        return ResultBuilder.buildSuccessResult(boardRepository.countBoardsByBoardIdLessThan(id));
    }

    public Result getByManager(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user==null)
            return ResultBuilder.buildFailResult("user don't exist");
        return ResultBuilder.buildSuccessResult(boardRepository.findBoardsByBoardManager(user));
    }

    public Result update(Board receivedBoard) {
        Board board = boardRepository.findById(receivedBoard.getBoardId()).orElse(null);
        if(board == null)
            return ResultBuilder.buildFailResult("don't exist");
        board.setBoardName(receivedBoard.getBoardName());
        board.setBoardDescription(receivedBoard.getBoardDescription());
        board.setBoardAccessLevel(receivedBoard.getBoardAccessLevel());
        if(AvatarUtil.isGlobalManager()) {
            User user = userRepository.getOne(receivedBoard.getBoardManager().getUserId());
            board.setBoardManager(user);
        }
        boardRepository.save(board);
        return ResultBuilder.buildSuccessResult(board.getBoardId());
    }

    public Result addBoard(ReceivedBoard receivedBoard) {
        User user = userRepository.findById(receivedBoard.getManager()).orElse(null);
        if (user == null)
            return ResultBuilder.buildFailResult("user don't exist");
        Role role = roleRepository.getOne(2);
        user.getRoles().add(role);
        Board board = new Board(null, receivedBoard.getName(), receivedBoard.getDescription(), receivedBoard.getLevel(), user);
        Board save = boardRepository.save(board);
        return ResultBuilder.buildSuccessResult(save.getBoardId());

    }
    public Result deleteBoard(int id) {
        Board board = boardRepository.getOne(id);
        List<Thread> threadsByThreadBoard = threadRepository.findThreadsByThreadBoard(board);
        if (threadsByThreadBoard.size() != 0) {
            for (Thread thread : threadsByThreadBoard) {
                List<Comment> comments = commentRepository.getCommentsByCommentThread(thread);
                if (comments.size()!=0)
                    for (Comment comment : comments) {
                        commentRepository.delete(comment);
                    }
                threadRepository.delete(thread);
            }
        }
        boardRepository.delete(board);
        return ResultBuilder.buildSuccessResult("deleted");
    }
}
