name := "Play1"

version := "1.0"

scalaVersion := "2.11.8"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += Resolver.jcenterRepo

libraryDependencies +=  "com.iheart" %% "play-swagger" % "0.3.3-PLAY2.5"
libraryDependencies += "org.webjars" % "swagger-ui" % "2.1.4"