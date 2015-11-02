package net.codejitsu.buka.service

import java.util.UUID

import net.codejitsu.buka.entity.{Paperback, Author, BookId, Book}
import org.scalatest.{Matchers, FlatSpec}

import scala.util.{Failure, Success}

import TestComponentRegistry._

class BookServiceTest extends FlatSpec with Matchers {
  val bookService = TestComponentRegistry.bookService
  val bookDao = TestComponentRegistry.bookDao

  "bookService" should "provide addBook method" in {
    val book = Book(BookId(UUID.randomUUID().toString), "Test book", List(Author("First", "Author")), Paperback, 2015)

    val addResult = bookService.addBook(book)

    addResult.isSuccess should be (true)
    addResult.get._1 should be (book)
    addResult.get._2 should be (empty)

    bookService.getBook(book.id) should be (Option(book))
  }

  "bookService" should "validate book's title min length on addition" in {
    val book = Book(BookId(UUID.randomUUID().toString), "", List(Author("First", "Author")), Paperback, 2015)

    val addResult = bookService.addBook(book)

    addResult.isSuccess should be (false)

    addResult match {
      case Success(_) => fail("This call should produce an error.")

      case Failure(BookServiceValidationException(violations)) =>
        violations.size should be (1)

        val titleViolation = violations.head

        titleViolation.msg should be ("Title can't be empty")
        titleViolation.constraint should be ("book.title.length > 0")

      case _ => fail("This call should produce a BookServiceValidationException.")
    }

    bookService.getBook(book.id) should be (None)
  }

  "bookService" should "validate book's title max length on addition" in {
    val book = Book(BookId(UUID.randomUUID().toString), "T" * 1000, List(Author("First", "Author")), Paperback, 2015)

    val addResult = bookService.addBook(book)

    addResult.isSuccess should be (false)

    addResult match {
      case Success(_) => fail("This call should produce an error.")

      case Failure(BookServiceValidationException(violations)) =>
        violations.size should be (1)

        val titleViolation = violations.head

        titleViolation.msg should be ("Title is too long")
        titleViolation.constraint should be ("book.title.length < 255")

      case _ => fail("This call should produce a BookServiceValidationException.")
    }

    bookService.getBook(book.id) should be (None)
  }

  "bookService" should "accept title length up to 255 characters" in {
    val book = Book(BookId(UUID.randomUUID().toString), "T" * 255, List(Author("First", "Author")), Paperback, 2015)

    val addResult = bookService.addBook(book)

    addResult.isSuccess should be (true)
    addResult.get._1 should be (book)
    addResult.get._2 should be (empty)

    bookService.getBook(book.id) should be (Option(book))
  }

  "bookService" should "validate book's authors list min length on addition" in {
    val book = Book(BookId(UUID.randomUUID().toString), "my book", List(), Paperback, 2015)

    val addResult = bookService.addBook(book)

    addResult.isSuccess should be (false)

    addResult match {
      case Success(_) => fail("This call should produce an error.")

      case Failure(BookServiceValidationException(violations)) =>
        violations.size should be (1)

        val titleViolation = violations.head

        titleViolation.msg should be ("Authors list can't be empty")
        titleViolation.constraint should be ("book.authors.length > 0")

      case _ => fail("This call should produce a BookServiceValidationException.")
    }

    bookService.getBook(book.id) should be (None)
  }

  "bookService" should "validate book's authors max length on addition" in {
    val authors = (1 to 25) map (_ => Author(UUID.randomUUID().toString, UUID.randomUUID().toString))

    val book = Book(BookId(UUID.randomUUID().toString), "my book", authors.toList, Paperback, 2015)

    val addResult = bookService.addBook(book)

    addResult.isSuccess should be (false)

    addResult match {
      case Success(_) => fail("This call should produce an error.")

      case Failure(BookServiceValidationException(violations)) =>
        violations.size should be (1)

        val titleViolation = violations.head

        titleViolation.msg should be ("Authors list is too long")
        titleViolation.constraint should be ("book.authors.length <= 20")

      case _ => fail("This call should produce a BookServiceValidationException.")
    }

    bookService.getBook(book.id) should be (None)
  }

