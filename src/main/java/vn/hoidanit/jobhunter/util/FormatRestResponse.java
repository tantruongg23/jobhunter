package vn.hoidanit.jobhunter.util;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletResponse;
import vn.hoidanit.jobhunter.domain.RestResponse;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(MethodParameter returnType, Class converterType) {
    // Ghi đè phản hồi hay không
    return true;
  }

  @Override
  public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType,
      Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
    HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
    int statusCode = servletResponse.getStatus();
    RestResponse<Object> restResponse = new RestResponse<>();
    restResponse.setStatusCode(statusCode);

    if (statusCode >= 400) {
      return body; // Trả về lỗi như cũ
    } else {

      restResponse.setData(body);
      restResponse.setMessage("Successfully!");
    }

    return restResponse;
  }

}
