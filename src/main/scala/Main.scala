
import org.apache.spark._
import org.apache.spark.resource.ResourceInformation

object Main {

	var sc: SparkContext = _

	def main(args: Array[String]): Unit = {

		sc = setup()

		val res = get_res()

		// Note:
		// This runs on the driver, so you need to request the use
		// of the ve on the driver!
		// (spark.driver.resource.ve.amount=1) & set discovery script
		// otherwise the list will be empty...
		println("=== RESOURCES ===")
		println("#resources: " + res.length)
		res.foreach( println _)
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

