// Copyright (C) 2015, codejitsu.

package net.codejitsu.buka.entity

sealed trait Isbn

case class Isbn13(id: String) extends Isbn

case class Isbn10(id: String) extends Isbn
