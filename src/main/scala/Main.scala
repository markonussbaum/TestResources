
import org.apache.spark._
import org.apache.spark.resource.ResourceInformation

object Main {

	var sc: SparkContext = _

	def main(args: Array[String]): Unit = {

		sc = setup()

		//print_res()
		println("=== RESOURCES ===")
		get_res().foreach( println _)
	}

	def setup(): SparkContext = {
		val conf = new SparkConf().setAppName("ResourcenTest").setMaster("yarn")
		new SparkContext(conf)
	}

	def print_res(): Unit = {
		sc.resources.foreach( (p: (String, ResourceInformation)) => {
			println(p._1 + " -> " + p._2.name)
		})
	}

	def get_res(): Array[String] = {
		sc.resources.toArray.map( (p: (String, ResourceInformation)) => {
			p._1 + " -> " + p._2.name
		}).toArray
	}

}

