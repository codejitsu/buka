// Copyright (C) 2015, codejitsu.

package net.codejitsu.buka.service

import net.codejitsu.buka.dao.BookDaoComponent
import net.codejitsu.buka.entity.{BookType, Author, Book, BookId}
import net.codejitsu.buka.service.validation.ConstraintViolation

import scala.util.Try
import scalaz.Scalaz._
import scalaz.{Failure, Success, ValidationNel}

trait BookServiceComponentImpl {
  this: BookDaoComponent =>

  val bookDao: BookDao

  class BookServiceImpl {
    //TODO make an amazon call to obtain book information for given isbn
    def addBook(book: Book): Try[(Book, List[String])] = validate(book) match {
      case Success(validatedBook) => bookDao.store(validatedBook).map { book =>
        (book, Nil)
      }
      case Failure(violations) => scala.util.Failure(new BookServiceValidationException(violations.toList))
    }

    def getBook(id: BookId): Option[Book] = bookDao.read(id)

    //TODO move all validation constraints into config
    private def validateTitle(title: String): ValidationNel[ConstraintViolation, String] = if (title.trim.isEmpty) {
      ConstraintViolation("Title can't be empty", "book.title.length > 0").failureNel
    } else if (title.trim.length > 255) {
      ConstraintViolation("Title is too long", "book.title.length < 255").failureNel
    } else {
      title.successNel
    }

    private def validateAuthors(authors: List[Author]): ValidationNel[ConstraintViolation, List[Author]] = if (authors.isEmpty) {
      ConstraintViolation("Authors list can't be empty", "book.authors.length > 0").failureNel
    } else if (authors.size > 20) {
      ConstraintViolation("Authors list is too long", "book.authors.length <= 20").failureNel
    } else {
      authors.successNel
    }

    private def validateAuthorUniqueness(authors: List[Author]): ValidationNel[ConstraintViolation, List[Author]] = {
      val authorSet = authors.map { author =>
        author.copy(firstName = author.firstName.toLowerCase(), lastName = author.lastName.toLowerCase())
      }.toSet

      if (authors.size != authorSet.size) {
        ConstraintViolation("There are some not unique authors in the list",
          "Each author has to be unique in the list").failureNel
      } else {
        authors.successNel
      }
    }

    private def validateBookId(bookId: BookId): ValidationNel[ConstraintViolation, BookId] = {
      val lookUp = bookDao.read(bookId)

      lookUp match {
        case Some(_) =>
          ConstraintViolation("A book with same id found",
            "Each book id should be unique").failureNel

        case None => bookId.successNel
      }
    }

    private def validateTitleAndType(title: String, bookType: BookType): ValidationNel[ConstraintViolation, (String, BookType)] = {
      val lookUp = bookDao.searchBy(title, bookType)

      lookUp match {
        case Some(_) =>
          ConstraintViolation("A book with same title and type found",
            "Each book should be unique").failureNel

        case None => (title, bookType).successNel
      }
    }

    private def validate(book: Book): ValidationNel[ConstraintViolation, Book] = {

      (validateBookId(book.id) |@|
       validateTitle(book.title) |@|
       validateAuthors(book.authors) |@|
       validateAuthorUniqueness(book.authors) |@|
       validateTitleAndType(book.title, book.bookType)) { case _ => book }
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
