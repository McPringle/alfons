/*
 * Alfons - Make Community Management Great Again
 * Copyright (C) Marcus Fihlon and the individual contributors to Alfons.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package ch.css.community.data.service;

import ch.css.community.alfons.data.db.tables.records.ConferenceRecord;
import ch.css.community.data.entity.Conference;
import ch.css.community.data.service.getter.DSLContextGetter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.impl.DSL;
import org.jooq.impl.UpdatableRecordImpl;

import java.util.Optional;
import java.util.stream.Stream;

import static ch.css.community.alfons.data.db.tables.Conference.CONFERENCE;

interface ConferenceService extends DSLContextGetter {

    default ConferenceRecord newConference() {
        final var conference = dsl().newRecord(CONFERENCE);
        conference.setName("");
        conference.setWebsite("");
        return conference;
    }

    default Stream<Conference> findConferences(final int offset, final int limit, @Nullable final String filter) {
        final var filterValue = filter == null || filter.isBlank() ? null : "%" + filter.trim() + "%";
        return dsl().select(CONFERENCE.asterisk())
                .from(CONFERENCE)
                .where(filterValue == null ? DSL.noCondition() : CONFERENCE.NAME.like(filterValue))
                .orderBy(CONFERENCE.NAME)
                .offset(offset)
                .limit(limit)
                .fetchInto(Conference.class)
                .stream();
    }

    default Optional<ConferenceRecord> getConferenceRecord(@NotNull final Long id) {
        return dsl().selectFrom(CONFERENCE)
                .where(CONFERENCE.ID.eq(id))
                .fetchOptional();
    }

    default void deleteConference(final long sponsorId) {
        getConferenceRecord(sponsorId).ifPresent(UpdatableRecordImpl::delete);
    }
}