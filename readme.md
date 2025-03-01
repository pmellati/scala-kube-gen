# scala-openapi-gen ![](https://github.com/pmellati/scala-openapi-gen/workflows/CI/badge.svg)

This project is aimed at generating purely functional & modern client libraries from [Swagger/OpenAPI specifications](https://en.wikipedia.org/wiki/OpenAPI_Specification).

The generated client libraries are abstract in effect type, and rely on type-classes from [cats](https://typelevel.org/cats/) and [cats-effect](https://typelevel.org/cats-effect/), and are therefore compatible with a variety of concrete effect types including cats-effect IO and [ZIO](https://zio.dev/).

The primary motivation for this project is to support the latest versions of the kubernetes swagger spec. Therefore, as a minimum, enough of the OpenAPI spec will always be supported to allow the translation of the k8s spec. By the same token, certain pathways in the code that are not needed for translating the k8s spec may simply fail with `NotImplementedError`. Contributions to fill these gaps are welcome.

## How ready is the project?

The project is still at very early stages, and requires a good amount of polishing. However, most of the core translation logic is implemented.

## Getting started (snapshots)

At the moment, only snapshot releases are available, since the project is not fully ready.

### mill

```scala
// Add the sonatype snapshots repo.
def repositories = super.repositories ++ Seq(
  MavenRepository("https://oss.sonatype.org/content/repositories/snapshots")
)

// Add library dependencies.
def ivyDeps = Agg(
  ivy"me.pouria::kubeclient:0.0.1-SNAPSHOT",
  // or:
  ivy"me.pouria::openapigen:0.0.1-SNAPSHOT",
)
```

### sbt

```scala
// Add the sonatype snapshots repo.
resolvers += Resolver.sonatypeRepo("snapshots"),
// Add library dependencies.
libraryDependencies += "me.pouria" %% "kubeclient" % "0.0.1-SNAPSHOT"
// or:
libraryDependencies += "me.pouria" %% "openapigen" % "0.0.1-SNAPSHOT"
```

## Generated source code

The generated sources can be found under [the `generated-sources` branch](https://github.com/pmellati/scala-openapi-gen/tree/generated-sources/kubeclient/generatedSrc/kubeclient).

(There is github action that, on each push to `master`, generates, reformats and pushes the generated code to the `generated-sources` branch.)