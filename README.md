![Alfons](https://raw.githubusercontent.com/McPringle/alfons/main/src/main/resources/META-INF/resources/images/alfons.png)

# Alfons

**An open-source application for companies to manage requests from their employees to attend a conference.
With approval workflow, budget management, and reports.**

## Architecture

*Alfons* is written using the [Java programming language](https://en.wikipedia.org/wiki/Java_(programming_language)). The main framework is [Spring](https://spring.io/). For the user interface, we use [Vaadin Flow](https://vaadin.com/flow). To access the database, we rely on [jOOQ](https://www.jooq.org/).

## Configuration

The file `application.properties` contains only some default values. To override the default values and to specify other configuration options, just set them as environment variables. The following sections describe all available configuration options. You only need to specify these options if your configuration settings differ from the defaults.

### Server

The server runs on port 8080 by default. If you don't like it, change it:

```
PORT=8080
```

### Mail

To be able to send mails, you need to specify an SMTP server (defaults are `localhost` and port`25`):

```
MAIL_HOST=localhost
MAIL_PORT=25
```

### Database

*Alfons* needs a database to store the business data. All JDBC compatible databases are supported. By default, *Alfons* uses an in memory [H2](https://www.h2database.com/) database. You don't need to configure anything, but you will lose all your data when you stop *Alfons*.

To permanently store data, we highly recommend [MariaDB](https://mariadb.org/), just because we are using it during development, and it is highly tested with *Alfons*. Please make sure that your database is using a unicode character set to avoid problems storing data containing unicode characters.

The `DB_USER` is used to access the *Alfons* database including automatic schema migrations and needs `ALL PRIVILEGES`.

```
DB_URL=jdbc:mariadb://localhost:3306/alfons?serverTimezone\=Europe/Zurich
DB_USER=johndoe
DB_PASS=verysecret
```

The database schema will be migrated automatically by *Alfons*.

#### Important MySQL and MariaDB configuration

MySQL and MariaDB have a possible silent truncation problem with the `GROUP_CONCAT` command. To avoid this it is necessary, to configure these two databases to allow multi queries. Just add `allowMultiQueries=true` to the JDBC database URL like in this example (you may need to scroll the example code to the right):

```
DB_URL=jdbc:mariadb://localhost:3306/alfons?serverTimezone\=Europe/Zurich&allowMultiQueries=true
```

### Admin

You will need at least one administrator. Therefore, you should add yourself as admin to the database, **after** you have started *Alfons* (because the database tables will be created at the first start):

```sql
INSERT INTO `employee` (`id`, `first_name`, `last_name`, `email`, `admin`, `password_change`)
VALUES (1, 'First name', 'Last name', 'email@domain.tld', TRUE, TRUE);
```

Then, open `http://localhost:8080/login`, enter your email address, and click on "I forgot my password" to start the password reset process (you will receive a one time password via email), and set your own admin password.

## Running the application

The project is a standard Maven project. To run it from the command line,
type `mvnw` (Windows), or `./mvnw` (Mac & Linux), then open
http://localhost:8080 in your browser.

You can also import the project to your IDE of choice as you would with any
Maven project. Read more on [how to import Vaadin projects to different 
IDEs](https://vaadin.com/docs/latest/guide/step-by-step/importing) (Eclipse, IntelliJ IDEA, NetBeans, and VS Code).

## Deploying to Production

To create a production build, call `mvnw clean package -Pproduction` (Windows),
or `./mvnw clean package -Pproduction` (Mac & Linux).
This will build a JAR file with all the dependencies and front-end resources,
ready to be deployed. The file can be found in the `target` folder after the build completes.

Once the JAR file is built, you can run it using
`java -jar target/alfons-1.0-SNAPSHOT.jar`

## Project structure

- `MainLayout.java` in `src/main/java` contains the navigation setup (i.e., the
  side/top bar and the main menu). This setup uses
  [App Layout](https://vaadin.com/docs/components/app-layout).
- `views` package in `src/main/java` contains the server-side Java views of your application.
- `views` folder in `frontend/` contains the client-side JavaScript views of your application.
- `themes` folder in `frontend/` contains the custom CSS styles.

## Useful links

- Read the documentation at [vaadin.com/docs](https://vaadin.com/docs).
- Follow the tutorials at [vaadin.com/tutorials](https://vaadin.com/tutorials).
- Watch training videos and get certified at [vaadin.com/learn/training](https://vaadin.com/learn/training).
- Create new projects at [start.vaadin.com](https://start.vaadin.com/).
- Search UI components and their usage examples at [vaadin.com/components](https://vaadin.com/components).
- View use case applications that demonstrate Vaadin capabilities at [vaadin.com/examples-and-demos](https://vaadin.com/examples-and-demos).
- Build any UI without custom CSS by discovering Vaadin's set of [CSS utility classes](https://vaadin.com/docs/styling/lumo/utility-classes). 
- Find a collection of solutions to common use cases at [cookbook.vaadin.com](https://cookbook.vaadin.com/).
- Find add-ons at [vaadin.com/directory](https://vaadin.com/directory).
- Ask questions on [Stack Overflow](https://stackoverflow.com/questions/tagged/vaadin) or join our [Discord channel](https://discord.gg/MYFq5RTbBn).
- Report issues, create pull requests in [GitHub](https://github.com/vaadin).

## Deploying using Docker

To build the Dockerized version of the project, run

```
./mvnw clean package -Pproduction
docker build . -t alfons:latest
```

Once the Docker image is correctly built, you can test it locally using

```
docker run -p 8080:8080 alfons:latest
```

## Deploying using Kubernetes

We assume here that you have the Kubernetes cluster from Docker Desktop running (can be enabled in the settings).

First build the Docker image for your application. You then need to make the Docker image available to you cluster. With Docker Desktop Kubernetes, this happens automatically. With Minikube, you can run `eval $(minikube docker-env)` and then build the image to make it available. For other clusters, you need to publish to a Docker repository or check the documentation for the cluster.

The included `kubernetes.yaml` sets up a deployment with 2 pods (server instances) and a load balancer service. You can deploy the application on a Kubernetes cluster using

```
kubectl apply -f kubernetes.yaml
```

If everything works, you can access your application by opening http://localhost:8000/.
If you have something else running on port 8000, you need to change the load balancer port in `kubernetes.yaml`.

Tip: If you want to understand which pod your requests go to, you can add the value of `VaadinServletRequest.getCurrent().getLocalAddr()` somewhere in your UI.

### Troubleshooting

If something is not working, you can try one of the following commands to see what is deployed and their status.

```
kubectl get pods
kubectl get services
kubectl get deployments
```

If the pods say `Container image "alfons:latest" is not present with pull policy of Never` then you have not built your application using Docker or there is a mismatch in the name. Use `docker images ls` to see which images are available.

If you need even more information, you can run

```
kubectl cluster-info dump
```

that will probably give you too much information but might reveal the cause of a problem.

If you want to remove your whole deployment and start over, run

```
kubectl delete -f kubernetes.yaml
```

## Contributors

Special thanks for all these wonderful people who had helped this project so far ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tbody>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="https://fihlon.swiss/"><img src="https://avatars.githubusercontent.com/u/1254039?v=4?s=100" width="100px;" alt="Marcus Fihlon"/><br /><sub><b>Marcus Fihlon</b></sub></a><br /><a href="#projectManagement-McPringle" title="Project Management">????</a> <a href="#ideas-McPringle" title="Ideas, Planning, & Feedback">????</a> <a href="https://github.com/McPringle/alfons/commits?author=McPringle" title="Code">????</a></td>
    </tr>
  </tbody>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

## Contributing

New contributors are always welcome! If you discover errors or omissions in the source code, documentation, or website content, please don???t hesitate to submit an issue or open a pull request with a fix.

Here are some ways **you** can contribute:

- by using prerelease (alpha, beta or preview) versions
- by reporting bugs
- by suggesting new features
- by writing or editing documentation
- by writing code with tests -- *no patch is too small*
  - fix typos
  - add comments
  - clean up inconsistent whitespace
  - write tests!
- by refactoring code
- by fixing [issues](https://github.com/McPringle/alfons/issues)
- by reviewing [patches](https://github.com/McPringle/alfons/pulls)

The [Contributing](CONTRIBUTING.md) guide provides information on how to create, style, and submit issues, feature requests, code, and documentation to the *Alfons* project.

## Copyright and License

[GNU Affero General Public License](https://www.gnu.org/licenses/agpl-3.0.html)

*Copyright ?? Marcus Fihlon and the individual contributors to Alfons.*

This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
