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
    val book = Book(BookId(UUID.randomUUID().toString), "Test book", List(Author("First", "Author")), Paperback)

    val addResult = bookService.addBook(book)

    addResult.isSuccess should be (true)
    addResult.get should be (book)

    bookService.getBook(book.id) should be (Option(book))
  }

  "bookService" should "validate book's title min length on addition" in {
    val book = Book(BookId(UUID.randomUUID().toString), "", List(Author("First", "Author")), Paperback)

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
    val book = Book(BookId(UUID.randomUUID().toString), "T" * 1000, List(Author("First", "Author")), Paperback)

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
    val book = Book(BookId(UUID.randomUUID().toString), "T" * 255, List(Author("First", "Author")), Paperback)

    val addResult = bookService.addBook(book)

    addResult.isSuccess should be (true)
    addResult.get should be (book)

    bookService.getBook(book.id) should be (Option(book))
  }

  "bookService" should "validate book's authors list min length on addition" in {
    val book = Book(BookId(UUID.randomUUID().toString), "my book", List(), Paperback)

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

    val book = Book(BookId(UUID.randomUUID().toString), "my book", authors.toList, Paperback)

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

    val book = Book(BookId(UUID.randomUUID().toString), "my book", authors.toList, Paperback)

    val addResult = bookService.addBook(book)

    addResult.isSuccess should be (true)
    addResult.get should be (book)

    bookService.getBook(book.id) should be (Option(book))
  }

  //TODO each author in the list has to be unique
  //TODO book id has to be unique
  //TODO book title + book type is unique
  //TODO test update field (all fields)
  //TODO test remove field (all fields)
  //TODO consider the same return type for all service operations (Try or Option, Future)
}