package utils

class StringUtils {
     def generateRandomString(lenght:Int): String = {
         return scala.util.Random.alphanumeric.take(lenght).mkString 
     }

} 