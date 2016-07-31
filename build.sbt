name := "Play1"

version := "1.0"

scalaVersion := "2.11.8"

lazy val root =
  (project in file("."))
    .enablePlugins(PlayScala, DockerPlugin)
    //.dependsOn(macros)

//lazy val scalaReflect = Def.setting { "org.scala-lang" % "scala-reflect" % scalaVersion.value }
//lazy val macros = (project in file("macros")).settings(libraryDependencies += scalaReflect.value)

resolvers += Resolver.jcenterRepo

resolvers ++= Seq(
  "Java.net Maven2 Repository"       at "http://download.java.net/maven/2/",
  "Twitter Repository"               at "http://maven.twttr.com",
  Resolver.typesafeRepo("releases"),
  Resolver.sonatypeRepo("releases"),
  Resolver.bintrayRepo("websudos", "oss-releases")
)

libraryDependencies +=  "com.iheart" %% "play-swagger" % "0.3.3-PLAY2.5"
libraryDependencies += "org.webjars" % "swagger-ui" % "2.1.4"
libraryDependencies += "com.websudos"  %% "phantom-dsl" % "1.27.0"
