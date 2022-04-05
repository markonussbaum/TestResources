#!/usr/bin/env bash

export SPARK_HOME=/opt/spark

time $SPARK_HOME/bin/spark-submit \
	--master yarn \
	--deploy-mode cluster \
  --name TestVEResource \
  --num-executors=2 \
	--executor-cores=1 \
	--executor-memory=1G \
	--driver-memory=1G \
	--conf spark.executor.resource.ve.amount=2 \
	--conf spark.executor.resource.ve.discoveryScript=/opt/spark/getVEsResources.sh \
	--conf spark.executorEnv.VE_OMP_NUM_THREADS=1 \
	--conf spark.executorEnv.VEO_LOG_DEBUG=1 \
	--conf spark.com.nec.spark.ncc.path=/opt/nec/ve/bin/ncc \
	target/scala-2.12/hadoop-resource-test_2.12-0.1.jar \
	$*
