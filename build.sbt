val root = project
  .in(file("."))
  .settings(
    scalaVersion := "3.1.2",
    organization := "dev.vgerasimov",
    name := "slowjson4s",
    version := "0.1.0",
    githubOwner := "wlad031",
    githubRepository := "slowjson4s",
    resolvers += Resolver.githubPackages("wlad031"),
    scalacOptions ++= Seq(
      "-rewrite",
      "-source", "future",
      "-Xfatal-warnings",
    ),
    libraryDependencies ++= {
      val munitVersion = "0.7.29"
      Seq(
        "org.scalameta"    %% "munit"            % munitVersion % Test,
        "org.scalameta"    %% "munit-scalacheck" % munitVersion % Test,
        "dev.vgerasimov"   %% "slowparse"        % "0.1.4"
      )
    },
  )