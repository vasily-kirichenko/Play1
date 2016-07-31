import reflect.runtime.universe._

val q = q"""
   x match {
     case x: Int => "foo"
     case _ => "bar"
   }
"""

showCode(q)