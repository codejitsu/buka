package net.codejitsu.buka.entity

sealed trait BookType

trait Ebook extends BookType

final case object Paperback extends BookType

final case object KindleBook extends Ebook

final case object EpubBook extends Ebook

final case object PdfBook extends Ebook
