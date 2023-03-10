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

package ch.fihlon.alfons.security;

import ch.fihlon.alfons.ui.view.login.ChangePasswordView;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;

@SpringComponent
public final class ForcePasswordChange implements VaadinServiceInitListener {


    @Serial
    private static final long serialVersionUID = -8608315239668545500L;
    private final AuthenticatedEmployee authenticatedEmployee;

    public ForcePasswordChange(@NotNull final AuthenticatedEmployee authenticatedEmployee) {
        this.authenticatedEmployee = authenticatedEmployee;
    }

    @Override
    public void serviceInit(@NotNull final ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final var ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::beforeEnter);
        });
    }

    private void beforeEnter(@NotNull final BeforeEnterEvent event) {
        if (authenticatedEmployee.get().isPresent() && authenticatedEmployee.get().get().getPasswordChange()) {
            event.forwardTo(ChangePasswordView.class);
        }
    }

}
