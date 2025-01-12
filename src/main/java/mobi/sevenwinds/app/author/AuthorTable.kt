package mobi.sevenwinds.app.author

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object AuthorTable : IntIdTable("author") {
    val fio = varchar("fio", 255)
    val createdAt = datetime("created_at")
}
class AuthorEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AuthorEntity>(AuthorTable)

    var fio by AuthorTable.fio
    var createdAt by AuthorTable.createdAt
    fun toResponse(): AuthorResponse {
        return AuthorResponse(
            id = this.id.value,
            fio = this.fio,
            createdAt = this.createdAt.toString()
        )
    }
}
