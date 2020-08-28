package per.lai.forum.result;

public class ResultBuilder {

    public static Result buildResult(int resultCode, String message, Object data) {
        return new Result(resultCode, message, data);
    }
    public static Result buildResult(ResultCode resultCode, String message, Object data) {
        return buildResult(resultCode.code, message, data);
    }
    public static Result buildSuccessResult(Object data) {
        return buildResult(ResultCode.SUCCESS, "success", data);
    }
    public static Result buildFailResult(String message) {
        return buildResult(ResultCode.FAIL, message, null);
    }
}
