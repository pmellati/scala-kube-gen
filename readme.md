# scala-openapi-gen ![](https://github.com/pmellati/scala-openapi-gen/workflows/CI/badge.svg)

This project is aimed at generating purely functional & modern client libraries from [Swagger/OpenAPI specifications](https://en.wikipedia.org/wiki/OpenAPI_Specification).

The generated client libraries are abstract in effect type, and rely on type-classes from [cats](https://typelevel.org/cats/) and [cats-effect](https://typelevel.org/cats-effect/), and are therefore compatible with a variety of concrete effect types including cats-effect IO and [ZIO](https://zio.dev/).

The primary motivation for this project is to support the latest versions of the kubernetes swagger spec. Therefore, as a minimum, enough of the OpenAPI spec will always be supported to allow the translation of the k8s spec. By the same token, certain pathways in the code that are not needed for translating the k8s spec may simply fail with `NotImplementedError`. Contributions to fill these gaps are welcome.

## How ready is the project?

The project is still at very early stages, and requires a good amount of polishing. However, most of the core translation logic is implemented.
