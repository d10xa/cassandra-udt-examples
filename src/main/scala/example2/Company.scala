package example2

import java.util.UUID

import com.datastax.driver.mapping.annotations.Column
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table

import scala.annotation.meta.field
import scala.beans.BeanProperty

@Table(keyspace = "ks", name = "company")
case class Company(
  @BeanProperty @(PartitionKey @field) @(Column @field)(name = "company_id", caseSensitive = true) companyId: UUID,
  @BeanProperty name: String,
  @BeanProperty address: Address
) {

  // default constructor with nulls are used for java example interoperability
  def this() = this(null, null, null)
}
