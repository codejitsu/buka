// Copyright (C) 2015, codejitsu.

package net.codejitsu.buka.entity

final case class BookId(id: String)

final case class Book(id: BookId,
                      title: String,
                      authors: List[Author],
                      bookType: BookType,
                      publicationYear: Int,
                      description: Option[String] = None,
                      isbn13: Option[Isbn13] = None,
                      isbn10: Option[Isbn10] = None,
                      edition: Option[Int] = None,
                      numberPages: Option[Int] = None,
                      tags: List[Tag] = List.empty,
                      copies: Int = 1)
