package csv

import java.io.File
import java.io.BufferedWriter
import java.io.FileWriter
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.model._

/** BlastrDB
  * pulls data from the csv-files folder and parses them into a formatted list.
  */
object BlastrDB extends App {
  pullData("https://nerf.fandom.com/wiki/Category:Nerf_blasters", "Nerf","csv-files\\Nerf.csv")
  //TODO: pull data from Adventure Force website
  //TODO: pull data from a 3rd party website
  val folder = getListOfFiles("./csv-files")
  val bufferFile = new File("compiled-list.txt")
  val bw = new BufferedWriter(new FileWriter(bufferFile))
  bw.write("Brand\t\t|\tBlaster Name\n")
  for (ftchfile <- folder) {
    val file = io.Source.fromFile(ftchfile)
    for (line <- file.getLines) {
      bw.append(
        (ftchfile
          .getAbsolutePath()
          .substring(
            // this line formats the path to display the file name as the brand
            ftchfile.getAbsolutePath().lastIndexOf("\\") + 1,
            ftchfile.getAbsolutePath().length() - 4
          ) + " | " + line + "\n").toString()
      )
    } //TODO: Replace for loops with futures call
  }
  bw.close()

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

  def pullData(link: String, brand: String, output: String) = {
    val browser = JsoupBrowser()
    val doc = browser.get(link)
    var scrape = Iterable[String]()
    
    val bufferDataFile = new File(output)
    val bdw = new BufferedWriter(new FileWriter(bufferDataFile))
    bdw.write("")
    brand match {
      case "Nerf" => scrape = doc >> attrs("title")("a[class=category-page__member-link]")
      //case "Adventure Force" => <<insert data fetch command here>>
      case _ => println("Invalid Brand Name")
    }
    
    for(name <- scrape) {
      bdw.append(name + "\n")
    }
    bdw.close()
  }
}
