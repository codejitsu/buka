package net.codejitsu.buka.service

import net.codejitsu.buka.dao.BookDaoComponent
import net.codejitsu.buka.entity.{Book, BookId}

trait BookServiceComponentImpl {
  this: BookDaoComponent =>

  val bookDao: BookDao

  class BookServiceImpl {
    def getBook(id: BookId): Option[Book] = bookDao.getBook(id)
  }
}

/*
// Usage:

trait BookDaoComponentImpl extends BookDaoComponent {
  class BookDaoImpl extends BookDao {
    def getBook(id: BookId): Option[Book] = Option(Book(...))
  }
}

object ComponentRegistry extends BookServiceComponentImpl with BookDaoComponentImpl {
  val bookDao = new BookDaoImpl
  val bookService = new BookServiceImpl
}

val bookService = ComponentRegistry.bookService
*/