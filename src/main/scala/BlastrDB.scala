package BlastrDB

import java.io.File
import java.io.BufferedWriter
import java.io.FileWriter

import org.mongodb.scala.bson.collection._
import org.mongodb.scala.MongoClient
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.Observer
import org.mongodb.scala.Completed

import java.io.StringWriter
import java.io.PrintWriter

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/** BlastrDB
  * pulls data from a formatted website link and parses them into formatted lists.
  *
  * @version 0.23
  */
object BlastrDB extends App {
  println("\nBlastrDB starting...")

  val loggerFile = new File("debugLog.txt")
  val debugFileBuffer = new BufferedWriter(new FileWriter(loggerFile, true))
  var currentTime = getCurrentTime()
  debugFileBuffer.write(s"\n$currentTime Debug Log start:\n")

  //database hooks
  currentTime = getCurrentTime()
  debugFileBuffer.write(s"$currentTime Connecting to MongoDB...\n")

  val mongoClient: MongoClient = MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase("testdb")
  val collection: MongoCollection[Document] = database.getCollection("name")
  Thread.sleep(1000)

  currentTime = getCurrentTime()
  debugFileBuffer.write(s"$currentTime MongoDB successfully connected.\n")

  //CLI
  var userInput = ""
  println(
    "\nWelcome to BlastrDB, the following options below are avilable to you:\n" +
      "\tpull:\tPulls the data from the list of websites.\n" +
      "\twrite:\tWrites the data to a compiled CSV file from the \\csv-files folder.\n" +
      "\tshow:\tlists all of the current blasters contained in the database.\n" +
      "\tadd:\tBrings you to the menu for adding new entries to the database.\n" +
      "\texit:\tExits the program."
  )
  while (userInput != "exit") {
    println("Please enter a command: ")
    userInput = scala.io.StdIn.readLine()
    currentTime = getCurrentTime()
    debugFileBuffer.write(s"$currentTime user input: '$userInput'\n")
    userInput match {
      case "pull"  => dataWriteToFile(debugFileBuffer)
      case "write" => writeToBrandFiles(debugFileBuffer)
      case "show" => {
        println(
          "\t| Displaying records from MongoDB (NOTE: if this list is empty, " +
            "make sure that\n\t| the mongoDB is currently running in Docker.)"
        )
        currentTime = getCurrentTime()
        debugFileBuffer.write(
          s"$currentTime Displaying records from MongoDB...\n"
        )
        for (i <- collection.find()) {
          println(
            i("Brand").asString().getValue + " " + i("Name").asString.getValue
          )
        }
        Thread.sleep(1000)
        currentTime = getCurrentTime()
        debugFileBuffer.write(s"$currentTime Records displayed in terminal.\n")
      }
      case "add"  => addDocument()
      case "exit" => println("Exiting program...")
      case _ => {
        println("Input not recognized, please try a different input.")
      }
    }
  }

  /** addDocument
    * adds a new record to the mongoDB from user input parameters
    */
  def addDocument() {
    currentTime = getCurrentTime()
    debugFileBuffer.write(s"$currentTime Entering 'add document' menu...\n")
    var addingEntry = "Y"
    while (addingEntry != "N") {
      println(
        "\tAdding a new entry to the database requires the following inputs:\n" +
          "\t\tBrand name: "
      )
      val brandName = scala.io.StdIn.readLine()
      currentTime = getCurrentTime()
      debugFileBuffer.write(s"$currentTime user input: '$brandName'\n")
      println("\t\tBlaster Name: ")
      val blasterName = scala.io.StdIn.readLine()
      currentTime = getCurrentTime()
      debugFileBuffer.write(s"$currentTime user input: '$blasterName'\n")
      println(s"\t\tadd '$brandName $blasterName' to the database? Y/N")
      val addEntry = scala.io.StdIn.readLine()
      currentTime = getCurrentTime()
      debugFileBuffer.write(s"$currentTime user input: '$addEntry'\n")
      if (addEntry == "Y") {
        currentTime = getCurrentTime()
        debugFileBuffer.write(
          s"$currentTime user confirmed to add entry to mongoDB, subscribing change...\n"
        )
        val doc: Document =
          Document("Brand" -> brandName, "Name" -> blasterName)
        collection
          .insertOne(doc)
          .subscribe(new Observer[Completed] {
            override def onNext(result: Completed): Unit = {
              println("Inserted")
              currentTime = getCurrentTime()
              debugFileBuffer
                .write(s"$currentTime @MongoDB: Entry inserted successsfully\n")
            }
            override def onError(e: Throwable): Unit = {
              println(s"Failed, $e")
              currentTime = getCurrentTime()
              debugFileBuffer
                .write(s"$currentTime @MongoDB: Entry failed due to error: $e\n")
            }
            override def onComplete(): Unit = {
              println("Completed")
              currentTime = getCurrentTime()
              debugFileBuffer.write(s"$currentTime @MongoDB: Entry completed\n")
            }
          })
        Thread.sleep(1000)
        currentTime = getCurrentTime()
        debugFileBuffer.write(
          s"$currentTime entry addition subscription completed. See above line for status of insertion.\n"
        )
      } else if (addEntry == "N") {
        println("entry addition cancelled. Add another entry? Y/N")
        addingEntry = scala.io.StdIn.readLine()
        currentTime = getCurrentTime()
        debugFileBuffer.write(s"$currentTime user input: '$addingEntry'\n")

      } else {
        println(
          "Invalid input, entry addition cancelled. Add another entry? Y/N"
        )
        addingEntry = scala.io.StdIn.readLine()
        currentTime = getCurrentTime()
        debugFileBuffer.write(s"$currentTime user input: '$addingEntry'\n")
      }
    }
    println("Returning to main menu...")
    currentTime = getCurrentTime()
    debugFileBuffer.write(
      s"$currentTime User exited entry addition screen, returning to previous menu...\n"
    )
  }

