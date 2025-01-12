package mobi.sevenwinds.app.author
import com.papsign.ktor.openapigen.annotations.parameters.PathParam
import com.papsign.ktor.openapigen.annotations.parameters.QueryParam
import com.papsign.ktor.openapigen.route.info
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.get
import com.papsign.ktor.openapigen.route.path.normal.post
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import io.ktor.http.*
import mobi.sevenwinds.app.author.error.HttpStatusCodeException

fun NormalOpenAPIRoute.author() {
    route("/author") {
        route("/add").post<Unit, AuthorResponse, AddAuthorRequest>(info("Добавить автора")) { _, body ->
            val response = AuthorService.addAuthor(body.fio)
            respond(response)
        }
        route("/{id}").get<GetAuthorByIdRequest, AuthorResponse>(info("Получить автора по ID")) { param ->
            val response = AuthorService.getAuthorById(param.id)
            if (response != null) {
                respond(response)
            } else throw HttpStatusCodeException(HttpStatusCode.NotFound, "Автор не найден")

        }
    }
}
data class AddAuthorRequest(
    @QueryParam("ФИО автора") val fio: String
)
data class GetAuthorByIdRequest(
    @PathParam("ID автора") val id: Int
)

data class AuthorResponse(
    val id: Int,
    val fio: String,
    val createdAt: String
)

