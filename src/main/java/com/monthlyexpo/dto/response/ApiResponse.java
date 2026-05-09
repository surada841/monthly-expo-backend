package com.monthlyexpo.dto.response;
import lombok.*; import java.time.LocalDateTime; import java.util.List;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ApiResponse<T>{ private LocalDateTime timestamp; private boolean success; private String message; private T data; private List<String> errors; private int status; private String path; public static <T> ApiResponse<T> ok(String msg,T data){ return ApiResponse.<T>builder().timestamp(LocalDateTime.now()).success(true).message(msg).data(data).status(200).build(); } public static <T> ApiResponse<T> fail(String msg,List<String> errors,int status){ return ApiResponse.<T>builder().timestamp(LocalDateTime.now()).success(false).message(msg).errors(errors).status(status).build(); }}
