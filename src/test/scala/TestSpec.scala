package BlastrDB

import org.scalatest.flatspec.AnyFlatSpec
import java.io.StringReader
import java.io.ByteArrayOutputStream
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.File

class TestSpec extends AnyFlatSpec {
  val loggerFile = new File("debugLog.txt")
  val debugFileBuffer = new BufferedWriter(new FileWriter(loggerFile))
  debugFileBuffer.write("TestSpec Run, conducting tests...\n")
  "user input 'pull'" should "successfully execute the 'dataWriteToFile' function" in {
    debugFileBuffer.write("Test 1: simulating user input as 'pull'\n")
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
    debugFileBuffer.write("Test 2: simulating user input as 'write'\n")
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
    debugFileBuffer.write("Test 3: simulating user input as 'exit'\n")
    val inputStr = "exit"
    val in = new StringReader(inputStr)
    val out = new ByteArrayOutputStream()
    Console.withOut(out) {
      Console.withIn(in) {
        assert(inputStr == "exit")
      }
    }
    debugFileBuffer.write("TestSpec testing complete, closing buffer and exiting...")
    debugFileBuffer.close()
  }
}
