package csv

import java.io.File

/** CSVReader
  */
object CSVReader extends App {
  println("Brand\t\t|\tBlaster Name")
  val folder = getListOfFiles("./csv-files")
  for (ftchfile <- folder) {
    val file = io.Source.fromFile(ftchfile)
    for (line <- file.getLines) {
      println(ftchfile.getAbsolutePath().substring(ftchfile.getAbsolutePath().lastIndexOf("\\")+1, ftchfile.getAbsolutePath().length()-4) + " | " + line)
    }
  }

  def getListOfFiles(dir: String): List[File] = {
    val output = new File(dir)
    if (output.exists && output.isDirectory) {
      output.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }
}
