package io.github.dingdangdog.entity;

/**
 * 返回值
 *
 * @author DingDangDog
 * @since 2023/1/11
 */
public class ResultDTO {
    private Integer code;
    private String message;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResultDTO{" +
                "code='" + code + '\'' +
                "message='" + message + '\'' +
                '}';
    }
}
