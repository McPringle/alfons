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

package ch.fihlon.alfons.data.service;

import ch.fihlon.alfons.data.service.getter.MailSenderGetter;
import ch.fihlon.alfons.data.entity.MailTemplateId;
import ch.fihlon.alfons.data.service.getter.ConfigurationGetter;
import ch.fihlon.alfons.data.service.getter.DSLContextGetter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.mail.SimpleMailMessage;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface MailService extends ConfigurationGetter, DSLContextGetter, MailSenderGetter, MailTemplateService {

    default void sendMail(@NotNull final MailTemplateId mailTemplateId,
                          @Nullable final Map<String, String> variables,
                          @NotNull final String... emailAddresses) {
        final var mailTemplateRecord = getMailTemplate(mailTemplateId).orElseThrow();
        final var message = new SimpleMailMessage();
        message.setTo(emailAddresses);
        message.setFrom(configuration().getEmailSenderAddress());
        message.setSubject(replaceVariables(mailTemplateRecord.getSubject(), variables));
        message.setText(replaceVariables(mailTemplateRecord.getContentText(), variables));
        mailSender().send(message);
    }

    private String replaceVariables(@NotNull final String text,
                                    @Nullable final Map<String, String> variables) {
        String returnValue = text;
        if (variables != null) {
            for (final var entry : variables.entrySet()) {
                final var value = Matcher.quoteReplacement(entry.getValue());
                final var regex = Pattern.quote("${%s}".formatted(entry.getKey()));
                returnValue = returnValue.replaceAll(regex, value);
            }
        }
        return returnValue;
    }

}
