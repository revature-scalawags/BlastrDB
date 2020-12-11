package csv

import java.io.File

/** BlastrDB
  * pulls data from the csv-files folder and parses them into a formatted list.
  */
object BlastrDB extends App {
  println("Brand\t\t|\tBlaster Name")
  val folder = getListOfFiles("./csv-files")
  for (ftchfile <- folder) {
    val file = io.Source.fromFile(ftchfile)
    for (line <- file.getLines) {
      println(
        ftchfile
          .getAbsolutePath()
          .substring(
            // this line formats the path to display the file name as the brand
            ftchfile.getAbsolutePath().lastIndexOf("\\") + 1,
            ftchfile.getAbsolutePath().length() - 4
          ) + " | " + line
      ) //TODO: replace println with an output to file
    } //TODO: Replace for loops with futures call
  }

  /** getListOfFiles
    * helper method that populates a list of files drawn from a specified folder
    *
    * @param dir = the directory to populate the files from
    * @return - List[File] (a list of File objects)
    */
  def getListOfFiles(dir: String): List[File] = {
    val output = new File(dir)
    if (output.exists && output.isDirectory) {
      output.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }
}
