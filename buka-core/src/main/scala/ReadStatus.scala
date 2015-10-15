// Copyright (C) 2015, codejitsu.

package net.codejitsu.buka.entity

import java.util.Date

sealed trait ReadStatus

final case class Reading(since: Date)
final case class Read(date: Date)
final case class ReRead(date: Date)
final case object Unread
