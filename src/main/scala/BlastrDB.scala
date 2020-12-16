package csv

import java.io.File
import java.io.BufferedWriter
import java.io.FileWriter
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.model._
import java.io.BufferedReader
import java.io.StringWriter
import java.io.PrintWriter

/** BlastrDB
  * pulls data from a formatted website link and parses them into formatted lists.
  * 
  * @version 0.13
  * @todo add runtime CLI menu to allow for user interaction
  * @todo add hooks and ouput for mongoDB
  * @todo add scalatest items
  */
object BlastrDB extends App {
  println("BlastrDB starting...")
  println(
    "BlastrDB is parsing and adding data from listed websites now.\n\t" +
      " If you would like to add, remove, or change a website\n\t " +
      "to add data from, you may do so in the \"dataPullParse.csv\" file."
  )
  //println("Welcome to BlastrDB, please enter one of the options below:")
  //TODO: refactor into CLI
  val loggerFile = new File("debugLog.txt")
  val debugFileBuffer = new BufferedWriter(new FileWriter(loggerFile))
  debugFileBuffer.write("Debug Log start:\n")
  val dataPullSource = io.Source.fromFile("dataPullParse.csv")
  for (line <- dataPullSource.getLines) {
    val cols = line.split(",").map(_.trim)
    debugFileBuffer.write("Pulling data from " + cols(0) + "... \n")
    try {
      pullData(cols(0), cols(1), cols(2), cols(3).toBoolean)
    } catch {
      case e: IllegalArgumentException =>
        println(
          "!!!Incorrect formatting found in dataPullParse.csv, check debug.txt for details!!!"
        )
        debugFileBuffer.write(getStackTraceAsString(e))

    }
  }
  dataPullSource.close

  //TODO: pull data from a 3rd party website
  debugFileBuffer.write(
    "Data pull complete, writing data to aggregate csv file \"compiled-list.csv\"...\n"
  )
  val folder = getListOfFiles("./csv-files")
  val bufferFile = new File("compiled-list.csv")
  val bw = new BufferedWriter(new FileWriter(bufferFile))
  //compiled-list.csv is organized by Brand,Name
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
          ) + "," + line + "\n").toString()
      )
    }
  }
  debugFileBuffer.write("Data write to files complete, closing buffer...\n")
  bw.close()
  debugFileBuffer.write("buffer closed.\n")

  /** getListOfFiles
    * populates a list of files drawn from a specified folder
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
    * pulls blaster data from a webpage based on the specific formatting of the webpage and
    *  formats it into a list in a csv file of the brand name
    *
    * @param link the complete URL of the webpage to pull from
    * @param site the website which the browser scraper will format the pull request from.
    *             See the match/case statement below for different websites to format to.
    * @param output the file path of the csv file to output to
    * @param appending Boolean value for if the new data is to be appended to a file (true),
    *                    or overwrite the current data (false).
    *
    * @example pullData(
    *             "https://example.site",
    *             "Brand",
    *             "folder\\File.txt",
    *             false
    *          )
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
      debugFileBuffer.write(
        s"\tAdditional page detected for $output, proceeding to append...\n"
      )
      bdw = new BufferedWriter(new FileWriter(bufferDataFile, true))
    }
    site match {
      case "Nerf Wiki" =>
        scrape = doc >> attrs("title")("a[class=category-page__member-link]")
      case _ =>
        debugFileBuffer.write(
          "Invalid Site Name detected, no list items added to scraper\n"
        )
    }

    for (name <- scrape) {
      bdw.append(name + "\n")
    }
    bdw.close()
  }

  /** getStackTraceString
    * gets the stack trace of a thrown exception and formats it
    * into a string for logging purposes
    * 
    * @param t the exception thrown
    */
  def getStackTraceAsString(t: Throwable) = {
    val sw = new StringWriter
    t.printStackTrace(new PrintWriter(sw))
    sw.toString
  }

  debugFileBuffer.write("debug log complete.")
  debugFileBuffer.close()
  println(
    "BlastrDB processes complete, check debugLog.txt for a detailed runtime log."
  )
}
