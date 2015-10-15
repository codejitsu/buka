// Copyright (C) 2015, codejitsu.

package net.codejitsu.buka.entity

import java.util.Date

sealed trait OwnershipStatus

final case object AtHome
final case class Borrowed(name: String, date: Date)
final case class Donated(name: String, date: Date)
final case class Lost(date: Date)
