package net.codejitsu.buka.service

import net.codejitsu.buka.entity.{Paperback, Author, BookId, Book}
import org.scalatest.{Matchers, FlatSpec}

class BookServiceTest extends FlatSpec with Matchers {
  val bookService = TestComponentRegistry.bookService

  "bookService" should "provide addBook method" in {
    val book = Book(BookId("test-book"), "Test book", List(Author("First", "Author")), Paperback)

    val addResult = bookService.addBook(book)

    addResult.isSuccess should be (true)
    addResult.get should be (book)

    bookService.getBook(book.id) should be (Option(book))
  }
}