// Copyright (C) 2015, codejitsu.

package net.codejitsu.buka.dao

import net.codejitsu.buka.entity.{Book, BookId}

import scala.util.Try

trait BookDaoComponent {
  trait BookDao {
    def read(id: BookId): Option[Book]
    def store(book: Book): Try[Book]
  }

  class BookDaoException(msg: String) extends Exception(msg)

  case class BookAlreadyExistsDaoException(id: BookId, title: String) extends BookDaoException("book already exists")
}
