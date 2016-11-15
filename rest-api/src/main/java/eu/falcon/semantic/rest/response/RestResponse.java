package eu.falcon.semantic.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRawValue;
import java.io.Serializable;

/**
 * @param <C>
 * @param <R>
 */
public class RestResponse<C extends Enum<? extends ResponseCode>, R> implements Serializable {

    private C code;
    private String message;
    private R returnobject;

    public RestResponse(C code, String message, R returnobject) {
        this.message = message;
        this.code = code;
        this.returnobject = returnobject;
    }
    
        public RestResponse(C code, String message) {
        this.message = message;
        this.code = code;
    }

    public C getCode() {
        return code;
    }

    public void setCode(C code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    @JsonInclude(Include.NON_NULL)
//    @JsonProperty(value = "jsondata")
//    @JsonRawValue
    public R getReturnobject() {
        return returnobject;
    }

//    @JsonInclude(Include.NON_NULL)
//    @JsonProperty(value = "jsondata")
//    @JsonRawValue
    public void setReturnobject(R returnobject) {
        this.returnobject = returnobject;
    }

}
