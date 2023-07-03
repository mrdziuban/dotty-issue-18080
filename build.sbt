val scalaV = "3.3.1-RC1"

// ThisBuild / logLevel := Level.Debug
// ThisBuild / incOptions ~= (_.withApiDebug(true).withRelationsDebug(true))

lazy val database = project.in(file(".")).settings(scalaVersion := scalaV)
