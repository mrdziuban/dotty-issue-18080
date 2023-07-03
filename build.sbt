val scalaV = "3.3.1-RC2"

lazy val debugMode = sys.env.get("SBT_DEBUG").exists(_ == "1")

ThisBuild / logLevel := (if (debugMode) Level.Debug else Level.Info)
ThisBuild / incOptions ~= (_.withApiDebug(debugMode).withRelationsDebug(debugMode))

lazy val database = project.in(file(".")).settings(scalaVersion := scalaV)
