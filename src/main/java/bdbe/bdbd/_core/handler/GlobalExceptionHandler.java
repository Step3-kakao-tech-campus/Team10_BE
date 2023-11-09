package bdbe.bdbd._core.handler;


import bdbe.bdbd._core.utils.ApiUtils;
import bdbe.bdbd._core.exception.*;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiUtils.ApiResult<?>> handleApiException(ApiException e) {
        return new ResponseEntity<>(e.body(), e.getStatus());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiUtils.ApiResult<?>> handleIllegalArgumentException(IllegalArgumentException e) {
        BadRequestError.ErrorCode errorCode = BadRequestError.ErrorCode.WRONG_REQUEST_TRANSMISSION;
        ApiUtils.ApiResult<?> errorResult = ApiUtils.error(
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                String.valueOf(errorCode.getCode()),
                e.getMessage()
        );
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiUtils.ApiResult<?>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String message = String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                e.getName(), e.getValue(), e.getRequiredType().getSimpleName());
        BadRequestError.ErrorCode errorCode = BadRequestError.ErrorCode.WRONG_REQUEST_TRANSMISSION;
        ApiUtils.ApiResult<?> errorResult = ApiUtils.error(
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                String.valueOf(errorCode.getCode()),
                message
        );

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiUtils.ApiResult<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        String errorMessage = errors.entrySet()
                .stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(","));

        BadRequestError.ErrorCode errorCode = BadRequestError.ErrorCode.WRONG_REQUEST_TRANSMISSION;
        ApiUtils.ApiResult<?> errorResult = ApiUtils.error(
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                String.valueOf(errorCode.getCode()),
                errorMessage
        );

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiUtils.ApiResult<?>> handleMissingParams(MissingServletRequestParameterException ex) {
        String message = String.format("The required parameter '%s' of type '%s' is missing", ex.getParameterName(), ex.getParameterType());
        BadRequestError.ErrorCode errorCode = BadRequestError.ErrorCode.MISSING_PART;
        ApiUtils.ApiResult<?> errorResult = ApiUtils.error(
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                String.valueOf(errorCode.getCode()),
                message
        );
        return new ResponseEntity<>(errorResult,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ApiUtils.ApiResult<?>> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        BadRequestError.ErrorCode errorCode = BadRequestError.ErrorCode.MISSING_PART;
        ApiUtils.ApiResult<?> errorResult = ApiUtils.error(
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                String.valueOf(errorCode.getCode()),
                e.getMessage()
        );
        return new ResponseEntity<>(errorResult,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiUtils.ApiResult<?>> unknownServerError(Exception e){
        InternalServerError.ErrorCode errorCode = InternalServerError.ErrorCode.INTERNAL_SERVER_ERROR;
        ApiUtils.ApiResult<?> errorResult = ApiUtils.error(
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                String.valueOf(errorCode.getCode()),
                e.getMessage()
        );
        return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiUtils.ApiResult<?>> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        BadRequestError.ErrorCode errorCode = BadRequestError.ErrorCode.WRONG_REQUEST_TRANSMISSION;
        String message = "The request body is not readable or has an invalid format.";
        ApiUtils.ApiResult<?> errorResult = ApiUtils.error(
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                String.valueOf(errorCode.getCode()),
                message
        );

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AmazonServiceException.class)
    public ResponseEntity<ApiUtils.ApiResult<?>> handleAmazonServiceException(AmazonServiceException ex) {
        String errorMsg = String.format("AmazonServiceException: %s", ex.getErrorMessage());
        InternalServerError.ErrorCode errorCode = InternalServerError.ErrorCode.INTERNAL_SERVER_ERROR;
        ApiUtils.ApiResult<?> errorResult = ApiUtils.error(
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                String.valueOf(errorCode.getCode()),
                errorMsg
        );

        return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SdkClientException.class)
    public ResponseEntity<ApiUtils.ApiResult<?>> handleSdkClientException(SdkClientException ex) {
        String errorMsg = String.format("SdkClientException: %s", ex.getMessage());
        InternalServerError.ErrorCode errorCode = InternalServerError.ErrorCode.INTERNAL_SERVER_ERROR;
        ApiUtils.ApiResult<?> errorResult = ApiUtils.error(
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                String.valueOf(errorCode.getCode()),
                errorMsg
        );
        return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
