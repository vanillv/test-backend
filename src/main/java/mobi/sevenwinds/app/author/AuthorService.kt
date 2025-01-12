package mobi.sevenwinds.app.author

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
object AuthorService {
    suspend fun addAuthor(fio: String): AuthorResponse = withContext(Dispatchers.IO) {
        transaction {
            val author = AuthorEntity.new {
                this.fio = fio
                this.createdAt = DateTime.now()
            }
            return@transaction author.toResponse()
        }
    }
    suspend fun getAuthorById(id: Int): AuthorResponse? = withContext(Dispatchers.IO) {
        transaction {
            val author = AuthorEntity.findById(id)
            return@transaction author?.toResponse()
        }
    }
}


