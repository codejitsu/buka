package net.codejitsu.buka.entity

import java.util.Date

sealed trait ReadStatus

final case class Read(date: Date)
final case class ReRead(date: Date)
final case object Unread
