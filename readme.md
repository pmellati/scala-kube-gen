# scala-openapi-gen ![](https://github.com/pmellati/scala-openapi-gen/workflows/CI/badge.svg)

This project is aimed at generating purely functional & modern client libraries from [Swagger/OpenAPI specifications](https://en.wikipedia.org/wiki/OpenAPI_Specification).

The generated client libraries are abstract in effect type, and rely on type-classes from [cats](https://typelevel.org/cats/) and [cats-effect](https://typelevel.org/cats-effect/), and are therefore compatible with a variety of concrete effect types including cats-effect IO and [ZIO](https://zio.dev/).

The primary motivation for this project is to support the latest versions of the kubernetes swagger spec. Therefore, we will support enough of OpenAPI to support k8s as a minimum. As a result, certain pathways in the code that are not needed for translating the k8s specs may simply fail with `NotImplementedError`. We welcome your contributions to fill these gaps.

## How ready is the project?

The project is still at very early stages, and requires a good amount of polishing. However, most of the core translation logic is implemented.

## Why not use [Swagger Codegen](https://github.com/swagger-api/swagger-codegen) or [OpenAPI Generator](https://github.com/OpenAPITools/openapi-generator)?

The current scala support in those projects does not support specifications as complex as k8s's, and it is too difficult to fix the issues.

We find these projects counter-intuitive. They provide a code generation framework based on a mixture of java and templating languages, which is too clunky, restrictive and complex for solving simple problems.

Instead, here we use all the expressiveness of the scala language, including its support for string interpolation to generate more scala.

## Sample generated lib

You can find a sample generated k8s client at: https://github.com/pmellati/scala-openapi-gen-sample