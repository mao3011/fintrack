package exception;

public class FintrackException extends RuntimeException {
    private final ErrorCode errorCode;

    public FintrackException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public FintrackException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public FintrackException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public enum ErrorCode {
        AUTHENTICATION_FAILED("認証に失敗しました"),
        INVALID_INPUT("入力値が無効です"),
        DATABASE_ERROR("データベースエラーが発生しました"),
        INTERNAL_SERVER_ERROR("内部サーバーエラーが発生しました");

        private final String message;

        ErrorCode(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}