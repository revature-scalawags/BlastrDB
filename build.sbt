lazy val root = (project in file("."))
.settings(
  inThisBuild(
    List(
      organizationName := "Scalawags, Inc",
      organizationHomepage := Some(
        url("https://github.com/revature-scalawags")
      ),
      scalaVersion := "2.13.3"
    )
  )
)
name := "blastrDB"
version := "1.0"

libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "2.2.0"
