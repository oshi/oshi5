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