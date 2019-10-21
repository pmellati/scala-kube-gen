package kubeclient

import java.util.UUID.randomUUID

import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll

object httpVerbsSpec extends Specification with BeforeAfterAll {
  val kindClusterName = randomUUID().toString

  override def beforeAll(): Unit = {
    os.proc("kind", "create", "cluster", "--name", kindClusterName).call()
  }

  override def afterAll(): Unit = {
    os.proc("kind", "delete", "cluster", "--name", kindClusterName).call()
  }

  "GET requests should work" in {
    println(s"IN TEST: ${os.proc("kind", "get", "clusters").call().out.trim}")
    pending
  }
}