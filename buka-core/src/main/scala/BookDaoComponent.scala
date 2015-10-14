package net.codejitsu.buka.dao

import net.codejitsu.buka.entity.{Book, BookId}

trait BookDaoComponent {
  trait BookDao {
    def getBook(id: BookId): Option[Book]
  }
}