  /** writetoBrandFiles
    * pulls the files from the ./csv-files folder and compiles them into a
    * single csv file containing the contents of all the seperate csv files
    *
    * @param debugFile the debug file for logging
    */
  def writeToBrandFiles(debugFile: BufferedWriter) {
    currentTime = getCurrentTime()
    debugFile.write(
      s"$currentTime Writing data to seperate CSV files based on brand...\n"
    )
    println("Writing data to seperate CSV files based on brand...")
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
    currentTime = getCurrentTime()
    debugFile.write(
      s"$currentTime Data write to files complete, closing local compiled-list.csv buffer...\n"
    )
    bw.close()
    currentTime = getCurrentTime()
    debugFile.write(s"$currentTime local buffer closed.\n")
    println("Data write to files complete.")
  }

  /** getListOfFiles
    * helper function for writetoBrandFiles that compiles the list of files
    * contained within a given directory.
    *
    * @param dir = the directory to populate the files from
    * @return = List[File] (a list of File objects)
    */
  def getListOfFiles(dir: String): List[File] = {
    val output = new File(dir)
    if (output.exists && output.isDirectory) {
      output.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
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

  /** dataWriteToFile
    * pulls the data from the dataPullParse.csv file and uses it to call the
    * 'pullData' function for each line of the dataPullParse.csv file
    *
    * @param fileBuffer the debug file
    */
  def dataWriteToFile(fileBuffer: BufferedWriter) = {
    currentTime = getCurrentTime()
    fileBuffer.write(s"$currentTime Beginning data pull from websites...\n")
    println("Beginning Data pull from websites...")
    val dataPullSource = io.Source.fromFile("dataPullParse.csv")
    for (line <- dataPullSource.getLines) {
      val cols = line.split(",").map(_.trim)
      currentTime = getCurrentTime()
      fileBuffer.write(s"$currentTime Pulling data from " + cols(0) + "... \n")
      try {
        pullData(cols(0), cols(1), cols(2), cols(3).toBoolean, fileBuffer)
      } catch {
        case e: IllegalArgumentException =>
          println(
            "!!!Incorrect formatting found in dataPullParse.csv, check debug.txt for details!!!"
          )
          fileBuffer.write(getStackTraceAsString(e))

      }
    }
    println("Data pull complete.")
    currentTime = getCurrentTime()
    fileBuffer.write(currentTime + " Data pull complete.\n")
    dataPullSource.close
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
    * @param fileBuffer the debug file for logging
    *
    * @example pullData(
    *             "https://example.site",
    *             "Brand",
    *             "folder\\File.txt",
    *             false,
    *             debugFileBuffer
    *          )
    */
  def pullData(
      link: String,
      site: String,
      output: String,
      appending: Boolean,
      fileBuffer: BufferedWriter
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
      currentTime = getCurrentTime()
      fileBuffer.write(
        s"$currentTime \tAdditional page detected for $output, proceeding to append...\n"
      )
      bdw = new BufferedWriter(new FileWriter(bufferDataFile, true))
    }
    site match {
      case "Nerf Wiki" =>
        scrape = doc >> attrs("title")("a[class=category-page__member-link]")
      case _ => {
        currentTime = getCurrentTime()
        fileBuffer.write(
          s"$currentTime Invalid Site Name detected, no list items added to scraper\n"
        )
      }
    }

    for (name <- scrape) {
      bdw.append(name + "\n")
    }
    bdw.close()
  }

  /** getCurrentTime
    * gets the current system time and formats it for logging purposes
    *
    * @return
    */
  def getCurrentTime(): String = {
    DateTimeFormatter
      .ofPattern("dd-MM-yyyy @ HH:mm:ss |")
      .format(LocalDateTime.now)
  }

  currentTime = getCurrentTime()
  debugFileBuffer.write(s"$currentTime debug logging complete.\n")
  debugFileBuffer.close()
  println(
    "BlastrDB closed, check debugLog.txt for a detailed runtime log."
  )
}
