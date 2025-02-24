// package vn.hoidanit.jobhunter.response;

// import org.springframework.core.MethodParameter;
// import org.springframework.http.MediaType;
// import org.springframework.http.server.ServerHttpRequest;
// import org.springframework.http.server.ServerHttpResponse;
// import org.springframework.http.server.ServletServerHttpResponse;
// import org.springframework.lang.Nullable;
// import org.springframework.web.bind.annotation.ControllerAdvice;
// import
// org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

// import jakarta.servlet.http.HttpServletResponse;
// import vn.hoidanit.jobhunter.exception.IdInvalidException;

// @ControllerAdvice
// public class FormatRestResponse implements ResponseBodyAdvice<Object> {

// @Override
// // format response always
// public boolean supports(MethodParameter returnType, Class converterType) {
// return true;
// }

// @Override
// @Nullable
// public Object beforeBodyWrite(@Nullable Object body, MethodParameter
// returnType, MediaType selectedContentType,
// Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse
// response) {
// // HttpServletResponse servletResponse = ((ServletServerHttpResponse)
// // response).getServletResponse();
// // int statusCode = servletResponse.getStatus();

// // RestResponse<Object> res = new RestResponse<>();
// // res.setStatusCode(statusCode);

// // if (body instanceof String) {
// // return body;
// // }

// // // case error
// // if (statusCode >= 400) {
// // return body;

// // } else {
// // // case success
// // res.setData(body);
// // res.setMessage("Api called successfully");
// // }
// // return res;
// return;
// }

// }