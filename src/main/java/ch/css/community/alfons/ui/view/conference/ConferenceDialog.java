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

package ch.css.community.alfons.ui.view.conference;

import ch.css.community.alfons.data.db.tables.records.ConferenceRecord;
import ch.css.community.alfons.ui.component.DatePicker;
import ch.css.community.alfons.ui.component.EditDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;

import static com.vaadin.flow.data.value.ValueChangeMode.EAGER;

public final class ConferenceDialog extends EditDialog<ConferenceRecord> {

    @Serial
    private static final long serialVersionUID = 8013889745455047755L;

    public ConferenceDialog(@NotNull final String title) {
        super(title);
    }

    @Override
    public void createForm(@NotNull final FormLayout formLayout, @NotNull final Binder<ConferenceRecord> binder) {
        final var name = new TextField("Name");
        final var beginDate = new DatePicker("Valid from");
        final var endDate = new DatePicker("Valid to");
        final var website = new TextField("Website");

        name.setRequiredIndicatorVisible(true);
        name.setValueChangeMode(EAGER);
        website.setValueChangeMode(EAGER);
        endDate.addFocusListener(event -> {
            if (endDate.isEmpty() && !beginDate.isInvalid()) {
                endDate.setValue(beginDate.getValue());
            }
        });

        formLayout.add(name, beginDate, endDate, website);

        binder.forField(name)
                .withValidator(new StringLengthValidator(
                        "Please enter the name of the conference (max. 255 chars)", 1, 255))
                .bind(ConferenceRecord::getName, ConferenceRecord::setName);

        binder.forField(beginDate)
                .withValidator(value -> value == null || endDate.isEmpty() || (value.isBefore(endDate.getValue()) || value.isEqual(endDate.getValue())),
                        "The begin date must be before the end date or they must be the same (1-day-conference)")
                .bind(ConferenceRecord::getBeginDate, ConferenceRecord::setBeginDate);

        binder.forField(endDate)
                .withValidator(value -> value == null || beginDate.isEmpty() || (value.isEqual(beginDate.getValue()) || value.isAfter(beginDate.getValue())),
                        "The end date must be after the begin date or they must be the same (1-day-conference)")
                .bind(ConferenceRecord::getEndDate, ConferenceRecord::setEndDate);

        binder.forField(website)
                .withValidator(value -> value.isEmpty() || value.startsWith("https://"),
                        "The website address must start with \"https://\"")
                .withValidator(new StringLengthValidator(
                        "The website address is too long (max. 255 chars)", 0, 255))
                .bind(ConferenceRecord::getWebsite, ConferenceRecord::setWebsite);
    }
}
