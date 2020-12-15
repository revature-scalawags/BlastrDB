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
  * pulls data from a formatted website link and parses them into formatted lists.
  */
object BlastrDB extends App {
  pullData(
    "https://nerf.fandom.com/wiki/Category:Nerf_blasters",
    "Nerf",
    "csv-files\\Nerf.csv",
    false
  )
  pullData(
    "https://nerf.fandom.com/wiki/Category:Nerf_blasters?from=Furyfire",
    "Nerf",
    "csv-files\\Nerf.csv",
    true
  )
  pullData(
    "https://nerf.fandom.com/wiki/Category:Nerf_blasters?from=Pyragon+%28VTX%29",
    "Nerf Wiki",
    "csv-files\\Nerf.csv",
    true
  )
  pullData(
    "https://nerf.fandom.com/wiki/Category:Nerf_blasters?from=Switchfire+%28Nerf+Action%29",
    "Nerf Wiki",
    "csv-files\\Nerf.csv",
    true
  )
  pullData(
    "https://nerf.fandom.com/wiki/Category:Adventure_Force_blasters",
    "Nerf Wiki",
    "csv-files\\Adventure Force.csv",
    false
  )
  pullData(
    "https://nerf.fandom.com/wiki/Category:Buzz_Bee_blasters",
    "Nerf Wiki",
    "csv-files\\Buzz bee.csv",
    false
  )
  pullData(
    "https://nerf.fandom.com/wiki/Category:BOOMco._blasters",
    "Nerf Wiki",
    "csv-files\\BoomCo.csv",
    false
  )
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

  /** pullData
    * pulls blaster data from a webpage based on the specific formatting of the webpage and formats it into a list in a csv file of the brand name
    *
    * @param link the complete URL of the webpage to pull from
    * @param site the website which the browser scraper will format the pull request from.
    *             See the match/case statement below for different websites to format to.
    * @param output the file path of the csv file to output to
    * @param appending Boolean value for if the new data is to be appended to a file (true), or overwrite the current data (false).
    */
  def pullData(
      link: String,
      site: String,
      output: String,
      appending: Boolean
  ) = {
    val browser = JsoupBrowser()
    val doc = browser.get(link)
    var scrape = Iterable[String]()
    var bdw: BufferedWriter = null

    val bufferDataFile = new File(output)

    if (appending == false) {
      bdw = new BufferedWriter(new FileWriter(bufferDataFile))
      bdw.write("")
    } else {
      bdw = new BufferedWriter(new FileWriter(bufferDataFile, true))
    }
    site match {
      case "Nerf Wiki" =>
        scrape = doc >> attrs("title")("a[class=category-page__member-link]")
      case _ =>
        println("Invalid Brand Name")
    }

    for (name <- scrape) {
      bdw.append(name + "\n")
    }
    bdw.close()
  }
}
