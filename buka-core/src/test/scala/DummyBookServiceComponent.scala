package net.codejitsu.buka.service

import net.codejitsu.buka.dao.BookDaoComponent
import net.codejitsu.buka.entity.{BookId, Book}

import scala.util.{Failure, Success, Try}

trait TestBookDaoComponentImpl extends BookDaoComponent {
  class InMemoryBookDaoImpl extends BookDao {
    val books = collection.mutable.HashMap[BookId, Book]()

    def read(id: BookId): Option[Book] = books.get(id)

    def store(book: Book): Try[Book] = {
      books.put(book.id, book)

      books.get(book.id) match {
        case Some(b) => Success(b)
        case None => Failure(new BookDaoException("Error by store book"))
      }
    }
  }
}

object TestComponentRegistry extends BookServiceComponentImpl with TestBookDaoComponentImpl {
  val bookDao = new InMemoryBookDaoImpl
  val bookService = new BookServiceImpl
}