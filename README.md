# Master Design Document

## Objectives
The redesign of OSHI intends to achieve:

- An API decoupled from the fetching layer
- A uniform API (across platforms and features) with no special cases
- Support for platform-specific features
- New features can be added later without adding new infrastructure
- A caching policy to improve performance
- Generic code for fetching that can be reused
- Usage similar to OSHI 4
- Runtime dependence on [JNA](https://github.com/java-native-access/jna) only

## [Terminology](https://github.com/oshi/oshi5/issues/7)
`// TODO`

## Design Decisions
### [Code Generation](https://github.com/oshi/oshi5/issues/4)
OSHI's API layer will be automatically generated from a central YAML definitions file. YAML is great for representing tree structures (like OSHI's API) and has a much simpler syntax than JSON or XML. This approach will ensure the resulting API is absolutely uniform, synchronized at all times, and will greatly simplify API changes.

The generator runs on every build (via Maven integration) and updates source files in the API layer's packages according to the central definitions file. Generated files will be committed to the repository.

The definitions file can also be converted into supplemental documentation that contains every feature's description and compatibility requirements in a single place. This addresses the concern brought up [in this comment](https://github.com/oshi/oshi5/issues/2#issuecomment-451220174).

### [API Return Types and Error Handling](https://github.com/oshi/oshi5/issues/2)
`// TODO`

### [Configuration](https://github.com/oshi/oshi5/issues/3)
`// TODO`
