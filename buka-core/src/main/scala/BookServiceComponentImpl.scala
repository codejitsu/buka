// Copyright (C) 2015, codejitsu.

package net.codejitsu.buka.service

import net.codejitsu.buka.dao.BookDaoComponent
import net.codejitsu.buka.entity.{Book, BookId}

import scala.util.Try

trait BookServiceComponentImpl {
  this: BookDaoComponent =>

  val bookDao: BookDao

  class BookServiceImpl {
    def addBook(book: Book): Try[Book] = bookDao.store(book)

    def getBook(id: BookId): Option[Book] = bookDao.read(id)
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
