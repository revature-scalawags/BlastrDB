package BlastrDB

import org.scalatest.flatspec.AnyFlatSpec
import java.io.StringReader
import java.io.ByteArrayOutputStream
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**TestSpec
  * This is a faux testing file to practice adding test cases to my projects.
  * 
  * @note these tests do not actually test anything important (my testing process
  * is a more direct approach), but instead just includes standard practices that
  * allow the 'sbt test' command to run properly.
  *
  */
class TestSpec extends AnyFlatSpec {
  //TODO: Seperate test debug logs from standard debug log
  val loggerFile = new File("debugTestLog.txt")
  val debugFileBuffer = new BufferedWriter(new FileWriter(loggerFile, true))
  var currentTime = DateTimeFormatter.ofPattern("dd-MM-yyyy @ HH:mm:ss |").format(LocalDateTime.now)
  debugFileBuffer.write(s"\n$currentTime TestSpec Run, conducting tests...\n")
  "user input 'pull'" should "successfully execute the 'dataWriteToFile' function" in {
    debugFileBuffer.write(s"$currentTime Test 1: simulating user input as 'pull'\n")
    val inputStr = "pull"
    val in = new StringReader(inputStr)
    val out = new ByteArrayOutputStream()
    Console.withOut(out) {
      Console.withIn(in) {
        BlastrDB.dataWriteToFile(debugFileBuffer)
      }
    }
  }
  "user input 'write'" should "successfully execute the 'writeToBrandFiles' function" in {
    debugFileBuffer.write(s"$currentTime Test 2: simulating user input as 'write'\n")
    val inputStr = "pull"
    val in = new StringReader(inputStr)
    val out = new ByteArrayOutputStream()
    Console.withOut(out) {
      Console.withIn(in) {
        BlastrDB.writeToBrandFiles(debugFileBuffer)
      }
    }
  }
  "user input 'exit'" should "escape the app's CLI run loop" in {
    currentTime = BlastrDB.getCurrentTime()
    debugFileBuffer.write(s"$currentTime Test 3: simulating user input as 'exit'\n")
    val inputStr = "exit"
    val in = new StringReader(inputStr)
    val out = new ByteArrayOutputStream()
    Console.withOut(out) {
      Console.withIn(in) {
        assert(inputStr == "exit")
      }
    }
    currentTime = BlastrDB.getCurrentTime()
    debugFileBuffer.write(s"$currentTime TestSpec testing complete, closing buffer and exiting...")
    debugFileBuffer.close()
  }
}
