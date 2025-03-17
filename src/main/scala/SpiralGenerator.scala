import java.io._
import sys.process._
import zio.json._

sealed trait Direction
case object NorthEast extends Direction
case object NorthWest extends Direction
case object SouthWest extends Direction
case object SouthEast extends Direction

case class BoundingBox(x1: Double, y1: Double, x2: Double, y2: Double)
case class Locus(x: Double, y: Double)
case class CurveSpec(origin: Locus, initialDirection: Direction, numIterations: Int)
case class CurvePoints(points: List[(Double,Double)])
case class MultiCurve(curves: List[CurvePoints])

object SpiralGenerator {

  type MultiSpec = List[CurveSpec]

  def rotate(current: Direction)(numRotations: Int): Direction = {
    def turn(d: Direction, i: Int): Direction = {
      if (i >= numRotations) d
      else turn(nextDirection(d), i+1)
    }
    if(numRotations <= 0) current
    else turn(current, 0)
  }

  def nextDirection(current: Direction): Direction = 
    current match {
      case NorthEast => NorthWest
      case NorthWest => SouthWest
      case SouthWest => SouthEast
      case SouthEast => NorthEast
    }

  def arcFunction(direction: Direction): (Double, Double, Double, List[Double]) => (List[Double], List[Double]) = 
    direction match {
      case NorthEast => northeastArc
      case NorthWest => northwestArc
      case SouthWest => southwestArc
      case SouthEast => southeastArc
    }

  def cornerFunction(direction: Direction): BoundingBox => Locus = 
    direction match {
      case NorthEast => northeastCorner
      case NorthWest => northwestCorner
      case SouthWest => southwestCorner
      case SouthEast => southeastCorner
    }

  def northeastCorner(b: BoundingBox): Locus = Locus(b.x2, b.y2)
  def northwestCorner(b: BoundingBox): Locus = Locus(b.x1, b.y2)
  def southwestCorner(b: BoundingBox): Locus = Locus(b.x1, b.y1)
  def southeastCorner(b: BoundingBox): Locus = Locus(b.x2, b.y1)

  def boxFunction(direction: Direction): (Locus, Double) => BoundingBox = 
    direction match {
      case NorthEast => northeastBox
      case NorthWest => northwestBox
      case SouthWest => southwestBox
      case SouthEast => southeastBox
    }

  def northeastBox(p: Locus, size: Double): BoundingBox = BoundingBox(p.x, p.y, p.x + size, p.y + size)
  def northwestBox(p: Locus, size: Double): BoundingBox = BoundingBox(p.x - size, p.y, p.x, p.y + size)
  def southwestBox(p: Locus, size: Double): BoundingBox = BoundingBox(p.x - size, p.y - size, p.x, p.y)
  def southeastBox(p: Locus, size: Double): BoundingBox = BoundingBox(p.x, p.y - size, p.x + size, p.y)

  def doubleRange(start: Double, end: Double, step: Double): List[Double] = {
    @annotation.tailrec
    def r(next: Double, acc: List[Double]): List[Double] = {
      if (next > end) (end :: acc).reverse
      else if (next == end) acc.reverse
      else r(next + step, next :: acc)
    }
    if (step == 0.0 || start > end) List()
    else r(start, List())
  }

  def northeastArc(x: Double, y: Double, radius: Double, t: List[Double] ): (List[Double], List[Double]) = {
    val xValues = t.map((k: Double) => x + radius * Math.cos(k))
    val yValues = t.map((k: Double) => y + radius * Math.sin(k))
    (xValues, yValues)
  }

  def northwestArc(x: Double, y: Double, radius: Double, t: List[Double] ): (List[Double], List[Double]) = {
    val xValues = t.reverse.map((k: Double) => x + (-1) * radius * Math.cos(k))
    val yValues = t.reverse.map((k: Double) => y + radius * Math.sin(k))
    (xValues, yValues)
  }

  def southwestArc(x: Double, y: Double, radius: Double, t: List[Double] ): (List[Double], List[Double]) = {
    val xValues = t.map((k: Double) => x + (-1) * radius * Math.cos(k))
    val yValues = t.map((k: Double) => y + (-1) * radius * Math.sin(k))
    (xValues, yValues)
  }

  def southeastArc(x: Double, y: Double, radius: Double, t: List[Double] ): (List[Double], List[Double]) = {
    val xValues = t.reverse.map((k: Double) => x + radius * Math.cos(k))
    val yValues = t.reverse.map((k: Double) => y + (-1) * radius * Math.sin(k))
    (xValues, yValues)
  }

