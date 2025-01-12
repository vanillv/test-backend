package mobi.sevenwinds.app.author.error

import io.ktor.http.*

class HttpStatusCodeException(val status: HttpStatusCode, override val message: String) : RuntimeException(message)
