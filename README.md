# OLE Loader Client

Contains base framework for using the (not yet released) OLE Loader RESTful API scheduled for OLE 2.0, along with unit tests for conformance to the specification.

This is a work in progress.

## Usage

After cloning, edit `src/main/resources/dataTypes.groovy` and make sure that the `apiBase` property points to the base URI for the service.  Typically this will be rooted at `/olefs/api` on the server you want to check for conformance.

Make sure the server in running!  Some of the tests do not require a running server, but most do.  Now, in the project root directory, execute

```
./gradlew test
```

(or `gradlew.bat test` on Windows)

The console should indicate overall success or failure of the tests, detailed reports will be in.

`build/reports/tests/index.html`

JSON schemas are in `src/main/resources/schemas`, and should be suitable for validating the content of responses (but not full conformance to the RESTful API in the specification).

### Note

This is a work in progress, I do not certify that it conforms to the specification as currently written.  It also does not test all aspects of the specification, but it should be enough to assist with further development.  License is GPL v3, and copyright is retained by North Carolina State University 2015.
