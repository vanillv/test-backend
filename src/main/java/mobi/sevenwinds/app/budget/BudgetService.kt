package mobi.sevenwinds.app.budget

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mobi.sevenwinds.app.author.AuthorEntity
import mobi.sevenwinds.app.author.AuthorTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object BudgetService {
    suspend fun addRecord(body: BudgetRecord): BudgetRecord = withContext(Dispatchers.IO) {
        transaction {
            val author = body.author?.id?.let { AuthorEntity.findById(it) }
            val entity = BudgetEntity.new {
                this.year = body.year
                this.month = body.month
                this.amount = body.amount
                this.type = body.type
                author?.let { this.author = it }
            }
            return@transaction entity.toResponse()
        }
    }

    suspend fun getYearStats(param: BudgetYearParam): BudgetYearStatsResponse = withContext(Dispatchers.IO) {
        transaction {
            val query = BudgetTable
                .join(AuthorTable, JoinType.LEFT, BudgetTable.authorId, AuthorTable.id)
                .slice(BudgetTable.columns + AuthorTable.columns)
                .select {
                    BudgetTable.year eq param.year and
                            (param.authorFio?.let { AuthorTable.fio.lowerCase() like "%${it.toLowerCase()}%" } ?: Op.TRUE)
                }
                .limit(param.limit, param.offset)
            val total = BudgetTable.select { BudgetTable.year eq param.year }.count()
            val data = BudgetEntity.wrapRows(query).map { it.toResponse() }
            val sumByType = data.groupBy { it.type.name }.mapValues { it.value.sumOf { v -> v.amount } }
            return@transaction BudgetYearStatsResponse(
                total = total,
                totalByType = sumByType,
                items = data
            )
        }
    }
}