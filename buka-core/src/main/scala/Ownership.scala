// Copyright (C) 2015, codejitsu.

package net.codejitsu.buka.entity

final case class Ownership(user: User,
                           book: Book,
                           status: OwnershipStatus,
                           readStatus: ReadStatus)
