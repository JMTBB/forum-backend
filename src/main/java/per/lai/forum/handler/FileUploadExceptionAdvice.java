package per.lai.forum.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import per.lai.forum.result.Result;
import per.lai.forum.result.ResultBuilder;
@ControllerAdvice
public class FileUploadExceptionAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Result> handleUploadMaxSizeExceededException() {
        return ResponseEntity.badRequest().body(ResultBuilder.buildResult(HttpStatus.BAD_REQUEST.value(),"file too large", null));
    }
}
