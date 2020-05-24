package com.olmedilla

import java.io.File
import java.util.jar.JarFile
import java.util.regex.Pattern

import scala.jdk.StreamConverters._


object jarFinder  extends App {

  def getListOfFiles(dir: File, extensions: List[String]): List[File] = {
    dir.listFiles.filter(_.isFile).toList.filter { file =>
      extensions.exists(file.getName.endsWith(_))
    }
  }


  val okFileExtensions = List("jar")
  //val path = "."

  if (args == null || args.size < 2) {
    System.out.println("Uso: scalaJarFinder <directorio> <patron de busqueda>");
    System.exit(0)
  }
  val path : String = args(0)
  val cadena : String = args(1)
  Console.println("Vamos a buscar esto: " + cadena)
  val patternEntrada = Pattern.compile("^(.+)" + cadena + "(.+)$")

  val files = getListOfFiles(new File(path),okFileExtensions)

  val jars = files
    .map(f => new JarFile(f.getCanonicalFile).stream.toScala(LazyList).map(l => (f.getName,l)))
    .flatMap(j => j)
    .filter(j => patternEntrada.matcher(j._2.getName).matches)
    .foreach(p=>Console.println(p._1+": "+p._2))

}
