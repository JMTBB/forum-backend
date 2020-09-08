package per.lai.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import per.lai.forum.pojo.Board;
import per.lai.forum.pojo.User;
import per.lai.forum.pojo.dto.ReceivedBoard;
import per.lai.forum.repository.BoardRepository;
import per.lai.forum.repository.UserRepository;
import per.lai.forum.result.Result;
import per.lai.forum.result.ResultBuilder;
import per.lai.forum.result.ResultCode;
import per.lai.forum.security.UserDetailsImpl;
import per.lai.forum.utils.AvatarUtil;



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
        boardRepository.save(board);
        return ResultBuilder.buildSuccessResult(board.getBoardId());
    }
}
