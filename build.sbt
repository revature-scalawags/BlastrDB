lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organizationName := "Scalawags, Inc",
      organizationHomepage := "https://github.com/revature-scalawags",
      scalaVersion := "2.13.3"
    )),
    name := "blastrDB"
  )
version := "1.0"

//libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % Test