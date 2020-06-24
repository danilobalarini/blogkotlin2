package br.com.dblogic.blogkotlin.recaptcha

class ReCaptchaInvalidException : RuntimeException {

    constructor() : super() {}
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}
    constructor(message: String?) : super(message) {}
    constructor(cause: Throwable?) : super(cause) {}

}