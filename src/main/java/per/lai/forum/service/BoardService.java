package per.lai.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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

    public Result getBoardListByLevel(int userId) {
        int level = 1;

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
}
