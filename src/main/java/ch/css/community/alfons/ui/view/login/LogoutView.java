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

package ch.css.community.alfons.ui.view.login;

import ch.css.community.alfons.security.AuthenticatedEmployee;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jetbrains.annotations.NotNull;

import jakarta.annotation.security.PermitAll;
import java.io.Serial;

@Route("logout")
@PageTitle("Logout")
@PermitAll
public class LogoutView extends Composite<VerticalLayout> {

    @Serial
    private static final long serialVersionUID = -1311609912638895522L;

    public LogoutView(@NotNull final AuthenticatedEmployee authenticatedEmployee) {
        authenticatedEmployee.logout();
    }

}
