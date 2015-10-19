// Copyright (C) 2015, codejitsu.

package net.codejitsu.buka.service

import net.codejitsu.buka.dao.BookDaoComponent
import net.codejitsu.buka.entity.{Book, BookId}
import net.codejitsu.buka.service.validation.ConstraintViolation

import scala.util.Try
import scalaz.Scalaz._
import scalaz.{Failure, Success, ValidationNel}

trait BookServiceComponentImpl {
  this: BookDaoComponent =>

  val bookDao: BookDao

  class BookServiceImpl {
    def addBook(book: Book): Try[Book] = validate(book) match {
      case Success(validatedBook) => bookDao.store(validatedBook)
      case Failure(nelViolations) => scala.util.Failure(new BookServiceValidationException(nelViolations.toList))
    }

    def getBook(id: BookId): Option[Book] = bookDao.read(id)

    private def validate(book: Book): ValidationNel[ConstraintViolation, Book] = {
      //TODO move all validation constraints into config
      def validateTitle(): ValidationNel[ConstraintViolation, Book] = if (book.title.trim.isEmpty) {
        ConstraintViolation("Title can't be empty", "book.title.length > 0").failureNel
      } else if (book.title.trim.length > 255) {
        ConstraintViolation("Title is too long", "book.title.length < 255").failureNel
      } else {
        book.successNel
      }

      validateTitle()
    }
  }

  abstract sealed class BookServiceException extends Exception

  case class BookServiceValidationException(violations: List[ConstraintViolation]) extends BookServiceException
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
