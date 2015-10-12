package net.codejitsu.buka.entity

final case class Book(title: String,
                      authors: List[Author],
                      bookType: BookType,
                      edition: Int,
                      description: Option[String],
                      isbn13: Option[Isbn13],
                      isbn10: Option[Isbn10],
                      numberPages: Int)
