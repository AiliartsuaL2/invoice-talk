package com.hocheoltech.invoicetalk.global.error

enum class ErrorCode(
    val statusCode: Int,
    val message: String,
) {
    // 공통
    INVALID_INPUT_VALUE(400, "유효하지 않은 파라미터입니다."),
    INVALID_TYPE_VALUE(400, "유효하지 않은 파라미터 타입입니다."),
    UNAUTHORIZED(401, "유효한 인증 자격 증명이 없습니다."),
    EXPIRED_TOKEN(401, "토큰이 만료되었습니다."),
    FORBIDDEN(403, "권한이 없습니다."),
    NOT_FOUND_RESOURCE(404, "존재하지 않는 URI 입니다."),
    METHOD_NOT_ALLOWED(405, "허용되지 않은 메소드입니다."),
    INTERNAL_SERVER_ERROR(500, "죄송합니다. 오류가 발생했습니다."),

    // 회원
    DUPLICATE_USERNAME(400, "이미 존재하는 ID 입니다."),
    NOT_EXISTS_USER(400, "존재하지 않는 사용자입니다."),

    // 송장
    NOT_EXISTS_INVOICE(400, "존재하지 않는 송장입니다."),
    ALREADY_UPDATED_STATUS(400, "이미 상태 변경이 되어있는 송장입니다."),
}
