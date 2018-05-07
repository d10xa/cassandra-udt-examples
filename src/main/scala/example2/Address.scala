package example2

import com.datastax.driver.mapping.annotations.Field
import com.datastax.driver.mapping.annotations.UDT

import scala.annotation.meta.field
import scala.beans.BeanProperty

@UDT(keyspace = "ks", name = "address")
case class Address(
  @BeanProperty street: String,
  @BeanProperty @(Field @field)(name = "zip_code") zipCode: Integer
) {

  // default constructor with nulls are used for java example interoperability
  def this() = this(null, null)
}
