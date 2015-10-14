package net.codejitsu.buka.entity

final case class BookId(id: String)

final case class Book(id: BookId,
                      title: String,
                      authors: List[Author],
                      bookType: BookType,
                      description: Option[String],
                      isbn13: Option[Isbn13],
                      isbn10: Option[Isbn10],
                      edition: Option[Int],
                      numberPages: Option[Int],
                      publicationYear: Option[Int],
                      tags: List[Tag])
