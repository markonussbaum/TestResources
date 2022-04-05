
import org.apache.spark._
import org.apache.spark.resource.ResourceInformation
import org.apache.spark.TaskContext

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
		println("=================\n\n")

		if (sc.resources.contains("ve")) {
			val resInfo = sc.resources("ve")
			println("VE Resource Information:")
			println("#VEs:      " + resInfo.addresses.length)
			println("name:      " + resInfo.name)
			println("addresses: ")
			resInfo.addresses.foreach(adr => println("  #" + adr))
		} else {
			println("No VE resources have been assigned to the driver.")
		}

		// get info from executors
		//val driverHost = sc.getConf("spark.driver.host")

		val rdd = sc.parallelize(Array(1,2,3,4,5,6,7,8))
		val result = rdd.map ( number => {

			// we are now on an executor, collect information
			val ctx = TaskContext.get()

			// build a string with info from executor
			var sb = new StringBuilder
			sb.append("Mapping number " + number + "\n==============\n\n")
			sb.append(
				"Task on partition with id " + ctx.partitionId()
					+ " on stage with id " + ctx.stageId() + "\n"
					+ "at attempt no. " + + ctx.attemptNumber()
					+ " with task attempt id: " + ctx.taskAttemptId() + "\n"
			)

			if (ctx.resources.contains("ve")) {
				sb.append(ctx.resources.get("ve") match {
					case Some(resourceInformation: ResourceInformation) => {
						("was assigned VE(s) at address(es):\n"
						+ resourceInformation.addresses.map( s => "  #"+s).reduceLeft(_+"\n"+_)
						+ "\n")
					}
					case None => {
						"was not assigned any VE"
					}
				})
			} else {
				sb.append("executor at does not contain any VEs in resources.")
			}
			sb.toString
		}).reduce(_ + "\n" + _)

		println("\nInfo from executors:\n\n")
		println(result)

		sc.stop()
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

