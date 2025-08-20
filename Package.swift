// swift-tools-version: 6.1
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "WikiArtServices",
    platforms: [
        .iOS(.v13),
        .tvOS(.v13)
    ],
    products: [
        .library(
            name: "WikiArtServices",
            targets: ["WikiArtServices"]
        ),
    ],
    targets: [
        .target(
            name: "WikiArtServices",
            path: "Sources/WikiArtServices"
        ),
        .testTarget(
            name: "WikiArtServicesTests",
            dependencies: ["WikiArtServices"]
        )
    ]
)