  def generateSpiral(curveSpec: CurveSpec): CurvePoints = {
    // TODO: replace the three sample lines of code given here with code that generates the points
    //       for one Golden Spiral. Note that none of the other code in this module should be changed.
    val sampleXvalues = List(1.0, 4.0, 4.0, 1.0, 1.0, 3.0, 3.0, 2.0)
    val sampleYvalues = List(1.0, 1.0, 4.0, 4.0, 2.0, 2.0, 3.0, 3.0)
    CurvePoints(sampleXvalues.zip(sampleYvalues))
  }

  def generateMultiCurve(curveSpecs: List[CurveSpec]): MultiCurve = 
    MultiCurve(curveSpecs.map(c => generateSpiral(c)))

  implicit val curvePointsEncoder: JsonEncoder[CurvePoints] = DeriveJsonEncoder.gen[CurvePoints]
  implicit val multiCurveEncoder: JsonEncoder[MultiCurve] = DeriveJsonEncoder.gen[MultiCurve]

  def getDemoCurveSpecs(demo: Int): MultiSpec = 
    demo match {
      case 1 => {
        List(CurveSpec(Locus(0, 0), NorthWest, 7))
      }
      case 2 => {
        val startingPoint = Locus(0, 0)
        val spec1 = CurveSpec(startingPoint, NorthWest, 7)
        val spec2 = CurveSpec(startingPoint, SouthWest, 7)
        val spec3 = CurveSpec(startingPoint, SouthEast, 7)
        val spec4 = CurveSpec(startingPoint, NorthEast, 7)
        List(spec1, spec2, spec3, spec4)
      }
      case 3 => {
        val sp1 = CurveSpec(Locus(1,1), NorthWest, 7)
        val sp2 = CurveSpec(Locus(-1,1), SouthWest, 7)
        val sp3 = CurveSpec(Locus(-1,-1), SouthEast, 7)
        val sp4 = CurveSpec(Locus(1,-1), NorthEast, 7)
        List(sp1, sp2, sp3, sp4)
      }
      case 4 => {
        val t = doubleRange(0.0, Math.PI/2.0, 0.4)
        val nw = northwestArc(0, 0, 1, t)._1.zip(northwestArc(0, 0, 1, t)._2)
        val sw = southwestArc(0, 0, 1, t)._1.zip(southwestArc(0, 0, 1, t)._2)
        val se = southeastArc(0, 0, 1, t)._1.zip(southeastArc(0, 0, 1, t)._2)
        val ne = northeastArc(0, 0, 1, t)._1.zip(northeastArc(0, 0, 1, t)._2)
        val allLoci = (nw ::: sw ::: se ::: ne).map(pt => Locus(pt._1, pt._2))
        allLoci.map(locus => CurveSpec(locus, NorthWest, 7))
      }
      case 5 => {
        val t = doubleRange(0.0, Math.PI/2.0, 0.4)
        val nw = northwestArc(0, 0, 1, t)._1.zip(northwestArc(0, 0, 1, t)._2)
        val sw = southwestArc(0, 0, 1, t)._1.zip(southwestArc(0, 0, 1, t)._2)
        val se = southeastArc(0, 0, 1, t)._1.zip(southeastArc(0, 0, 1, t)._2)
        val ne = northeastArc(0, 0, 1, t)._1.zip(northeastArc(0, 0, 1, t)._2)
        val allLoci = (nw ::: sw ::: se ::: ne).map(pt => Locus(pt._1, pt._2))
        allLoci.map(locus => CurveSpec(locus, NorthWest, 7)).zipWithIndex
          .map(indexedSpec => CurveSpec(
            indexedSpec._1.origin, 
            rotate(indexedSpec._1.initialDirection)(indexedSpec._2), 
            7))
      }
      case _ => {
        println("Please choose between 1 and 5")
        List()
      }
    }

  def main(args: Array[String]): Unit = {
    print("Which demo? (1,2,3,4,5) > ")
    val x = scala.io.StdIn.readInt()
    val multiSpec = getDemoCurveSpecs(x)
    val bw = new BufferedWriter(new FileWriter(new File("spirals.json")))
    bw.write(generateMultiCurve(multiSpec).toJson)
    bw.close()
    val res = "python3 plot_multi.py".!
  }
}
