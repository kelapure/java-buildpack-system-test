#AppFog v2 fork

This fork of the Java Buildpack System Tests contains some changes required to run the tests against the Cloud Foundry
v2 instance provided by AppFog. This fork is provided as is and with no guarantees that it will be maintained or that
the tests are being regularly run. There is also no guarantee that these tests are applicable and exhaustive for AppFog.

Read the setup instructions below and set the environment variables to the values provided in the
CenturyLink [control panel][] under the AppFog service pane. For example.

| Environment Variable | Value
| -------------------- | -----
| `CF_TARGET` | `https://api.useast.appfog.ctl.io`
| `CF_ORG` | The three or four letter alias value that can be found in the account details of the [control panel][]
| `CF_SPACE` | `QA`

The `CF_BUILDPACK` value should not be set in order to test the one provided by the AppFog Cloud Foundry v2 instance.
`CF_USERNAME` and `CF_PASSWORD` should be your usual account details for CenturyLink/AppFog.

This forks test applications written with Grails, Groovy, Java Main, Play, Ratpack, Spring Boot, Spring Web and Plain
Servlet. Each application is then bound to the available services, Mysql, MongoDB and Redis. Other services can be
tested but they have been disabled until they become available in the AppFog service.

# Cloud Foundry Java Buildpack System Tests

The purpose of this repository is to exercise the [Cloud Foundry Java Buildpack][] together with its dependencies and associated services to ensure that the whole works together correctly.

The tests are JUnit tests in a gradle build which push the [Java test applications][] submodule (in `vendor/java-test-applications`) to Cloud Foundry.

The Cloud Foundry Client Library from the [CF Java Client repository][] is used to drive the Cloud Foundry Cloud Controller REST API.

[Cloud Foundry Java Buildpack]: https://github.com/cloudfoundry/java-buildpack
[Java test applications]: https://github.com/cloudfoundry/java-test-applications
[CF Java Client repository]: https://github.com/cloudfoundry/cf-java-client

## Environment Variables
The following environment variables must be set before building this project or running the tests.

| Environment Variable | Value
| -------------------- | -----
| `CF_BUILDPACK` | The URI of the buildpack to use.  Defaults to the "blessed buildpack" installed on the DEAs if it is not specified.
| `CF_ORG` | The Cloud Foundry organization to use.
| `CF_PASSWORD` | The password corresponding to the Cloud Foundry username.
| `CF_SPACE` | The Cloud Foundry space to use.
| `CF_TARGET` | The Cloud Foundry instance to use, Defaults to <`https://api.run.pivotal.io`>.
| `CF_USERNAME` | A Cloud Foundry username, which often consists of an email address.

Note: when running tests in Eclipse or IntelliJ IDEA, you can set environment variables in a run configuration.

## Building

This project is built with Gradle. Ensure the submodule containing the test applications has been updated. 
```plain
git submodule update --init --recursive
```

Set the environment variables as described above and then issue:
```plain
./gradlew
```

## Running Tests
To run the tests, ensure the submodule containing the test applications has been updated. 
```plain
git submodule update --init --recursive
```

Set the environment variables as described above and then issue:
```bash
./gradlew
```

## Contributing
[Pull requests][] are welcome; see the [contributor guidelines][] for details.

[Pull requests]: http://help.github.com/send-pull-requests
[contributor guidelines]: CONTRIBUTING.md

## License
This project is released under version 2.0 of the [Apache License][].

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
[control panel]: https://control.ctl.io/Organization/account/details
