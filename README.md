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

The definitions file will also be converted into supplemental documentation (markdown on the OSHI wiki) that contains every feature's description, type, compatibility requirements, and links to the relevant Javadoc in a single place. This addresses the concern brought up [in this comment](https://github.com/oshi/oshi5/issues/2#issuecomment-451220174) about spreading features out in the Javadoc.

#### Attribute Definitions YAML Specification
```
# OSHI Attribute Definitions

- <container name>:
  - <attribute name>: <short description of attribute>
    type: <Java type of attribute>
    platforms: <comma-separated list of required (whitelisted) platforms>
    incompatible: <comma-separated list of incompatible (blacklisted) platforms>
    external: <comma-separated list of external dependencies>
  ...
...
```

| Field | Description |
|----------------|-----------------------------------|
| container name | CamelCase identifier which must start with a capital letter. Must be unique across all containers. |
| attribute name | Lowercase identifier in underscore format (for easy conversion to any other case format by the generator). Must be unique across all attributes in the same container. |
| attribute description | Short description of what the attribute contains |
| type | The attribute's Java type (which could be other containers) |
| platforms | A list of the **only** platforms that the attribute is compatible with. Values must match [JNA's `Platform` class](http://java-native-access.github.io/jna/5.2.0/javadoc/com/sun/jna/Platform.html). |
| incompatible | A list of platforms that the attribute is **not** compatible with. Values must match [JNA's `Platform` class](http://java-native-access.github.io/jna/5.2.0/javadoc/com/sun/jna/Platform.html). |
| external | A list of external dependencies (like OpenHardwareMonitor) that the attribute requires |
| permissions | Whether elevated permissions are required. Values are `elevated` or `none`. |

Here's a contrived example displaying all of these elements:
```
- Disk
  - read_bytes: The number of bytes read by the disk
      type: Long
      incompatible: solaris                      # All platforms except Solaris
  - partitions: The disk's partitions
      type: Partition[]                          # Nested container
  - power_on_time: The disk's total power-on time in hours
      type: Long
      platforms: linux                           # Only compatible with Linux
      external: smart                            # Needs S.M.A.R.T. access
      permissions: elevated                      # Needs highest user permissions
```

### [API Return Types and Error Handling](https://github.com/oshi/oshi5/issues/2)
`// TODO`

### [Configuration](https://github.com/oshi/oshi5/issues/3)
`// TODO`