  "bookService" should "accept authors length up to 20 authors" in {
    val authors = (1 to 20) map (_ => Author(UUID.randomUUID().toString, UUID.randomUUID().toString))

    val book = Book(BookId(UUID.randomUUID().toString), "my book", authors.toList, Paperback, 2015)

    val addResult = bookService.addBook(book)

    addResult.isSuccess should be (true)
    addResult.get._1 should be (book)
    addResult.get._2 should be (empty)

    bookService.getBook(book.id) should be (Option(book))
  }

  "bookService" should "validate book's authors uniqueness on addition" in {
    val authors = List(Author("First", "Second"), Author("First", "Second"))

    val book = Book(BookId(UUID.randomUUID().toString), "my book", authors, Paperback, 2015)

    val addResult = bookService.addBook(book)

    addResult.isSuccess should be (false)

    addResult match {
      case Success(_) => fail("This call should produce an error.")

      case Failure(BookServiceValidationException(violations)) =>
        violations.size should be (1)

        val titleViolation = violations.head

        titleViolation.msg should be ("There are some not unique authors in the list")
        titleViolation.constraint should be ("Each author has to be unique in the list")

      case _ => fail("This call should produce a BookServiceValidationException.")
    }

    bookService.getBook(book.id) should be (None)
  }

  "bookService" should "validate book's authors uniqueness on addition (case insensitive)" in {
    val authors = List(Author("First", "Second"), Author("first", "second"))

    val book = Book(BookId(UUID.randomUUID().toString), "my book", authors, Paperback, 2015)

    val addResult = bookService.addBook(book)

    addResult.isSuccess should be (false)

    addResult match {
      case Success(_) => fail("This call should produce an error.")

      case Failure(BookServiceValidationException(violations)) =>
        violations.size should be (1)

        val titleViolation = violations.head

        titleViolation.msg should be ("There are some not unique authors in the list")
        titleViolation.constraint should be ("Each author has to be unique in the list")

      case _ => fail("This call should produce a BookServiceValidationException.")
    }

    bookService.getBook(book.id) should be (None)
  }

  "bookService" should "check if book id is unique" in {
    val authors = List(Author("First", "Second"))

    val book = Book(BookId(UUID.randomUUID().toString), "my book", authors, Paperback, 2015)

    val addResult = bookService.addBook(book)

    addResult.isSuccess should be (true)

    val addAgainResult = bookService.addBook(book)

    addAgainResult.isSuccess should be (false)

    addAgainResult match {
      case Success(_) => fail("This call should produce an error.")

      case Failure(BookAlreadyExistsDaoException(bookId, bookTitle)) =>
        bookId should be (book.id)
        bookTitle should be (book.title)

      case _ => fail("This call should produce a BookAlreadyExistsDaoException.")
    }

    bookService.getBook(book.id) should be (Option(book))
  }

  "bookService" should "check if book title + book type + pub year is unique" in {
    val authors = List(Author("First", "Second"))

    val firstId = UUID.randomUUID().toString

    val book = Book(BookId(firstId), "my book", authors, Paperback, 2015)

    val addResult = bookService.addBook(book)

    addResult.isSuccess should be (true)

    val addAgainResult = bookService.addBook(book.copy(id = BookId(UUID.randomUUID().toString)))

    addAgainResult.isSuccess should be (true)

    addAgainResult.get._2 should contain ("Possible duplicate: my book (" + firstId +  ")")

    bookService.getBook(book.id).get.copies should be (2)
  }

  //TODO book title + book type + pub year is unique
  //TODO test update field (all fields)
  //TODO test remove field (all fields)
  //TODO consider the same return type for all service operations (Try or Option, Future)
  //TODO start/stop/pause book reading
  //TODO get list of books which I am currently reading
  //TODO calculate velocity per book or general reading velocity
}