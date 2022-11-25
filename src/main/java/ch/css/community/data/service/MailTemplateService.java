/*
 * Komunumo - Open Source Community Manager
 * Copyright (C) Marcus Fihlon and the individual contributors to Komunumo.
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

import ch.css.community.alfons.data.db.tables.records.MailTemplateRecord;
import ch.css.community.data.entity.MailTemplateId;
import ch.css.community.data.service.getter.DSLContextGetter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.impl.DSL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static ch.css.community.alfons.data.db.tables.MailTemplate.MAIL_TEMPLATE;

public interface MailTemplateService extends DSLContextGetter {

    default MailTemplateRecord newMailTemplate() {
        return dsl().newRecord(MAIL_TEMPLATE);
    }

    default Optional<MailTemplateRecord> getMailTemplate(@NotNull final MailTemplateId mailTemplateId) {
        return dsl().selectFrom(MAIL_TEMPLATE)
                .where(MAIL_TEMPLATE.ID.eq(mailTemplateId.name()))
                .fetchOptional();
    }

    default Stream<MailTemplateRecord> findMailTemplate(final int offset, final int limit, @Nullable final String filter) {
        final var filterValue = filter == null || filter.isBlank() ? null : "%" + filter.trim() + "%";
        return dsl().selectFrom(MAIL_TEMPLATE)
                .where(filterValue == null ? DSL.noCondition()
                        : MAIL_TEMPLATE.ID.like(filterValue).or(MAIL_TEMPLATE.SUBJECT.like(filterValue)))
                .orderBy(MAIL_TEMPLATE.ID)
                .offset(offset)
                .limit(limit)
                .stream();
    }

    default List<MailTemplateId> findMissingMailTemplateIds() {
        final var mailTemplateIds = new ArrayList<>(List.of(MailTemplateId.values()));
        dsl().selectFrom(MAIL_TEMPLATE)
                .forEach(mailTemplateRecord -> mailTemplateIds.remove(MailTemplateId.valueOf(mailTemplateRecord.getId())));
        return Collections.unmodifiableList(mailTemplateIds);
    }

}
